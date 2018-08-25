package com.pernia.pwa.api.request;

import javax.ws.rs.QueryParam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Saying {

  @QueryParam("id")
  private Integer id;

  @QueryParam("content")
  private String content;
}
