### 스프링 무작정 따라하기  클론코딩() 생각이 안날떄는 다시 재정의
### 기술참조 kuzuro https://kuzuro.blogspot.com/2018/10/3.html

```
스프링 무작정따라하기 21챕터
카트 테이블 생성 
create table tbl_cart (
cartnum number not null,
userid varchar2(50) not null,
gdsnum number not null,
cartstock number not null,
adddate date default sysdate,
primary key (cartnum , userid)
);

상품이 마음에 들면 카트에 담아둔뒤 잠시 고민하는(!)척 하다가 구입하게 됨 
2개 이상의 상품을 구입하게 될떄면 바로바로 구입하는것보다 카트에 담아놓고 한번에 구입하는게 
사용자나 관리자나 여러모로 편리함

카트번호 cartnum은 값을 구분하는 고유번호 , 카트수량은 상품별 담은 갯수입니다.
상품 번호와 아이디는 다른 테이블에서 참조하는 컬럼입니다

카트 번호를 생성할 시퀀스입니다.
create sequence tbl_cart_seq;
카트 번호는 시퀀스만 사용하는것 보다 날짜와 조합하여 사용하는것이 좋음

카트 테이블과 멤버테이블 , 상품 테이블을  참조하는 쿼리
insert into tbl_cart (cartnum , userid ,gdsnum,cartstock)
values(tbl_cart_seq.nextval,'leejy@naver.com',21,99);


CartVO형태의 제이슨(Json) 데이터를 만들어서 에이젝스를 이용해 지정한 URL로 보낸 뒤, 전송에 성공하면 success 함수를 실행, 실패하면 error 함수를 실행합니다.

실패할 경우는 세션이 다 되어서 로그인이 자동으로 풀렸거나, 로그인을 안한 사용자입니다.

```



```
스프링 무작정 따라하기 20챕터 상품 소감 (댓글) 수정 구현

view.jsp /body 바로 위에 올림 

모달창으로 사용됨


<div class="replyModal">

 <div class="modalContent">
  
  <div>
   <textarea class="modal_repCon" name="modal_repCon"></textarea>
  </div>
  
  <div>
   <button type="button" class="modal_modify_btn">수정</button>
   <button type="button" class="modal_cancel">취소</button>
  </div>
  
 </div>

 <div class="modalBackground"></div>
 
</div>

변수 repNum에는 버튼에 부여된 소감 번호(data-repNum)를 저장, 변수 repCon에는 버튼의 부모(parent)의 부모에서 자식의 클래스가 replyContent인 요소의 값을 저장했습니다.

 M 버튼의 부모는 <div class="replyFooter">, <div class="replyFooter"> 의 부모는 <li data-repNum="11">, <li data-repNum="11"> 의 자식중 클래스가 replyContent인건 '소감 내용'입니다.

repNum에 저장된 소감 번호는 모달창의 버튼에 부여하고, repCon에 저장된 소감 내용은 모달창의 텍스트에어리어에 부여합니다.

repNum에 저장된 소감 번호는 모달창의 버튼에 부여하고, repCon에 저장된 소감 내용은 모달창의 텍스트에어리어에 부여합니다.

이제 M버튼을 눌러서 나오는 모달창의 텍스트에어리어에는 선택했던 버튼에 해당되는 소감 내용이 들어가있고, 수정 버튼에는 선택했던 버튼의 상품 번호가 부여되어있습니다.


이제 모달창의 수정버튼을 클릭하면 컨트롤러로 데이터가 전달될 수 있도록 스크립트를 추가합니다.


상품 소감 삭제에서 했던것과 마찬가지로 컨펌(confirm)을 이용해 수정 여부를 확인하고, 데이터를 제이슨(Json) 형태로 저장한 뒤 에이젝스(Ajax)를 이용해 컨트롤러로 값을 전달합니다. 컨트롤러에서 되돌아오는 result의 값이 1이면 성공, 0이면 실패한것으로 구분하여 작업합니다.

댓글이 달린 글은 삭제가 안되는 이유는 ??
```

```
상품 소감 (댓글을) 작성 할 수 있으니 이번엔 수정과 삭제입니다 .

지난 게시판 만들기에서 했던 것과 다르게 이번엔 모든 CRUD가 에이젝스로 이루어지기 떄문에 약간 다름

수정과 삭제 중 삭제 기능을 먼저 구현 

삭제 쿼리는 간단합니다. 삭제할 소감 번호(repNum)와 사용자(userId)로 구분하여 삭제할 수 있습니다.

여기서 사용자(userId)가 중요한데, 현재 로그인한 사용자가 실제로 소감을 작성한 사용자인지 구분해야합니다. 구분하지 않는다면 다른 사용자가 작성한 소감을 아무나 삭제할 수 있으면 안되겠죠.

이 쿼리는 소감 번호(repNum)의 작성자를 확인하는 쿼리입니다.

<!-- 상품 소감(댓글) 삭제 -->
<delete id="deleteReply">
 delete tbl_reply
     where repNum = #{repNum}
         and userId = #{userId}
</delete>

<!-- 아이디 체크 -->
<select id="replyUserIdCheck" resultType="String">
 select userId
  from tbl_reply
      where repNum = #{repNum}
</select>

아이디 체크용 쿼리는 아이디(userId)만 가져오기 때문에, 
결과 타입(resultType)을 ReplyVO가 아닌 String으로 사용합니다.

<script>
 $(document).on("click", ".delete", function(){
  
  var data = {repNum : $(this).attr("data-repNum")};
   
  $.ajax({
   url : "/shop/view/deleteReply",
   type : "post",
   data : data,
   success : function(){
     replyList();
   }
  });
 });
</script>

소감 목록은 스크립트로 인해 생성된 동적인 html 코드로 일반적인 클릭 메서드가. click아니라 .on 메서드로 사용해야함

버튼의 html 코드에 있는 data-repnum의 값을 에이젝스를 통해 컨트롤러로 전달합니다

컨트롤러에 상품 소감 삭제용 메서드를 생성합니다.

// 상품 소감(댓글) 삭제
@ResponseBody
@RequestMapping(value = "/view/deleteReply", method = RequestMethod.POST)
public int getReplyList(ReplyVO reply,  HttpSession session) throws Exception {
 logger.info("post delete reply");

 int result = 0;
 
 MemberVO member = (MemberVO)session.getAttribute("member");
 String userId = service.idCheck(reply.getRepNum());
   
 if(member.getUserId().equals(userId)) {
  
  reply.setUserId(member.getUserId());
  service.deleteReply(reply);
  
  result = 1;
 }
 
 return result; 
}

현재 세션을 가져와서 member 에 저장하고 아이디 체크용 쿼리의 결과를 가져와서 변수 userid 저장
변수 member에서 userid 부분만 추출한 값과 , 변수 userid의 값을 비교
이떄 비교식은 이퀄 == 이아닌 equals 메서드를 사용

현재 로그인한 사용자의 아이디와 소감을 작성한 사용자의 아이디를 비교했을떄 , 결과가 참이라면 삭제 작업을
진행 한뒤 변수에 result 1 을 저장하고 거짓 이라면 아무 작업을 하지 않고 끝냄

변수 result 의 결과가 0이 라면 아이디가 다르기에 삭제 작업이 진해이 되지 않은것이며 
변수 result 의 결과가 1이 라면 아이디가 같아서 삭제 작업이 진행이 된것
success : function(result){
 
 if(result == 1) {
  replyList();
 } else {
  alert("작성자 본인만 할 수 있습니다.");     
 }
}
컨트롤러로 받아온 result 값이 1이라면 삭제 작업이 진행 되었으니 소감 목록을 다시 불러오고
1이 아니라면 삭제 작업이 진행되지 않은 것이며 그렇다는건 로그인 한 사용자와 소감을 작성한 사용자가 다른것이므로
메시지를 띄웁

다른 아이디로 작성한 소감을 삭제하려고 할 경우 이렇게 메시지가 나옵니다 
자신이 작성한 소감을 지우려고 하면 잘 지워지긴 하는데 실수로 누를 경우에도 지워지니 대책이 필요함
 
이 때 컨펌 메서드를 사용합니다.
컨펌 메서드는 내부의 텍스트를 사용자에게 보여주며 확인 버튼과 취소 버튼을 같이 보여줍니다 
확인 버튼을 클릭하면 참true 반환하고 취소 버튼을 클릭하면 거짓을 반환 합니다 

컨펌 메세지가 있는 동안 다른 작업을 할 수 없으므로 사용자가 실수로 삭제 버튼을 누르더라도 삭제되는 일을 방지 할 수 있습니다.

다음은 로그인 하지 않은 상태에서 버튼을 눌렀을 떄입니다 .

컨트롤러에서 세션을 가져온 뒤 작업하는데 , 로그인 하지 않았다면 모든 세션이 널이므로 에러가 발생하게 됩
에이젝스 에러가 발생할 경우에는 실행될 코드를 따로 설정할수 있음 로그인 하셔야 합니다

로그인 하지 않은 상태에서 삭제 버튼을 클릭할 경우 , 이렇게 로그인 하라는 메세지가 출력됩니다,

org.apache.ibatis.exceptions.TooManyResultsException: Expected one result (or null) to be returned by selectOne(), but found: 4] with root cause
return type 상 1개가 select 되는데 결과값은 3개가 나오고있다.?
원인 :쿼리문의 결과로 결과가 하나만 나와야하는데 두개이상의 값이 나오는경우
해결 :
```

