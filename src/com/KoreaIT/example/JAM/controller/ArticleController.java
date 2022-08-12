package com.KoreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.example.JAM.Article;
import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class ArticleController extends Controller {
	private Connection conn;
	private Scanner sc;

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public void setScanner(Scanner sc) {
		this.sc = sc;
	}

	public void doWrite(String cmd) {
		System.out.println("< 게시물 작성 >");
		System.out.printf("* 제목 : ");
		String title = sc.nextLine();
		System.out.printf("* 내용 : ");
		String body = sc.nextLine();

		SecSql sql = new SecSql();

		sql.append("INSERT INTO article");
		sql.append(" SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);

		int id = DBUtil.insert(conn, sql);

		System.out.printf("%d번 게시물 작성이 완료되었습니다. :) \n", id);
	}

	public void showList(String cmd) {
		System.out.println("< 게시물 목록 >");

		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();

		sql.append("SELECT *");
		sql.append(" FROM article");
		sql.append(" ORDER BY id DESC");

		List<Map<String, Object>> articlesListMap = DBUtil.selectRows(conn, sql);

		for (Map<String, Object> articleMap : articlesListMap) {
			articles.add(new Article(articleMap));
		}

		if (articles.size() == 0) {
			System.out.println("등록된 게시물이 존재하지 않습니다. :(");
			return;
		}

		System.out.println("== 번 호 | 제 목 | 게 시 날 짜 ==");

		for (Article article : articles) {
			System.out.printf("번호 : %d | 제목 : %s | 날짜 : %s \n", article.id, article.title, article.regDate);
		}		
	}

	public void doDelete(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);

		int articlesCount = DBUtil.selectRowIntValue(conn, sql);

		if (articlesCount == 0) {
			System.out.printf("%d번 게시물은 존재하지 않습니다. :( \n", id);
			return;
		}

		System.out.printf("== %d번 게시물 삭제 ==\n", id);

		sql = new SecSql();
		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);

		DBUtil.delete(conn, sql);

		System.out.printf("%d번 게시물이 삭제 되었습니다. :) \n", id);		
	}

	public void doModify(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);
		
		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);

		int articlesCount = DBUtil.selectRowIntValue(conn, sql);

		if (articlesCount == 0) {
			System.out.printf("%d번 게시물은 존재하지 않습니다. :( \n", id);
			return;
		}

		System.out.printf("< %d번 게시물 수정 > \n", id);
		System.out.printf("* 새로운 제목 : ");
		String title = sc.nextLine();
		System.out.printf("* 새로운 내용 : ");
		String body = sc.nextLine();

		sql = new SecSql();

		sql.append("UPDATE article");
		sql.append(" SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", `body` = ?", body);
		sql.append("WHERE id = ? ", id);

		DBUtil.update(conn, sql);

		System.out.printf("!! %d번 게시물 수정이 완료되었습니다 :) !!\n", id);
	}

	public void showDetail(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);

		Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

		if (articleMap.isEmpty()) {
			System.out.printf("%d번 게시글은 존재하지 않습니다. :( \n", id);
			return;
		}

		System.out.printf("< %d번 게시물 상세보기 > \n", id);

		Article article = new Article(articleMap);

		System.out.printf("번    호 : %d\n", article.id);
		System.out.printf("작성 날짜 : %s\n", article.regDate);
		System.out.printf("수정 날짜 : %s\n", article.updateDate);
		System.out.printf("제    목 : %s\n", article.title);
		System.out.printf("내    용 : %s\n", article.body);
	}
	
	

}
