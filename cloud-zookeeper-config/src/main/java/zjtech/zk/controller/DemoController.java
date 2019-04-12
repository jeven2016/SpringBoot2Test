/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.zk.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
//@RefreshScope
public class DemoController {

  //  @Value("${hello.name}")
  String name;


  @GetMapping("/info")
  public String getInfo() {
    return "name=" + name;
  }

  @GetMapping("/echo")
  public String echo() {
    return "echo";
  }

  @Autowired
  ZookeeperServiceRegistry serviceRegistry;

  @GetMapping("/register")
  public void register(@RequestParam("node") String node,
      @RequestBody Body body) {

  }

  @Getter
  @Setter
  static class Body {

    String name;
    String value;
  }
}
