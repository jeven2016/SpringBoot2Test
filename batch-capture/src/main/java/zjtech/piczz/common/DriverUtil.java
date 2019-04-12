/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DriverUtil {

  @Value("${driver.chrome}")
  private String chromeDriverPath;

  public WebDriver getDriver() {
    return WebDriverManager.getChromeDriver(chromeDriverPath);
  }


  public <T> void waitFor(WebDriver driver, ExpectedCondition expectedCondition,
      long timeOutInSeconds) {
    new WebDriverWait(driver, timeOutInSeconds, 2000)
        .until(expectedCondition);
  }

}