```
상품에 소감을 작성하는 기능은 게시판에서 댓글을 작성하는 기능과 같음
 
쇼핑몰은 상품페이지에 이미지가 첨부되어있는 경우가 많은데 상품 소감을 작성하면서 
페이지가 새로고침 하게 되면 이미지를 계속 새로 불러와서 불필요한 데이터 소모가 있음
그렇기 떄문에 소감 기능에는 ajax를 적용할 예정인데 기본적인 기능부터 구현을함

상품 소감은 이름에서 느껴지듯 상품에 대한 소감 그렇기 떄문에 상품 소감 테이블의 기본키는 
상품 번호 gdsnum과 리뷰 repnum으로 되어있음
상품 번호는 소감이 어느 상품에 작성 되어있는지는 구분할수 있고 소감 번호는 한 상품에 작성된 소감을 구분할수 있음

댓글 테이블
create table tb_reply (
gdsnum number not null,
userid varchar2(50) not null,
repnum number   not null,
repcon varchar2(2000) not null,
repdate date default sysdate,
primary key (gdsnum,repnum)
);

create sequence tb_reply_seq; 시퀀
상품 번호 repnum을 자동으로 생성할 시퀀스임 

제약조건 외래키잡아줌

alter table tb_reply
add constraint tb_reply_gdsnum foreign key(gdsnum)
references tbl_goods(gdsnum);

alter table tb_reply
add constraint tb_reply_userid foreign key (userid)
references tb_member(userid);

소감 테이블의 상품번호와 유저 아이디는 다른 테이블에서 참조 테이블을 참조하게 되면 참조하는 테이블에 값이 없는 경우
추가 되지 않도록 막을수 있음

참조키를 사용하는 이유는 데이터의 무결성을 위해서 어떠한 이유가 생겨서 존재 하지 않는 상품번호가 유저 아이디가 
데이터 베이스에 전달 될 경우 참조키가 있다면 데이터가 입력 되는걸 차단합니다 생성했다면 다 commit 을해줌

view.jsp  상품설명 하단에 리뷰 영역을 추가 
<div id="reply">
 <section class="replyForm">
  <form role="form" method="post" autocomplete="off">
   댓글 폼
  </form>
 </section>
 </c:if>
 
 <section class="replyList">
  <ol>
   <li>댓글 목록</li>
   </ol>    
 </section>
</div>
조건문 c:if 를 추가하여 로그인에 따른 출력을 나누고 텍스트 에어리어 버튼을 추가함
<div id="reply">

 <c:if test="${member == null }">
  <p>소감을 남기시려면 <a href="/member/signin">로그인</a>해주세요</p>
 </c:if>
 
 <c:if test="${member != null}">
 <section class="replyForm">
  <form role="form" method="post" autocomplete="off">
   <div class="input_area">
    <textarea name="repCon" id="repCon"></textarea>
   </div>
   
   <div class="input_area">
    <button type="submit" id="reply_btn">소감 남기기</button>
   </div>
   
  </form>
 </section>
 </c:if>
 
 <section class="replyList">
  <ol>
   <li>댓글 목록</li>
   </ol>    
 </section>
</div>
로그인 여부에 따라서 출력되는 모습이 다릅니다.
dao , service impl 구현 체 작성
public void registReply(ReplyVO reply) throws Exception;

@Override
public void registReply(ReplyVO reply) {
sql.insert( namespace + ".registReply , reply);
}
controller
컨트롤러에도 메서드를 추가합니다. 이때 메서드의 매핑된 주소가 /view 인것에 주의합니다.
// 상품 조회 - 소감(댓글) 작성
@ResponseBody
@RequestMapping(value = "/view", method = RequestMethod.POST)
public String registReply(ReplyVO reply, HttpSession session) throws Exception {
 logger.info("regist reply");
 
 MemberVO member = (MemberVO)session.getAttribute("member");
 reply.setUserId(member.getUserId());
 
 service.registReply(reply);
 
 return "redirect:/shop/view?n=" + reply.getGdsNum();
}
기존 ReplyVO에서 유저 닉네임(userName)가 추가된 새로운 형태이므로, 새로운 VO를 생성합니다. ReplyVO를 복사/붙여넣기 한 뒤 ReplyListVO로 이름을 변경하고, 유저 이름(userName)를 추가한 뒤 Getter/Setter를 추가하면 됩니다.
HttpSession 을 이용해 member 세션에 저장되어있는 유저 아이디를 가져올 수 있습니다..

<!-- 상품 소감(댓글) 리스트 -->
<select id="replyList" resultType="com.kubg.domain.ReplyListVO">
 select
     r.gdsNum, r.userId, r.repNum, r.repCon, r.repDate, m.userName
 from tbl_reply r
     inner join tbl_member m
         on r.userId = m.userId
     where gdsNum = #{gdsNum}
</select>
상품 소감 테이블과 맴버 테이블을 조인하여, 유저 닉네임까지 출력할 수 있도록 쿼리를 수정했습니다.

select r.gdsnum, r.userid,r.repnum,r.repcon,r.repdate,m.username
from tb_reply r
    inner join tb_member m
        on r.userid = m.userid
        where gdsnum  =gdsnum;
컨트롤러의 상품 조회용 메서드에 코드를 추가합니다.
List<ReplyListVO> reply = service.replyList(gdsNum);
model.addAttribute("reply", reply);
```



