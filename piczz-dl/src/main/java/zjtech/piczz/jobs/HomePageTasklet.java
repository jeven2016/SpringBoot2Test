/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz.jobs;


import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import zjtech.piczz.jpa.entity.PageEntity;
import zjtech.piczz.jpa.entity.WebSiteEntity;
import zjtech.piczz.jpa.rep.WebSiteRep;

/**
 * Home page task to calculate the page count and the link pattern
 */
@Component
@Slf4j
public class HomePageTasklet implements Tasklet {

  @Value("${picczz.home-page}")
  private String homePageUrl;

  @Value("${picczz.default-timeout-seconds}")
  private int timeout;


  @Value("${picczz.name}")
  private String websiteName;


  @Autowired
  private WebSiteRep webSiteRep;

  @Transactional
  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    DynamicBooleanProperty ignored = DynamicPropertyFactory.getInstance()
        .getBooleanProperty("ignore.homepage", false);

    if (ignored.get()) {
      log.info(">>>> the HomePageTasklet is ignored.");
      return RepeatStatus.FINISHED;
    }

    //check if this task needs to run
    WebSiteEntity webSiteEntity = webSiteRep.findByName(websiteName);
    if (webSiteEntity != null) {
      return RepeatStatus.FINISHED;
    }

    try {
      Document doc = Jsoup.connect(homePageUrl).timeout(timeout).get();
      execute(doc);
    } catch (Exception e) {

      throw e;
    }
    return RepeatStatus.FINISHED;
  }

  private void execute(Document doc) {
    Elements elements = doc.getElementsByClass("last");
    if (elements.size() > 1) {
      throw new IllegalStateException("the last element is not unique");
    }

    Element element = elements.first();

    String link = element.attr("href");
    int lastIndex = link.lastIndexOf("/");
    String linkPrefix = link.substring(0, lastIndex);

    log.info("the prefix link of page is : {}", linkPrefix);

    int pageSize = Integer.parseInt(link.substring(lastIndex + 1));
    log.info("the page count is {}", pageSize);

    // insert pages to db
    Set<PageEntity> pageSet = IntStream.range(0, pageSize).parallel()
        .mapToObj(index -> PageEntity.builder().pageNo(index + 1).urlPrefix(linkPrefix).build())
        .collect(Collectors.toSet());

    WebSiteEntity webSiteEntity = WebSiteEntity.builder().name(websiteName).url(homePageUrl)
        .pages(pageSet)
        .build();

    // save website
    webSiteRep.save(webSiteEntity);

    log.info(">>> website {} , page count is {}", websiteName, pageSize);
  }
}
