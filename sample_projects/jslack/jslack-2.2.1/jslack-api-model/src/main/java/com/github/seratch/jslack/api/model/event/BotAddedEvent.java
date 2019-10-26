package com.github.seratch.jslack.api.model.event;

import com.github.seratch.jslack.api.model.BotIcons;
import lombok.Data;

/**
 * The bot_added event is sent to all connections for a workspace when an integration "bot" is added.
 * Clients can use this to update their local list of bots.
 * <p>
 * If the bot belongs to a Slack app, the event will also include an app_id pointing to its parent app.
 * <p>
 * https://api.slack.com/events/bot_added
 */
@Data
public class BotAddedEvent implements Event {

    public static final String TYPE_NAME = "bot_added";

    private final String type = TYPE_NAME;
    private Bot bot;

    @Data
    public static class Bot {
        private String id;
        private String appId;
        private String name;
        private BotIcons icons;
    }

}