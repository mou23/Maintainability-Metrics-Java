package com.github.seratch.jslack.lightning.servlet;

import com.github.seratch.jslack.lightning.App;
import com.github.seratch.jslack.lightning.request.Request;
import com.github.seratch.jslack.lightning.response.Response;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SlackOAuthAppServlet extends HttpServlet {

    private final App app;
    private final ServletAdapter adapter;

    public App getApp() {
        return this.app;
    }

    public SlackOAuthAppServlet(App app) {
        this.app = app;
        this.adapter = new ServletAdapter(app.config());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Request slackReq = adapter.buildSlackRequest(req);
        if (slackReq != null) {
            try {
                Response slackResp = app.run(slackReq);
                adapter.writeResponse(resp, slackResp);
            } catch (Exception e) {
                log.error("Failed to handle a request - {}", e.getMessage(), e);
                resp.setStatus(500);
                resp.setContentType("application/json");
                resp.getWriter().write("{\"error\":\"Something is wrong\"}");
            }
        }
    }
}