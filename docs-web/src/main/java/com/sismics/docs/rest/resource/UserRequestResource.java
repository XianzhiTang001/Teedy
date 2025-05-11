package com.sismics.docs.rest.resource;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.sismics.docs.core.constant.AclTargetType;
import com.sismics.docs.core.constant.ConfigType;
import com.sismics.docs.core.constant.Constants;
import com.sismics.docs.core.dao.*;
import com.sismics.docs.core.model.jpa.*;
import com.sismics.rest.exception.ClientException;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.exception.ServerException;
import com.sismics.rest.util.ValidationUtil;
import com.sismics.security.UserPrincipal;
import com.sismics.util.JsonUtil;
import com.sismics.util.context.ThreadLocalContext;
import com.sismics.util.filter.TokenBasedSecurityFilter;
import com.sismics.util.totp.GoogleAuthenticator;
import com.sismics.util.totp.GoogleAuthenticatorKey;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Path("/user/request")
public class UserRequestResource extends BaseResource {
    @POST
    public Response submit(
        @FormParam("username") String username,
        @FormParam("password") String password,
        @FormParam("email") String email) {
        // Validate the input data
        username = ValidationUtil.validateLength(username, "username", 3, 50);
        ValidationUtil.validateUsername(username, "username");
        password = ValidationUtil.validateLength(password, "password", 8, 50);
        email = ValidationUtil.validateLength(email, "email", 1, 100);
        ValidationUtil.validateEmail(email, "email");

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(username);
        userRequest.setPassword(password);
        userRequest.setEmail(email);

        UserRequestDao userRequestDao = new UserRequestDao();
        try {
            userRequestDao.create(userRequest);
        } catch (Exception e) {
            if ("AlreadyExistingUsername".equals(e.getMessage())) {
                throw new ClientException("AlreadyExistingUsername", "Login already used", e);
            } else {
                throw new ServerException("UnknownError", "Unknown server error", e);
            }
        }

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "PENDING");
        return Response.ok().entity(response.build()).build();
    }

    @POST
    @Path("approve")
    public Response approve(
        @FormParam("id") String id) {

        UserRequestDao userRequestDao = new UserRequestDao();
        UserRequest userRequest = userRequestDao.getById(id);
        if (userRequest == null) {
            throw new ClientException("RequestNotFound", "The request does not exist");
        }
        if (!"PENDING".equals(userRequest.getStatus())) {
            throw new ClientException("NotPending", "Request is not in PENDING status");
        }

        UserDao userDao = new UserDao();
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setRoleId("user");
        user.setOnboarding(true);

        try {
            userDao.create(user, principal.getId());
        } catch (Exception e) {
            throw new ServerException("CreateUserFailed", "Failed to create user", e);
        }

        userRequest.setStatus("APPROVED");
        userRequestDao.update(userRequest);

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "APPROVED");
        return Response.ok().entity(response.build()).build();
    }

    @POST
    @Path("reject")
    public Response reject(
            @FormParam("id") String id) {

        UserRequestDao userRequestDao = new UserRequestDao();
        UserRequest userRequest = userRequestDao.getById(id);
        if (userRequest == null) {
            throw new ClientException("RequestNotFound", "The request does not exist");
        }
        if (!"PENDING".equals(userRequest.getStatus())) {
            throw new ClientException("NotPending", "Request is not in PENDING status");
        }
        userRequest.setStatus("REJECTED");
        userRequestDao.update(userRequest);

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("status", "REJECTED");
        return Response.ok().entity(response.build()).build();
    }

    @GET
    public Response list() {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        UserRequestDao userRequestDao = new UserRequestDao();
        List<UserRequest> req = userRequestDao.getPendingRequests();

        JsonArrayBuilder requests = Json.createArrayBuilder();
        for (UserRequest r : req) {
            requests.add(Json.createObjectBuilder()
                    .add("id", r.getId())
                    .add("username", r.getUsername())
                    .add("email", r.getEmail())
                    .add("status", r.getStatus())
                    .add("createDate", r.getCreateDate().toString()));
        }

        JsonObjectBuilder response = Json.createObjectBuilder()
                .add("requests", requests);
        return Response.ok().entity(response.build()).build();
    }

}
