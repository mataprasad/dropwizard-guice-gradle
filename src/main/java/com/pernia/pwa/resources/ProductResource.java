package com.pernia.pwa.resources;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    return new Saying(counter.incrementAndGet(), value);
  }
}
