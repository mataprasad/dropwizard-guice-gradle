package com.pernia.pwa.core.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import com.pernia.pwa.api.response.ResponseBean;
import com.pernia.pwa.api.response.ResponseErrorNode;
import com.pernia.pwa.api.response.ResponseMetaNode;

@Provider
public class PslUnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {
  private static final Logger logger = LoggerFactory.getLogger(PslUnrecognizedPropertyExceptionMapper.class);

  @Override
  public Response toResponse(final UnrecognizedPropertyException exception) {
    ResponseBean responseBean = getResponseBean(exception);
    logger.error("{} : {}", exception.getMessage(), responseBean.getErrorNode().getMessage());
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(responseBean)
        .build();
  }

  private ResponseBean getResponseBean(final UnrecognizedPropertyException exception) {
    ResponseMetaNode metaNode = createResponseMetaNode();
    ResponseErrorNode errorNode = createResponseErrorNode(exception);
    return new ResponseBean(metaNode, errorNode);
  }

  private ResponseMetaNode createResponseMetaNode() {
    ResponseMetaNode metaNode = new ResponseMetaNode();
    metaNode.setStatusMessage(ResponseMetaNode.Status.FAILURE.getStatusMessage());
    metaNode.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
    metaNode.setSuccessResponse(false);
    return metaNode;
  }

  private ResponseErrorNode createResponseErrorNode(final UnrecognizedPropertyException exception) {
    return new ResponseErrorNode("unrecognized field : " + exception.getPropertyName());
  }
}
