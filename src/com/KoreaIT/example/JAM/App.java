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

		// 회원 가입
		else if (cmd.equals("member join")) {
			String loginId = null;
			String loginPw = null;
			String loginPwConfirm = null;
			String name = null;

			System.out.println("< 회원가입 >");
			
			while (true) {
				System.out.printf("* 이름 : ");
				name = sc.nextLine();
				if (name.length() == 0) {
					System.out.println("!! 이름을 입력해주세요. !!");
					continue;
				}
				break;
			}

			while (true) {
				System.out.printf("* 아이디 : ");
				loginId = sc.nextLine();

				if (loginId.length() == 0) {
					System.out.println("!! 아이디를 입력 해 주세요. !!");
					continue;
				}

				SecSql sql = new SecSql();

				sql.append("SELECT COUNT(*) > 0");
				sql.append("FROM `member`");
				sql.append("WHERE loginId =?", loginId);

				boolean isLoginIdDup = DBUtil.selectRowBooleanValue(conn, sql);

				if (isLoginIdDup) {
					System.out.printf("!! %s 는(은) 이미 사용중인 아이디 입니다. !!", loginId);
					continue;
				}

				break;
			}

			// 비밀번호 확인
			while (true) {
				System.out.printf("* 비밀번호 : ");
				loginPw = sc.nextLine().trim();

				if (loginPw.length() == 0) {
					System.out.println("!! 비밀번호를 입력 해 주세요. !!");
					continue;
				}

				boolean loginPwCheck = true;

				// 비밀번호 중복체크
				while (true) {
					System.out.printf("* 비밀번호 확인 : ");
					loginPwConfirm = sc.nextLine().trim();

					if (loginPw.length() == 0) {
						System.out.println("!! 비밀번호를 재입력 해 주세요. !!");
						continue;
					}

					if (loginPw.equals(loginPwConfirm) == false) {
						System.out.println("!! 비밀번호가 일치하지 않습니다. 다시 입력해주세요 !!");
						loginPwCheck = false;
					}
					break;

				}
				if (loginPwCheck) {
					break;
				}
			}

			SecSql sql = new SecSql();

			sql.append("INSERT INTO member");
			sql.append(" SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", loginId = ?", loginId);
			sql.append(", loginPw = ?", loginPw);
			sql.append(", `name` = ?", name);

			int id = DBUtil.insert(conn, sql);

			System.out.printf("%s 님! 회원가입이 완료되었습니다! 환영합니다 ! :) \n", name);

		} // 회원 정보
		else if (cmd.startsWith("member detail ")) {
			int id = Integer.parseInt(cmd.split(" ")[2]);

			SecSql sql = new SecSql();
			sql.append("SELECT *");
			sql.append("FROM member");
			sql.append("WHERE id = ?", id);

			Map<String, Object> memberMap = DBUtil.selectRow(conn, sql);

			if (memberMap.isEmpty()) {
				System.out.printf("%d번 회원은 존재하지 않습니다. :( \n", id);
				return 0;
			}

			Member member = new Member(memberMap);

			System.out.printf("< %s 회원님 상세 정보 > \n", member.name);
			System.out.printf("이    름 : %s \n", member.name);
			System.out.printf("번    호 : %d \n", member.id);
			System.out.printf("가입 날짜 : %s \n", member.regDate);
			System.out.printf("아 이 디 : %s \n", member.loginId);
			System.out.printf("비밀 번호 : %s \n", member.loginPw);
		}

		// 프로그램 종료
		if (cmd.equals("exit")) {
			System.out.println("!! 프로그램을 종료합니다 !!");
			return -1;
		}
		return 0;
	}

}