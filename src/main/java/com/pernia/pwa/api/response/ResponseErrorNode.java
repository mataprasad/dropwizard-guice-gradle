package com.pernia.pwa.api.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;

@Data
@ApiModel("error")
@JacksonXmlRootElement
public class ResponseErrorNode {
  @JacksonXmlProperty
  private String message;
  @JacksonXmlProperty
  private JsonNode reason;

  public ResponseErrorNode(final String message) {
    this.message = message;
  }

  @Getter
  public enum Error {
    UNABLE_TO_PROCESS_REQUEST("unable to process request");

    private String reasonPhrase;

    Error(final String reasonPhrase) {
      this.reasonPhrase = reasonPhrase;
    }
  }
}
