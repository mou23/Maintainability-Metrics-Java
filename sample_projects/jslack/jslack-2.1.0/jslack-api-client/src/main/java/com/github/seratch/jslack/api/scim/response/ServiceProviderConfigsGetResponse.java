package com.github.seratch.jslack.api.scim.response;

import com.github.seratch.jslack.api.scim.SCIMApiResponse;
import lombok.Data;

import java.util.List;

@Data
public class ServiceProviderConfigsGetResponse implements SCIMApiResponse {

    private List<AuthenticationScheme> authenticationSchemes;
    private Patch patch;
    private Bulk bulk;
    private Filter filter;
    private ChangePassword changePassword;
    private Sort sort;
    private Etag etag;
    private XmlDataFormat xmlDataFormat;

    @Data
    public static class AuthenticationScheme {
        private String type;
        private String name;
        private String description;
        private String specUrl;
        private Boolean primary;
    }

    @Data
    public static class Patch {
        private Boolean supported;
    }

    @Data
    public static class Bulk {
        private Boolean supported;
        private Integer maxOperations;
        private Integer maxPayloadSize;
    }

    @Data
    public static class Filter {
        private Boolean supported;
        private Integer maxResults;
    }

    @Data
    public static class ChangePassword {
        private Boolean supported;
    }

    @Data
    public static class Sort {
        private Boolean supported;
    }

    @Data
    public static class Etag {
        private Boolean supported;
    }

    @Data
    public static class XmlDataFormat {
        private Boolean supported;
    }

}
