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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

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
		// LMSのトップページへアクセス
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
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// 特定日のレポート提出状況が 提出済み であることを確認
		try {
			String xpathForSubmitStatus = "//tr[td[contains(text(), '%s')]]/td/span[contains(text(), '提出済み')]"
					.formatted(targetDateString);
			getWebElementByXpath(xpathForSubmitStatus);
		} catch (NoSuchElementException e) {
			// レポート提出状況が 提出済み でなければテスト失敗
			fail("テスト失敗（指定した日付がコースに存在しないか、当日のレポート提出状況が「提出済み」ではありません）");
		}

		// 特定日の[詳細]ボタンを押下 → ページ遷移を待ち
		String xpathForButtonClick = "//tr[td[contains(text(), '%s')]]/td/form/input[@value='詳細']"
				.formatted(targetDateString);
		getWebElementByXpath(xpathForButtonClick).click();
		pageTransitionTimeOut("セクション詳細 | LMS", 1);

		// 表示内容の確認(画面名)
		assertEquals("セクション詳細 | LMS", getPageTitle());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// [提出済み週報【デモ】を確認]ボタンを押下 → ページ遷移を待ち
		getWebElementByXpath("//input[@value='提出済み週報【デモ】を確認する']").click();
		pageTransitionTimeOut("レポート登録 | LMS", 1);

		// 表示内容の確認(画面名)
		assertEquals("レポート登録 | LMS", getPageTitle());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() {
		// 学習項目名を変更
		getWebElementById("intFieldName_0").clear();
		getWebElementById("intFieldName_0").sendKeys("ITリテラシー②");

		// 名前を変更した項目の理解度
		Select select = new Select(getWebElementById("intFieldValue_0"));
		select.selectByValue("3");

		// 達成度を変更
		WebElement elementAchieveLevel = getWebElementById("content_0");
		elementAchieveLevel.clear();
		elementAchieveLevel.sendKeys("7");

		// 所感を変更
		WebElement elementImpression = getWebElementById("content_1");
		elementImpression.clear();
		elementImpression.sendKeys("週報のサンプルです。内容をあとから変更しました。");

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// [提出する]ボタンを押下 → ページ遷移を待ち
		getWebElementByXpath("//button[@type='submit']").click();
		pageTransitionTimeOut("セクション詳細 | LMS", 1);

		// 表示内容の確認(画面名)
		assertEquals("セクション詳細 | LMS", getPageTitle());

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() {
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
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() {
		// 変更したレポートの[詳細]を押下 → ページ遷移を待ち
		String xpathForButtonClick = "//tr[td[contains(text(), '%s')]][td[contains(text(), '週報【デモ】')]]/td/form/input[@value='詳細']"
				.formatted(targetDateString);
		getWebElementByXpath(xpathForButtonClick).click();
		pageTransitionTimeOut("レポート詳細 | LMS", 1);

		// 表示内容の確認
		// (画面名)
		assertEquals("レポート詳細 | LMS", getPageTitle());

		// (修正内容_学習項目名)
		String resultLearningTopicName = getInnerTextFromElement(
				getWebElementByXpath("//tbody[tr[th[contains(text(), '学習項目')]]]/tr/td[1]"));
		assertTrue(resultLearningTopicName.contains("ITリテラシー②"));

		// (修正内容_理解度)
		String resultLearningLevel = getInnerTextFromElement(
				getWebElementByXpath("//tbody[tr[th[contains(text(), '学習項目')]]]/tr/td[2]"));
		assertTrue(resultLearningLevel.contains("3"));

		// (修正内容_目標の達成度)
		String resultAchieveLevel = getInnerTextFromElement(
				getWebElementByXpath("//tr[th[contains(text(), '目標の達成度')]]/td"));
		assertTrue(resultAchieveLevel.contains("7"));

		// (修正内容_所感)
		String resultImpression = getInnerTextFromElement(getWebElementByXpath("//tr[th[contains(text(), '所感')]]/td"));
		assertTrue(resultImpression.contains("週報のサンプルです。内容をあとから変更しました。"));

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

}
