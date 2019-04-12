/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.listeners;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class Job2Listener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    String msg = jobExecution.getExitStatus().getExitDescription();
    System.out.println("====== beforeJob: " + msg);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    String msg = jobExecution.getExitStatus().getExitDescription();
    System.out.println("====== afterJob: " + msg);
  }
}
