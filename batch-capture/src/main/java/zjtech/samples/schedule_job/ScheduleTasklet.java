/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.schedule_job;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTasklet implements Tasklet {

  private AtomicInteger count = new AtomicInteger();

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    if (count.getAndIncrement() < 5) {
      System.out.println(">>>> continue......" + count.get());
      TimeUnit.SECONDS.sleep(3);
      return RepeatStatus.CONTINUABLE;
    }
    System.out.println(">>>> done now.");
    return RepeatStatus.FINISHED;
  }
}
