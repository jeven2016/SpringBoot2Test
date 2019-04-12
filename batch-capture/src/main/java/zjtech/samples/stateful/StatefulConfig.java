/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.stateful;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * construct the stateful job with java class instead of xml
 */
@Configuration
public class StatefulConfig {

  @Bean("statefulA")
  public Step aStep(StepBuilderFactory stepFactory, StatefulATasklet aTasklet) {
    return stepFactory.get("statefulA").tasklet(aTasklet)
        .allowStartIfComplete(true).build();
  }

  @Bean("statefulB")
  public Step bStep(StepBuilderFactory stepFactory, StatefulBTasklet bTasklet) {
    return stepFactory.get("statefulB").tasklet(bTasklet)
        .allowStartIfComplete(true).build();
  }


  @Bean("statefulC")
  public Step cStep(StepBuilderFactory stepFactory, StatefulCTasklet cTasklet) {
    return stepFactory.get("statefulC").tasklet(cTasklet)
        .allowStartIfComplete(true).build();
  }

  @Bean
  public Job prepareTeaJob(JobBuilderFactory jobBuilderFactory,
      @Qualifier("statefulA") Step aStep,
      @Qualifier("statefulB") Step bStep,
      @Qualifier("statefulC") Step cStep) {
    return jobBuilderFactory.get("prepareTeaJob")
        .start(aStep)
        .next(bStep)
        .next(cStep)
        .build();
  }

}
