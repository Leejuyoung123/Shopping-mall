package com.shop.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.shop.persistence.AdminDAO;
import com.shop.vo.CategoryVO;
import com.shop.vo.GoodsVO;
import com.shop.vo.GoodsViewVO;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Inject 
	private AdminDAO dao;
	
	// 카테고리
	@Override
	public List<CategoryVO> category() throws Exception {
		return dao.category();
	}
	// 상품 등록
	@Override
	public void register(GoodsVO vo) throws Exception {
		dao.register(vo);
	}
	
	// 상품 목록
	@Override
	public List<GoodsViewVO> goodslist() throws Exception {
		return dao.goodslist();
	}
	
	// 상품 조회 + 카테고리 조인 
	@Override
	public GoodsViewVO goodsView(int gdsnum) throws Exception {
		return dao.goodsView(gdsnum);
	}
	
	// 상품 수정
	@Override
	public void goodsModify(GoodsVO vo) throws Exception {
		dao.GoodsModify(vo);
	}
	
	// 상품 삭제
	@Override
	public void goodsDelete(int gdsnum) throws Exception {
		dao.goodsDelete(gdsnum);
	}

}
