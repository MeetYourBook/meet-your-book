package com.meetyourbook.entity;

import jakarta.persistence.Embeddable;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Slf4j
public class LibraryUrl {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String PROTOCOL_DELIMITER = "://";
    private static final String CONTENT_LIST = "/content/contentList.ink";
    private static final String ELIBRARY_FRONT = "/elibrary-front";
    private static final String QUERY_PARAMETER_TEMPLATE = "?brcd=&sntnAuthCode=&contentAll=Y&cttsDvsnCode=001&ctgrId=&orderByKey=publDate&selViewCnt=%d&pageIndex=1&recordCount=20";

    private String url;

    public String getUrlWithQueryParameters(int viewCnt) {
        return getBaseUrl() + getELibraryFront() + CONTENT_LIST + getQueryParameters(
            viewCnt);
    }

    private String getQueryParameters(int viewCnt) {
        return String.format(QUERY_PARAMETER_TEMPLATE, viewCnt);
    }

    private String getELibraryFront() {
        return url.contains(ELIBRARY_FRONT) ? ELIBRARY_FRONT : "";
    }

    public boolean hasMainInk() {
        return url.contains("main.ink");
    }

    public String getBaseUrl() {
        try {
            URI uri = new URI(url);
            return getProtocol() + PROTOCOL_DELIMITER + uri.getHost() + getPort();
        } catch (URISyntaxException e) {
            log.error("Invalid URL: {}", url, e);
            throw new IllegalArgumentException("Invalid URL provided", e);
        }
    }

    private String getProtocol() {
        return url.contains(HTTPS) ? HTTPS : HTTP;
    }

    private String getPort() {
        try {
            URI uri = new URI(this.url);
            if (uri.getPort() != -1) {
                return ":" + uri.getPort();
            }
            return "";
        } catch (URISyntaxException e) {
            log.error("Invalid URL: {}", url, e);
            throw new IllegalArgumentException("Invalid URL provided", e);
        }

    }
}