```
무기 카테고리에 마우스를 가져가면 2차 분류가 옆에 생기고, 마우스를 다른곳으로 옮기면 사라집니다.
각 카테고리를 구분하는 cateCode를 참고하여 링크 주소를 작성합니다.
/shop/list 는 기본 경로이며, 뒤의 ?c=[번호1]&l=[번호2] 는 구분자입니다.

번호1은 cateCode와 같은 숫자를, 번호2는 카테고리의 레벨을 넣었습니다. 카테고리의 레벨이 높을수록(숫자가 클수록) 하위 카테고리입니다.

shopMapper.xml 파일을 만들고 코드 및 쿼리를 추가합니다
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.kubg.mappers.shopMapper">
   

<select id="list" resultType="com.kubg.domain.GoodsViewVO">
select
    g.gdsNum, g.gdsName, g.cateCode, c.cateCodeRef, c.cateName, gdsPrice, gdsStock, gdsDes, gdsImg, gdsDate, g.gdsImg, g.gdsThumbImg
        from tbl_goods g
            inner join goods_category c
                on g.cateCode = c.cateCode           
            where g.cateCode = #{cateCode}
</select>

</mapper>
카테고리 이름(catenName)까지 표시할 수 있어야하기 때문에, 2개의 테이블을 조인했습니다.

dao service impl 생성
shop
package com.kubg.controller;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kubg.domain.GoodsViewVO;
import com.kubg.service.ShopService;

@Controller
@RequestMapping("/shop/*")
public class ShopController {

 private static final Logger logger = LoggerFactory.getLogger(ShopController.class);
 
 @Inject
 ShopService service;
    
 // 카테고리별 상품 리스트
 @RequestMapping(value = "/list", method = RequestMethod.GET)
 public void getList(@RequestParam("c") int cateCode,
      @RequestParam("l") int level, Model model) throws Exception {
  logger.info("get llist");
  
  List<GoodsViewVO> list = null;
  list = service.list(cateCode);
 
  model.addAttribute("list", list);
  
 }
}
controller 생성
@RequestParam을 이용하여 URL에 있는 값을 가져와 변수에 저장하여 사용합니다.
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
shop 폴더 생성 home.jsp 복사해서 list jsp 생성

<ul>
 <c:forEach items="${list}" var="list">
 <li>
  <div class="goodsThumb">
   <img src="${list.gdsThumbImg}">
  </div> 
  <div class="goodsName">
   <a href="/shop/view?n=${list.gdsNum}">${list.gdsName}</a>
  </div>
 </li>
 </c:forEach>
</ul>
추가
관리자 모드에서는 테이블 태그를 이용했지만, 이번엔 목록 태그를 사용했습니다.
상의 카테고리를 클릭해봤는데.. 상의 카테고리의 하위 카테고리가 나오질 않네요.

당연하지만, 이것에 대한 처리를 하지 않았기 때문입니다.
기존에 있던 쿼리의 아이디값 을 수정하고 새로운 쿼리를 추가
<!-- 카테고리별 상품 리스트 : 1차 분류 -->
<select id="list_1" resultType="com.kubg.domain.GoodsViewVO">
select
    g.gdsNum, g.gdsName, g.cateCode, c.cateCodeRef, c.cateName,
    gdsPrice, gdsStock, gdsDes, gdsDate, g.gdsImg, g.gdsThumbImg
        from tbl_goods g
            inner join goods_category c
                on g.cateCode = c.cateCode           
            where g.cateCode = #{cateCode}
             or c.cateCodeRef = #{cateCodeRef}
</select>

<!-- 카테고리별 상품 리스트 : 2차 분류 -->
<select id="list_2" resultType="com.kubg.domain.GoodsViewVO">
select
    g.gdsNum, g.gdsName, g.cateCode, c.cateCodeRef, c.cateName,
    gdsPrice, gdsStock, gdsDes, gdsDate, g.gdsImg, g.gdsThumbImg
        from tbl_goods g
            inner join goods_category c
                on g.cateCode = c.cateCode           
            where g.cateCode = #{cateCode}
</select>
list_1은 분류용 list_2는 2차 분류용
둘다 거의 같은 코드지만 1차분류에 or 하나가 추가 된거라서 
불필요한 코드가 많으니 나중에 통합 시킬 필요가 잇음

2차 분류는 기존 코드와 차이점이 없습니다
1차 분류는 카테고리 코등와 카테고리 참조코드 2개의 매개변수를 받아온뒤 
해시 맵을 이용해 두값을 하나의 맵으로 합쳐서 매퍼로 보냈음

1차 분류와 2차 분류의 메서드 명이 list로 똑같은데 이 둘은 매개변수가 다르기 떄문에 
오버로딩 매개변수값,타입이 다르면 중복사용가능 에러는 발생 ㄴ
<style>
 section#content ul li { display:inline-block; margin:10px; }
 section#content div.goodsThumb img { width:200px; height:200px; }
 section#content div.goodsName { padding:10px 0; text-align:center; }
 section#content div.goodsName a { color:#000; }
</style>
원래 있던 스타일은 거의 공통적으로 사용되겠지만 새로 추가한 스타일은 목록에서만 사용되기 떄문에 
나중에 파일로 분류하기 용이하도록 구분해둡니다 
상의 카테고리를 클릭하면 , 상의 카테고리의 하위 카테고리도 같이나옴  카테고리의 하위 카테고리를 선택하면, 선택한 카테고리만 나오게됩니다.


<form role="form" method="post">
 <input type="hidden" name="gdsNum" value="${view.gdsNum}" />
</form>

<div class="goods">
 <div class="goodsImg">
  <img src="${view.gdsImg}">
 </div>
 
 <div class="goodsInfo">
  <p class="gdsName"><span>상품명</span>${view.gdsName}</p>
  
  <p class="cateName"><span>카테고리</span>${view.cateName}</p>
  
  <p class="gdsPrice">
   <span>가격 </span><fmt:formatNumber pattern="###,###,###" value="${view.gdsPrice}" /> 원
  </p>
  
  <p class="gdsStock">
   <span>재고 </span><fmt:formatNumber pattern="###,###,###" value="${view.gdsStock}" /> EA
  </p>
  
  <p class="cartStock">
   <span>구입 수량</span>
   <input type="number" cmin="1" max="${view.gdsStock}" value="1" />  
  </p>
  
  <p class="addToCart">
   <button type="button">카트에 담기</button>
  </p>
 </div>
 
 <div class="gdsDes">${view.gdsDes}</div>
</div>
 view.jsp
form에 상품번호 gdsnum을 숨겨놓은 이유는 장바구니에 기능과 소감을 남기기위한 이용하기 위함임<style>
 div.goods div.goodsImg { float:left; width:350px; }
 div.goods div.goodsImg img { width:350px; height:auto; }
 
 div.goods div.goodsInfo { float:right; width:330px; font-size:22px; }
 div.goods div.goodsInfo p { margin:0 0 20px 0; }
 div.goods div.goodsInfo p span { display:inline-block; width:100px; margin-right:15px; }
 
 div.goods div.goodsInfo p.cartStock input { font-size:22px; width:50px; padding:5px; margin:0; border:1px solid #eee; }
 div.goods div.goodsInfo p.cartStock button { font-size:26px; border:none; background:none; }
 div.goods div.goodsInfo p.addToCart { text-align:right; }
 div.goods div.gdsDes { font-size:18px; clear:both; padding-top:30px; }
</style>
상품이미지는 왼쪽 텍스튼는 밑에 상품정보는 오른쪽에 위치함
상품설명은 아래에 위치함 구입수량은 숫자만 입력 카트에 담기 버튼을 클릭하면 해당 상품에 카트에 담길예정
<script src="/resources/jquery/jquery-3.3.1.min.js"></script> 제이쿼리의 선택자 기능을 이용하므로 제이쿼리를 내부에 추가해야함
구입수량 수정
<p class="cartStock">
 <span>구입 수량</span>
 <button type="button" class="plus">+</button>
 <input type="number" class="numBox" min="1" max="${view.gdsStock}" value="1" readonly="readonly"/>
 <button type="button" class="minus">-</button>
 
 <script>
  $(".plus").click(function(){
   var num = $(".numBox").val();
   var plusNum = Number(num) + 1;
   
   if(plusNum >= ${view.gdsStock}) {
    $(".numBox").val(num);
   } else {
    $(".numBox").val(plusNum);          
   }
  });
  
  $(".minus").click(function(){
   var num = $(".numBox").val();
   var minusNum = Number(num) - 1;
   
   if(minusNum <= 0) {
    $(".numBox").val(num);
   } else {
    $(".numBox").val(minusNum);          
   }
  });
 </script>
 
</p>
인풋박스에 직접 입력할수없고 버튼을 눌러서 수량을 조절할 수잇음
 +- 증가감소가능  최대량은  재고보다 클수없으며 최소량은 1 직접 입력하지 않는이유는
 실수로 입력을 막기위해서 개인취향

```

