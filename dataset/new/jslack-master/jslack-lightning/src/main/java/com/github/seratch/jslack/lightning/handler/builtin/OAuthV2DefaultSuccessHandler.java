package com.github.seratch.jslack.lightning.handler.builtin;

import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.response.oauth.OAuthV2AccessResponse;
import com.github.seratch.jslack.api.methods.response.users.UsersInfoResponse;
import com.github.seratch.jslack.lightning.context.builtin.OAuthCallbackContext;
import com.github.seratch.jslack.lightning.handler.OAuthV2SuccessHandler;
import com.github.seratch.jslack.lightning.model.Installer;
import com.github.seratch.jslack.lightning.model.builtin.DefaultInstaller;
import com.github.seratch.jslack.lightning.request.builtin.OAuthCallbackRequest;
import com.github.seratch.jslack.lightning.response.Response;
import com.github.seratch.jslack.lightning.service.InstallationService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class OAuthV2DefaultSuccessHandler implements OAuthV2SuccessHandler {

    private InstallationService installationService;

    public OAuthV2DefaultSuccessHandler(InstallationService installationService) {
        this.installationService = installationService;
    }

    @Override
    public Response handle(OAuthCallbackRequest req, OAuthV2AccessResponse o) {
        OAuthCallbackContext context = req.getContext();
        if (o.getEnterprise() != null) {
            context.setEnterpriseId(o.getEnterprise().getId());
        }
        context.setTeamId(o.getTeam().getId());
        context.setBotUserId(o.getBotUserId());
        context.setBotToken(o.getAccessToken());
        context.setRequestUserId(o.getAuthedUser().getId());
        context.setRequestUserToken(o.getAccessToken());

        DefaultInstaller.DefaultInstallerBuilder i = DefaultInstaller.builder()
                .botUserId(o.getBotUserId())
                .botAccessToken(o.getAccessToken())
                .enterpriseId(o.getEnterprise() != null ? o.getEnterprise().getId() : null)
                .teamId(o.getTeam().getId())
                .teamName(o.getTeam().getName())
                .scope(o.getScope())
                .botScope(o.getScope())
                .installedAt(System.currentTimeMillis());

        if (o.getAuthedUser() != null) {
            // we can assume authed_user should exist but just in case
            i = i.installerUserId(o.getAuthedUser().getId())
                    .installerUserAccessToken(o.getAuthedUser().getAccessToken())
                    .installerUserScope(o.getAuthedUser().getScope());
        }

        if (o.getBotUserId() != null) {
            try {
                UsersInfoResponse user = context.client().usersInfo(r -> r.user(o.getBotUserId()));
                if (user.isOk()) {
                    i = i.botId(user.getUser().getProfile().getBotId());
                } else {
                    log.warn("Failed to call users.info to fetch botId for the user: {} - {}", o.getBotUserId(), user.getError());
                }
            } catch (SlackApiException | IOException e) {
                log.warn("Failed to call users.info to fetch botId for the user: {}", o.getBotUserId(), e);
            }
        }

        if (o.getIncomingWebhook() != null) {
            i = i.incomingWebhookChannelId(o.getIncomingWebhook().getChannelId())
                    .incomingWebhookUrl(o.getIncomingWebhook().getUrl())
                    .incomingWebhookConfigurationUrl(o.getIncomingWebhook().getConfigurationUrl());
        }

        Installer installer = i.build();

        Map<String, String> headers = new HashMap<>();
        try {
            installationService.saveInstallerAndBot(installer);
            headers.put("Location", context.getOauthCompletionUrl());
        } catch (Exception e) {
            log.warn("Failed to store the installation - {}", e.getMessage(), e);
            headers.put("Location", context.getOauthCancellationUrl());
        }
        return Response.builder().statusCode(302).headers(headers).build();
    }
}
