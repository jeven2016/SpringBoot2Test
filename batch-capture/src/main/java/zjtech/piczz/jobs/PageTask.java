/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz.jobs;

import com.netflix.config.DynamicBooleanProperty;
import com.netflix.config.DynamicPropertyFactory;
import java.io.IOException;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zjtech.piczz.common.DriverUtil;
import zjtech.piczz.jpa.entity.BookEntity;
import zjtech.piczz.jpa.entity.PageEntity;
import zjtech.piczz.jpa.rep.PageRep;
import zjtech.piczz.jpa.service.BookService;

@Component
@Slf4j
public class PageTask implements Tasklet {

  @Autowired
  private DriverUtil driverUtil;

  @Autowired
  private BookService bookService;

  @Autowired
  private PageRep pageRep;

  @Value("${picczz.name}")
  private String websiteName;

  @Value("${picczz.default-timeout-seconds}")
  private int timeout;


  @Autowired
  private PageMgr pageMgr;

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {

    DynamicBooleanProperty ignored = DynamicPropertyFactory.getInstance()
        .getBooleanProperty("ignore.page", false);

    if (ignored.get()) {
      return RepeatStatus.FINISHED;
    }

    PageEntity pageEntity;
    do {
      pageEntity = pageMgr.allocate(websiteName);
      if (pageEntity == null) {
        break;
      }
      handlePage(pageEntity);
    } while (pageEntity != null);

    return RepeatStatus.FINISHED;
  }

  private void handlePage(PageEntity pageEntity) throws IOException, InterruptedException {
    //get the link of this page
    String link = getPageLink(pageEntity);

    // navigate to the book
    Document doc = Jsoup.connect(link).timeout(timeout).get();

    // find the books
    Elements books = doc.getElementsByClass("post_box");

    books.forEach(book -> {
      //get title link
      Elements elements = book.select(".tit a[rel=bookmark]");
      if (elements.size() > 1) {
        throw new IllegalStateException("invlid bookmark found");
      }

      Element element = elements.first();
      String name = element.text();
      String url = element.attr("href");

      //add book
      BookEntity bookEntity = BookEntity.builder().name(name).url(url)
          .pageNo(pageEntity.getPageNo()).build();
      bookService.save(bookEntity);

    });

    log.info("the page {} is processed, {] books found in this page", pageEntity.getPageNo(),
        books.size());
  }

  private String getPageLink(PageEntity pageEntity) {
    String link = pageEntity.getUrlPrefix();
    if (!link.endsWith("/")) {
      link += "/";
    }
    link += pageEntity.getPageNo();

    log.info("Will parse page {} through url {}", pageEntity.getPageNo(), link);
    return link;
  }
}
