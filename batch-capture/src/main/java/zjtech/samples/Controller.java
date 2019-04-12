/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class Controller {

  @Autowired
  JobLauncher jobLauncher;

  @Autowired
  @Qualifier("extendScopesJob")
  Job job;

  @Autowired
  @Qualifier("retryJob")
  Job retryJob;

  @Autowired
  @Qualifier("multipleThreadJob")
  Job multipleThreadJob;

  @Autowired
  @Qualifier("parallelJob")
  Job parallelJob;


  /**
   * Test extend batch jobs
   */
  @RequestMapping("/extend")
  public String extend()
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
      JobRestartException, JobInstanceAlreadyCompleteException {
    return jobLauncher.run(job, new JobParameters()).getExitStatus().getExitDescription();
  }


  @RequestMapping("/retry")
  public String retryAndSkip()
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
      JobRestartException, JobInstanceAlreadyCompleteException {
    return jobLauncher.run(retryJob, new JobParameters()).getExitStatus().getExitDescription();
  }

  @GetMapping("mt")
  public String multipleThreadChunk()
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
      JobRestartException, JobInstanceAlreadyCompleteException {
    return jobLauncher.run(multipleThreadJob, new JobParameters()).getExitStatus()
        .getExitDescription();
  }

  @GetMapping("para")
  public String paraTasks()
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
      JobRestartException, JobInstanceAlreadyCompleteException {
    return jobLauncher.run(parallelJob, new JobParameters()).getExitStatus()
        .getExitDescription();
  }
}
