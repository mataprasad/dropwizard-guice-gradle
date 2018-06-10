package com.pernia.pwa.core.exception.mapper;

import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pernia.pwa.api.response.ResponseBean;
import com.pernia.pwa.api.response.ResponseErrorNode;
import com.pernia.pwa.api.response.ResponseMetaNode;

import io.dropwizard.jersey.validation.ConstraintMessage;
import io.dropwizard.jersey.validation.JerseyViolationException;

@Provider
public class PslJerseyViolationExceptionMapper implements ExceptionMapper<JerseyViolationException> {
  private static final Logger logger = LoggerFactory.getLogger(PslJerseyViolationExceptionMapper.class);

  @Override
  public Response toResponse(final JerseyViolationException exception) {
    ResponseBean responseBean = getResponseBean(exception);
    logger.error("{} : {}", exception.getMessage(), responseBean.getErrorNode().getMessage());
    return Response
        .status(ConstraintMessage.determineStatus(exception.getConstraintViolations(), exception.getInvocable()))
        .entity(responseBean)
        .build();
  }

  private ResponseBean getResponseBean(final JerseyViolationException exception) {
    ResponseMetaNode metaNode = createResponseMetaNode(exception);
    ResponseErrorNode errorNode = createResponseErrorNode(exception);
    return new ResponseBean(metaNode, errorNode);
  }

  private ResponseMetaNode createResponseMetaNode(final JerseyViolationException exception) {
    ResponseMetaNode metaNode = new ResponseMetaNode();
    metaNode.setStatusMessage(ResponseMetaNode.Status.FAILURE.getStatusMessage());
    metaNode.setStatusCode(
        ConstraintMessage.determineStatus(exception.getConstraintViolations(), exception.getInvocable()));
    metaNode.setSuccessResponse(false);
    return metaNode;
  }

  private ResponseErrorNode createResponseErrorNode(final JerseyViolationException exception) {
    List<String> errors = getConstraintViolations(exception);
    String errorMessage = getErrorMessage(errors);
    return new ResponseErrorNode(errorMessage);
  }

  private List<String> getConstraintViolations(final JerseyViolationException exception) {
    return exception.getConstraintViolations()
        .stream()
        .map(constraintViolation -> constraintViolation.getPropertyPath().toString() + " "
            + constraintViolation.getMessage())
        .collect(Collectors.toList());
  }

  private String getErrorMessage(final List<String> errors) {
    return errors.stream()
        .map(String::toString)
        .collect(Collectors.joining(", "));
  }
}
