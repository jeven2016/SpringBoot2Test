/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.failure_skip_retry;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class SkipTaskProcessor implements ItemProcessor<String, String> {

  @Override
  public String process(String item) throws Exception {
    if (item.equals("msg: 3")) {
      throw new IllegalAccessException("count==3 will throw a exception for skipping");
    }
    System.out.println("Process " + item);
    return item;
  }
}