```
14챕터
상품 등록 / 조회 / 수정 / 삭제 기능 정도만 있는 관리자 모드지만, 기본적인 기능은 모두 구현함

상품 소감과 , 유저 목록은 사용자 모드가 개발되고 난뒤 이어서 개발 하게 됨 
일단 소감을 작성해야 관리자가 볼수 있을 텐데 , 사용자 모드가 없으면 소감을 작성할수 없음

사용자 모드를 개발하기전 관리자 모드 점검

상품등록화면
상품 가격과 수량은 숫자만 입력해야하는데 만약 숫자가 다른 다른문자를 입력하면 데이터베이스에서 입력을 받지못해
에러가 발생합니다 
실수로 라도 숫자가아닌 다른 문자를 입력하지 못하게 막는게 좋음

<script>
	// 정규 표현식 중 하나로 숫자만 허용
	var regExp = /[^0-9]/gi;
	// 상품 가격과 수량은 입력할떄마다 numcheck를 호출 이떄 현재 선택자 this 같이보냄
	// this 현재 실행중인 선택자로 상품 가격에 입력할떄는 각자의 본인 객체를 의미 
	$("#gdsprice").keyup(function(){ numCheck($this)); });
	$("#gdsstock").keyup(function(){ numCheck($this)); });	
	// numcheck 함수는 현재 선택된 선택자를 selector 에 저장한뒤 입력된 값을 정규 표현식에 맞게 변경함
	function numCheck(selector) {
		var tempVal= selector.val();
		selector.val(tempVal.replace(regExp,""));
	}
</script>
상품 가격과 수량에 숫자가 아닌 다른 문자를 입력하려고 하면 곧 바로 지워지게 됨
상품 수정도 같은 방법으로 코드를 추가함

이번에는 상품 목록
이미지 첨부 썸네일 생성 되는 데 상품 목록에 아무것도 나오지 않는건 조금 아쉽, 그리고 카테고리가 숫자로 되어서는 한눈에
알아보기 힘듬

매퍼에서 목록을 불러오는 쿼리 수정 
이미지 첨부 기능 구현에서 했던것처럼 2개의 테이블을 조인하여 쿼리를작성
반환되는 타입은 goodsvo 가아닌 goodviewvo인것에 주의
DAO Service goodsvo를 goodsviewvo로 변경 컨트롤러도 수정
list jsp에서 img src 로 썸네일추가 이미지를 추가햇을때 나오는 이미지가 없어서 이상하게 나옴
resources/imgaes 에 none.png 회색사진 추가 
서블릿에 대체 이미지 경로 추가 
<!-- 대체 이미지 경로 -->
<resources mapping="/images/**" location="/resources/images/" />
```



```
위지윅 에디터란 
에디터에서 작성한 형태와 거의 똑같은 형태로 볼수 있게 해주는 에디터 위지윅 에디터가 없으면
태그를 하나하나 입력 해야함 이미지를 하나 추가하려면 FTP등을 이용해 이미지를 업로드 하고 URL을 알아내어
IMG 태그로 추가해야함 
하지만 이지윅 에디터가 있으면 매우 편리하게 글을 작성할 수 있음 CK에디터임

<!-- 이지윅 에디터 ck editor -->
	<script src="/resources/ckeditor/ckeditor.js"></script>
추가

상품 소개의 텍스트 에어리어 아래에 스크립트 추가

						<script>
								var ckeditor_config = {
									resize_enable : false,
									enterMode : CKEDITOR.ENTER_BR,
									shiftEnterMode : CKEDITOR.ENTER_P,
									filebrowserUploadUrl : "/admin/goods/ckUpload"
								};
								CKEDITOR.replace("gdsdes",ckeditor_config);
						</script>

Json 형태의 ckeditor_config 를 선언및 설정하고 마지막줄의 ckeditor.replace 텍스트 에어리어의 id 변수 를 이용해
텍스트 에어리어를 ckeditor로 교체함
이떄 filebrowserUploadUrl이라는 부분 파일을 업로드 할 경우 해당 부분에서 설정한 url로 전송되므로 컨트롤러에서 만들어줄
필요가있음

servlet-context.xml 추가
<!-- ck에디터 파일 업로드 경로 -->
<resources mapping="/ckUpload/**" location="/resources/ckUpload/" />

컨트롤러에 ck에디터 업로드용 메서드를 추가해줌

이미지 첨부 기능 구현과 비슷하지만 약간 다름
저장하는 폴더가 연/월/일 나누어 지지 않고 ckupload 폴더에 모두 들어가게 됨 
모든 이미지가 한 폴더에 들어가게 될 경우 파일명이 똑같아서 덮어쓰기가 될 가능성이 있으니 
UUID를 이용하여 중복되지 않도록 함 이미지 업로드가 완료되면 메세지를 띄우로도록 설정
이미지를 선택하고 서버로 전송 버튼을 클릭하면 이렇게 미리보기 까지 보이게 됨 이미지의 크기가 크다보니
부분적으로만 보임
실제 경로에도 파일이 복사 되었음

.gdsdes img { max-width:600px; height:auto;} 
최대 가로 크기이며 600 픽셀보다 작다면 작은 크기를 기준으로 표시되고 이 600 픽셀보다 크다면 600 픽셀로 표시 됨
가로크기만 변경되고 세로크기는 고정되어잇으면 이미지가 이상하게 표시 될테니까 height 는 자동으로 설정함

13강 이지윅 에디터 수정하는 파일업로드 수정하는란이 안됨





1.쇼핑몰 css입힘 
2.관리자 css입힘 
3.그 css 를 프로젝트에입힘
4.git에 다 올림
5.spring-scurity -restcontroll - 첨부파일 - ajax - oauth2.0 - crud - category
6.git v1.0.0 이렇게 정리
7.java js html css jquery oracle mysql 
```







