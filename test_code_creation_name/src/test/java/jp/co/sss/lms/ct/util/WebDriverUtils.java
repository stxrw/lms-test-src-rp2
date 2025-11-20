package jp.co.sss.lms.ct.util;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.io.Files;

/**
 * Webドライバーユーティリティ
 * @author holy
 */
public class WebDriverUtils {

	/** Webドライバ */
	public static WebDriver webDriver;

	/**
	 * インスタンス取得
	 * @return Webドライバ
	 */
	public static void createDriver() {
		System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
		webDriver = new ChromeDriver();
	}

	/**
	 * インスタンス終了
	 */
	public static void closeDriver() {
		webDriver.quit();
	}

	/**
	 * 画面を最大化
	 * @param url
	 */
	public static void maximizeWindow() {
		webDriver.manage().window().maximize();
	}

	/**
	 * 画面遷移
	 * @param url
	 */
	public static void goTo(String url) {
		webDriver.get(url);
		pageLoadTimeout(5);
	}

	/**
	 * 表示されているページのタイトルを取得
	 * @return 現在表示されているページのタイトル
	 */
	public static String getPageTitle() {
		return webDriver.getTitle();
	}

	/**
	 * id属性からWebElement要素を取得
	 * @param id
	 * @return 取得した要素
	 */
	public static WebElement getWebElementById(String id) {
		return webDriver.findElement(By.id(id));
	}

	/**
	 * class属性からWebElement要素を取得
	 * @param className
	 * @return 取得した要素
	 */
	public static WebElement getWebElementByClassName(String className) {
		return webDriver.findElement(By.className(className));
	}

	/**
	 * CSSセレクタからWebElement要素を取得
	 * @param cssSelector
	 * @return 取得した要素
	 */
	public static WebElement getWebElementByCssSelector(String cssSelector) {
		return webDriver.findElement(By.cssSelector(cssSelector));
	}

	/**
	 * 取得した要素から、指定した属性の値を取得
	 * @param element 取得したwebElement
	 * @param AttributeName 属性名
	 * @return 属性値
	 */
	public static String getAttributeFromElement(WebElement element, String AttributeName) {
		return element.getAttribute(AttributeName);
	}

	/**
	 * 取得した要素から、インナーテキストを取得
	 * @param element 取得したwebElement
	 * @return インナーテキスト
	 */
	public static String getInnerTextFromElement(WebElement element) {
		return element.getText();
	}

	/**
	 * ページロードタイムアウト設定
	 * @param second
	 */
	public static void pageLoadTimeout(int second) {
		webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(second));
	}

	/**
	 * 要素の可視性タイムアウト設定
	 * @param locater
	 * @param second
	 */
	public static void visibilityTimeout(By locater, int second) {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(second));
		wait.until(ExpectedConditions.visibilityOfElementLocated(locater));
	}

	/**
	 * 指定ピクセル分だけスクロール
	 * @param pixel
	 */
	public static void scrollBy(String pixel) {
		((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0," + pixel + ");");
	}

	/**
	 * 指定位置までスクロール
	 * @param pixel
	 */
	public static void scrollTo(String pixel) {
		((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0," + pixel + ");");
	}

	/**
	 * エビデンス取得
	 * @param instance
	 */
	public static void getEvidence(Object instance) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String className = instance.getClass().getEnclosingClass().getSimpleName();
			String methodName = instance.getClass().getEnclosingMethod().getName();
			Files.move(tempFile,
					new File(System.getProperty("user.dir") + "\\evidence\\" + className + "_" + methodName + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * エビデンス取得（サフィックスあり）
	 * @param instance
	 * @param suffix
	 */
	public static void getEvidence(Object instance, String suffix) {
		File tempFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		try {
			String className = instance.getClass().getEnclosingClass().getSimpleName();
			String methodName = instance.getClass().getEnclosingMethod().getName();
			Files.move(tempFile, new File(System.getProperty("user.dir") + "\\evidence\\" + className + "_" + methodName
					+ "_" + suffix + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
