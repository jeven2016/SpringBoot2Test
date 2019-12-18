/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz.entity;


import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import zjtech.piczz.jpa.entity.entity.Region;
import zjtech.piczz.jpa.entity.entity.RegionRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RegionTest {

  @Autowired
  RegionRepository rep;

  @Test
  public void create_one_two() {
    Region region = new Region();
    region.setName("parent");

    Region child1 = new Region();
    child1.setName("child1");


    Region child2 = new Region();
    child2.setName("child1");

    region.addChildren(child1);
    region.addChildren(child2);
    rep.save(region);

    List<Region> list = rep.findAll();

    System.out.println(list.size());
  }

}
