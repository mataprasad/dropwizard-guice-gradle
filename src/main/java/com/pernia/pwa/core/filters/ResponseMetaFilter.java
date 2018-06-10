package com.pernia.pwa.core.filters;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.pernia.pwa.api.response.ResponseBean;

@Provider
public class ResponseMetaFilter extends BaseFilter implements ContainerResponseFilter {
  private static final Logger logger = LoggerFactory.getLogger(ResponseMetaFilter.class);

  @Inject
  private javax.inject.Provider<HttpServletRequest> servletRequestProvider;

  @Override
  public void filter(final ContainerRequestContext requestContext,
                     final ContainerResponseContext responseContext)
      throws IOException {
    try {
      logger.debug("ResponseMetaFilter invoked");
      setResponseEntity(responseContext);
    } catch (Exception e) {
      logger.error("Exception applying response meta filter", e);
    }
  }

  private void setResponseEntity(final ContainerResponseContext responseContext) {
    if (responseContext.getEntity() instanceof ResponseBean) {
      ResponseBean responseBean = (ResponseBean) responseContext.getEntity();
      int status = responseBean.getMetaNode().getStatusCode();
      responseContext.setStatus(status);

      responseBean.getMetaNode().setTimeTaken(getTimeTaken());
      responseBean.getMetaNode().setCreatedAt(LocalDateTime.now().toString());
      responseBean.getMetaNode().setUid(MDC.get("Correlation-Id"));
    }
  }

  private long getTimeTaken() {
    long endTime = System.currentTimeMillis();
    long startTime = (Long) servletRequestProvider.get().getAttribute(RQ_REQUEST_TIME);
    return endTime - startTime;
  }
}
