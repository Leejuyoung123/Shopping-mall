package com.shop.persistence;

import java.util.List;

import com.shop.vo.GoodsViewVO;
import com.shop.vo.ReplyListVO;
import com.shop.vo.ReplyVO;

public interface ShopDAO {
	
	// 카테고리별 상품 리스트 1차 분류
	public List<GoodsViewVO> list(int catecode, int catecoderef) throws Exception;
	
	// 카테고리별 상품 리스트 2차 분류
	public List<GoodsViewVO> list(int catecode) throws Exception;
	
	// 상품 조회
	public GoodsViewVO goodsView(int gdsnum) throws Exception;
	
	// 상품후기(댓글 작성)
	public void registReply(ReplyVO reply) throws Exception;
	
	// 상품 후기(댓글) 리스트
	public List<ReplyListVO> replyList(int gdsnum) throws Exception;
	
	// 상품 소감(댓글)삭제
	public void deleteReply(ReplyVO reply) throws Exception;
	
	// 아이디 체크 
	public String idCheck(int repnum) throws Exception;
	
}
