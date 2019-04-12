/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz.jobs;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zjtech.piczz.GlobalSetting;
import zjtech.piczz.common.DriverUtil;
import zjtech.piczz.jobs.downloadpicture.DownloadUtil;
import zjtech.piczz.jobs.downloadpicture.DownloadingThreadPool;
import zjtech.piczz.jpa.entity.BookEntity;
import zjtech.piczz.jpa.entity.DownloadedBookEntity;
import zjtech.piczz.jpa.entity.FailedBookEntity;
import zjtech.piczz.jpa.entity.PictureEntity;
import zjtech.piczz.jpa.rep.BlackListRep;
import zjtech.piczz.jpa.rep.BookRep;
import zjtech.piczz.jpa.rep.PictureRep;

@Component
@Slf4j
public class BookPageTask implements Tasklet {

  @Autowired
  DownloadingThreadPool threadPool;

  @Autowired
  StoreFailedBooksService storeFailedBooksService;

  @Autowired
  DownloadedBooksService downloadedBooksService;

  @Autowired
  BlackListRep blackListRep;

  @Autowired
  DownloadUtil downloadUtil;

  @Autowired
  private DriverUtil driverUtil;

  @Autowired
  private BookRep bookRep;

  @Autowired
  private PictureRep pictureRep;

  @Value("${picczz.name}")
  private String websiteName;

  @Value("${picczz.default-timeout-seconds}")
  private int timeout;

  @Autowired
  private PageMgr pageMgr;

  @Autowired
  private GlobalSetting globalSetting;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {
    log.info("begin to check books now .....");

    BookEntity bookEntity = null;
    do {
      if (globalSetting.isSuspending()) {
        log.info("suspend downloading task now.");
        TimeUnit.SECONDS.sleep(10);
        continue;
      }

      int poolSize = threadPool.getPoolSize();
      if (poolSize > 50) {
        log.info("Wait for 5 seconds , the pool size is {}", poolSize);
        TimeUnit.SECONDS.sleep(5);
        continue;
      }

      bookEntity = pageMgr.allocateBook();
      if (bookEntity == null) {
        log.info("Done, no books need to download now. ***************************************");
        break;
      }

      //ignore the black list books
      final String bookName = bookEntity.getName();
      boolean isExisted = blackListRep.findAll().stream()
          .anyMatch(blackListEntity -> bookName.contains(blackListEntity.getContainName()));
      if (isExisted) {
        log.info("{} is existed , just ignore it.", bookName);
        continue;
      }

      //check is this book duplicated
      if (downloadUtil.isBookExisted(bookEntity)) {
        log.info("Ignore the book({}) since it has been handled: {}",
            bookEntity.getName() + ", " + bookEntity.getUrl());
        continue;
      }

      log.info("Will download picture, poolSize={}", poolSize);
      try {
        handle(bookEntity);

        //record the book successfully being downloaded
        DownloadedBookEntity downloadedBookEntity = new DownloadedBookEntity();
        BeanUtils.copyProperties(bookEntity, downloadedBookEntity);
        downloadedBookEntity.setId(0);
        downloadedBooksService.store(downloadedBookEntity);
      } catch (Exception e) {
        //if failed to download , reinsert this book again and mark it.
        FailedBookEntity failedBookEntity = new FailedBookEntity();
        BeanUtils.copyProperties(bookEntity, failedBookEntity);
        failedBookEntity.setId(0);
        storeFailedBooksService.store(failedBookEntity);

        log.warn("failed to handle book : {}", bookEntity.getName());
      }
    } while (bookEntity != null);

    return RepeatStatus.FINISHED;
  }

  private void handle(BookEntity iter) throws Exception {
    // find the sub page count
    Document document = Jsoup.connect(iter.getUrl()).timeout(timeout).get();
    Elements elements = document.select(".wp-pagenavi a:nth-last-child(2)");
    String href = elements.first().attr("href");

    int splitIndex = href.lastIndexOf("/");
    String prefix = href.substring(0, splitIndex);
    int count = Integer.parseInt(href.substring(splitIndex + 1));

    // navigate to each sub page
    for (int i = 1; i <= count; i++) {
      document = Jsoup.connect(prefix + "/" + i).timeout(timeout).get();
      Elements imgs = document.select("img[class^='alignnone size-']");

      int imgIndex = 1;
      for (Element img : imgs) {
        PictureEntity pictureEntity = PictureEntity.builder().url(img.attr("src"))
            .booKName(iter.getName())
            .picIndex(imgIndex++)
            .subPageNo(i)
            .build();
        threadPool.addPicture(pictureEntity);
        log.info("add a picture into quue: {}", pictureEntity.getBooKName());
      }
    }

  }
}
