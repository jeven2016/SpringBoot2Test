/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz.jpa.entity.entity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "region")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Region extends BaseEntitiy{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private Region parent;

  @OneToMany(
      cascade = {CascadeType.ALL},
      orphanRemoval = true
  )
  @JoinColumn(name = "region_id")
  private Set<Region> children = new HashSet<>();


  /**
   * Add a region children
   *
   * @param region Region
   * @throws NullPointerException if{@code region} is {@code null}
   */
  public void addChildren(Region region) {
    this.children.add(region);
  }

}
