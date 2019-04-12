/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz;

import java.util.Random;
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
import zjtech.piczz.jobs.downloadpicture.DownloadingThreadPool;
import zjtech.piczz.jpa.entity.PictureEntity;
import zjtech.piczz.jpa.rep.PageRep;
import zjtech.piczz.jpa.rep.WebSiteRep;

@RestController
@RequestMapping("/pic")
public class PicController {

  @Autowired
  @Qualifier("analyJob")
  Job analyJob;
  @Autowired
  JobLauncher jobLauncher;
  @Autowired
  GlobalSetting globalSetting;
  /**
   * Download the picture
   */
//  @Scheduled(fixedRate = 20000)
 /*   @GetMapping("download")
    public String download()
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        return jobLauncher.run(downloadPicJob, new JobParameters()).getExitStatus()
                .getExitDescription();
    }*/

  @Autowired
  @Qualifier("dpJob")
  Job dpJob;
  @Autowired
  DownloadingThreadPool downloadingThreadPool;
  @Autowired
  private PageRep pageInfoRep;
  @Autowired
  private WebSiteRep webSiteRep;

  /**
   * Scan the website
   */
  @GetMapping("scan")
  public String scan()
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
      JobRestartException, JobInstanceAlreadyCompleteException {
    return jobLauncher.run(analyJob, new JobParameters()).getExitStatus().getExitDescription();
  }

  @GetMapping("download")
  public String download()
      throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
    downloadingThreadPool.run();
    return "yes";
  }

  @GetMapping("suspend")
  public String suspend() {
    globalSetting.setSuspending(true);
    return "suspending now";
  }

  @GetMapping("start")
  public String start() {
    globalSetting.setSuspending(false);
    return "Not suspending now";
  }

  @GetMapping("testPool")
  public String testPool() {
    PictureEntity pictureEntity = PictureEntity.builder()
        .url("http://" + new Random().nextInt(10000)).build();
    downloadingThreadPool.addPicture(pictureEntity);
    return "ok";
  }

  @GetMapping("poolSize")
  public int poolSize() {
    return downloadingThreadPool.getPoolSize();
  }
}
