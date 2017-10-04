package com.service.security;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.internal.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.StringTokenizer;

@Provider
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter {
    private final static Logger LOGGER = LoggerFactory.getLogger(SecurityManagerImpl.class);
    private static final String AUTHORIZATION_PROPERTY_NAME = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";

    @Context
    private ResourceInfo resourceInfo;

    @Autowired
    private SecurityManagerImpl securityManager;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method method = resourceInfo.getResourceMethod();
        if (method.isAnnotationPresent(SecuredEndpoint.class)) {
            LOGGER.info("Authenticating request");
            MultivaluedMap<String, String> headers = requestContext.getHeaders();
            String authorizationPropertyValue = headers.getFirst(AUTHORIZATION_PROPERTY_NAME);
            if (StringUtils.isEmpty(authorizationPropertyValue)) {
                requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
                return;
            }
            String user;
            String pass;
            try {
                String base64UserPass = authorizationPropertyValue.replaceFirst(AUTHENTICATION_SCHEME + " ", "");
                LOGGER.debug("Encoded auth: {}", base64UserPass);
                String userPass = new String(Base64.decode(base64UserPass.getBytes()));
                StringTokenizer tok = new StringTokenizer(userPass, ":");
                user = tok.nextToken();
                pass = tok.nextToken();
            } catch (Exception e) {
                requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
                return;
            }
            SecuredEndpoint annotation = method.getAnnotation(SecuredEndpoint.class);
            if (!securityManager.isAuthorized(user, pass, Arrays.asList(annotation.resources()), Arrays.asList(annotation.actions()))) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                return;
            }
        }
    }

}
