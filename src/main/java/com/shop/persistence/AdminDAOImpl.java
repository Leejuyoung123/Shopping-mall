package com.shop.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.shop.vo.CategoryVO;
import com.shop.vo.GoodsVO;
import com.shop.vo.GoodsViewVO;

@Repository
public class AdminDAOImpl implements AdminDAO {
	@Inject
	private SqlSession sql;
	// 매퍼
	private String namespace="com.shop.psersistence.AdminDAO";
	
	// 카테고리
	@Override
	public List<CategoryVO> category() throws Exception {
		return sql.selectList(namespace + ".category");
	}
	
	// 상품등록
	@Override
	public void register(GoodsVO vo) throws Exception {
		sql.insert(namespace + ".register",vo);
	}
	
	// 상품목록
	@Override
	public List<GoodsViewVO> goodslist() throws Exception {
		return sql.selectList(namespace +".goodslist"); 
	}
	
	// 상품 조회
	@Override
	public GoodsViewVO goodsView(int gdsnum) throws Exception {
		
		return sql.selectOne(namespace + ".goodsView", gdsnum);
	}
	
	// 상품 수정
	@Override
	public void GoodsModify(GoodsVO vo) throws Exception {
		sql.update(namespace + ".goodsModify", vo); 
	}
	
	// 상품 삭제
	@Override
	public void goodsDelete(int gdsnum) throws Exception {
		sql.delete(namespace + ".goodsDelete", gdsnum);
		
	}
	
	
}
