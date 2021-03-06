package com.github.seratch.jslack.api.methods.response.conversations;

import com.github.seratch.jslack.api.methods.SlackApiResponse;
import com.github.seratch.jslack.api.model.Conversation;
import lombok.Data;

import java.util.List;

@Data
public class ConversationsInviteResponse implements SlackApiResponse {

    private boolean ok;
    private String warning;
    private String error;
    private List<Error> errors;
    private String needed;
    private String provided;

    private Conversation channel;

    @Data
    public static class Error {
        private boolean ok;
        private String error;
    }
}
