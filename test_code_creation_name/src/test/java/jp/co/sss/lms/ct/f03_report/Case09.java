package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.constants.Constants.*;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

	// レポート修正対象の日付
	LocalDate targetDate = LocalDate.of(2022, 10, 2);
	String targetDateString = targetDate.format(DateTimeFormatter.ofPattern("yyyy年M月d日(E)"));

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		/// LMSのトップページへアクセス
		goTo(LMS_URL);

		// ブラウザを最大化
		maximizeWindow();

		// 表示内容の確認
		// (画面名)
		assertEquals("ログイン | LMS", getPageTitle());
		// (ボタンの表示文字列)
		String buttonValueString = getAttributeFromElement(getWebElementByCssSelector(".btn.btn-primary"), "value");
		assertEquals("ログイン", buttonValueString);

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// ログインIDを入力
		getWebElementById("loginId").sendKeys("StudentAA01");

		// パスワードの入力
		getWebElementById("password").sendKeys("JukouseiAA01");

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// ログインボタンの押下 → ページ遷移を待ち
		getWebElementByCssSelector(".btn.btn-primary").click();
		pageTransitionTimeOut("コース詳細 | LMS", 1);

		// 表示内容の確認
		// (画面名)
		assertEquals("コース詳細 | LMS", getPageTitle());

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		// 「ようこそ○○さん」を押下 → ページ遷移を待ち
		getWebElementByLinkText("ようこそ受講生ＡＡ１さん").click();
		pageTransitionTimeOut("ユーザー詳細", 1);

		// 表示内容の確認(画面名)
		assertEquals("ユーザー詳細", getPageTitle());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		String xpathForButtonClick = "//tr[td[contains(text(), '%s')]][td[contains(text(), '週報【デモ】')]]/td/form/input[@value='修正する']"
				.formatted(targetDateString);
		getWebElementByXpath(xpathForButtonClick).click();
		pageTransitionTimeOut("レポート登録 | LMS", 1);

		// 表示内容の確認(画面名)
		assertEquals("レポート登録 | LMS", getPageTitle());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() throws InterruptedException {
		// 学習項目名を削除
		getWebElementById("intFieldName_0").clear();

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// [提出する]ボタンを押下 → ページ遷移を待ち
		getWebElementByXpath("//button[@type='submit']").click();
		pageTransitionTimeOut("レポート登録 | LMS", 1);

		// 表示内容の確認(画面遷移が行われていないこと)
		assertEquals("レポート登録 | LMS", getPageTitle());

		// 表示内容の確認(変更した入力欄がエラー表示になっていること)
		String className = getWebElementById("intFieldName_0").getAttribute("class");
		assertTrue(className.contains("errorInput"));

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		// test05実施前の状態に戻しておく
		getWebElementById("intFieldName_0").sendKeys("ITリテラシー①");

		// 「理解度」の選択値を初期値（空文字）にする
		Select select = new Select(getWebElementById("intFieldValue_0"));
		select.selectByVisibleText("");

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// [提出する]ボタンを押下 → ページ遷移を待ち
		getWebElementByXpath("//button[@type='submit']").click();
		pageTransitionTimeOut("レポート登録 | LMS", 1);

		// 表示内容の確認(画面遷移が行われていないこと)
		assertEquals("レポート登録 | LMS", getPageTitle());

		// 表示内容の確認(変更した入力欄がエラー表示になっていること)
		String className = getWebElementById("intFieldValue_0").getAttribute("class");
		assertTrue(className.contains("errorInput"));

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		// test05実施前の状態に戻しておく
		Select select = new Select(getWebElementById("intFieldValue_0"));
		select.selectByValue("2");

		// 達成度を数値以外に変更
		WebElement elementAchieveLevel = getWebElementById("content_0");
		elementAchieveLevel.clear();
		elementAchieveLevel.sendKeys("合格");

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// [提出する]ボタンを押下 → ページ遷移を待ち
		getWebElementByXpath("//button[@type='submit']").click();
		pageTransitionTimeOut("レポート登録 | LMS", 1);

		// 表示内容の確認(画面遷移が行われていないこと)
		assertEquals("レポート登録 | LMS", getPageTitle());

		// 表示内容の確認(変更した入力欄がエラー表示になっていること)
		String className = getWebElementById("content_0").getAttribute("class");
		assertTrue(className.contains("errorInput"));

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");

	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		// 達成度を範囲外に変更
		WebElement elementAchieveLevel = getWebElementById("content_0");
		elementAchieveLevel.clear();
		elementAchieveLevel.sendKeys("100");

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// [提出する]ボタンを押下 → ページ遷移を待ち
		getWebElementByXpath("//button[@type='submit']").click();
		pageTransitionTimeOut("レポート登録 | LMS", 1);

		// 表示内容の確認(画面遷移が行われていないこと)
		assertEquals("レポート登録 | LMS", getPageTitle());

		// 表示内容の確認(変更した入力欄がエラー表示になっていること)
		String className = getWebElementById("content_0").getAttribute("class");
		assertTrue(className.contains("errorInput"));

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");

	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		// 目標の達成度を削除
		WebElement elementAchieveLevel = getWebElementById("content_0");
		elementAchieveLevel.clear();

		// 所感を削除
		WebElement elementImpression = getWebElementById("content_1");
		elementImpression.clear();

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// [提出する]ボタンを押下 → ページ遷移を待ち
		getWebElementByXpath("//button[@type='submit']").click();
		pageTransitionTimeOut("レポート登録 | LMS", 1);

		// 表示内容の確認(画面遷移が行われていないこと)
		assertEquals("レポート登録 | LMS", getPageTitle());

		// 表示内容の確認(削除した入力欄がエラー表示になっていること)
		String className = getWebElementById("content_0").getAttribute("class");
		assertTrue(className.contains("errorInput")); // 目標の達成度

		className = getWebElementById("content_1").getAttribute("class");
		assertTrue(className.contains("errorInput")); // 所感

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");

	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		// 目標の達成度をtest05実施前の状態に戻す
		WebElement elementAchieveLevel = getWebElementById("content_0");
		elementAchieveLevel.clear();
		elementAchieveLevel.sendKeys("5");

		// 所感を変更（2000文字超）
		WebElement elementImpression = getWebElementById("content_1");
		String impressionOverSize = "2000文字を超える所感" + "0123456789".repeat(200);
		elementImpression.sendKeys(impressionOverSize);

		// 一週間の振り返りを変更（2000文字超）
		WebElement elementLookingBack = getWebElementById("content_2");
		String lookingBackOverSize = "2000文字を超える振り返り" + "0123456789".repeat(200);
		elementLookingBack.clear();
		elementLookingBack.sendKeys(lookingBackOverSize);

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// [提出する]ボタンを押下 → ページ遷移を待ち
		getWebElementByXpath("//button[@type='submit']").click();
		pageTransitionTimeOut("レポート登録 | LMS", 1);

		// 表示内容の確認(画面遷移が行われていないこと)
		assertEquals("レポート登録 | LMS", getPageTitle());

		// 表示内容の確認(変更した入力欄がエラー表示になっていること)
		String className = getWebElementById("content_1").getAttribute("class");
		assertTrue(className.contains("errorInput")); // 所感

		className = getWebElementById("content_2").getAttribute("class");
		assertTrue(className.contains("errorInput")); // 一週間の振り返り

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");
	}

}
