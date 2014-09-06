package com.zaya.selenium;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
		String str = "http://www.lufthansa.com/asdfasdf";
		String str1 = "http://ummyfoods.co.nr";
	
		try {
			System.out.println(new StatusCodes(driver).getResponseCodes(str1));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//new StatusCodes(driver).getHTTPResponseCodes();
		//System.out.println(new StatusCodes(driver).getResponseCode(str));
	}
	//working fine
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
	//working fine
	public static int getResponseCodes(String urlString) throws MalformedURLException, IOException {
	     URL u = new URL(urlString); 
	     HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
	     huc.setRequestMethod("GET"); 
	     huc.connect(); 
	     return huc.getResponseCode();
	 }
	
	public void getHTTPResponseCodes() {
		int status;
		List<WebElement> links = new ArrayList<WebElement>();
    	links = driver.findElements(By.tagName("a"));
    	System.out.println("Total Links: "+links.size());
    	for(WebElement link : links) {
    		if (link.isDisplayed() 
					&& !links.contains(link.getText())) {
    			link.click();
    			
    			//status = getResponseCode(link.getAttribute("href"));
    			status = getResponseCode("http://www.lufthansa.com/online/portal/lh/ua/homepag");
    			linksWithStatus.put(link.getText(), status);
    		}
    		
    	status = getResponseCode("http://www.codesappers.byethost33.com");
    	}
    		//System.out.println(link.getText());
    	//System.out.println(linksWithStatus.size());
		//System.out.println(status);
	}
	
	 /*public int getResponseCodeByHTMLClient(String url) {
	        try {
	            return Request.Get(url).execute().returnResponse().getStatusLine()
	                    .getStatusCode();
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
*/
}
