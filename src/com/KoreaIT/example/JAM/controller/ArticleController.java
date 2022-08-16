package com.KoreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.example.JAM.Article;
import com.KoreaIT.example.JAM.service.ArticleService;

public class ArticleController extends Controller {
	
	private ArticleService articleService;

	public ArticleController(Connection conn, Scanner sc) {
		super(sc);
		articleService = new ArticleService(conn);
	}

	// 게시물 작성
	public void doWrite(String cmd) {
		System.out.println("< 게시물 작성 >");
		System.out.printf("* 제목 : ");
		String title = sc.nextLine();
		System.out.printf("* 내용 : ");
		String body = sc.nextLine();
		
		int id = articleService.doWrite(title, body);

		System.out.printf("%d번 게시물 작성이 완료되었습니다. :) \n", id);
	}

	// 게시물 목록
	public void showList(String cmd) {
		System.out.println("< 게시물 목록 >");
		
		List<Article> articles = articleService.getArticles();
		

		if (articles.size() == 0) {
			System.out.println("등록된 게시물이 존재하지 않습니다. :(");
			return;
		}

		System.out.println("== 번 호 | 제 목 | 게 시 날 짜 ==");

		for (Article article : articles) {
			System.out.printf("번호 : %d | 제목 : %s | 날짜 : %s \n", article.id, article.title, article.regDate);
		}
	}

	// 게시물 삭제
	public void doDelete(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);
		
		boolean isArticleExists = articleService.isArticleExists(id);

		if (isArticleExists == false) {
			System.out.printf("%d번 게시물은 존재하지 않습니다. :( \n", id);
			return;
		}

		System.out.printf("== %d번 게시물 삭제 ==\n", id);

		articleService.doDelete(id);

		System.out.printf("%d번 게시물이 삭제 되었습니다. :) \n", id);
	}

	// 게시물 수정
	public void doModify(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticleById(id);

		if (article == null) {
			System.out.printf("%d번 게시물은 존재하지 않습니다. :( \n", id);
			return;
		}

		System.out.printf("< %d번 게시물 수정 > \n", id);
		System.out.println("* 현재 제목 : " + article.title);
		System.out.printf("* 새로운 제목 : ");
		String title = sc.nextLine();
		System.out.println("* 현재 내용 : " + article.body);
		System.out.printf("* 새로운 내용 : ");
		String body = sc.nextLine();
		
		articleService.doUpdate(id,title,body);

		System.out.printf("!! %d번 게시물 수정이 완료되었습니다 :) !!\n", id);
	}

	// 게시물 상세보기
	public void showDetail(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);
		
		Article article = articleService.getArticleById(id);
		
		if (article == null) {
			System.out.printf("%d번 게시글은 존재하지 않습니다. :( \n", id);
			return;
		}

		System.out.printf("< %d번 게시물 상세보기 > \n", id);

		System.out.printf("번    호 : %d\n", article.id);
		System.out.printf("작성 날짜 : %s\n", article.regDate);
		System.out.printf("수정 날짜 : %s\n", article.updateDate);
		System.out.printf("제    목 : %s\n", article.title);
		System.out.printf("내    용 : %s\n", article.body);
	}

}
