package zjtech.legacy;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import zjtech.piczz.common.WebDriverManager;

public class HomePage {

  private static final String driverPath = "/root/Desktop/workspace/projects/SpringBoot2Test/batch-capture/drivers/chromedriver";

  public static void main(String[] args) {
    WebDriver driver = WebDriverManager.getChromeDriver(driverPath);
    HomePage page = new HomePage();
    page.parseBook(driver);
    page.getPageCount(driver);
  }

  //分析首页链接情况
  public void parseBook(WebDriver driver) {
    driver.get("http://www.177piczz.info/html/category/tt");
    waitFor(driver, ExpectedConditions.visibilityOfElementLocated(By.id("primary")), 10);

    getPageCount(driver);

    //list all books of a page
    listBooks(driver);


  }

  private void listBooks(WebDriver driver) {
    List<WebElement> bookList = driver.findElements(By.xpath("//div[@class='post_box']"));
    bookList.forEach(book -> {
      //get title link
      WebElement titleElement = book.findElement(By.xpath(".//a[@rel='bookmark']"));
      System.out.println(titleElement.getAttribute("href") + "---" + titleElement.getText());
      String bookSrc = titleElement.getAttribute("href");

      WebElement imageElement = book.findElement(By.xpath(".//img"));
      System.out.println(imageElement.getAttribute("src"));
      System.out.println();

      //goto this book
      driver.get(bookSrc);
      waitFor(driver, ExpectedConditions.invisibilityOfElementLocated(By.id("wp-pagenavi")), 10);
      //list all pictures
      System.out.println("pictures:======");
      List<WebElement> pictures = driver
          .findElements(By.xpath("//img[contains(@class,'alignnone')]"));
      System.out.println("pic=" + pictures.size());

      for (WebElement element : pictures) {
        System.out.println("img=" + element.getAttribute("src"));
      }

      //todo: back to previous page
//            driver.navigate().back();
      return;
    });
  }

  public void getPageCount(WebDriver driver) {
    WebElement lastPageLink = driver
        .findElement(By.xpath("//div[@class='wp-pagenavi']/a[@class='last']"));
    String link = lastPageLink.getAttribute("href");
    int lastIndex = link.lastIndexOf("/");
    int page = Integer.valueOf(link.substring(lastIndex + 1));
    System.out.println("page=" + page);
  }

  private void waitFor(WebDriver driver, ExpectedCondition expectedCondition,
      long timeOutInSeconds) {
    new WebDriverWait(driver,
        timeOutInSeconds, 2000)
        .until(expectedCondition);
  }
}
