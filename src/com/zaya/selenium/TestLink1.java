package com.zaya.selenium;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
 
public class TestLink1 {
     //list to save visited links
     static List<String> links = new ArrayList<String>();
 
     WebDriver driver;
     static int level = 0;
    
     public static Map<String, Integer> linksWithStatus = new HashMap<String,Integer>();
    
     public static Set<String> setOfOldHandles = null;
     public static Set<String> setOfNewHandles = null;
 
     public TestLink1(WebDriver driver) {
          this.driver = driver;
     }
 
     /**
      * This methode traverses through the all links in a given website
      */
     public void linkTest() {
          String mainWinId;
          int status;
          try{
              for(WebElement link : driver.findElements(By.tagName("a"))) {
                   if (link.isDisplayed()
                             && !links.contains(link.getText())) {
                        //System.out.println(link.getAttribute("href"));
                        links.add(link.getText());
                       System.out.println(link.getAttribute("href")+"::"+getResponseCode(link.getAttribute("href")));
                       linksWithStatus.put(link.getAttribute("href"), getResponseCode(link.getAttribute("href")));
                        //System.out.println(link.getText());
                        mainWinId = driver.getWindowHandle();
                       
                        saveOldHandles(driver);
                       
                        //status = getResponseCode(link.getAttribute("href"));
                        //linksWithStatus.put(link.getAttribute("href"), status);
                        //System.out.println(link.getAttribute("href")+"::"+status);
                        link.click();
                       // level++;
                        saveNewHandles(driver);
 
                        ifNewWindowOccursFocusOnIt(driver);
                       
                        if(mainWinId.equals(driver.getWindowHandle())) {
                             if(level<3) {
                                 // System.out.println(level);
                                  new TestLink1(driver).linkTest();
                             }
                             level--;
                             driver.navigate().back();
                        }else {
                             if(level<3)
                                  new TestLink1(driver).linkTest();
                             level--;
                             driver.close();
                             driver.switchTo().window(mainWinId);
                        }
                   }
              }
          }catch(StaleElementReferenceException e) {
              e.printStackTrace();
          }
     }
     
     /**
      * This method saves current opened window ids into an arraylist before clicking 'link'
      * @param driver
      */
     public static void saveOldHandles(WebDriver driver) {
          if (setOfOldHandles != null) {
              setOfOldHandles.clear();
          }
          setOfOldHandles = driver.getWindowHandles(); // here we save id's of windows
     }
 
     /**
      * This method saves current opened window ids into an arraylist after clicking 'link'
      * @param driver
      */
     public static void saveNewHandles(WebDriver driver) {
          if (setOfNewHandles != null) {
              setOfNewHandles.clear();
          }
          setOfNewHandles = driver.getWindowHandles();
     }
 
     /**
      * This method focuses newly opened browser
      * @param driver
      */
     public static void ifNewWindowOccursFocusOnIt(WebDriver driver){
          if (setOfNewHandles != null) {
              setOfNewHandles.removeAll(setOfOldHandles); 
          }
 
          if (!setOfNewHandles.isEmpty()) {
              String newWindowHandle = setOfNewHandles.iterator().next(); 
              driver.switchTo().window(newWindowHandle);
          }       
     }
 
    /**
     * 
     * @param url
     * @return 
     * This methode will accept any URL and returns its HTTP status code
     */
     public static int getResponseCode(String url) {
          try {
              WebClient client = new WebClient();
              client.getOptions().setThrowExceptionOnScriptError(false);
               client.getOptions().setThrowExceptionOnFailingStatusCode(false);
              if(url != null)
                   return client.getPage(url).getWebResponse().getStatusCode();
          } catch (IOException ioe) {
              throw new RuntimeException(ioe);
          }
          return 0;
     }
 
 
     public static void main(String[] args) throws InterruptedException {
    	 // for speed retrieving of result with out page loadings 
          WebDriver driver = new HtmlUnitDriver();
          
          // if i want to see the script in action i will use below driver
          //WebDriver driver = new FirefoxDriver();
          
          driver.get("http://www.lufthansa.com/online/portal/lh/ua/homepage");
          // start recursive linkText
          new TestLink1(driver).linkTest();
          for(Entry<String, Integer> entry : linksWithStatus.entrySet()){
              System.out.println(entry.getKey() +" :: "+ entry.getValue());
          }
     }
}
