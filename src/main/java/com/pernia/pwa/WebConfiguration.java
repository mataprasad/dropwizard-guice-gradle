package com.pernia.pwa;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConfiguration;

import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebConfiguration extends Configuration {

  @NotEmpty
  @JsonProperty("template")
  private String template = "Stranger";

  @JsonProperty
  public String getTemplate() {
    return template;
  }

  @JsonProperty("defaultName")
  public String defaultName;

  private List<String> whiteListedSubNets;

  @JsonProperty("whiteListedSubNets")
  public void setWhiteListedSubNets(final String whiteListedSubNets) {
    String[] split = whiteListedSubNets.split(",");
    this.whiteListedSubNets = Arrays.stream(split)
        .map(StringUtils::trim)
        .collect(Collectors.toList());
  }

  @NotBlank
  @JsonProperty("appName")
  private String applicationName;

  @Valid
  @NotNull
  @JsonProperty("swagger")
  private SwaggerBundleConfiguration swaggerBundleConfiguration;

  @Valid
  @NotNull
  @JsonProperty("requestTracker")
  private RequestTrackerConfiguration requestTrackerConfiguration;
}