```
위지윅 에디터란 
에디터에서 작성한 형태와 거의 똑같은 형태로 볼수 있게 해주는 에디터 위지윅 에디터가 없으면
태그를 하나하나 입력 해야함 이미지를 하나 추가하려면 FTP등을 이용해 이미지를 업로드 하고 URL을 알아내어
IMG 태그로 추가해야함 
하지만 이지윅 에디터가 있으면 매우 편리하게 글을 작성할 수 있음 CK에디터임

<!-- 이지윅 에디터 ck editor -->
	<script src="/resources/ckeditor/ckeditor.js"></script>
추가

상품 소개의 텍스트 에어리어 아래에 스크립트 추가

						<script>
								var ckeditor_config = {
									resize_enable : false,
									enterMode : CKEDITOR.ENTER_BR,
									shiftEnterMode : CKEDITOR.ENTER_P,
									filebrowserUploadUrl : "/admin/goods/ckUpload"
								};
								CKEDITOR.replace("gdsdes",ckeditor_config);
						</script>

Json 형태의 ckeditor_config 를 선언및 설정하고 마지막줄의 ckeditor.replace 텍스트 에어리어의 id 변수 를 이용해
텍스트 에어리어를 ckeditor로 교체함
이떄 filebrowserUploadUrl이라는 부분 파일을 업로드 할 경우 해당 부분에서 설정한 url로 전송되므로 컨트롤러에서 만들어줄
필요가있음

servlet-context.xml 추가
<!-- ck에디터 파일 업로드 경로 -->
<resources mapping="/ckUpload/**" location="/resources/ckUpload/" />

컨트롤러에 ck에디터 업로드용 메서드를 추가해줌

이미지 첨부 기능 구현과 비슷하지만 약간 다름
저장하는 폴더가 연/월/일 나누어 지지 않고 ckupload 폴더에 모두 들어가게 됨 
모든 이미지가 한 폴더에 들어가게 될 경우 파일명이 똑같아서 덮어쓰기가 될 가능성이 있으니 
UUID를 이용하여 중복되지 않도록 함 이미지 업로드가 완료되면 메세지를 띄우로도록 설정
이미지를 선택하고 서버로 전송 버튼을 클릭하면 이렇게 미리보기 까지 보이게 됨 이미지의 크기가 크다보니
부분적으로만 보임
실제 경로에도 파일이 복사 되었음

.gdsdes img { max-width:600px; height:auto;} 
최대 가로 크기이며 600 픽셀보다 작다면 작은 크기를 기준으로 표시되고 이 600 픽셀보다 크다면 600 픽셀로 표시 됨
가로크기만 변경되고 세로크기는 고정되어잇으면 이미지가 이상하게 표시 될테니까 height 는 자동으로 설정함

13강 이지윅 에디터 수정하는 파일업로드 수정하는란이 안됨





1.쇼핑몰 css입힘 
2.관리자 css입힘 
3.그 css 를 프로젝트에입힘
4.git에 다 올림
5.spring-scurity -restcontroll - 첨부파일 - ajax - oauth2.0 - crud - category
6.git v1.0.0 이렇게 정리
7.java js html css jquery oracle mysql 
```





```
상품 등ㄹ록시 이미지 첨부 기능이 구현 
상품을 수정할 떄 이미지를 수정하는 기능은 아직 없음

상품을 수정할떄 이미지를 변경 안하는 경우도 있고 , 변경하는 경우도 있음 
이미지를 변경하지 않았을떈 기존에 있는 이미지를 다시 데이터 베이스에 저장하거나 이미지를 다루지 않는
쿼리문을 이용하면 되는데 저는 기존에 있는 이미지를 다시 데이터베이스에 저장하는 방법으로 함

register.jsp 에 있는 이미지 관련 코드 복사해서 modify.jsp로 붙여 넣음 기존 이미지를 출력하는 HTML 코드와
원본 이미지 썸에일을 저장하는 숨겨진 hidden 인풋박스를 추가해줌
<div class="inputArea">
 <label for="gdsImg">이미지</label>
 <input type="file" id="gdsImg" name="file" />
 <div class="select_img">
  <img src="${goods.gdsImg}" />
  <input type="hidden" name="gdsImg" value="${goods.gdsImg}" />
  <input type="hidden" name="gdsThumbImg" value="${goods.gdsThumbImg}" /> 
 </div>
 
 <script>
  $("#gdsImg").change(function(){
   if(this.files && this.files[0]) {
    var reader = new FileReader;
    reader.onload = function(data) {
     $(".select_img img").attr("src", data.target.result).width(500);        
    }
    reader.readAsDataURL(this.files[0]);
   }
  });
 </script>
 <%=request.getRealPath("/") %>
</div>
폼에 enctype=multipart/form-data 추가해줌
이미지  원본 크기가 그대로 나오면 너무 큼, css 적당한 크기를 설정

매퍼쿼리에 업데이트문에 쿼리추가

컨트롤러에 상품 수정 메서드의 매개변수와 내부 코드를 추가
// 상품 수정
@RequestMapping(value = "/goods/modify", method = RequestMethod.POST)
public String postGoodsModify(GoodsVO vo, MultipartFile file, HttpServletRequest req) throws Exception {
 logger.info("post goods modify");

 // 새로운 파일이 등록되었는지 확인
 if(file.getOriginalFilename() != null && file.getOriginalFilename() != "") {
  // 기존 파일을 삭제
  new File(uploadPath + req.getParameter("gdsImg")).delete();
  new File(uploadPath + req.getParameter("gdsThumbImg")).delete();
  
  // 새로 첨부한 파일을 등록
  String imgUploadPath = uploadPath + File.separator + "imgUpload";
  String ymdPath = UploadFileUtils.calcPath(imgUploadPath);
  String fileName = UploadFileUtils.fileUpload(imgUploadPath, file.getOriginalFilename(), file.getBytes(), ymdPath);
  
  vo.setGdsImg(File.separator + "imgUpload" + ymdPath + File.separator + fileName);
  vo.setGdsThumbImg(File.separator + "imgUpload" + ymdPath + File.separator + "s" + File.separator + "s_" + fileName);
  
 } else {  // 새로운 파일이 등록되지 않았다면
  // 기존 이미지를 그대로 사용
  vo.setGdsImg(req.getParameter("gdsImg"));
  vo.setGdsThumbImg(req.getParameter("gdsThumbImg"));
  
 }
 
 adminService.goodsModify(vo);
 
 return "redirect:/admin/index";
}

상품 등록 메서드와 거의 같은 코드인데 , 새로운 이미지 파일이 등록되었는지 확인하는 과정이 추가됨
새로운 이미지가 등록 되지 않았다면 기존 이미지를 그대로 사용하고 새로운 이미지가 등록 되어있으면 기존 이미지를
삭제 후 새로운 이미지를 등록
```


