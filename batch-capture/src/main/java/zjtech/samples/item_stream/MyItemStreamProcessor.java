/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.samples.item_stream;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.stereotype.Component;

@Component
public class MyItemStreamProcessor implements ItemStream {

  @Override
  public void open(ExecutionContext executionContext) throws ItemStreamException {
    System.out.println(">>>> open db connection");
  }

  @Override
  public void update(ExecutionContext executionContext) throws ItemStreamException {
    System.out.println(">>>> update db connection");
  }

  @Override
  public void close() throws ItemStreamException {
    System.out.println(">>>> close db connection");
  }
}
