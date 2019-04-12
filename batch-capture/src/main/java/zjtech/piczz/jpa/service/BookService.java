/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zjtech.piczz.jpa.entity.BookEntity;
import zjtech.piczz.jpa.rep.BookRep;

@Service
public class BookService {

  @Autowired
  BookRep bookRep;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void save(BookEntity bookEntity) {
    bookRep.save(bookEntity);
  }


}
