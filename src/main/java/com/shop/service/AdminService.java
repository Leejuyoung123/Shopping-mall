package com.shop.service;

import java.util.List;

import com.shop.vo.CategoryVO;
import com.shop.vo.GoodsVO;
import com.shop.vo.GoodsViewVO;

public interface AdminService {
	
		// 카테고리
		public List<CategoryVO> category() throws Exception;
	
		// 상품등록 
		public void register(GoodsVO vo) throws Exception;	

		// 상품 목록
		public List<GoodsViewVO> goodslist() throws Exception;
		
		// 상품 조회
		public GoodsViewVO goodsView(int gdsnum) throws Exception;
		
		// 상품 수정
		public void goodsModify(GoodsVO vo) throws Exception;
		
		// 상품 삭제
		public void goodsDelete(int gdsnum) throws Exception;
		
}
