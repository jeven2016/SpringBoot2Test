/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz.jpa.rep;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import zjtech.piczz.jpa.entity.BookEntity;

public interface BookRep extends JpaRepository<BookEntity, Long> {

  void deleteByIdIn(List<Long> ids);

  boolean existsByName(String name);

}
