package com.zaya.selenium;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebClient;

public class test1 {
	//list to save visited links
	static List<String> links = new ArrayList<String>();

	static Set<String> activeUrls = new HashSet<String>();
	static Set<String> urlWith401 = new HashSet<String>();
	static Set<String> urlWith500 = new HashSet<String>();
	WebDriver driver;
	static Map<String, Integer> linksWithStatus;

	public static Set<String> setOfOldHandles = null;
	public static Set<String> setOfNewHandles = null;

	public test1(WebDriver driver) {
		this.driver = driver;
		linksWithStatus = new HashMap<String,Integer>();	
	}

	public void linkTest() {
		String mainWinId;
		int status;
		// loop over all the a elements in the page
		/*List<WebElement> linkss = new ArrayList<WebElement>();
    	linkss = driver.findElements(By.tagName("a"));
    	System.out.println(linkss.size());
    	for(WebElement link : linkss) 
    		System.out.println(link.getText());*/
		try{
			for(WebElement link : driver.findElements(By.tagName("a"))) {
				// Check if link is displayed and not previously visited
				if (link.isDisplayed() 
						&& !links.contains(link.getText())) {
					// add link to list of links already visited
					links.add(link.getText());
					
					String location = link.getAttribute("href");
					if(location.charAt(0) == '/')
						location = "https://www.lufthansa.com"+location;
					
					status = getResponseCode(location);
					
					linksWithStatus.put(link.getAttribute("href"), status);
				}
			}
			//driver.navigate().back();
		}catch(StaleElementReferenceException e) {
			e.printStackTrace();
		}
	}

	public static int getResponseCode(String url) {
		try {
			WebClient client = new WebClient();
			client.getOptions().setThrowExceptionOnScriptError(false);
			// webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			if(url != null)
				return client.getPage(url).getWebResponse().getStatusCode();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		return 0;
	}

	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new HtmlUnitDriver();
		driver.get("http://www.lufthansa.com/online/portal/lh/ua/homepage");
		// start recursive linkText
		new test1(driver).linkTest();
		for(Entry<String, Integer> entry : linksWithStatus.entrySet()){
            System.out.println(entry.getKey() +" :: "+ entry.getValue());
        }
	}
}
