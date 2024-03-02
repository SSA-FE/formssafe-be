package com.formssafe.global.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private static void logRequest(RequestWrapper request) throws IOException {
        String queryString = request.getQueryString();
        log.info("Request : {} uri=[{}] content-type=[{}]", request.getMethod(),
                queryString == null ? request.getRequestURI() : request.getRequestURI() + queryString,
                request.getContentType());
        logRequestPayload(request.getContentType(), request.getInputStream());
    }

    private static void logResponse(ContentCachingResponseWrapper response, long elapsedTime) {
        log.info("Response : status=[{}] elapsed time=[{}ms]", response.getStatus(), elapsedTime);
    }

    private static void logRequestPayload(String contentType, InputStream inputStream) throws IOException {
        boolean visible = isVisible(MediaType.valueOf(contentType == null ? "application/json" : contentType));
        if (visible) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            if (content.length > 0) {
                String contentString = new String(content);
                log.info("{} Payload: {}", "Request", contentString);
            }
        } else {
            log.info("{} Payload: Binary Content", "Request");
        }
    }

    private static boolean isVisible(MediaType mediaType) {
        final List<MediaType> visibleTypes = Arrays.asList(MediaType.valueOf("text/*"),
                MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
                MediaType.valueOf("application/*+json"), MediaType.valueOf("application/*+xml"),
                MediaType.MULTIPART_FORM_DATA);
        return visibleTypes.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        MDC.put("traceId", UUID.randomUUID().toString());
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(new RequestWrapper(request), new ResponseWrapper(response), filterChain);
        }
        MDC.clear();
    }

    protected void doFilterWrapped(RequestWrapper request, ContentCachingResponseWrapper response,
                                   FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();
        try {
            logRequest(request);
            filterChain.doFilter(request, response);
        } finally {
            logResponse(response, System.currentTimeMillis() - start);
            response.copyBodyToResponse();
        }
    }
}