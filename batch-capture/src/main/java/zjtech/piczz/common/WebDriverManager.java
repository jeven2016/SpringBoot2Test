/*
 * Copyright (c) 2018 Zjtech. All rights reserved.
 * This material is the confidential property of Zjtech or its
 * licensors and may be used, reproduced, stored or transmitted only in
 * accordance with a valid MIT license or sublicense agreement.
 */

package zjtech.piczz.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Web driver manager.
 */
public class WebDriverManager {

  /**
   * Init a chrome driver
   *
   * @return WebDriver
   */
  public static WebDriver getChromeDriver(String path) {
    DesiredCapabilities capabilities = init(path);
    WebDriver driver = new ChromeDriver(capabilities);
    return driver;
  }

  private static DesiredCapabilities init(String path) {
    //load the driver
    System.setProperty("webdriver.chrome.driver", path);

    //allow launching chrome by root user by default
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--no-sandbox");
    options.addArguments("--start-maximized");

    //init a web driver
    DesiredCapabilities capabilities = DesiredCapabilities.chrome();

    capabilities.setCapability(ChromeOptions.CAPABILITY, options);
    return capabilities;
  }

  /**
   * Init a firefox driver
   *
   * @return WebDriver
   */
  public WebDriver getDriver(String path) {
    System.setProperty("webdriver.gecko.driver", path);

    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("acceptInsecureCerts", true);

    FirefoxProfile profile = new FirefoxProfile();
    profile.setPreference("browser.tabs.remote.autostart.2", false);

    FirefoxOptions options = new FirefoxOptions();
    options.addPreference("app.update.enabled", false);
    options.addPreference("app.update.service.enabled", false);
    options.addPreference("app.update.auto", false);
    options.addPreference("app.update.staging", false);
    options.addPreference("app.update.silent", false);
    options.addCapabilities(capabilities);
    options.setProfile(profile);
    WebDriver driver = new FirefoxDriver(options);
    return driver;
  }
}
