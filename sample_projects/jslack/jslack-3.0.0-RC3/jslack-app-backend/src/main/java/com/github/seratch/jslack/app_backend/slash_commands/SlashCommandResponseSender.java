package com.github.seratch.jslack.app_backend.slash_commands;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.WebhookResponse;
import com.github.seratch.jslack.app_backend.slash_commands.response.SlashCommandResponse;
import com.github.seratch.jslack.common.http.SlackHttpClient;
import okhttp3.Response;

import java.io.IOException;

public class SlashCommandResponseSender {

    private final Slack slack;

    public SlashCommandResponseSender(Slack slack) {
        this.slack = slack;
    }

    public WebhookResponse send(String responseUrl, SlashCommandResponse response) throws IOException {
        SlackHttpClient httpClient = slack.getHttpClient();
        Response httpResponse = httpClient.postJsonBody(responseUrl, response);
        String body = httpResponse.body().string();
        httpClient.runHttpResponseListeners(httpResponse, body);

        return WebhookResponse.builder()
                .code(httpResponse.code())
                .message(httpResponse.message())
                .body(body)
                .build();
    }
}
