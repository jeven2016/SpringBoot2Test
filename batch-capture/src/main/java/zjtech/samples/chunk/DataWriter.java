/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.chunk;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class DataWriter implements ItemWriter<String> {

  @Override
  public void write(List<? extends String> items) throws Exception {
    String msg = items.stream().collect(Collectors.joining(","));
    System.out.println(msg + " --- is stored into a external storage\n");
  }
}
