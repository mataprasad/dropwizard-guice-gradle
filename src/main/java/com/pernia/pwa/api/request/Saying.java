package com.pernia.pwa.api.request;

import javax.ws.rs.QueryParam;

import lombok.Data;


@Data
public class Saying {

  @QueryParam("id")
  private Integer id;

  @QueryParam("content")
  private String content;
}
