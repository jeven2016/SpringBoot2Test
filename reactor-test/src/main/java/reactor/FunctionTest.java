/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;

@Slf4j
public class FunctionTest {

  @Test
  public void testFlatMap() {
    Flux.just(1, 2, 3).flatMap(val -> {
      return Flux.just(val, val * 10);
    }).subscribe(
        val -> {
          log.info("value={}", val);
        }
    );
  }


  @Test
  public void testGroupBy() {
    Flux.range(0, 10)
        .groupBy(val -> val % 2 == 0 )
        .concatMap(g -> {
          return g.map(String::valueOf);
        })
        .subscribe(val -> {
          System.out.print(val+", ");
        });
  }

}
