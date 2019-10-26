package com.github.seratch.jslack.api.scim.request;

import com.github.seratch.jslack.api.scim.SCIMApiRequest;
import com.github.seratch.jslack.api.scim.model.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersUpdateRequest implements SCIMApiRequest {
    private String token;
    private String id;
    private User user;
}
