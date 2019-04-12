package zjtech.piczz.jobs.downloadpicture;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import zjtech.piczz.jpa.entity.PictureEntity;

@Component
@Slf4j
public class DownloadingThreadPool {

  ExecutorService executorService;
  @Value("${picczz.download-threads-count}")
  private int threadCount;
  @Autowired
  private DownloadUtil util;
  private BlockingQueue<PictureEntity> blockingQueue = new ArrayBlockingQueue(100000);

  public void run() {
    log.info("thread count is {}", threadCount);
    executorService = Executors.newFixedThreadPool(threadCount);
    for (int i = 0; i < threadCount; i++) {
      DownloadTask downloadTask = new DownloadTask(blockingQueue, util);
      executorService.submit(downloadTask);
    }
  }

  public void addPicture(PictureEntity pictureEntity) {
    try {
      blockingQueue.put(pictureEntity);
    } catch (Exception e) {
      log.info("cannot add pic to queue", pictureEntity.getUrl());
    }
  }

  public int getPoolSize() {
    return blockingQueue.size();
  }
}
