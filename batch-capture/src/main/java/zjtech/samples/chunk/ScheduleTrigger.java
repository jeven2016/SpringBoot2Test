/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.chunk;

import java.util.Random;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTrigger {

  @Autowired
  @Qualifier("simpleRecordsJob")
  Job job;
  @Autowired
  TaskExecutor taskExecutor;
  @Autowired
  ApplicationContext context;
  @Autowired
  private JobLauncher jobLauncher;

  /**
   * A timer to trigger a Job
   */
//  @Scheduled(fixedRate = 4000)
  public void triggerJob()
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
    System.out.println("................triggerJob");
    JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
    jobParametersBuilder.addString("jobParam", "jobParamValue" + new Random().nextInt());
    JobParameters jobParameter = jobParametersBuilder.toJobParameters();

    // In this configuration, we introduce the new Spring bean TaskExecutor, which is used for
    // JobLauncher bean creation. This setup ensures that Job is executed asynchronously by JobLauncher.
    // Asynchronous JobLauncher Example
//    ((SimpleJobLauncher) jobLauncher).setTaskExecutor(taskExecutor);

    jobLauncher.run(job, jobParameter);

    //check the count of tasks executed
    JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean(JdbcTemplate.class);
    long stepExecutionCount = jdbcTemplate
        .queryForObject("select count(*) from BATCH_STEP_EXECUTION",
            Long.class);
    System.out.println("Number of steps executed: " + stepExecutionCount);
  }


}
