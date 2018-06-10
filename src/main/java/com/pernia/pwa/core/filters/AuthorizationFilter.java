package com.pernia.pwa.core.filters;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pernia.pwa.WebConfiguration;


@Provider
public class AuthorizationFilter extends BaseFilter implements ContainerRequestFilter {
  private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

  @Inject
  private javax.inject.Provider<HttpServletRequest> servletRequestProvider;
  private WebConfiguration configuration;

  public AuthorizationFilter(final WebConfiguration webConfiguration) {
    this.configuration = webConfiguration;
  }

  @Override
  public void filter(final ContainerRequestContext requestContext) throws IOException {
    logger.debug("Authorization filter invoked");
    String userIpAddress = getIpAddress(servletRequestProvider.get());
    if (!isExcludedUrl(requestContext) && !isRequestFromWhiteListedIp(userIpAddress)) {
      throw new WebApplicationException("Unauthorized User", Response.Status.UNAUTHORIZED);
    }
  }

  private boolean isRequestFromWhiteListedIp(final String userIpAddress) {
    return configuration.getWhiteListedSubNets().stream()
        .filter(userIpAddress::startsWith)
        .count() > 0;
  }

  private boolean isExcludedUrl(final ContainerRequestContext requestContext) {
    return requestContext.getUriInfo().getPath().contains("swagger")
        || requestContext.getUriInfo().getPath().contains("hotel/booking/confirm");
  }
}
