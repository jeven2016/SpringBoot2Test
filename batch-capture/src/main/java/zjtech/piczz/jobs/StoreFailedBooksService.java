package zjtech.piczz.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zjtech.piczz.jpa.entity.FailedBookEntity;
import zjtech.piczz.jpa.rep.FailedBookRep;

@Service
public class StoreFailedBooksService {

  @Autowired
  FailedBookRep failedBookRep;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void store(FailedBookEntity failedBookEntity) {
    failedBookRep.save(failedBookEntity);
  }
}
