package com.zaya.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Demo1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WebDriver driver = new FirefoxDriver();
		driver.get("http://stackoverflow.com/");
		System.out.println(driver.getWindowHandle());
		driver.findElement(By.linkText("Ask Question")).click();
		System.out.println(driver.getWindowHandle());
		
		
	}

}
