package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.constants.Constants.*;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

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
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// 上部メニューの「機能」をクリック
		getWebElementByLinkText("機能").click();

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// 「機能」押下により表示された項目から「ヘルプ」をクリック → ページ遷移を待ち
		getWebElementByLinkText("ヘルプ").click();
		pageTransitionTimeOut("ヘルプ | LMS", 1);

		// 表示内容の確認(画面名)
		assertEquals("ヘルプ | LMS", getPageTitle());

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// 「よくある質問」のテキストリンクをクリック
		getWebElementByLinkText("よくある質問").click();

		// WebDriverのWindowHandleを新しいタブに切り替え
		switchTargetedTab();

		// 表示内容の確認(画面名)
		assertEquals("よくある質問 | LMS", getPageTitle());

		// エビデンスの取得
		getEvidence(new Object() {
		});
	}

}
