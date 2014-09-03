package com.zaya.selenium;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.WebClient;

public class StatusCodes {
	WebDriver driver;
	Map<String, Integer> linksWithStatus;
	public StatusCodes(WebDriver driver) {
		this.driver = driver;
		linksWithStatus = new HashMap<String,Integer>();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebDriver driver = new HtmlUnitDriver();
		driver.get("http://www.lufthansa.com/online/portal/lh/ua/homepage");
		new StatusCodes(driver).getHTTPResponseCodes();
	}
	
	public void getHTTPResponseCodes() {
		int status;
		List<WebElement> links = new ArrayList<WebElement>();
    	links = driver.findElements(By.tagName("a"));
    	System.out.println("Total Links: "+links.size());
    	/*for(WebElement link : links) {
    		if (link.isDisplayed() 
					&& !links.contains(link.getText())) {
    			link.click();
    			
    			//status = getResponseCode(link.getAttribute("href"));
    			status = getResponseCode("http://www.lufthansa.com/online/portal/lh/ua/homepag");
    			linksWithStatus.put(link.getText(), status);
    		}
    		*/
    	status = getResponseCode("http://www.codesappers.byethost33.com");
    	//}
    		//System.out.println(link.getText());
    	//System.out.println(linksWithStatus.size());
		System.out.println(status);
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
/*	 public int getResponseCodeByHTMLClient(String url) {
	        try {
	            return Request.Get(url).execute().returnResponse().getStatusLine()
	                    .getStatusCode();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }*/

}
