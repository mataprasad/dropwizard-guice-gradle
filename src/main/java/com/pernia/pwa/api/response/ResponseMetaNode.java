package com.pernia.pwa.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;

@Data
@ApiModel("meta")
@JacksonXmlRootElement
public class ResponseMetaNode {
  @JacksonXmlProperty
  private String statusMessage;
  @JacksonXmlProperty
  private Integer statusCode;
  @JacksonXmlProperty
  private String createdAt;
  @JacksonXmlProperty
  private String uid;
  @JacksonXmlProperty
  private long timeTaken;

  @JsonIgnore
  @JacksonXmlProperty
  private boolean successResponse;

  @Getter
  public enum Status {
    SUCCESS("Success"),
    FAILURE("Failure"),
    PARTIAL_SUCCESS("Partial Success");

    private String statusMessage;

    Status(final String statusMessage) {
      this.statusMessage = statusMessage;
    }
  }
}
