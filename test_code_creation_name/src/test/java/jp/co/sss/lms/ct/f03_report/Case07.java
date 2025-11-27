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

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {
	// 日報を新規登録する日付
	LocalDate targetDate = LocalDate.of(2022, 10, 5);
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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// 特定日のレポート提出状況が 未提出 であることを確認
		try {
			String xpathForSubmitStatus = "//tr[td[contains(text(), '%s')]]/td/span[contains(text(), '未提出')]"
					.formatted(targetDateString);
			getWebElementByXpath(xpathForSubmitStatus);
		} catch (NoSuchElementException e) {
			// レポート提出状況が 未提出 でなければテスト失敗
			fail("テスト失敗（指定した日付がコースに存在しないか、当日のレポート提出状況が「未提出」ではありません）");
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
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// [日報【デモ】を提出する]ボタンを押下 → ページ遷移を待ち
		getWebElementByXpath("//input[@value='日報【デモ】を提出する']").click();
		pageTransitionTimeOut("レポート登録 | LMS", 1);

		// 表示内容の確認(画面名)
		assertEquals("レポート登録 | LMS", getPageTitle());

		// エビデンスの取得
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		// テキストエリアに日報の内容を入力
		getWebElementByXpath("//textarea[@name='contentArray[0]']").sendKeys("(テスト)日報の新規登録");

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// [提出する]ボタンを押下 → ページ遷移を待ち
		getWebElementByXpath("//button[@type='submit']").click();
		pageTransitionTimeOut("セクション詳細 | LMS", 1);

		// 表示内容の確認
		// (画面名)
		assertEquals("セクション詳細 | LMS", getPageTitle());

		// (ボタン名)
		String buttonValue = getAttributeFromElement(getWebElementByXpath("//input[@type='submit']"), "value");
		assertEquals("提出済み日報【デモ】を確認する", buttonValue);

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");
	}

}
