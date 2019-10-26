package com.github.seratch.jslack.api.audit.request;

import com.github.seratch.jslack.api.audit.AuditApiRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionsRequest implements AuditApiRequest {
    private String token;
}
