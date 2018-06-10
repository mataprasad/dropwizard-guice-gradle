package com.pernia.pwa.core.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pernia.pwa.api.response.ResponseBean;
import com.pernia.pwa.api.response.ResponseErrorNode;
import com.pernia.pwa.api.response.ResponseMetaNode;


@Provider
public class PslExceptionMapper implements ExceptionMapper<Exception> {
  private static final Logger logger = LoggerFactory.getLogger(PslExceptionMapper.class);

  @Override
  public Response toResponse(final Exception exception) {
    logger.error("Exception occurred : ", exception);
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(getResponseBean())
        .build();
  }

  private ResponseBean getResponseBean() {
    ResponseMetaNode metaNode = createResponseMetaNode();
    ResponseErrorNode errorNode = createResponseErrorNode();
    return new ResponseBean(metaNode, errorNode);
  }

  private ResponseMetaNode createResponseMetaNode() {
    ResponseMetaNode metaNode = new ResponseMetaNode();
    metaNode.setStatusMessage(ResponseMetaNode.Status.FAILURE.getStatusMessage());
    metaNode.setStatusCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
    metaNode.setSuccessResponse(false);
    return metaNode;
  }

  private ResponseErrorNode createResponseErrorNode() {
    return new ResponseErrorNode(ResponseErrorNode.Error.UNABLE_TO_PROCESS_REQUEST.getReasonPhrase());
  }
}
