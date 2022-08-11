package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.KoreaIT.example.JAM.util.DBUtil;
import com.KoreaIT.example.JAM.util.SecSql;

public class App {

	public void run() {
		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.printf("★ 명령어 : ");
			String cmd = sc.nextLine().trim();

			// DB 연결
			Connection conn = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");

			} catch (ClassNotFoundException e) {
				System.out.println("!! 예외 : MySql 드라이버 클래스가 없습니다. !!");
				System.out.println("프로그램을 종료합니다. :(");
				break;
			}

			String url = "jdbc:mysql://127.0.0.1:3306/article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			try {
				conn = DriverManager.getConnection(url, "root", "");

				int actionResult = doAction(conn, sc, cmd);

				if (actionResult == -1) {
					break;
				}

			} catch (SQLException e) {
				System.out.println("**** 에러 ****: " + e);
				break;
			} finally {
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private int doAction(Connection conn, Scanner sc, String cmd) {

		// 게시물 작성
		if (cmd.equals("article write")) {
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

		} // 게시물 목록
		else if (cmd.equals("article list")) {

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
				return 0;
			}

			System.out.println("== 번 호 | 제 목 | 게 시 날 짜 ==");

			for (Article article : articles) {
				System.out.printf("번호 : %d | 제목 : %s | 날짜 : %s \n", article.id, article.title, article.regDate);
			}

		} // 게시물 수정
		else if (cmd.startsWith("article modify ")) {
			int id = Integer.parseInt(cmd.split(" ")[2]);

			System.out.printf("< %d번 게시물 수정 > \n", id);
			System.out.printf("* 새로운 제목 : ");
			String title = sc.nextLine();
			System.out.printf("* 새로운 내용 : ");
			String body = sc.nextLine();

			SecSql sql = new SecSql();

			sql.append("UPDATE article");
			sql.append(" SET updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", `body` = ?", body);
			sql.append("WHERE id = ? ", id);

			DBUtil.update(conn, sql);

			System.out.printf("!! %d번 게시물 수정이 완료되었습니다 :) !!\n", id);

		} // 게시물 삭제
		else if (cmd.startsWith("article delete ")) {
			int id = Integer.parseInt(cmd.split(" ")[2]);

			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*)");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);

			int articlesCount = DBUtil.selectRowIntValue(conn, sql);

			if (articlesCount == 0) {
				System.out.printf("%d번 게시물은 존재하지 않습니다. :( \n", id);
				return 0;
			}

			System.out.printf("== %d번 게시물 삭제 ==\n", id);

			sql = new SecSql();
			sql.append("DELETE FROM article");
			sql.append("WHERE id = ?", id);

			DBUtil.delete(conn, sql);

			System.out.printf("%d번 게시물이 삭제 되었습니다. :) \n", id);

		} // 게시물 상세보기
		else if (cmd.startsWith("article detail ")) {
			int id = Integer.parseInt(cmd.split(" ")[2]);

			SecSql sql = new SecSql();
			sql.append("SELECT *");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);

			Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

			if (articleMap.isEmpty()) {
				System.out.printf("%d번 게시글은 존재하지 않습니다. :( \n", id);
				return 0;
			}

			System.out.printf("< %d번 게시물 상세보기 >", id);

			Article article = new Article(articleMap);

			System.out.printf("번    호 : %d\n", article.id);
			System.out.printf("작성 날짜 : %s\n", article.regDate);
			System.out.printf("수정 날짜 : %s\n", article.updateDate);
			System.out.printf("제    목 : %s\n", article.title);
			System.out.printf("내    용 : %s\n", article.body);
		}

		// 프로그램 종료
		if (cmd.equals("exit")) {
			System.out.println("!! 프로그램을 종료합니다 !!");
			return -1;
		}
		return 0;
	}

}