```
8강 카테고리가 정상적으로 나오니 상품등록  게시판의 게시물 작성이랑 같음
CRUD Create read update delete

<form role="form" method="post" autocomplete="off">

<div class="inputArea"> 
 <label>1차 분류</label>
 <select class="category1">
  <option value="">전체</option>
 </select>

 <label>2차 분류</label>
 <select class="category2" name="cateCode">
  <option value="">전체</option>
 </select>
</div>

<div class="inputArea">
 <label for="gdsName">상품명</label>
 <input type="text" id="gdsName" name="gdsName" />
</div>

<div class="inputArea">
 <label for="gdsPrice">상품가격</label>
 <input type="text" id="gdsPrice" name="gdsPrice" />
</div>

<div class="inputArea">
 <label for="gdsStock">상품수량</label>
 <input type="text" id="gdsStock" name="gdsStock" />
</div>

<div class="inputArea">
 <label for="gdsDes">상품소개</label>
 <textarea rows="5" cols="50" id="gdsDes" name="gdsDes"></textarea>
</div>

<div class="inputArea">
 <button type="submit" id="register_Btn" class="btn btn-primary">등록</button>
</div>
이용해 각 입력요소를 묶어두었음 이렇게 하면 나중에 CSS  적용할떄
일괄적으로 작업이 가능 
div inputArea로 단락을 주었기 떄문에 css 줄떄 편함
input id , name은 GoodsVO를 참고 만듬 GoodsVO는 tbl_goods를 참고
 변수나 , vo나 쿼리는 이름이 같아야 알아보기도 쉽고 역할에도 맞고
새로운 이름을 만들어야할 귀찮음도 없음 

</form>
더미데이터 
insert into tbl_goods (gdsnum,gdsname,catecode,gdsprice,gdsstock,gdsdes) values (tbl_goods_seq.nextval,'상품이름',100,1000,30,'상품 설명');

상품 등록시 이미지 파일을 업로드하는 기능은 없기 떄문에 , gdsimg는 입력하지 않았음 그에 따라 null 이 표시 되고있음

<!-- 상품 등록 -->
<insert id="register">
	insert into tbl_goods (GDSNUM,GDSNAME,CATECODE,GDSPRICE,GDSSTOCK,GDSDES) 
	values (tbl_goods_seq.nextval, #{gdsnum},#{gdsname},#{catecode},#{gdsprice},#{gdsstock},#{gdsdes}
</insert>
어드민 매퍼에 만들고 

하면서 겪었던 문제들 -  
500 error 부적합한 널유형 , 쿼리단 필드값을 무의식 적으로 하나 추가 더해서 계속 부적합 널 유형 쿼리문과 VO를 대문자로 바꾸고 소문자로 바꾸고 , 지지고 볶았는데 필드값이 더들어간거였음, error가 영어로 나오기 떄문에 지레짐작으로 생각해서 하지 않기 영어공부가 필수적으로 느껴짐 , 영미권 사람들은 다소 우리나라 사람보다 다소 편하게 느껴질수 있을듯,
405 error가 있었는데 post 에러엿음 , admin 단에 get단만 만들고 jsp 에서 보내주는 post값을 받아오는 컨트롤단 
메서드가 없었기 떄문에 에러가 생김 당연한건데 무의식으로 하다보니 에러가 자주 발생, 결국엔 post단 만들어서 해결
나중에 프로젝트가 커지게 되면 에러 파악하기 힘든점 , + 검증하면서 코딩하기 

```

