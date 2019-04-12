/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.multiple_thrd;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zjtech.samples.chunk.SourceRep;

@Component
public class MtDataReader implements ItemReader<String> {

  private AtomicInteger count = new AtomicInteger();

  @Autowired
  private SourceRep sourceRep;

  @Override
  public String read() {
    int currentCount = count.get();
    if (currentCount >= 10) {
      return null;
    }
    return count.incrementAndGet() + "";
  }
}
