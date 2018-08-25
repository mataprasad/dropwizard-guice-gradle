package com.pernia.pwa.api.request;

import javax.ws.rs.QueryParam;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Saying {

  @QueryParam("id")
  private long id;

  @QueryParam("content")
  private String content;
}