```
스프링 무작정 따라하기 7강 (챕터)
상품 등록 기능을 추가하기 전에 카테고리를 먼저 구성

다양한 상품을 다룰떈 카테고리가 유동적일수 있지만 
쇼핑몰 상품 갯수가 적고 거의 변동이 없기 떄문에 미리 카테고리 구성을 해두면 편함

카테고리 추가 , 삭제 , 수정 기능도 구현
catename , catecode , catecoderef
이름 / 고유번호 / 참조번호
상의 / 100
반팔 티셔츠 101 긴팔 티셔츠 101  참조코드 100
맨투맨 101 100  

카테고리 1, 100
카테고리 1-1 101 100
카테고리 1-2 102 100
아우터 
가디건 201  슈트/블레이저 201 
카테고리 2. 200
카테고리 2-1 201 200
카테고리 2-2 202 200

catecoderef 참조코드가 없으면 최상위 카테고리 / 값이 다른 catecode와 같다면 카테고리는 catecode 하위 카테고리
ref는 catecode를 참조 하기 떄문에 cateCoderef는 존재하지 않는 catecode를
입력 할수 없음
insert into goods_category(catename,catecode) values("상의","100");
insert into goods_category(catename,catecode,catecoderef) values("반팔 티셔츠","101","100");
insert into goods_category(catename,catecode,catecoderef) values("긴팔 티셔츠","102","100");
insert into goods_category(catename,catecode,catecoderef) values("맨투맨","103","100");
insert into goods_category(catename,catecode,catecoderef) values("후드 티","104","100");
insert into goods_category(catename,catecode) values("아우터","200");
insert into goods_category(catename,catecode,catecoderef) values("가디건","201","200");
insert into goods_category(catename,catecode,catecoderef) values("슈트/블레이저","202","200");
insert into goods_category(catename,catecode) values("하의","300");
insert into goods_category(catename,catecode) values("신발","400");

카테고리를 사용하기위해 CategoryVO 추가
select 
		LEVEL,CATENAME,CATECODE,CATECODEREF
		from goods_category
		start width CATECODEREF IS NULL CONNECT BY PRIOR CATECODE=CATECODEREF
매퍼쿼리 카테고리의 계층에 맞게 레벨값이 커지는 구조
1.상의
2.반팔티셔츠
2.후드티
2긴팔티셔츠
2.맨투맨
1.아우터
1.하의
1.신발
컨트롤단 / VO / 매퍼 / DAO / Service / jsp 순 관리자단먼저
1차분류는 상위  / 2차 분류는 하위 카테고리
자바스크립트와 제이쿼리 이용

//컨트롤러에서 데이터 받기
var jsonData = JSON.parse('${category}');
console.log(jsonData);

var cate1Arr = new Array();
var cate1Obj = new Object();

//1차 분류 셀렉트 박스에 삽입할 데이터 준비
for(var i = 0; i < jsonData.length; i++) {

if(jsonData[i].level == "1") {
cate1Obj = new Object();  //초기화
cate1Obj.cateCode = jsonData[i].cateCode;
cate1Obj.cateName = jsonData[i].cateName;
cate1Arr.push(cate1Obj);
}
}

//1차 분류 셀렉트 박스에 데이터 삽입
var cate1Select = $("select.category1")

for(var i = 0; i < cate1Arr.length; i++) {
cate1Select.append("<option value='" + cate1Arr[i].cateCode + "'>"
   + cate1Arr[i].cateName + "</option>"); 
}

//  컨트롤러에서 모델에 보낸 값인 category를 jsonData 저장 / jsonData에서 level 값이 1인 경우에만 1obj추가 이 추가한 데이터를 cate1Arr 추가 
//  이럻게 추가한 값을 cate1Select 에 추가

상품 등록페이지로 이동해 보면 1차 분류에 데이터가 들어가있는걸 확인 할 수 있음


============================ 
 select box 1차 메뉴 2차메뉴 생성하는 js
<script>
// 컨트롤러에서 데이터 받기
var jsonData = JSON.parse('${category}');

// 필요한 배열과 오브젝트 변수 생성
var cate1Arr = new Array();
var cate1Obj = new Object();

// 1차 분류 셀렉트 박스에 삽입할 데이터 준비
for(var i = 0; i < jsonData.length; i++) {
	
	if(jsonData[i].level == "1") {  // 레벨이 1인 데이터가 있다면 
		cate1Obj = new Object();  // 초기화
		
		// cate1Obj에 cateCode와 cateName를 저장
		cate1Obj.catecode = jsonData[i].catecode; 
		cate1Obj.catename = jsonData[i].catename;
		
		// cate1Obj에 저장된 값을 cate1Arr 배열에 저장
		cate1Arr.push(cate1Obj);
	}
}

// 1차 분류 셀렉트 박스에 데이터 삽입
var cate1Select = $("select.category1")

for(var i = 0; i < cate1Arr.length; i++) {

	// cate1Arr에 저장된 값을 cate1Select에 추가
	cate1Select.append("<option value='" + cate1Arr[i].catecode + "'>"
						+ cate1Arr[i].catename + "</option>");	
}


// 클래스가 category1인 select변수의 값이 바뀌었을 때 실행
$(document).on("change", "select.category1", function(){

	
	// 필요한 배열과 오브젝트 변수를 생성
	var cate2Arr = new Array();
	var cate2Obj = new Object();
	
	// 2차 분류 셀렉트 박스에 삽입할 데이터 준비
	for(var i = 0; i < jsonData.length; i++) {
		
		if(jsonData[i].level == "2") {  // 레빌이 2인 데이터가 있다면
			cate2Obj = new Object();  // 초기화
			
			// cate2Obj에 cateCode, cateName, cateCodeRef를 저장
			cate2Obj.catecode = jsonData[i].catecode;
			cate2Obj.catename = jsonData[i].catename;
			cate2Obj.catecoderef = jsonData[i].catecoderef;
			
			// cate2Obj에 저장된 값을 cate1Arr 배열에 저장
			cate2Arr.push(cate2Obj);
		} 
	}
	
	var cate2Select = $("select.category2");
	
	/*
	for(var i = 0; i < cate2Arr.length; i++) {
			cate2Select.append("<option value='" + cate2Arr[i].cateCode + "'>"
								+ cate2Arr[i].cateName + "</option>");
	}
	*/
	
	// cate2Select의 값을 제거함(초기화)
	cate2Select.children().remove();
 	
	
	// cate1Select에서 선택한 값을 기준으로 cate2Select의 값을 조정
	$("option:selected", this).each(function(){
		
		var selectVal = $(this).val();  // 현재 선택한 cate1Select의 값을 저장
	
		cate2Select.append("<option value='" + selectVal + "'>전체</option>");  // cate2Select의 '전체'에 현재 선택한 cate1Select와 같은 값 부여
		
		// cate2Arr의 데이터를 cate2Select에 추가
		for(var i = 0; i < cate2Arr.length; i++) {
			
			// 현재 선택한 cate1Select의 값과 일치하는 cate2Arr의 데이터를 가져옴
			if(selectVal == cate2Arr[i].catecoderef) {
				cate2Select.append("<option value='" + cate2Arr[i].catecode + "'>"
									+ cate2Arr[i].catename + "</option>");
			}
		}		
	});
});

```



```
6강
관리자단 /사용자단  두화면 직접 css를 입력하여 사용할것
관리자 화면 에 부트스트랩을 아주 조금만 사용할것
버튼이나 모달 정도 만 사용할 것이기 떄문에 굳이 부트스트랩 사용안해도됨 

부트 스트랩 이전에 제이쿼리를 넣어줘야함 
부트 스트랩은 제이쿼리 기반으로 작동 되기 떄문임
아무런 설정을 하지않고 부트스트랩만 적용해도 약간 달라짐;

상품 등록은 새로운 상품을 등록할떄 사용함
상품 목록은 등록했던 상품을 확인 수정/삭제를 할수 있음
상품 소감은 사용자들이 상품에 작성한 댓글을 확인할수 있음
유저 목록은 사이트에 가입된 유저에 대한 정보를 확인할수 있음
기본적으로 이정도 기능
<script src="/resources/jquery/jquery-3.3.1.min.js"></script>

<link rel="stylesheet" href="/resources/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/resources/bootstrap/bootstrap-theme.min.css">
<script src="/resources/bootstrap/bootstrap.min.js"></script>

	
<style>
	body { font-family:'맑은 고딕', verdana; padding:0; margin:0; }
	ul { padding:0; margin:0; list-style:none;  }
 
	div#root { width:90%; margin:0 auto; }
	
	header#header { font-size:60px; padding:20px 0; }
	header#header h1 a { color:#000; font-weight:bold; }
	
	nav#nav { padding:10px; text-align:right; }
	nav#nav ul li { display:inline-block; margin-left:10px; }
 
	section#container { padding:20px 0; border-top:2px solid #eee; border-bottom:2px solid #eee; }
	section#container::after { content:""; display:block; clear:both; }
	aside { float:left; width:200px; }
	div#container_box { float:right; width:calc(100% - 200px - 20px); }
	
	aside ul li { text-align:center; margin-bottom:10px; }
	aside ul li a { display:block; width:100%; padding:10px 0;}
	aside ul li a:hover { background:#eee; }
	
	footer#footer { background:#f9f9f9; padding:20px; }
	footer#footer ul li { display:inline-block; margin-right:10px; } 
</style>
		 

```


