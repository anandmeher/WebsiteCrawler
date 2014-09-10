package com.zaya.selenium;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import com.gargoylesoftware.htmlunit.WebClient;

public class GetLinks {

	WebDriver driver;
	
	/**
	 * different list variables taken just to get more modularised level wise links
	 */
	static List<String> links0 = new ArrayList<String>();
	static List<String> links1 = new ArrayList<String>();
	static List<String> links2 = new ArrayList<String>();
	
	/**
	 * Getting all links into this list and traversing through it to get status codes of each link
	 */
	static List<String> allLinks = new ArrayList<String>();

	/**
	 * finally i am storing each link with its status code into this map
	 */
	public static Map<String, Integer> linksWithStatus = new HashMap<String,Integer>();

	static int count = 0;
	public GetLinks(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * This method will gives all the links upto the deapth of three levels
	 */
	public void getAllLinks() {
		try{
			//Level-0 links
			for(WebElement link : driver.findElements(By.tagName("a"))) {
				if (isValidLink(link)) {
					links0.add(link.getAttribute("href"));
				}
			}

			//Level-1 links
			for(String s : links0) {
				driver.get(s);
				for(WebElement link1 : driver.findElements(By.tagName("a"))) {
					if (isValidLink(link1)) {
						links1.add(link1.getAttribute("href"));
					}
				}
			}

			//Level-2 links
			for(String s : links1) {
				driver.get(s);
				for(WebElement link2 : driver.findElements(By.tagName("a"))) {
					//System.out.println("Link2:"+link2.getAttribute("href"));
					if (isValidLink(link2)) {
						links2.add(link2.getAttribute("href"));
					}
				}
			}

		}
		catch(StaleElementReferenceException e) {
		}
		allLinks.addAll(links0);
		allLinks.addAll(links1);
		allLinks.addAll(links2);

		/*System.out.println("Link count: "+links0.size());
          for(String s : links0) {
              System.out.println(s);
          }
          System.out.println("################Level-1##################");
          System.out.println("Link count: "+links1.size());
          for(String s : links1)
              System.out.println(s);
          System.out.println("################Level-2##################");
          System.out.println("Link count: "+links2.size());
          for(String s : links2)
              System.out.println(s);*/

		/*System.out.println("################Links with HTTP status codes##################");
          int status;
          for(String s : allLinks) {
              status = getResponseCode(s);
              System.out.println(s+"::"+status);
          }*/
		driver.quit();

	}

	/**
	 * This method tells whether the link to be considered or not
	 * @param link
	 * @return
	 */
	public static boolean isValidLink(WebElement link) {
		if(link.isDisplayed() && !(links0.contains(link.getAttribute("href"))) && !(links1.contains(link.getAttribute("href"))) && !(links2.contains(link.getAttribute("href")))) {
			if((link.getAttribute("href") != null))
				if(link.getAttribute("href").contains("http"))
					return true;
		}
		return false;
	}
	
	/**
	 * This methode accepts URL as string and gives its HTTP status code
	 * @param urlString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static int getResponseCodes(String urlString) throws MalformedURLException, IOException {
		URL u = new URL(urlString); 
		HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
		huc.setRequestMethod("GET"); 
		huc.connect(); 
		return huc.getResponseCode();
	}

	/**
	 * This is one more way of getting HTTP status codes but i have used above method instead of it 
	 * as it gives unnecessary warning log on 404 error
	 * @param url
	 * @return
	 */
	public static int getResponseCode(String url) {
		try { 
			WebClient client = new WebClient();
			client.getOptions().setThrowExceptionOnScriptError(false);
			client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			client.getOptions().setJavaScriptEnabled(false);
			if(url != null)
				return client.getPage(url).getWebResponse().getStatusCode();
		} catch (IOException ioe) {
			//throw new RuntimeException(ioe);
		}
		return 0;
	}

	
	public static void main(String[] args) {
		/**
		 * Intializing webdriver
		 */
		WebDriver driver = new FirefoxDriver();
		driver.get("http://www.lufthansa.com/online/portal/lh/ua/homepage");
		
		//Gets all the links upto the level of 3
		new GetLinks(driver).getAllLinks();

		//Gets the HTTP status code for each link
		for(String s:allLinks) {
			try {
				linksWithStatus.put(s, getResponseCodes(s));
				//System.out.println(getResponseCodes(str2));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("################Links with HTTP status codes################");
        for(Entry<String, Integer> entry : linksWithStatus.entrySet()){
            System.out.println(entry.getKey() +" :: "+ entry.getValue());
        }
        int i=0,j=0,k=0,l=0;
        for(int status1 : linksWithStatus.values()) {
      	  if(status1==200)
      		  i++;
      	  else if(status1==401)
      		  j++;
      	  else if(status1==404)
      		  k++;
      	  else if(status1==500)
      		  l++;
        }
        System.out.println("Total active URL(200):"+i);
        System.out.println("Total active URL(401):"+j);
        System.out.println("Total active URL(404):"+k);
        System.out.println("Total active URL(500):"+l);
	}

}
