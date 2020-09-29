package com.shop.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.service.ShopService;
import com.shop.vo.GoodsViewVO;
import com.shop.vo.MemberVO;
import com.shop.vo.ReplyListVO;
import com.shop.vo.ReplyVO;

@Controller
@RequestMapping(value = "/shop/*")
public class ShopController {

	private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

	@Inject
	ShopService service;

	/*
	 * 카테고리별 상품리스트
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void getList(@RequestParam("c") int catecode, @RequestParam("l") int level, Model model) throws Exception {

		logger.info("get List");

		List<GoodsViewVO> list = null;
		list = service.list(catecode, level);

		model.addAttribute("list", list);
	}
	/*
	 *  상품 조회
	 */
	@RequestMapping(value= "/view", method=RequestMethod.GET)
	public void getView(@RequestParam("n") int gdsnum , Model model) throws Exception {

		logger.info(" get View");
		
		GoodsViewVO view = service.goodsView(gdsnum);
		model.addAttribute("view", view);
		
		/*	
		 *  소감 목록을 읽어오는 메서드는 밑에 ajax용 생성함
		 *  /view/ReplyList 경로로 설정되어있음  
		 *  상품 조회용 메서드에 있는 소감 목록 부분은 이제 필요없음 
			List<ReplyListVO> reply = service.replyList(gdsnum);
			model.addAttribute("reply", reply);
		*/
	}
	/*
	 * 상품 조회-소감(리뷰) 작성 
	
	@RequestMapping(value="/view",method=RequestMethod.POST)
	public String registReply(ReplyVO reply , HttpSession session)throws Exception{
		logger.info("reply regist");
		
		MemberVO member = (MemberVO) session.getAttribute("member");
		reply.setUserid(member.getUserid());
		
		service.registReply(reply);
		return "redirect:/shop/view?n=" + reply.getGdsnum();
	}
	  */
	
	/*
	 * 상품소감 (댓글) 작성
	 */
	@ResponseBody
	@RequestMapping(value="/view/registReply", method = RequestMethod.POST)
	public void registReply(ReplyVO reply, HttpSession session) throws Exception{
		logger.info("register reply");
		
		MemberVO member = (MemberVO)session.getAttribute("member");
		reply.setUserid(member.getUserid());
		
		service.registReply(reply);
	}
	
	/*
	 *  상품소감 (목록)
	 */
	@ResponseBody
	@RequestMapping(value="/view/replyList", method = RequestMethod.GET)
	public List<ReplyListVO> getReplyList(@RequestParam("n")int gdsnum) throws Exception{
		logger.info("get Reply List");
		
		List<ReplyListVO> reply = service.replyList(gdsnum);
		
		return reply;
	}
	/*
	 * 상품 소감(삭제)
	 */
	
	@ResponseBody
	@RequestMapping(value = "/view/deleteReply", method = RequestMethod.POST)
	public int getReplyList(ReplyVO reply,  HttpSession session) throws Exception {
		logger.info("post delete reply");

		// 정상작동 여부를 확인하기 위한 변수
		int result = 0;
		
		MemberVO member = (MemberVO)session.getAttribute("member");  // 현재 로그인한  member 세션을 가져옴
		String userid = service.idCheck(reply.getRepnum());  // 소감(댓글)을 작성한 사용자의 아이디를 가져옴
				
		// 로그인한 아이디와, 소감을 작성한 아이디를 비교
		if(member.getUserid().equals(userid)) {
			
			// 로그인한 아이디가 작성한 아이디와 같다면
			
			reply.setUserid(member.getUserid());  // reply에 userId 저장
			service.deleteReply(reply);  // 서비스의 deleteReply 메서드 실행
			
			// 결과값 변경
			result = 1;
		}
		
		// 정상적으로 실행되면 소감 삭제가 진행되고, result값은 1이지만
		// 비정상적으로 실행되면 소감 삭제가 안되고, result값이 0
		return result;	
	}
}	
	