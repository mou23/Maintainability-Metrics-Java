package com.github.seratch.jslack.app_backend.events.payload;

import com.github.seratch.jslack.api.model.event.StarAddedEvent;
import lombok.Data;

import java.util.List;

@Data
public class StarAddedPayload implements EventsApiPayload<StarAddedEvent> {

    private String token;
    private String teamId;
    private String apiAppId;
    private StarAddedEvent event;
    private String type;
    private List<String> authedUsers;
    private String eventId;
    private Integer eventTime;

}
