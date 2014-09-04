package com.zaya.selenium;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebClient;

public class TestLink {
	//list to save visited links
	static List<String> links = new ArrayList<String>();

	static Set<String> activeUrls = new HashSet<String>();
	static Set<String> urlWith401 = new HashSet<String>();
	static Set<String> urlWith500 = new HashSet<String>();
	WebDriver driver;
	Map<String, Integer> linksWithStatus;

	public static Set<String> setOfOldHandles = null;
	public static Set<String> setOfNewHandles = null;

	public TestLink(WebDriver driver) {
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
					System.out.println(link.getAttribute("href"));

					mainWinId = driver.getWindowHandle();
					saveOldHandles(driver); 
					// click on the link. This may opens a new page
					link.click(); 
					
					//StatusCodes.getResponseCode(driver.getCurrentUrl());
					saveNewHandles(driver); 

					ifNewWindowOccursFocusOnIt(driver);
					
					//status = getResponseCode(driver.getCurrentUrl());
					//linksWithStatus.put(link.getText(), status);
					
					for(WebElement link2 : driver.findElements(By.tagName("a"))) {
						System.out.println(link2.getText());
					}
					//new TestLink(driver).linkTest();
					System.out.println("going to main window:"+mainWinId);
					if(!(driver.getWindowHandle().equals(mainWinId)))
						driver.switchTo().window(mainWinId);
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
			// webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			if(url != null)
				return client.getPage(url).getWebResponse().getStatusCode();
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
		return 0;
	}


	public static void saveOldHandles(WebDriver driver) {
		if (setOfOldHandles != null) {
			setOfOldHandles.clear();
		}
		setOfOldHandles = driver.getWindowHandles(); // here we save id's of windows
	}


	// just the same for new set of id's

	public static void saveNewHandles(WebDriver driver) {
		if (setOfNewHandles != null) {
			setOfNewHandles.clear();
		}
		setOfNewHandles = driver.getWindowHandles();
	}

	// here is the code of method which decides whether to focus on a new window or not

	public static void ifNewWindowOccursFocusOnIt(WebDriver driver){
		if (setOfNewHandles != null) {
			setOfNewHandles.removeAll(setOfOldHandles); // this method removeAll() take one set and puts it in another set and if there are same positions it will erase them and leaves only that are not equals
		}
		else {

			System.out.println("setOfNewHandles is null. Can't compare old and new handles. New handle may have not enough time to load and save. Maybe you should add some time to load new window by adding Thread.Sleep(3000); - wait for 3 second ");
		}

		if (!setOfNewHandles.isEmpty()) {
			String newWindowHandle = setOfNewHandles.iterator().next(); // here IF we have new window it will shift on it
			driver.switchTo().window(newWindowHandle);
		}        
	}


	public static void main(String[] args) throws InterruptedException {
		WebDriver driver = new HtmlUnitDriver();
		driver.get("http://www.lufthansa.com/online/portal/lh/ua/homepage");
		// start recursive linkText
		new TestLink(driver).linkTest();
		/*for(String str : links) {
			System.out.println(str);
		}*/
	}
}
