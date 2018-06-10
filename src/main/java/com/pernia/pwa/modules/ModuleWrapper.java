package com.pernia.pwa.modules;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import com.pernia.pwa.WebConfiguration;
import com.pernia.pwa.api.request.Saying;
import com.pernia.pwa.core.exception.mapper.*;
import com.pernia.pwa.core.filters.EntryFilter;
import com.pernia.pwa.core.filters.ExitFilter;
import com.pernia.pwa.core.filters.ResponseMetaFilter;
import com.pernia.pwa.health.TemplateHealthCheck;
import com.pernia.pwa.resources.ProductResource;


public class ModuleWrapper extends AbstractModule {

  @Override
  protected void configure() {

    bind(PslJerseyViolationExceptionMapper.class);
    bind(PslJsonMappingExceptionMapper.class);
    bind(PslWebApplicationExceptionMapper.class);
    bind(PslUnrecognizedPropertyExceptionMapper.class);
    bind(PslExceptionMapper.class);

    bind(EntryFilter.class);
    bind(ResponseMetaFilter.class);
    bind(ExitFilter.class);

    bind(ProductResource.class);
    bind(TemplateHealthCheck.class);
    bind(Saying.class);
  }

  @Provides
  @Singleton
  public String provideTemplate(final WebConfiguration configuration) {
    return configuration.getTemplate();
  }
}
