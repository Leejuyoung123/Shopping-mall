package com.shop.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.service.MemberService;
import com.shop.vo.MemberVO;

@Controller
@RequestMapping("/member/*")
public class MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Inject
	MemberService service;
	
	@Autowired
	BCryptPasswordEncoder passEncoder;  // 비밀번호 암호화
		
	// 회원 가입 get
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public void getSignup() throws Exception {
		logger.info("get signup");
	}
	
	// 회원 가입 post
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String postSignup(MemberVO vo) throws Exception {
		logger.info("post signup");
			
		String inputPass = vo.getUserpass();
		String pass = passEncoder.encode(inputPass);  // 비밀번호를 암호화
		vo.setUserpass(pass);  // 암호화된 비밀번호를 userPass에 저장
	
		service.signup(vo);
		return "redirect:/";
	}	
	
	// 로그인 get
	@RequestMapping(value="/signin", method= RequestMethod.GET)
	public void signin()throws Exception{
		logger.info("get signin");
	}
	
	// 로그인 post
	@RequestMapping(value="/signin", method= RequestMethod.POST)
	public String postSignin(MemberVO vo, HttpServletRequest req, RedirectAttributes rttr) throws Exception{
		MemberVO login = service.signin(vo);
		HttpSession session =req.getSession();
	
		boolean passMatch = passEncoder.matches(vo.getUserpass(),login.getUserpass());
		//로그인에 값이 널이면 passMatch 를 실행시켜?
		//  signin은 insert 로그인처리 select 받아오는값
		// login = memverVO 매칭
		//  값이 널이아니면 패스매치 에서 받아온값 userpass워드와 인서트에서 받아온 패스워드가
		// 세션 member 로 들어가게됨
		// 그게 아니면 세션은 null 값이고 msg를 보내면서 로그인 화면으로새로고침함 패스워드가 맞지 않으면 널값이 들어가게 되므로 
		// 500에러처리하게됨 리다이렉트 , membersigin으로 보내려면?
		
		if(login != null && passMatch) {
			session.setAttribute("member", login);
		}else {
			session.setAttribute("member", null);
			rttr.addFlashAttribute("msg",false);
			return "redirect:/member/signin";
		}
			
		return "redirect:/";
	}
	
	// 로그아웃
	@RequestMapping(value="/signout", method=RequestMethod.GET)
	public String signout(HttpSession session) throws Exception{
		logger.info("get logout");
		service.signout(session);
		return "redirect:/";
	}
}