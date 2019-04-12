/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zjtech.piczz.jpa.entity.BookEntity;
import zjtech.piczz.jpa.entity.FailedBookEntity;
import zjtech.piczz.jpa.entity.PageEntity;
import zjtech.piczz.jpa.rep.BookRep;
import zjtech.piczz.jpa.rep.FailedBookRep;
import zjtech.piczz.jpa.rep.PageRep;

@Component
@Slf4j
public class PageMgr {

  private int apgeSize = 1;
  private ReentrantLock lock = new ReentrantLock();

  private BlockingQueue<PageEntity> evictedList = new ArrayBlockingQueue(100000);
  private List<PageEntity> avaliablePages = new ArrayList<>();

  @Autowired
  private PageRep pageRep;

  @Autowired
  private BookRep bookRep;

  @Autowired
  private FailedBookRep failedBookRep;


  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public PageEntity allocate(String websiteName) {
    PageEntity pageEntity;
    Sort sort = Sort.by(Sort.Order.asc("pageNo"));
    Pageable pageable = PageRequest.of(0, apgeSize, sort);
    Page<PageEntity> page = pageRep.findAll(pageable);
    List<PageEntity> list = page.getContent();
    if (list.isEmpty()) {
      return null;
    }
    pageRep.delete(list.get(0));
    pageRep.flush();
    return list.get(0);
  }


  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public BookEntity allocateBook() {
    List<BookEntity> list = getBookEntities(bookRep);
    if (list.isEmpty()) {

      List<FailedBookEntity> failedBookEntityList = getBookEntities(bookRep);
      if (failedBookEntityList.isEmpty()) {
        return null;
      }
      log.info("Book list is empty but failed list is not empty.....");
      FailedBookEntity failedBookEntity = failedBookEntityList.get(0);
      failedBookRep.delete(failedBookEntity);
      failedBookRep.flush();

      BookEntity bookEntity = BookEntity.builder().build();
      BeanUtils.copyProperties(failedBookEntity, bookEntity);

      return bookEntity;
    }
    bookRep.delete(list.get(0));
    bookRep.flush();
    return list.get(0);
  }

  private List getBookEntities(JpaRepository rep) {
    Sort sort = Sort.by(Sort.Order.asc("pageNo"));
    Pageable pageable = PageRequest.of(0, apgeSize, sort);
    Page page = rep.findAll(pageable);
    return page.getContent();
  }
}
