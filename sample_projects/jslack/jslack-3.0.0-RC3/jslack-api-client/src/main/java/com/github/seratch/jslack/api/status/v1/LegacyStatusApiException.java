package com.github.seratch.jslack.api.status.v1;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;

@Data
@Slf4j
public class LegacyStatusApiException extends Exception {

    private final Response response;
    private final String responseBody;

    public LegacyStatusApiException(Response response, String responseBody) {
        this.response = response;
        this.responseBody = responseBody;
    }

}
