package com.service.security;

import java.util.List;

public interface SecurityManager {

    boolean isAuthorized(String user, String pass, List<String> resources, List<String> actions);

}
