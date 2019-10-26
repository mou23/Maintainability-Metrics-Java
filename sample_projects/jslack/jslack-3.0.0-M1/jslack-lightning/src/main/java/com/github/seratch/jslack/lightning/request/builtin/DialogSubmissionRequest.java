package com.github.seratch.jslack.lightning.request.builtin;

import com.github.seratch.jslack.app_backend.dialogs.payload.DialogSubmissionPayload;
import com.github.seratch.jslack.common.json.GsonFactory;
import com.github.seratch.jslack.lightning.context.builtin.DialogSubmissionContext;
import com.github.seratch.jslack.lightning.request.Request;
import com.github.seratch.jslack.lightning.request.RequestHeaders;
import com.github.seratch.jslack.lightning.request.RequestType;
import lombok.ToString;

@ToString(callSuper = true)
public class DialogSubmissionRequest extends Request<DialogSubmissionContext> {

    private final String requestBody;
    private final RequestHeaders headers;
    private final DialogSubmissionPayload payload;

    public DialogSubmissionRequest(
            String requestBody,
            String payloadBody,
            RequestHeaders headers) {
        this.requestBody = requestBody;
        this.headers = headers;
        this.payload = GsonFactory.createSnakeCase().fromJson(payloadBody, DialogSubmissionPayload.class);
        getContext().setResponseUrl(payload.getResponseUrl());
    }

    private DialogSubmissionContext context = new DialogSubmissionContext();

    @Override
    public DialogSubmissionContext getContext() {
        return context;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.DialogSubmission;
    }

    @Override
    public String getRequestBodyAsString() {
        return requestBody;
    }

    @Override
    public RequestHeaders getHeaders() {
        return this.headers;
    }

    public DialogSubmissionPayload getPayload() {
        return payload;
    }

}
