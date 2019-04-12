/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.chunk;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Repository;

@Repository
@JobScope
public class SourceRep {

  int maxNumber = 10;
  AtomicInteger count = new AtomicInteger();
  private ReentrantLock lock = new ReentrantLock();
  private Random random = new Random(10000);

  public String nextMsg() {
    lock.lock();
    try {
      if (count.get() >= 10) {
        System.out.println("return null ...............");
        return null;
      }
      String msg = "message: " + random.nextInt();
      count.getAndIncrement();
      return msg;
    } finally {
      lock.unlock();
    }
  }

}
