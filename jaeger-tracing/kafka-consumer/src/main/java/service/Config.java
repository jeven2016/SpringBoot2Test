/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package service;

import io.jaegertracing.internal.samplers.ProbabilisticSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
/*
  @Bean
  public io.opentracing.Tracer jaegerTracer() {
    return new io.jaegertracing.Configuration("spring-boot",
        new io.jaegertracing.Configuration.SamplerConfiguration(ProbabilisticSampler.TYPE, 1),
        new io.jaegertracing.Configuration.ReporterConfiguration())
        .getTracer();
  }*/

//  @Bean
//  public io.opentracing.Tracer jaegerTracer() {
//
//    // The configuration is fetched from environment, so we need these environment variables:
//    // see https://github.com/jaegertracing/jaeger-client-java/blob/master/jaeger-core/README.md
//    return io.jaegertracing.Configuration.fromEnv().getTracer();
//  }
}
