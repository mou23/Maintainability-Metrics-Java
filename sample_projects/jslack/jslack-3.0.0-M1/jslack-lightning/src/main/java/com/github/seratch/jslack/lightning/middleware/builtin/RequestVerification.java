package com.github.seratch.jslack.lightning.middleware.builtin;

import com.github.seratch.jslack.app_backend.SlackSignature;
import com.github.seratch.jslack.lightning.middleware.Middleware;
import com.github.seratch.jslack.lightning.middleware.MiddlewareChain;
import com.github.seratch.jslack.lightning.request.Request;
import com.github.seratch.jslack.lightning.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestVerification implements Middleware {

    private final SlackSignature.Verifier verifier;

    public RequestVerification(SlackSignature.Verifier verifier) {
        this.verifier = verifier;
    }

    @Override
    public Response apply(Request req, MiddlewareChain chain) throws Exception {
        if (req.isValid(verifier)) {
            return chain.next(req);
        } else {
            String signature = req.getHeaders().get(SlackSignature.HeaderNames.X_SLACK_SIGNATURE);
            log.info("Invalid signature detected - {}", signature);
            return Response.json(401, "{\"error\":\"invalid request\"}");
        }
    }
}
