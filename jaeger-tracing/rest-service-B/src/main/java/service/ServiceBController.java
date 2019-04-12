/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service-B")
@Slf4j
public class ServiceBController {

  @Autowired
  private StreamChannel channel;

  @GetMapping
  public String say() {
    return "service-A says something...";
  }

  @GetMapping("/error")
  public String error() {
    throw new RuntimeException("e internal exception");
  }

  @GetMapping("/message")
  public String message() {
    Message<String> msg = MessageBuilder.withPayload("hello, this is my message").build();
    channel.output().send(msg);
    return "ok";
  }

  @GetMapping("/slow")
  public String slow() {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      log.warn("internal error", e);
    }
    return "slow";
  }
}
