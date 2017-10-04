package com.rest;


import com.domain.Message;
import com.service.MessageManager;
import com.service.security.SecuredEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;

@Path("/messages")
@Component
public class MessagesResource {
    private final MessageManager messageManager;

    @Autowired
    public MessagesResource(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @SecuredEndpoint(resources = {"MESSAGES"}, actions = {"VIEW"})
    public Response getMessages() {
        Collection<Message> msgs = messageManager.getMessages();
        return Response.status(Response.Status.OK).entity(msgs.toArray(new Message[msgs.size()])).build();
    }

}
