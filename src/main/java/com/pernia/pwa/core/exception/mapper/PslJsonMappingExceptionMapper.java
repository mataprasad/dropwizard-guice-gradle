package com.pernia.pwa.core.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonMappingException;

import com.pernia.pwa.api.response.ResponseBean;
import com.pernia.pwa.api.response.ResponseErrorNode;
import com.pernia.pwa.api.response.ResponseMetaNode;

@Provider
public class PslJsonMappingExceptionMapper implements ExceptionMapper<JsonMappingException> {
  private static final Logger logger = LoggerFactory.getLogger(PslJsonMappingExceptionMapper.class);

  @Override
  public Response toResponse(final JsonMappingException exception) {
    logger.error("Exception occurred : ", exception);
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(getResponseBean(exception))
        .build();
  }

  private ResponseBean getResponseBean(final JsonMappingException exception) {
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

  private ResponseErrorNode createResponseErrorNode(final JsonMappingException exception) {
    return new ResponseErrorNode(exception.getOriginalMessage());
  }
}
