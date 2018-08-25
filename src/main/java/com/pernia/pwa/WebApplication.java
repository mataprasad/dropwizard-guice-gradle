package com.pernia.pwa;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerBundle;
import com.serviceenabled.dropwizardrequesttracker.RequestTrackerConfiguration;

import com.pernia.pwa.core.exception.mapper.*;
import com.pernia.pwa.core.filters.AuthorizationFilter;
import com.pernia.pwa.core.filters.EntryFilter;
import com.pernia.pwa.core.filters.ExitFilter;
import com.pernia.pwa.core.filters.ResponseMetaFilter;
import com.pernia.pwa.health.TemplateHealthCheck;
import com.pernia.pwa.modules.ModuleWrapper;
import com.pernia.pwa.resources.ProductResource;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class WebApplication extends Application<WebConfiguration> {

  private static final Logger logger = LoggerFactory.getLogger(WebApplication.class);
  private GuiceBundle<WebConfiguration> guiceBundle;

  public static void main(final String[] args) throws Exception {
    new WebApplication().run(args);
  }

  @Override
  public String getName() {
    return "hello-world";
  }

  @Override
  public void initialize(final Bootstrap<WebConfiguration> bootstrap) {
    addBundles(bootstrap);
    bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(
        bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
  }

  private void addBundles(final Bootstrap<WebConfiguration> bootstrap) {
    guiceBundle = createGuiceBundle();
    bootstrap.addBundle(guiceBundle);
    bootstrap.addBundle(createSwaggerBundle());
    bootstrap.addBundle(createRequestTrackerBundle());
    bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
  }

  private GuiceBundle<WebConfiguration> createGuiceBundle() {
    return GuiceBundle.<WebConfiguration>newBuilder()
        .setConfigClass(WebConfiguration.class)
        .addModule(new ModuleWrapper())
        .build(Stage.DEVELOPMENT);
  }

  private RequestTrackerBundle<WebConfiguration> createRequestTrackerBundle() {
    return new RequestTrackerBundle<WebConfiguration>() {
      @Override
      public RequestTrackerConfiguration getRequestTrackerConfiguration(WebConfiguration configuration) {
        return configuration.getRequestTrackerConfiguration();
      }
    };
  }

  private SwaggerBundle<WebConfiguration> createSwaggerBundle() {
    return new SwaggerBundle<WebConfiguration>() {
      @Override
      protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(WebConfiguration configuration) {
        return configuration.getSwaggerBundleConfiguration();
      }
    };
  }

  @Override
  public void run(final WebConfiguration configuration,
                  final Environment environment) {
    logger.info("Starting MO Web Service");
    // environment.jersey().setUrlPattern("/*");
    environment.getObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
    registerJerseyResources(environment);
    registerExceptionMapper(environment);
    registerServletRequestFilters(environment, configuration);
    registerServletResponseFilters(environment);
    enableCrossOriginRequests(environment);
    environment.jersey().register(MultiPart.class);
    environment.jersey().register(MultiPartFeature.class);

    environment.healthChecks().register("template",
        guiceBundle.getInjector().getInstance(TemplateHealthCheck.class));
  }

  private void registerJerseyResources(final Environment environment) {
    environment.jersey().register(ProductResource.class);

  }

  private void registerExceptionMapper(final Environment environment) {
    environment.jersey().register(PslJerseyViolationExceptionMapper.class);
    environment.jersey().register(PslJsonMappingExceptionMapper.class);
    environment.jersey().register(PslWebApplicationExceptionMapper.class);
    environment.jersey().register(PslUnrecognizedPropertyExceptionMapper.class);
    environment.jersey().register(PslExceptionMapper.class);
  }

  private void registerServletRequestFilters(final Environment environment,
                                             final WebConfiguration configuration) {
    int priority = 1;
    environment.jersey().getResourceConfig().register(EntryFilter.class, priority++);

    AuthorizationFilter authorizationFilter = new AuthorizationFilter(configuration);
    environment.jersey().getResourceConfig().register(authorizationFilter, priority);
  }

  private void registerServletResponseFilters(final Environment environment) {
    int priority = 100;
    environment.jersey().getResourceConfig().register(ResponseMetaFilter.class, priority--);
    environment.jersey().getResourceConfig().register(ExitFilter.class, priority);
  }

  private void enableCrossOriginRequests(final Environment environment) {
    FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
    cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
    cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST");
    cors.setInitParameter(CrossOriginFilter.CHAIN_PREFLIGHT_PARAM, Boolean.FALSE.toString());
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
  }
}
