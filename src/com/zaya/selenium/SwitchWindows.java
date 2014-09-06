package com.zaya.selenium;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebClient;

public class SwitchWindows {
	WebDriver driver;
	static List<String> links = new ArrayList<String>();
	static int count = 0;
	public SwitchWindows(WebDriver driver) {
		this.driver = driver;
	}

	public void switchWind() {
		String mainWinId,level1Wind=null,level2Wind=null;
		String currURL = null;
		String errorProneLink = null;
		try{
			for(WebElement link : driver.findElements(By.tagName("a"))) {
				errorProneLink = link.getAttribute("href");
				if (link.isDisplayed() && !(links.contains(link.getAttribute("href")))) {
					mainWinId = driver.getWindowHandle();
					System.out.println("main:"+mainWinId);
					links.add(link.getAttribute("href"));
					count++;
					System.out.println(link.getAttribute("href"));
					link.click();
					for(String window : driver.getWindowHandles()) {
						if(!window.equals(mainWinId)) {
							driver.switchTo().window(window);
							level1Wind = driver.getWindowHandle();
						}
					}
					//Level-1 opened in DIFFERENT WINDOW
					if(level1Wind != null) {
						for(WebElement link1 : driver.findElements(By.tagName("a"))) {
							if (link1.isDisplayed() && !(links.contains(link1.getAttribute("href")))) {
								links.add(link1.getAttribute("href"));
								count++;
								//System.out.println(link1.getAttribute("href")+"::"+getResponseCode(link1.getAttribute("href")));
								System.out.println(link1.getAttribute("href"));
								link1.click();
								for(String window1 : driver.getWindowHandles()) {
									if(!(window1.equals(level1Wind))) {
										driver.switchTo().window(window1);
										level2Wind = driver.getWindowHandle();
									}
									//Level-2 opened in DIFFERENT window
									if(level2Wind != null) {
										for(WebElement link2 : driver.findElements(By.tagName("a"))) {
											if (link2.isDisplayed() && !links.contains(link2.getAttribute("href"))) {
												links.add(link2.getAttribute("href"));
												count++;
												//System.out.println(link1.getAttribute("href")+"::"+getResponseCode(link1.getAttribute("href")));
												System.out.println(link2.getAttribute("href"));
											}
										}
										driver.close();
										driver.switchTo().window(level1Wind);
									}
									//Level-2 opened in SAME window
									else {
										for(WebElement link2 : driver.findElements(By.tagName("a"))) {
											if (link2.isDisplayed() && !links.contains(link2.getAttribute("href"))) {
												links.add(link2.getAttribute("href"));
												count++;
												//System.out.println(link1.getAttribute("href")+"::"+getResponseCode(link1.getAttribute("href")));
												System.out.println(link2.getAttribute("href"));
											}
										}
										driver.navigate().back();
									}
								}
							}
						}
						driver.close();
						driver.switchTo().window(mainWinId);
					}
					//level-1 opened in SAME WINDOW
					else {
						for(WebElement link1 : driver.findElements(By.tagName("a"))) {
							if (link1.isDisplayed() && !links.contains(link1.getAttribute("href"))) {
								links.add(link1.getAttribute("href"));
								count++;
								//System.out.println(link1.getAttribute("href")+"::"+getResponseCode(link1.getAttribute("href")));
								System.out.println(link1.getAttribute("href"));
								currURL = driver.getCurrentUrl();
								link1.click();
								//level-2 opened in different window
								System.out.println("main:"+mainWinId+"curr:"+driver.getWindowHandle());
								if(!(driver.getWindowHandle().equals(mainWinId))) {
									System.out.println("level-2 opened in different window");
									for(WebElement link2 : driver.findElements(By.tagName("a"))) {
										if (link2.isDisplayed() && !links.contains(link2.getAttribute("href"))) {
											links.add(link2.getAttribute("href"));
											count++;
											System.out.println(link2.getAttribute("href"));
										}
									}
									System.out.println(driver.getWindowHandle());
									for(String s:driver.getWindowHandles())
										System.out.println("Before:"+s);
									driver.close();
									System.out.println(driver.getWindowHandle());
									for(String s1: driver.getWindowHandles())
										System.out.println("After:"+s1);
									driver.switchTo().window(mainWinId);
									//driver.get(currURL);
								}
								//level-2 opened in same window
								else {
									System.out.println("level-2 opened in same window");
									for(WebElement link2 : driver.findElements(By.tagName("a"))) {
										if (link2.isDisplayed() && !links.contains(link2.getAttribute("href"))) {
											links.add(link2.getAttribute("href"));
											count++;
											System.out.println(link2.getAttribute("href"));
										}
									}
									driver.navigate().back();
								}
							}
						}
						driver.navigate().back();
					}
				}
			}
		}
		catch(StaleElementReferenceException e) {
			System.out.println("Error with: "+errorProneLink);
			e.printStackTrace();
		}
		System.out.println(count);
		driver.quit();
	}

	public static int getResponseCode(String url) {
		try {
			WebClient client = new WebClient();
			client.getOptions().setThrowExceptionOnScriptError(false);
			client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			client.getOptions().setJavaScriptEnabled(false);
			if(url != null)
				return client.getPage(url).getWebResponse().getStatusCode();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		return 0;
	}

	public static void main(String[] args) {
		//WebDriver driver = new HtmlUnitDriver();
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.lufthansa.com/online/portal/lh/ua/homepage");
		new SwitchWindows(driver).switchWind();
		/*for(String s : links) 
			System.out.println(s);*/
	}

}