```
4강 다시보기
5강
일반 사용자와 관리자를 구분지어서 화면을 분리 , 
관리자가 아닌 일반 사용자도 관리자 화면에 직접 url 주소를 입력하면 접속할수있는 문제점
사용자가 /admin/index 관리자 화면의 주소를 입력하면  아무 문제없이 접속이가능
로그인을 하지 않아도 접속이 잘됨  
문제점 : 
1.일반사용자가 로그인을 하지않아도 관리자 주소를 입력하면 접속가능
2.일반사용자가 로그인하고 관리자 주소를 입력하면 접속이 가능

해결책 : 인터셉터사용
인터셉터 = 특정 경로를 요청하여 컨트롤러에 접근하는 도중에 가로채서 그 전 / 후 에 실행 되는 기능
다양한곳에서  사용할수있음 / 로그인 여부에 대해 사용하기 좋다

ex) 로그인한 상태에서 장시간 대기상태에 있으며 세션이 만료되어 자동으로 로그아웃이 되지만
현재 화면에서 다른 페이지로 이동하거나 새로고침을 하지 않으면 
로그인 상태의 화면이 그대로 보임 이 떄 회원정보 수정 등의 로그인이 필요한 
페이지로 이동하게 되면 ? 
화면에서 로그인이 되어있지만 실제로는 세션이 만료되어 로그아웃 되어잇는 상태이기 떄문에 에러가발생
인터셉터를 추가하면 특정 경로의 페이지로 이동되는 순간 세션의 여부를 확인하여 로그인이 되어있다면
컨트롤러로 진행을 계속하고 로그인이 되어있지 않다면 로그인 페이지로 이동하여 로그인을 진행할수있음


```


```
2020-09-23
회원테이블
아이디 , 비밀번호 , 닉네임 , 연락처 , 주소 , 가입날짜 , 인증여부 
주소 , 우편번호 와 주소 상세주소 / 제약조건 null 회원가입시 주소를 입력하지 않아도 됨
회원가입시 메일인증할 예정  / 인증여부 컬럼 존재 / 기본값 0이면 인증이 안된 상태 
0이 아니면 인증이 된 상태로 구분

오라클에서는 기본적으로 카멜표기법, 소문자로 , create 해도  대문자로 받아들여지기떄문에 
mybatis 에서 값을줄떄
insert into tb_member (USERID,USERPASS,USERNAME,USERPHON)
values (#{userid},#{userpass},#{username},#{userphon}
과 같은 방식으로 DB 테이블필드값은 대문자로  MemberVO 필드값은 개발자가 준 방식대로 지정해야 500에러가 발생하지않음 
view단 (jsp)단에서는 vo 값을 매칭 시켜주려면 input이나 다른 태그에 name="userid"값을 줘야 컨트롤단에서 받아올수 있음 

============================================================
제약조건 변경하는 쿼리문 알아야함 //
멤버테이블 
create table tb_member (
userId varchar2(50) not null,
userPass varchar2(100) not null,
userName varchar2(30) not null,
userPhon varchar2(20) not null,
userAddr1 varchar2(20) null,
userAddr2 varchar2(50) null,
userAddr3 varchar(50) null,
regdate date default sysdate,
verify number default 0,
primary key(userId)
);
======================
상품번호 , 상품 이름 , 분류 , 가격 , 수량 , 설명 , 이미지 , 등록 날짜 로 구성
수량과 설명 이미지는 입력하지 않더라도 등록 될수 있도록 제약조건을 널로 했습니다.
not null 시에는 무조건 입력해야지만 등록이 되는상태 - 기입하지않으면 쿼리 작동안함 

분류는 1차 분류할수있지만 2차 또는 그 이상 분류가 가능 
# 커피 
- 아메리카노 
- 카페라떼
# 간식
-쿠키 -초코쿠키
-빵 - 프레즐 , 미니모카

2차분류까지 할예정이기 떄문에 분류용 테이블 하나 더만듬
여기서 2차태그를 걸려면 그에 맞게 테이블을 하나더 생성해줘야함 
상품 테이블 
create table tbl_goods ( 
gdsNum number not null,
gdsName varchar2(50) not null,
cateCode varchar2(30) not null,
gdsPrice number not null,
gdsStock number null,
gdsDes varchar(500) null,
gdsImg varchar(200) null,
gdsDate date default sysdate,
primary key (gdsNum)
);
상품 분류 테이블 
카테고리 테이블은 카테고리 이름 , 코드 ,참조코드, 구성
테이블 내부에서 참조가 발생하므로 생성과 동시에 참조키가 생성
카테코리 이름은 말그대로 이름  실제 표시되는이름
코드 , 개발자가 규칙에 의거하여 부여하는 코드
참조 코드 상위 카테고리가 무엇인지 가르키는코드임

이름 		코드 참조코드
커피 		100   null
아메리카노 	101   100
카페라떼 		102   100
에이드		200   null
자몽에이드	201   200

참조 코드가 없는 (null) 카테고리는 무엇도 참조하지 않는 최상위 카테고리
아메리카노는 자신만의 코드를 가지고 있으며 참조코드로 100을 가지고 있음
코드가 100 인것은 커피이므로 아메리카노는 커피의 하위 카테고리임을 알수있음
에이드도 마찬가지로 자몽에이드는 참조코드로 200 을 가지고 있음 자몽에이드는 에이드의 하위 카테고리임

create table goods_category (
cateName varchar(20) not null,
cateCode varchar(30) not null,
cateCodeReg varchar(30) null
primary key (cateCode),
foreign key (cateCodeRef) reference goods_category(cateCode)
);
==============================================
카테고리 테이블 에서는 같은 테이블에서 참조가 발생하지만 
상품 테이블과 카테고리 테이블은 다른 테이블이기 떄문에 별도로 쿼리작성 해야함
alter table [ 테이블 이름 ] add
    constraint [ 제약조건 이름 ]
    foreign key ([ 참조할 컬럼 이름 ])
        references [ 참조되는 테이블 이름 ]([ 참조되는 컬럼 이름 ]);
alter table tbl_goods add constraint fk_goods_category
foreign key (cateCode)references goods_category(cateCode);
참조라는것은 참조되는 테이블의 컬럼에 있는값이 참조하는 테이블의 컬럼에도 있어야만 한다는것
참조되는 테이블과 참조하는 테이블의 값이 다르면 에러가발생 
================================================
상품 테이블의 상품 번호의 자동입력을 위한 시퀀스 생성
create sequence tbl_goods_seq;
===========================================
매퍼 > DAO > Service > Controoler  > JSP
회원기능은 모두 member 폴더에 통합 보관 하여 회원가입용 페이지
signup.jsp 작성 
모든 jsp 파일을 하나 하나 입력 하지말고 home.jsp 를 복사한뒤 
div container 안에 입력하는 방식 모든 페이지가 똑같이 구성 되어있다면 추가/편집이 용이하기 떄문에
매우 편리함  더나아가서 tiles를 이용하는 방법도 있는데 타일즈는 나중에 사용
===================================================
sign up
아이디 인풋박스의 타입을 이메일로 하여서 메일형식으로만 받을수있도록 설정
패스워드 타입을 *  표시 설정 모든 인풋박스에 required 설정
이렇게 해서 jsp 에서 미리 설정 해둠  나중에 다시한번 작업을 거침
====================================================
```
