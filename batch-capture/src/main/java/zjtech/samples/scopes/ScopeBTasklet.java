/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.scopes;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScopeBTasklet implements Tasklet {

  @Autowired
  JobScopeEntity jobScopeEntity;

  @Autowired
  StepScopeEntity stepScopeEntity;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    System.out.println("jobCount is ==" + jobScopeEntity.getCount());
    System.out.println("stepCount is ==" + stepScopeEntity.getCount());
    return RepeatStatus.FINISHED;
  }
}
