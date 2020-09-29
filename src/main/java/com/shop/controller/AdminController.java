package com.shop.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.shop.service.AdminService;
import com.shop.utils.UploadFileUtils;
import com.shop.vo.CategoryVO;
import com.shop.vo.GoodsVO;
import com.shop.vo.GoodsViewVO;

import net.sf.json.JSONArray;

@Controller
@RequestMapping("/admin/*")

public class AdminController {
	@Resource(name = "uploadPath")
	private String uploadPath;

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

	@Inject
	private AdminService adminService;

	/*
	 * 관리자화면
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	private void getIndex() throws Exception {
		logger.info("get index");
	}

	/*
	 * 상품 등록
	 */
	@RequestMapping(value = "/goods/register", method = RequestMethod.GET)
	private void getGoodsRegister(Model model) throws Exception {
		logger.info("get Goods register");
		/*
		 * category VO 형태의 List 변수 category 선언 adminservice.category()호출 결과값을 category에
		 * 입력 JsonArray를 이용해 category를 JSON 타입으로 변경 category 라는 별치으로 모델에 추가 이 메서드가 호출될떄
		 * 모델을 JSP에 넘겨서 사용할수 있음
		 */
		List<CategoryVO> category = null;
		category = adminService.category();
		model.addAttribute("category", JSONArray.fromObject(category));
	}

	/*
	 * 상품 등록
	 */
	@RequestMapping(value = "/goods/register", method = RequestMethod.POST)
	public String postGoodsRegister(GoodsVO vo, MultipartFile file) throws Exception {
		String imgUploadPath = uploadPath + File.separator + "imgUpload";
		String ymdPath = UploadFileUtils.calcPath(imgUploadPath);
		String fileName = null;
		if (file.getOriginalFilename() != null && file.getOriginalFilename() != "") {
			fileName = UploadFileUtils.fileUpload(imgUploadPath, file.getOriginalFilename(), file.getBytes(), ymdPath);
			
			vo.setGdsimg(File.separator + "imgUpload" + ymdPath + File.separator + fileName);
			vo.setGdsthumbimg(
					File.separator + "imgUpload" + ymdPath + File.separator + "s" + File.separator + "s_" + fileName);
		} else {
			fileName = File.separator + "images" + File.separator + "none.png";
			
			vo.setGdsimg(fileName);
			vo.setGdsthumbimg(fileName);
		}

		adminService.register(vo);
		
		return "redirect:/admin/index";
	}

	/*
	 * 상품 목록
	 */
	@RequestMapping(value = "/goods/list", method = RequestMethod.GET)
	public void getGoodsList(Model model) throws Exception {
		logger.info("get Goods list");
		List<GoodsViewVO> list = adminService.goodslist();
		model.addAttribute("list", list);
	}

	/*
	 * 상품 조회
	 */
	@RequestMapping(value = "/goods/view", method = RequestMethod.GET)
	public void getGoodsView(@RequestParam("n") int gdsnum, Model model) throws Exception {
		/*
		 * UrL 주소에서 n 의 값을 찾아서 int gdsnum 전달 목록에서 링크 주소를 /admin/goods/view?n=[상품번호] 형식
		 * n을찾아 다른문자로 했으면 다른 문자로 찾아야함
		 */
		logger.info("get Goods view");

		GoodsViewVO goods = adminService.goodsView(gdsnum);

		model.addAttribute("goods", goods);
	}

	/*
	 * 상품 수정
	 */
	@RequestMapping(value = "/goods/modify", method = RequestMethod.GET)
	public void getGoodsModify(@RequestParam("n") int gdsnum, Model model) throws Exception {
		logger.info(" get Goods Modify");

		GoodsViewVO goods = adminService.goodsView(gdsnum);
		model.addAttribute("goods", goods);

		List<CategoryVO> category = null;
		category = adminService.category();
		model.addAttribute("category", JSONArray.fromObject(category));
	}

	@RequestMapping(value = "/goods/modify", method = RequestMethod.POST)
	public String postGoodsModify(GoodsVO vo, MultipartFile file, HttpServletRequest req) throws Exception {
		logger.info(" post Goods Modify ");

		// 새로운 파일이 등록되었는지 확인
		if (file.getOriginalFilename() != null && file.getOriginalFilename() != "") {
			// 기존 파일을 삭제
			new File(uploadPath + req.getParameter("gdsImg")).delete();
			new File(uploadPath + req.getParameter("gdsThumbImg")).delete();

			// 새로 첨부한 파일을 등록
			String imgUploadPath = uploadPath + File.separator + "imgUpload";
			String ymdPath = UploadFileUtils.calcPath(imgUploadPath);
			String fileName = UploadFileUtils.fileUpload(imgUploadPath, file.getOriginalFilename(), file.getBytes(),
					ymdPath);

			vo.setGdsimg(File.separator + "imgUpload" + ymdPath + File.separator + fileName);
			vo.setGdsthumbimg(
					File.separator + "imgUpload" + ymdPath + File.separator + "s" + File.separator + "s_" + fileName);

		} else { // 새로운 파일이 등록되지 않았다면
			// 기존 이미지를 그대로 사용
			vo.setGdsimg(req.getParameter("gdsImg"));
			vo.setGdsthumbimg(req.getParameter("gdsThumbImg"));

		}

		adminService.goodsModify(vo);

		return "redirect:/admin/index";
	}

	/*
	 * 상품 삭제
	 */
	@RequestMapping(value = "/goods/delete", method = RequestMethod.POST)
	public String postGoodsDelete(@RequestParam("n") int gdsnum) throws Exception {
		logger.info(" post Goods Delete");

		adminService.goodsDelete(gdsnum);

		return "redirect:/admin/index";
	}

	/*
	 * ck 에디터에서 파일 업로드
	 */
	@RequestMapping(value = "/goods/ckUpload", method = RequestMethod.POST)
	public void postCKEditorImgUpload(HttpServletRequest req, HttpServletResponse res,
			@RequestParam MultipartFile upload) throws Exception {

		logger.info("post CKEditor img upload");

		// 랜덤 문자 생성
		UUID uid = UUID.randomUUID();

		OutputStream out = null;
		PrintWriter printWriter = null;

		// 인코딩
		res.setCharacterEncoding("utf-8");
		res.setContentType("text/html;charset=utf-8");

		try {

			String fileName = upload.getOriginalFilename();// 파일 이름 가져오기
			byte[] bytes = upload.getBytes();

			// 업로드 경로
			String ckUploadPath = uploadPath + File.separator + "ckUpload" + File.separator + uid + "_" + fileName;

			out = new FileOutputStream(new File(ckUploadPath));
			out.write(bytes);
			out.flush();// out에 저장된 데이터를 전송하고 초기화

			String callback = req.getParameter("CKEditorFuncNum");
			printWriter = res.getWriter();
			String fileUrl = "/ckUpload/" + uid + "_" + fileName; // 작성화면

			// 업로드시 메시지 출력
			printWriter.println("<script type='text/javascript'>" + "window.parent.CKEDITOR.tools.callFunction("
					+ callback + ",'" + fileUrl + "','이미지를 업로드하였습니다.')" + "</script>");

			printWriter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (printWriter != null) {
					printWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return;
	}
}
