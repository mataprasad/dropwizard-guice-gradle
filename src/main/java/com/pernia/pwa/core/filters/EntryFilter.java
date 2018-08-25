package com.pernia.pwa.core.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Provider
public class EntryFilter extends BaseFilter implements ContainerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(EntryFilter.class);

  @Inject
  private javax.inject.Provider<HttpServletRequest> servletRequestProvider;

  @Override
  public void filter(final ContainerRequestContext requestContext) {
    logger.debug("EntryFilter invoked");
    String correlationId = getCorrelationId();
    servletRequestProvider.get().setAttribute(RQ_CORRELATION_ID, correlationId);
    servletRequestProvider.get().setAttribute(RQ_REQUEST_TIME, System.currentTimeMillis());
    setMDC(servletRequestProvider.get());
  }

  private void setMDC(final HttpServletRequest httpRequest) {
    MDC.put("ip", getIpAddress(httpRequest));
  }
}
