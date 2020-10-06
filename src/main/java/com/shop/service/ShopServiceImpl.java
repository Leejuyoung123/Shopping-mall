package com.shop.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.shop.persistence.ShopDAO;
import com.shop.vo.CartVO;
import com.shop.vo.GoodsViewVO;
import com.shop.vo.ReplyListVO;
import com.shop.vo.ReplyVO;

@Service
public class ShopServiceImpl implements ShopService {
	@Inject
	private ShopDAO dao;

	// 카테고리별 상품 리스트
	@Override
	public List<GoodsViewVO> list(int catecode, int level) throws Exception {
		int catecoderef = 0;
		if (level == 1) { // 1차 분류
			catecoderef = catecode;
			return dao.list(catecode, catecoderef);
		} else { // 2차 분류
			return dao.list(catecode);
		}

	}

	// 상품 조회
	@Override
	public GoodsViewVO goodsView(int gdsnum) throws Exception {
		return dao.goodsView(gdsnum);
	}

	// 상품 소감 (댓글 작성)
	@Override
	public void registReply(ReplyVO reply) throws Exception {
		dao.registReply(reply);
	}

	// 상품 소감 (댓글 리스트)
	@Override
	public List<ReplyListVO> replyList(int gdsnum) throws Exception {
		return dao.replyList(gdsnum);
	}

	// 상품 소감 (댓글 삭제)
	@Override
	public void deleteReply(ReplyVO reply) throws Exception {
		dao.deleteReply(reply);
	}

	// 아이디 체크
	@Override
	public String idCheck(int repnum) throws Exception {
		return dao.idCheck(repnum);
	}
	// 상품 소감 (댓글 수정)
	@Override
	public void modifyReply(ReplyVO reply) throws Exception {
		dao.modifyReply(reply);
	}

	@Override
	public void addCart(CartVO cart) throws Exception {
		dao.addCart(cart);
	}
}
