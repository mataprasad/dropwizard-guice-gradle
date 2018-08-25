package com.pernia.pwa.resources;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;

import com.pernia.pwa.api.request.Saying;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/products")
public class ProductResource {
  private final String template;
  private final String defaultName;
  private final AtomicLong counter;

  @Inject
  public ProductResource() {
    this.template = "template %s";
    this.defaultName = "defaultName";
    this.counter = new AtomicLong();
  }

  @GET
  @ApiOperation(value = "Product Details", tags = "Product Details",
      notes = "Fetch the product information for given product id")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of product information."),
      @ApiResponse(code = 500, message = "Internal server error"), @ApiResponse(code = 400, message = "Bad Request")})
  @Timed
  public Saying sayHello(@QueryParam("productId") Optional<String> name,
                         @Context final HttpServletRequest servletRequest) {
    final String value = String.format(template, name.orElse(defaultName));
    Saying saying = new Saying();
    saying.setContent(value);
    saying.setId(counter.incrementAndGet());
    return saying;
  }

  @GET
  @Path("/a")
  public Saying sayHello1(@BeanParam Saying obj) {

    if (Objects.nonNull(obj)) {
      obj.setId(counter.incrementAndGet());
    }
    return obj;
  }
}
