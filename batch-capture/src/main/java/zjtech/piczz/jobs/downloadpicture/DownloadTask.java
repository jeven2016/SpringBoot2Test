package zjtech.piczz.jobs.downloadpicture;

import java.util.concurrent.BlockingQueue;
import lombok.extern.slf4j.Slf4j;
import zjtech.piczz.jpa.entity.PictureEntity;

@Slf4j
public class DownloadTask implements Runnable {

  private BlockingQueue<PictureEntity> blockingQueue;
  private DownloadUtil util;

  public DownloadTask(BlockingQueue<PictureEntity> blockingQueue, DownloadUtil util) {
    this.blockingQueue = blockingQueue;
    this.util = util;
  }


  @Override
  public void run() {
    PictureEntity pic;
    try {
      log.info("{} is waiting for next picture task", Thread.currentThread().getName());
      log.info("current pool size is {}", blockingQueue.size());
      while ((pic = blockingQueue.take()) != null) {
        doSomething(pic);
      }
    } catch (Exception e) {
      log.error("exit thread", e);
    }

  }

  private void doSomething(PictureEntity pic) {
    try {
      log.info(Thread.currentThread().getName() + ": pic is downloading:" + pic.getUrl());
      util.process(pic);
      log.info(Thread.currentThread().getName() + ": pic is downloaded:" + pic.getUrl());
    } catch (Exception e) {
      log.warn("failed to download picture for" + pic.getUrl(), e);
    }
  }
}
