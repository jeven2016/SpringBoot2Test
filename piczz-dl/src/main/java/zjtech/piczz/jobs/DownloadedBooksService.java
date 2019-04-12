package zjtech.piczz.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zjtech.piczz.jpa.entity.DownloadedBookEntity;
import zjtech.piczz.jpa.rep.DownloadedBookRep;

@Service
public class DownloadedBooksService {

  @Autowired
  DownloadedBookRep downloadedBookRep;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void store(DownloadedBookEntity downloadedBookEntity) {
    downloadedBookRep.saveAndFlush(downloadedBookEntity);
  }
}
