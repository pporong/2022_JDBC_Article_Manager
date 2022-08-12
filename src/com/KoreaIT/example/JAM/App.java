package com.KoreaIT.example.JAM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import com.KoreaIT.example.JAM.controller.ArticleController;
import com.KoreaIT.example.JAM.controller.MemberController;

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

				int actionResult = action(conn, sc, cmd);

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

	private int action(Connection conn, Scanner sc, String cmd) {

		MemberController memberController = new MemberController();
		memberController.setConn(conn);
		memberController.setScanner(sc);

		ArticleController articleController = new ArticleController();
		articleController.setConn(conn);
		articleController.setScanner(sc);

		if (cmd.equals("member join")) {
			memberController.doJoin(cmd);
		}
//			else if (cmd.equals("member detail ")) {
//			memberController.showProfile(cmd);
//		} 
		  else if (cmd.equals("article write")) {
			articleController.doWrite(cmd);
		} else if (cmd.equals("article list")) {
			articleController.showList(cmd);
		} else if (cmd.startsWith("article delete ")) {
			articleController.doDelete(cmd);
		} else if (cmd.startsWith("article modify ")) {
			articleController.doModify(cmd);
		} else if (cmd.startsWith("article detail ")) {
			articleController.showDetail(cmd);
		} else {
			System.out.println("!! 존재하지 않는 명령어 입니다. !!");
		}

		// 프로그램 종료
		if (cmd.equals("exit")) {
			System.out.println("!! 프로그램을 종료합니다 !!");
			return -1;
		}

		return 0;
	}

}