package zjtech.piczz.common;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DriversPool {

  private static final String driverPath = "/root/Desktop/workspace/projects/SpringBoot2Test/batch-capture/drivers/chromedriver";
  private volatile boolean initlized;
  private ReentrantLock lock = new ReentrantLock();
  private Queue<WebDriver> queue;
  @Value("${driver.pool.pool-size}")
  private int poolSize;

  @Value("${driver.pool.initial-size}")
  private int initialSize;


  public void ensureInit() {
    if (initlized) {
      return;
    }
    lock.lock();
    try {
      queue = new ArrayBlockingQueue<>(poolSize);
      IntStream.range(0, initialSize).forEach(i -> {
        WebDriver driver = WebDriverManager.getChromeDriver(driverPath);
        queue.offer(driver);
      });
    } finally {
      lock.unlock();
    }
  }

  public WebDriver acquire() {
    ensureInit();
    return queue.poll();
  }

  public void release(WebDriver driver) {
    queue.offer(driver);
  }
}
