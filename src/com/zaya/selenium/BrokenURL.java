package com.zaya.selenium;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.SeleneseTestBase;

public class BrokenURL extends SeleneseTestBase {
 
 public int invalidLink;
 String currentLink;
 String temp;
 public DefaultSelenium selenium;
  
 @BeforeMethod
 public void setUp() throws Exception
 {
  selenium=new DefaultSelenium("localhost", 4444, "*firefox", "http://www.yahoo.com");
  selenium.start();
 }
 
 @Test
 public void testUntitled() throws Exception {
  
  FileOutputStream fout = new FileOutputStream ("broken_links.txt", true);
  invalidLink=0;
  selenium.open("/");
  int linkCount = selenium.getXpathCount("//a").intValue();
       
  new PrintStream(fout).println("URL : " + selenium.getLocation());
  new PrintStream(fout).println("--------------------------------------------");
     for (int i = 0; i < linkCount; i++) 
     {
      int statusCode=0;
      
         currentLink = "this.browserbot.getUserWindow().document.links[" + i + "]";
         temp = selenium.getEval(currentLink + ".href");
         statusCode=getResponseCode(temp);
         if (statusCode==404)
         {
          new PrintStream(fout).println(selenium.getEval(currentLink + ".href") + " "+ statusCode);
          invalidLink++; 
         }
     }
     new PrintStream(fout).println("Total broken Links = " + invalidLink);
     new PrintStream(fout).println(" ");
  fout.close();
     System.out.println(currentLink);
     System.out.println(temp);
 }
 
 public static int getResponseCode(String urlString) throws MalformedURLException, IOException {
     URL u = new URL(urlString); 
     HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
     huc.setRequestMethod("GET"); 
     huc.connect(); 
     return huc.getResponseCode();
 }


 public void tearDown()
 {
  selenium.close();
  selenium.stop();
 }
     
}
