package com.zaya.selenium;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebClient;

public class SwithWindows_Recursive {
	WebDriver driver;
	static List<String> links = new ArrayList<String>();
	static int level=0;
	static Stack winStack = new Stack();
	public SwithWindows_Recursive(WebDriver driver) {
		this.driver = driver;
	}

	public List<String> getAllLinksInPage() {
		List<String> allLinksOfPage = new ArrayList<String>();
		for(WebElement link : driver.findElements(By.tagName("a"))) {
			if (link.isDisplayed() && !(links.contains(link.getAttribute("href")) && !(allLinksOfPage.contains(link.getAttribute("href"))))) {
				allLinksOfPage.add(link.getAttribute("href"));
			}
		}
		return allLinksOfPage;
	}
	
	public void switchWind(WebDriver driver) {
		String winId,currWindow=null,currWindow1=null;
		String errorProneLink = null;
		try{
			for(WebElement link : driver.findElements(By.tagName("a"))) {
				errorProneLink = link.getAttribute("href");
				if (link.isDisplayed() && !(links.contains(link.getAttribute("href")))) {
					winId = driver.getWindowHandle();
					winStack.push(winId);
					links.add(link.getAttribute("href"));
					System.out.println(link.getAttribute("href"));
					link.click();
					
					for(String window : driver.getWindowHandles()) {
						if(!window.equals(winId)) {
							winStack.push(window);
							driver.switchTo().window(window);
							currWindow = driver.getWindowHandle();
						}
					}
					if(currWindow != null) {
						if(!((++level) == 2))
							switchWind(driver);
						driver.close();
						level--;
						driver.switchTo().window(mainWinId);
					}else {
						switchWind(driver);
						driver.navigate().back();
					}
				}
			}
		}
		catch(StaleElementReferenceException e) {
			System.out.println("Error with: "+errorProneLink);
			e.printStackTrace();
		}
		driver.quit();
	}

	/*public static int getResponseCode(String url) {
		try {
			WebClient client = new WebClient();
			client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			client.getOptions().setThrowExceptionOnScriptError(false);
			if(url != null)
				return client.getPage(url).getWebResponse().getStatusCode();
		} catch (IOException ioe) {
			//throw new RuntimeException(ioe);
			ioe.printStackTrace();
		}
		return 0;
	}*/
	
	public static void main(String[] args) {
		//WebDriver driver = new HtmlUnitDriver();
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.lufthansa.com/online/portal/lh/ua/homepage");
		new SwithWindows_Recursive(driver).switchWind(driver);
		/*for(String s : links) 
			System.out.println(s);*/
	}

}
