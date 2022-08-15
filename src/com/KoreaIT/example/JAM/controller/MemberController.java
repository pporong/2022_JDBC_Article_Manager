package com.KoreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.Scanner;

import com.KoreaIT.example.JAM.service.MemberService;

public class MemberController extends Controller {
	private MemberService memberService;

	public MemberController(Connection conn, Scanner sc) {
		super(sc);
		memberService = new MemberService(conn);

	}


	// 회원 가입
	public void doJoin(String cmd) {
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

			boolean isLoginIdDup =  memberService.isLoginIdDup(loginId);
			
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
//
//		SecSql sql = new SecSql();
//
//		sql.append("INSERT INTO member");
//		sql.append(" SET regDate = NOW()");
//		sql.append(", updateDate = NOW()");
//		sql.append(", loginId = ?", loginId);
//		sql.append(", loginPw = ?", loginPw);
//		sql.append(", `name` = ?", name);

		int id = memberService.doJoin(loginId, loginPw, name);

		System.out.printf(" %s 님! 회원가입이 완료되었습니다! 환영합니다 ! :) \n", name);

	}
	
	// 회원 정보
//
//	public void showProfile(String cmd) {
//		int id = Integer.parseInt(cmd.split(" ")[2]);
//
//		SecSql sql = new SecSql();
//		sql.append("SELECT *");
//		sql.append("FROM member");
//		sql.append("WHERE id = ?", id);
//
//		Map<String, Object> memberMap = DBUtil.selectRow(conn, sql);
//
//		if (memberMap.isEmpty()) {
//			System.out.printf("%d번 회원은 존재하지 않습니다. :( \n", id);
//			return;
//		}
//
//		Member member = new Member(memberMap);
//
//		System.out.printf("< %s 회원님 상세 정보 > \n", member.name);
//		System.out.printf("이    름 : %s \n", member.name);
//		System.out.printf("번    호 : %d \n", member.id);
//		System.out.printf("가입 날짜 : %s \n", member.regDate);
//		System.out.printf("아 이 디 : %s \n", member.loginId);
//		System.out.printf("비밀 번호 : %s \n", member.loginPw);
//	}
	
}