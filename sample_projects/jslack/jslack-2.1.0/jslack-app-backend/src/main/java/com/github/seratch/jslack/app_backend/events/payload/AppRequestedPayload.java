package com.github.seratch.jslack.app_backend.events.payload;

import com.github.seratch.jslack.api.model.event.AppRequestedEvent;
import lombok.Data;

import java.util.List;

@Data
public class AppRequestedPayload implements EventsApiPayload<AppRequestedEvent> {

    private String token;
    private String teamId;
    private String apiAppId;
    private AppRequestedEvent event;
    private String type;
    private List<String> authedUsers;
    private String eventId;
    private Integer eventTime;

}
