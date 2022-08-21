package com.KoreaIT.example.JAM.controller;

import java.sql.Connection;
import java.util.Scanner;

import com.KoreaIT.example.JAM.Member;
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

			boolean isLoginIdDup = memberService.isLoginIdDup(loginId);

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

		int id = memberService.doJoin(loginId, loginPw, name);

		System.out.printf(" %s 님! 회원가입이 완료되었습니다! 환영합니다 ! :) \n", name);

	}

	// 로그인
	public void doLogin(String cmd) {

		String loginId = null;
		String loginPw = null;

		System.out.println("< 로그인 >");

		while (true) {
			// ID 받기
			System.out.printf("★ 아이디 : ");
			loginId = sc.nextLine().trim();
			if (loginId.length() == 0) {
				System.out.println("!! 아이디가 입력되지 않았습니다. !!");
				continue;
			}

			boolean isLoginIdDup = memberService.isLoginIdDup(loginId);

			if (isLoginIdDup == false) {
				System.out.printf("!! %s는(은) 존재하지 않는 아이디입니다. !! \n", loginId);
				continue;
			}

			break;
		}

		Member member = memberService.getMemberByLoginId(loginId);

		int tryMaxCount = 3;
		int tryCount = 0;

		while (true) {
			if (tryCount >= tryMaxCount) {
				System.out.println("!! 비밀번호를 확인하고 다시 시도해주세요. !!");
				break;
			}
			// PW 받기
			System.out.printf("★ 비밀번호 : ");
			loginPw = sc.nextLine().trim();

			if (loginPw.length() == 0) {
				tryCount++;
				System.out.println("!! 비밀번호가 입력되지 않았습니다. !! ");
				continue;
			}

			if (member.loginPw.equals(loginPw) == false) {
				tryCount++;
				System.out.println("!! 비밀번호가 일치하지 않습니다. !!");
				continue;
			}
			System.out.println("반갑습니다 ! " + member.name + " 님 !");
			break;
		}

	}

}