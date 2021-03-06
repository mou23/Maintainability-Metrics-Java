package com.github.seratch.jslack.api.scim.request;

import com.github.seratch.jslack.api.scim.SCIMApiRequest;
import com.github.seratch.jslack.api.scim.model.Group;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupsUpdateRequest implements SCIMApiRequest {
    private String token;
    private String id;
    private Group group;
}
