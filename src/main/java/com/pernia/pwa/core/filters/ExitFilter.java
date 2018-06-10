package com.pernia.pwa.core.filters;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Provider
public class ExitFilter extends BaseFilter implements ContainerResponseFilter {
  private static final Logger logger = LoggerFactory.getLogger(ExitFilter.class);

  @Override
  public void filter(final ContainerRequestContext requestContext,
                     final ContainerResponseContext responseContext)
      throws IOException {
    logger.debug("ExitFilter invoked");
    MDC.clear();
  }
}
