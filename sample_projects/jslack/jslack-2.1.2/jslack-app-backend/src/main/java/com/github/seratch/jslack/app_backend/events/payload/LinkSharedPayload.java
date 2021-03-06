package com.github.seratch.jslack.app_backend.events.payload;

import com.github.seratch.jslack.api.model.event.LinkSharedEvent;
import lombok.Data;

import java.util.List;

@Data
public class LinkSharedPayload implements EventsApiPayload<LinkSharedEvent> {

    private String token;
    private String teamId;
    private String apiAppId;
    private LinkSharedEvent event;
    private String type;
    private List<String> authedUsers;
    private String eventId;
    private Integer eventTime;

}
