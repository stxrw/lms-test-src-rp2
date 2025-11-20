package jp.co.sss.lms.ct.f01_login1;

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
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

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
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		// ログインIDとしてDBに登録されていないものを入力
		getWebElementById("loginId").sendKeys("StudentAA00");

		// パスワードの入力
		getWebElementById("password").sendKeys("StudentAA00");

		// エビデンスの取得
		getEvidence(new Object() {
		}, "1");

		// ログインボタンの押下
		getWebElementByCssSelector(".btn.btn-primary").click();

		// 表示内容の確認
		// (画面名)
		assertEquals("ログイン | LMS", getPageTitle());
		// (エラーメッセージ)
		String errMsg = getInnerTextFromElement(getWebElementByCssSelector(".help-inline.error"));
		assertEquals("* ログインに失敗しました。", errMsg);

		// エビデンスの取得
		getEvidence(new Object() {
		}, "2");
	}

}
