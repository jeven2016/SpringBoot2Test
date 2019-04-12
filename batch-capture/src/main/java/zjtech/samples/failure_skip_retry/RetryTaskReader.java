/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.failure_skip_retry;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Retry Task
 */
@Component
public class RetryTaskReader implements ItemReader<String> {

  @Autowired
  private DataRep dataRep;


  @Override
  public String read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    int count = dataRep.getAndIncrement();
    if (count == 3) {
      //intercepted by retry listener
      throw new IllegalArgumentException("count==3 will throw a exception");
    }
    if (count < 5) {
      return "msg: " + count;
    }
    return null;
  }
}
