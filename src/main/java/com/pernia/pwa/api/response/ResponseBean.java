package com.pernia.pwa.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("response")
@JacksonXmlRootElement(localName = "response")
public class ResponseBean {
  @JsonProperty("meta")
  @JacksonXmlProperty(localName = "meta")
  private ResponseMetaNode metaNode;
  @JsonProperty("data")
  @JacksonXmlProperty(localName = "data")
  private Object dataNode;
  @JsonProperty("error")
  @JacksonXmlProperty(localName = "error")
  private ResponseErrorNode errorNode;

  public ResponseBean(final ResponseMetaNode meta) {
    this.metaNode = meta;
  }

  public ResponseBean(final ResponseMetaNode meta,
                      final JsonNode data) {
    this.metaNode = meta;
    this.dataNode = data;
  }

  public ResponseBean(final ResponseMetaNode meta,
                      final ResponseErrorNode error) {
    this.metaNode = meta;
    this.errorNode = error;
  }

  public ResponseBean(final ResponseMetaNode meta,
                      final JsonNode data,
                      final ResponseErrorNode error) {
    this.metaNode = meta;
    this.dataNode = data;
    this.errorNode = error;
  }

  public ResponseBean() {
    super();
  }


}
