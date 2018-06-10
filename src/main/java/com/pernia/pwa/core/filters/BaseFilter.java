package com.pernia.pwa.core.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.common.base.Splitter;

class BaseFilter {
  static final String RQ_REQUEST_TIME = "tg-request-time",
      RQ_CORRELATION_ID = "Correlation-Id";

  private static final String AUTHORIZATION = "Authorization",
      X_FORWARDED_FOR = "X-Forwarded-For";

  private static final Logger logger = LoggerFactory.getLogger(BaseFilter.class);

  String getCorrelationId() {
    return MDC.get(RQ_CORRELATION_ID);
  }

  String getIpAddress(final HttpServletRequest request) {
    String ip = null;
    try {
      if (request.getHeader(X_FORWARDED_FOR) != null) {
        ip = Splitter.on(',').trimResults().omitEmptyStrings().split(request.getHeader(X_FORWARDED_FOR))
            .iterator().next();
      }
      if (ip == null) {
        ip = request.getRemoteAddr();
      }
    } catch (Exception ignored) {
      logger.error("Unable to get IP", ignored);
    }
    return ip;
  }

  String getAuthorizationHeader(final HttpServletRequest request) {
    return request.getHeader(AUTHORIZATION);
  }
}
