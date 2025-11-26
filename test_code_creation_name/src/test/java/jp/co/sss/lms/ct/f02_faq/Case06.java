package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.constants.Constants.*;
import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jp.co.sss.lms.dto.FaqCategoryDto;
import jp.co.sss.lms.dto.FaqDto;
import jp.co.sss.lms.mapper.MFrequentlyAskedQuestionCategoryMapper;
import jp.co.sss.lms.mapper.MFrequentlyAskedQuestionMapper;
import jp.co.sss.lms.util.Constants;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

	@Autowired
	private MFrequentlyAskedQuestionCategoryMapper mFrequentlyAskedQuestionCategoryMapper;

	@Autowired
	private MFrequentlyAskedQuestionMapper mFrequentlyAskedQuestionMapper;

	// 検索対象カテゴリ名を設定
	String CategoryNameForSarch = "研修関係";

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

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		// 【操作】 検索するカテゴリ名をクリック
		// 表示されているカテゴリ一覧を取得
		List<WebElement> aElementList = getWebElementsByXpath("//li/a");

		// 取得したカテゴリを１つづつ走査
		if (!aElementList.isEmpty()) {
			for (WebElement element : aElementList) {

				// 検索対象カテゴリに一致した場合クリック
				if (element.getText().contains(CategoryNameForSarch)) {
					element.click();
					break;
				}
			}
		}

		// 【結果の取得】 表示された検索結果（質問）の一覧を取得
		List<String> resultQuestionList = new ArrayList<>(); // ここに質問文一覧（結果）を格納

		//　表示されている検索結果を取得し、質問内容を結果リストに格納
		List<WebElement> questionElementList = getWebElementsByXpath("//dl[@id='question-h[${status.index}]']/dt");
		for (WebElement element : questionElementList) {
			resultQuestionList.add(getInnerTextFromElement(element));
		}

		// 【期待値の取得】 期待値をDBより取得
		List<String> expectedQuestionList = new ArrayList<>(); // ここに質問文一覧（期待値）を格納

		// DBより全質問カテゴリのデータを取得
		List<FaqCategoryDto> faqCategoryDtoList = mFrequentlyAskedQuestionCategoryMapper
				.getFaqCategoryDtoList(Constants.DB_FLG_FALSE);

		// 検索カテゴリ名に一致するものに絞り込む
		Optional<FaqCategoryDto> faqCategoryDtoOpt = faqCategoryDtoList.stream()
				.filter(dto -> dto.getFrequentlyAskedQuestionCategoryName().contains(CategoryNameForSarch))
				.findFirst();

		// 該当するカテゴリがあれば、カテゴリIDを取得し、DB上の該当する質問を取得　
		if (faqCategoryDtoOpt.isPresent()) {
			Integer categoryId = faqCategoryDtoOpt.get().getFrequentlyAskedQuestionCategoryId();
			List<FaqDto> allFaq = mFrequentlyAskedQuestionMapper
					.getFaqDtoList(Constants.DB_FLG_FALSE, null, categoryId);

			// 質問文を期待値リストに追加
			for (FaqDto dto : allFaq) {
				expectedQuestionList.add(dto.getQuestion());
			}
		}

		// 【アサーション】
		// 項目数が等しいことを確認
		assertEquals(expectedQuestionList.size(), resultQuestionList.size());

		for (String expectQuestion : expectedQuestionList) {
			// すべての期待値に対応する結果があればOK
			boolean containsExpectedQuestion = false;
			for (String resultQuestion : resultQuestionList) {
				if (resultQuestion.contains(expectQuestion)) {
					containsExpectedQuestion = true;
					break;
				}
			}
			assertTrue(containsExpectedQuestion);
		}

		// 【エビデンスの取得】
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		// 【操作】 表示されている検索結果の質問を取得しクリック
		WebElement questionElement = getWebElementById("question-h[${status.index}]");
		questionElement.click();

		// 【結果の取得】 実際に表示された回答を取得
		String resultAnswer = getInnerTextFromElement(getWebElementById("answer-h[${status.index}]"));

		// 【期待値の取得】 クリックした質問の文字列を利用し、回答の期待値をDBより取得
		String clickedQuestion = getInnerTextFromElement(questionElement);
		Optional<FaqDto> fapDtoOpt = mFrequentlyAskedQuestionMapper.getFaqDtoList(Constants.DB_FLG_FALSE, null, null)
				.stream().filter(faqDto -> clickedQuestion.contains(faqDto.getQuestion())).findFirst();
		String expectedAnswer = fapDtoOpt.get().getAnswer();

		// 【アサーション】
		// 表示された回答が期待値を含んでいるか確認(半角スペースと改行は無視する)
		assertTrue(resultAnswer.replaceAll("\\s+", "").contains(expectedAnswer.replaceAll("\\s+", "")));

		// 【エビデンスの取得】
		getEvidence(new Object() {
		});
	}
}
