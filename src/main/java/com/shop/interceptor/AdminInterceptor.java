package com.shop.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.shop.vo.MemberVO;

public class AdminInterceptor extends HandlerInterceptorAdapter {
	
	/*
	 * HandlerInterceptorAdapter 상속 
	 * 컨트롤러 실행전에 실행되는 preHandler 메서드와 컨트롤러 실행 후 postHandle로 나뉨
	 * 컨트롤러가 실행되기 전에 관리자 여부를 확인 - prehandle을 오버라이드 
	 * 현재의 세션을 불러와 session에 저장 member 세션을 불러와 
	 * MemberVO로 변환한뒤 MemberVO 형태의 변수인 member에 저장
	 * 조건문 사용 member에 값이 없는 null  비 로그인 상태와 
	 * member.verify()==9의 값이 9가 아닐경우 조건문의 내부가 실행
	 * 로그인되지 않은 상태와 / 관리자권한이 아닐경우 조건문은 실행됨
	 * 조건문 내부는 가장 처음화면 / 경로로 되돌리는 역활 
	 * false를 반환함 prehandle 값이 true이면 컨트롤러 진행 false면 진행이 멈춤
	 * 
	 * 패키지를 추가했으니, 패키지를 인식할 수 있도록 설정을 해주어야하는데, 
	 * 이때 root-context에서는 설정을 하지 않습니다. 
	 * 프로젝트가 실행되는 웹환경과 관련된 설정은 root-context가 아닌 
	 * servlet-context에서 설정을 해야합니다.
	 */
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object obj)
			throws Exception {
		// 세션 초기화 
		HttpSession session= req.getSession();
		MemberVO member = (MemberVO)session.getAttribute("member");
		// member 즉 vo 의 데이터가 없으면 로그인이 되어있지않음 즉 / admin/index 페이지로 갈려고할떄 
		// /member/signin 으로 보내줌  인터셉터에서 가로채기떄문에  즉 response 반환을 해주게되는데
		// 로그인 페이지로 이동하라고 값을줌 
		if(member == null ) {
			res.sendRedirect("/member/signin");
			return false;
		}
		///  권한이 9 관리자 권한이아니면 / 절대경로로 보줌 일반사용자는 관리자 페이지에 접근을 못하게 됨 
		// 왜냐면 verify 초기값은 0 이기떄문에 
		if(member.getVerify() != 9) {
			res.sendRedirect("/");
			return false;
		}
		return true;
	}
}
