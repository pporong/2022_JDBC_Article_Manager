package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

	public void run() {

		Scanner sc = new Scanner(System.in);

		int lastArticleId = 0;

		while (true) {
			System.out.printf("명령어 : ");
			String cmd = sc.nextLine().trim();

			// DB 연결
			Connection conn = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
//				System.out.println("연결 성공!");
			} catch (ClassNotFoundException e) {
				System.out.println("!! 예외 : MySql 드라이버 클래스가 없습니다. !!");
				System.out.println("프로그램을 종료합니다 :(");
				break;
			}

			String url = "jdbc:mysql://127.0.0.1:3306/article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			// DB 존재
			try {
				conn = DriverManager.getConnection(url, "root", "");

				int actionResult = doAction(conn, sc, cmd);

				if (actionResult == -1) {
					System.out.println(" == 종료합니다. == ");
					break;
				}
			} catch (SQLException e) {
				System.out.println("*** 에러 **** : " + e);
			} finally {
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private int doAction(Connection conn, Scanner sc, String cmd) {

		if (cmd.equals("article write")) {
			System.out.println("< 게시물 작성 >");
			int id = lastArticleId + 1;
			System.out.printf("제목 : ");
			String title = sc.nextLine();
			System.out.printf("내용 : ");
			String body = sc.nextLine();

			Article article = new Article(id, title, body);

			PreparedStatement pstmt = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://127.0.0.1:3306/article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

				conn = DriverManager.getConnection(url, "root", "");
				System.out.println("연결 성공!");

				String sql = "INSERT INTO article";
				sql += " SET regDate = NOW()";
				sql += ", updateDate = NOW()";
				sql += ", title = ('" + title + "')";
				sql += ", `body` = ('" + body + "')";

				System.out.println(sql);

				pstmt = conn.prepareStatement(sql);

				int affectedRows = pstmt.executeUpdate();

				System.out.println("affectedRows : " + affectedRows);

			} catch (SQLException e) {
				System.out.println("에러: " + e);
			} finally {
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			System.out.println(article);

		}
		// article list
		else if (cmd.equals("article list")) {
			System.out.println("< 게시물 목록 >");
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			List<Article> articles = new ArrayList<>();

			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://127.0.0.1:3306/article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

				conn = DriverManager.getConnection(url, "root", "");
				System.out.println("연결 성공 !!");

				String sql = "SELECT * ";
				sql += " FROM article";
				sql += " ORDER BY id DESC";

				System.out.println(sql);

				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();

				while (rs.next()) {
					int id = rs.getInt("id");
					String regDate = rs.getString("regDate");
					String updateDate = rs.getString("updateDate");
					String title = rs.getString("title");
					String body = rs.getString("body");

					Article article = new Article(id, regDate, updateDate, title, body);

					articles.add(article);

				}

			} catch (ClassNotFoundException e) {
				System.out.println("드라이버 로딩 실패");
			} catch (SQLException e) {
				System.out.println("에러: " + e);
			} finally {
				try {
					if (rs != null && !rs.isClosed()) {
						rs.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// 게시물 없는 경우
			if (articles.size() == 0) {
				System.out.println("게시물이 없습니다. :( ");
				continue;
			}
			System.out.println(" [ 번호 / 제목 / 게시날짜 ] ");

			for (int i = articles.size() - 1; i >= 0; i--) {
				Article article = articles.get(i);
				System.out.printf("번호 : %d | 제목 : %s | 날짜 : %s \n", article.id, article.title, article.regDate);
			}

		}
		// 게시물 수정
		else if (cmd.startsWith("article modify ")) {
			int id = Integer.parseInt(cmd.split(" ")[2]);
			System.out.printf("< %d번 게시물 수정 > \n", id);
			System.out.printf("새로운 제목 : ");
			String title = sc.nextLine();
			System.out.printf("새로운 내용 : ");
			String body = sc.nextLine();

			System.out.printf("%d번 게시물 수정이 완료 되었습니다 ! :) \n", id);

			Connection conn = null;
			PreparedStatement pstmt = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				String url = "jdbc:mysql://127.0.0.1:3306/article_manager?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

				conn = DriverManager.getConnection(url, "root", "");
				System.out.println("연결 성공!");

				String sql = "UPDATE article";
				sql += " SET updateDate = NOW()";
				sql += ", updateDate = NOW()";
				sql += ", title = ('" + title + "')";
				sql += ", `body` = ('" + body + "')";
				sql += " WHERE id = " + id;

				System.out.println(sql);

				pstmt = conn.prepareStatement(sql);

				int affectedRows = pstmt.executeUpdate();

			} catch (ClassNotFoundException e) {
				System.out.println("드라이버 로딩 실패");
			} catch (SQLException e) {
				System.out.println("에러: " + e);
			} finally {
				try {
					if (conn != null && !conn.isClosed()) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (pstmt != null && !pstmt.isClosed()) {
						pstmt.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
