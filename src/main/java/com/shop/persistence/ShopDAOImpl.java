package com.shop.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.shop.vo.CartVO;
import com.shop.vo.GoodsViewVO;
import com.shop.vo.ReplyListVO;
import com.shop.vo.ReplyVO;

@Repository
public class ShopDAOImpl implements ShopDAO {

	@Inject
	private SqlSession sql;

	private static String namespace="com.shop.psersistence.ShopDAO";

	// 카테고리별 상품 리스트 1차 분류
	@Override
	public List<GoodsViewVO> list(int catecode, int catecoderef) throws Exception {
		
		HashMap<String,Object> map =  new HashMap<String,Object>();
		
		map.put("catecode", catecode);
		map.put("catecoderef", catecoderef);
		
		return sql.selectList(namespace + ".list_1",map);
	}

	// 카테고리별 상품 리스트 2차 분류

	@Override
	public List<GoodsViewVO> list(int catecode) throws Exception {

		return sql.selectList(namespace + ".list_2", catecode);
	}
	// 상품 조회
	@Override
	public GoodsViewVO goodsView(int gdsnum) throws Exception {
		
		return sql.selectOne("com.shop.psersistence.AdminDAO" +".goodsView",gdsnum);
	}
	// 상품 소감(댓글) 작성
	@Override
	public void registReply(ReplyVO reply) throws Exception {
		 	sql.insert(namespace + ".registReply", reply);
	}
	// 상품 소감(댓글) 리스트
	@Override
	public List<ReplyListVO> replyList(int gdsnum) throws Exception {
		return sql.selectList(namespace + ".replyList" , gdsnum);
	}
	// 상품 소감(댓글) 삭제
	@Override
	public void deleteReply(ReplyVO reply) throws Exception {
		sql.delete(namespace + ".deleteReply",reply);
	}
	// 아이디 체크
	@Override
	public String idCheck(int repnum ) throws Exception {
		return sql.selectOne(namespace + ".replyUserIdCheck",repnum);
	}
	@Override
	public void modifyReply(ReplyVO reply) throws Exception {
		sql.update(namespace + ".modifyReply", reply);
		
	}

	@Override
	public void addCart(CartVO cart) throws Exception {
		sql.insert(namespace + ".addCart",cart);
	}
}
