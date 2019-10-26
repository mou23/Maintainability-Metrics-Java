package com.github.seratch.jslack.api.webhook;

import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.block.LayoutBlock;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * https://api.slack.com/incoming-webhooks
 * <p>
 * Implementation of <a href="https://api.slack.com/reference/messaging/payload">RTMMessage Payloads</a>
 */
@Data
@Builder
public class Payload {

    /**
     * You can add the thread_ts parameter to your POST request
     * in order to make your message appear as a reply in a thread.
     */
    private String threadTs;

    /**
     * The first step is to prepare this message as a key/value pair in JSON.
     * For a simple message, your JSON payload only needs to define a text property, containing the text that will be posted to the channel.
     */
    private String text;

    /**
     * NOTE: No longer works if your webhook is managed in a Slack app
     * <p>
     * Incoming webhooks output to a default channel and can only send messages to a single channel at a time.
     * You can override a custom integration's configured channel by specifying the channel field in your JSON payload.
     * <p>
     * Specify a Slack channel by name with "channel": "#other-channel", or send a Slackbot message to a specific user with "channel": "@username".
     */
    @Deprecated
    private String channel;

    /**
     * NOTE: No longer works if your webhook is managed in a Slack app
     * <p>
     * Incoming webhooks originate from a default identity you configured when originally creating your webhook.
     * You can override a custom integration's configured name with the username field in your JSON payload.
     */
    @Deprecated
    private String username;

    /**
     * NOTE: No longer works if your webhook is managed in a Slack app
     * <p>
     * You can also override the bot icon either with icon_url or icon_emoji.
     */
    @Deprecated
    private String iconUrl;
    /**
     * NOTE: No longer works if your webhook is managed in a Slack app
     * <p>
     * You can also override the bot icon either with icon_url or icon_emoji.
     */
    @Deprecated
    private String iconEmoji;

    /**
     * An array of {@link LayoutBlock layout blocks} in the same format as described in the
     * <a href="https://api.slack.com/messaging/composing/layouts#getting-started">layout block guide</a>.
     */
    private List<LayoutBlock> blocks;

    /**
     * An array of legacy secondary attachments. We recommend you use {@link #blocks} instead.
     */
    private List<Attachment> attachments;
}