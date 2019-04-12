/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.failure_skip_retry;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class WriterTask implements ItemWriter<String> {

  @Override
  public void write(List<? extends String> items) throws Exception {
    String mss = items.stream().collect(Collectors.joining(","));
    System.out.println("result: " + mss);
  }
}
