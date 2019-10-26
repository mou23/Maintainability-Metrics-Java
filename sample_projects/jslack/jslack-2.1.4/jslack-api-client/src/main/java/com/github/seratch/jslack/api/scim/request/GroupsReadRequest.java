package com.github.seratch.jslack.api.scim.request;

import com.github.seratch.jslack.api.scim.SCIMApiRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupsReadRequest implements SCIMApiRequest {
    private String token;
    private String id;
}
