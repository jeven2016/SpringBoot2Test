/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
public class KafkaConsumerController {

  @GetMapping
  public String say() {
    return "service-A says something...";
  }

  @StreamListener(Sink.INPUT)
  public void handle(String msg) {
    log.info("handle message: " + msg);
  }
}
