package com.github.seratch.jslack.api.methods.response.users;

import com.github.seratch.jslack.api.methods.SlackApiResponse;
import com.github.seratch.jslack.api.model.ResponseMetadata;
import com.github.seratch.jslack.api.model.User;
import lombok.Data;

import java.util.List;

@Data
public class UsersListResponse implements SlackApiResponse {

    private boolean ok;
    private String warning;
    private String error;
    private String needed;
    private String provided;

    private String offset; // user id
    private List<User> members;
    private String cacheTs;
    private ResponseMetadata responseMetadata;
}
