package com.pernia.pwa.core.exception.mapper;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pernia.pwa.api.response.ResponseBean;
import com.pernia.pwa.api.response.ResponseErrorNode;
import com.pernia.pwa.api.response.ResponseMetaNode;


@Provider
public class PslWebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
  private static final Logger logger = LoggerFactory.getLogger(PslWebApplicationExceptionMapper.class);

  @Override
  public Response toResponse(final WebApplicationException exception) {
    logger.error("Exception occurred : ", exception);
    return Response.status(exception.getResponse().getStatus())
        .entity(getResponseBean(exception))
        .build();
  }

  private ResponseBean getResponseBean(final WebApplicationException exception) {
    ResponseMetaNode metaNode = createResponseMetaNode(exception);
    ResponseErrorNode errorNode = createResponseErrorNode(exception);
    return new ResponseBean(metaNode, errorNode);
  }

  private ResponseMetaNode createResponseMetaNode(final WebApplicationException exception) {
    ResponseMetaNode metaNode = new ResponseMetaNode();
    metaNode.setStatusMessage(ResponseMetaNode.Status.FAILURE.getStatusMessage());
    metaNode.setStatusCode(exception.getResponse().getStatus());
    metaNode.setSuccessResponse(false);
    return metaNode;
  }

  private ResponseErrorNode createResponseErrorNode(final WebApplicationException exception) {
    return new ResponseErrorNode(exception.getMessage());
  }
}
