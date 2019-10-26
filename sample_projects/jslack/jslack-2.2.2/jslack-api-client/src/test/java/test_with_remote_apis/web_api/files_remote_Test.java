package test_with_remote_apis.web_api;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.response.files.remote.*;
import com.github.seratch.jslack.api.methods.response.search.SearchFilesResponse;
import com.github.seratch.jslack.api.model.File;
import com.github.seratch.jslack.api.model.MatchedItem;
import com.github.seratch.jslack.shortcut.model.ApiToken;
import com.github.seratch.jslack.shortcut.model.ChannelName;
import config.Constants;
import config.SlackTestConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@Slf4j
public class files_remote_Test {

    Slack slack = Slack.getInstance(SlackTestConfig.get());
    String userToken = System.getenv(Constants.SLACK_TEST_OAUTH_ACCESS_TOKEN);
    String botToken = System.getenv(Constants.SLACK_BOT_USER_TEST_OAUTH_ACCESS_TOKEN);

    @Test
    public void listAllFiles() throws IOException, SlackApiException {
        {
            FilesRemoteListResponse response = slack.methods(botToken).filesRemoteList(r -> r);
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));
            assertThat(response.getFiles().size(), is(greaterThan(0)));
        }
    }

    @Test
    public void listFilesInAChannel() throws IOException, SlackApiException {
        {
            String randomChannelId = slack.shortcut(ApiToken.of(botToken))
                    .findChannelIdByName(ChannelName.of("random"))
                    .get().getValue();
            FilesRemoteListResponse response = slack.methods(botToken).filesRemoteList(r -> r.channel(randomChannelId));
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));
        }
    }

    @Test
    public void addAndInfoAndRemove() throws IOException, SlackApiException {
        FilesRemoteAddResponse addResponse = slack.methods(botToken).filesRemoteAdd(r -> r
                .externalId("test-external-id-" + System.currentTimeMillis())
                .externalUrl("https://a.slack-edge.com/4a5c4/marketing/img/icons/icon_slack.svg")
                .filetype("imageData/svg")
                .title("Slack Logo Image File"));
        assertThat(addResponse.getError(), is(nullValue()));
        assertThat(addResponse.isOk(), is(true));
        File file = addResponse.getFile();

        {
            FilesRemoteInfoResponse response = slack.methods(botToken).filesRemoteInfo(r -> r
                    .externalId(file.getExternalId()));
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));
        }

        {
            FilesRemoteInfoResponse response = slack.methods(botToken).filesRemoteInfo(r -> r
                    .file(file.getId()));
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));
        }
        {
            FilesRemoteRemoveResponse response = slack.methods(botToken).filesRemoteRemove(r -> r
                    .externalId(file.getExternalId()));
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));

            FilesRemoteRemoveResponse response2 = slack.methods(botToken).filesRemoteRemove(r -> r
                    .externalId(file.getExternalId()));
            assertThat(response2.getError(), is("file_not_found"));
        }
    }

    @Data
    public static class Image {
        String url = "https://a.slack-edge.com/a8b6/marketing/img/homepage/uis/calls/voice-and-video-call-slack-product-mobile@2x.png";
        byte[] content;

        public Image() throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int length;
            byte[] b = new byte[2048];
            URLConnection conn = new URL(url).openConnection();
            try (InputStream is = conn.getInputStream()) {
                while ((length = is.read(b)) != -1) {
                    out.write(b, 0, length);
                }
                this.content = out.toByteArray();
            }
        }
    }

    @Test
    public void updateAndShare() throws Exception {
        String externalId = "test-external-id-" + System.currentTimeMillis();
        Image image = new Image();
        FilesRemoteAddResponse addResponse = slack.methods(botToken).filesRemoteAdd(r -> r
                .externalId(externalId)
                .externalUrl(image.url)
                .previewImage(image.content)
                .title("Slack Call"));
        assertThat(addResponse.getError(), is(nullValue()));
        assertThat(addResponse.isOk(), is(true));

        File file = addResponse.getFile();

        String randomChannelId = slack.shortcut(ApiToken.of(botToken))
                .findChannelIdByName(ChannelName.of("random"))
                .get().getValue();

        {
            FilesRemoteUpdateResponse response = slack.methods(botToken).filesRemoteUpdate(r -> r
                    .externalId(file.getExternalId())
                    .title("Slack Call (edited)"));
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));
        }

        {
            FilesRemoteShareResponse response = slack.methods(botToken).filesRemoteShare(r -> r
                    .externalId(file.getExternalId())
                    .channels(Arrays.asList(randomChannelId)));
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));
        }

        {
            FilesRemoteUpdateResponse response = slack.methods(botToken).filesRemoteUpdate(r -> r
                    .externalId(file.getExternalId())
                    .title("Slack Call (re: edited)"));
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));
        }

        {
            FilesRemoteShareResponse response = slack.methods(botToken).filesRemoteShare(r -> r
                    .file(file.getId())
                    .channels(Arrays.asList(randomChannelId)));
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));
        }

        {
            // One tricky bit: in the response, the file object may indicate that "has_rich_preview" is false,
            // even if you include preview_image.
            // That's because it takes a few seconds for Slack to parse the preview_image you pass.
            // If you call files.remote.add with the same external_id later, you'll see "has_preview_image": true.
            long millis = 0;
            FilesRemoteInfoResponse info = slack.methods(botToken).filesRemoteInfo(r -> r.externalId(externalId));
            while (!info.getFile().isHasRichPreview() && millis < 20 * 1000) {
                Thread.sleep(100);
                millis += 100;
                info = slack.methods(botToken).filesRemoteInfo(r -> r.externalId(externalId));
            }
            log.info("Preview imageData took {} milliseconds to get the imageData ready", millis);
        }

    }

    @Test
    public void updateAndShare_searchable() throws Exception {
        String externalId = "test-searchable-external-id-" + System.currentTimeMillis();
        Image image = new Image();
        FilesRemoteAddResponse addResponse = slack.methods(botToken).filesRemoteAdd(r -> r
                .externalId(externalId)
                .externalUrl(image.url)
                .previewImage(image.content)
                .indexableFileContents((externalId + ": Seamlessly start a voice call from any conversation").getBytes())
                .title("Searchable content " + externalId));
        assertThat(addResponse.getError(), is(nullValue()));
        assertThat(addResponse.isOk(), is(true));

        File file = addResponse.getFile();

        {
            String randomChannelId = slack.shortcut(ApiToken.of(botToken))
                    .findChannelIdByName(ChannelName.of("random"))
                    .get().getValue();
            FilesRemoteShareResponse response = slack.methods(botToken).filesRemoteShare(r -> r
                    .externalId(file.getExternalId())
                    .channels(Arrays.asList(randomChannelId)));
            assertThat(response.getError(), is(nullValue()));
            assertThat(response.isOk(), is(true));

        }

        boolean found = false;
        String query = externalId + ": Seamlessly start a voice call";
        long millis = 0;
        while (!found && millis < 20 * 1000) {
            Thread.sleep(500);
            millis += 500;
            SearchFilesResponse searchResults = slack.methods(userToken).searchFiles(r -> r.query(query));
            assertThat(searchResults.getError(), is(nullValue()));
            for (MatchedItem item : searchResults.getFiles().getMatches()) {
                if (item.getId().equals(file.getId())) {
                    found = true;
                    break;
                }
            }
        }
        assertTrue("The uploaded file not found", found);

        log.info("Searchable file contents took {} milliseconds to get indexed", millis);
    }

}