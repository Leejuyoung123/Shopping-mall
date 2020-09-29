<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Lee Jy Store</title>
	
	<script src="/resources/jquery/jquery-3.3.1.min.js"></script>
	
	<link rel="stylesheet" href="/resources/css/user/shop/default.css" />
	
	<style>
		div.goods div.goodsimg { float:left; width:300px; }
		div.goods div.goodsimg img { width:300px; height:auto;  }
		
		div.goods div.goodsinfo { float:right; width:340px; font-size:22px; margin:30px; margin-right:20px;}
		div.goods div.goodsinfo p { margin:0 0 20px 0; }
		div.goods div.goodsinfo p span { display:inline-block; width:100px; margin-right:15px; } 
		
		div.goods div.goodsinfo p.cartstock input { font-size:22px; width:50px; padding:5px; margin:0; border:1px solid #eee; }
		div.goods div.goodsinfo p.cartstock button { font-size:26px; border:none; background:none; } 
		div.goods div.goodsinfo p.addtocart { text-align:right; }
		div.goods div.goodsinfo p.addtocart button { font-size:22px; padding:5px 10px; border:1px solid #eee; background:#eee;}
		div.goods div.gdsdes { font-size:18px; clear:both; padding-top:30px; }
	</style>
	
	<style>
		section.replyForm { padding:30px 0; }
		section.replyForm div.input_area { margin:10px 0; }
		section.replyForm textarea { font-size:16px; font-family:'맑은 고딕', verdana; padding:10px; width:500px;; height:150px; }
		section.replyForm button { font-size:20px; padding:5px 10px; margin:10px 0; background:#fff; border:1px solid #ccc; }
		
		section.replyList { padding:30px 0; }
		section.replyList ol { padding:0; margin:0; }
		section.replyList ol li { padding:10px 0; border-bottom:2px solid #eee; }
		section.replyList div.userinfo { }
		section.replyList div.userinfo .username { font-size:24px; font-weight:bold; }
		section.replyList div.userinfo .date { color:#999; display:inline-block; margin-left:10px; }
		section.replyList div.replyContent { padding:10px; margin:20px 0; }
		section.replyList div.replyFooter { margin-bottom:10px; }
		
		section.replyList div.replyFooter button { font-size:14px; border: 1px solid #999; background:none; margin-right:10px; }
		
	</style>
	
	<style>
		div.replyModal { position:relative; z-index:1; display:none; }
		div.modalBackground { position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0, 0, 0, 0.8); z-index:-1; }
		div.modalContent { position:fixed; top:20%; left:calc(50% - 250px); width:500px; height:250px; padding:20px 10px; background:#fff; border:2px solid #666; }
		div.modalContent textarea { font-size:16px; font-family:'맑은 고딕', verdana; padding:10px; width:500px; height:200px; }
		div.modalContent button { font-size:20px; padding:5px 10px; margin:10px 0; background:#fff; border:1px solid #ccc; }
		div.modalContent button.modal_cancel { margin-left:20px; }
	</style>
<script> 
function replyList() { 
	 
	var gdsnum = ${view.gdsnum};
	 $.getJSON("/shop/view/replyList" + "?n=" + gdsnum, function(data){
	  
	  var str = "";
	  $(data).each(function(){
	   
	   console.log(data);
	   
	   var repdate = new Date(this.repdate);
	   repdate = repdate.toLocaleDateString("ko-US")
	   
	 str += "<li data-gdsnum='" + this.gdsnum + "'>"
	     + "<div class='userinfo'>"
	     + "<span class='username'>" + this.username + "</span>"
	     + "<span class='date'>" + repdate + "</span>"
	     + "</div>"
	     + "<div class='replyContent'>" + this.repcon + "</div>"
	     
	     + "<div class='replyFooter'>"
	     + "<button type='button' class='modify' data-repnum='" + this.repnum + "'>M</button>"
	     + "<button type='button' class='delete' data-repnum='" + this.repnum + "'>D</button>"
	     + "</div>"
	     
	     + "</li>";           
	  });
	  
	  	$("section.replyList ol").html(str);
	 });
}
</script>

</head>
<body>

	<div id="root">
		<header id="header">
			<div id="header_box">
				<%@ include file="../include/header.jsp"%>
			</div>
		</header>
		<nav id="nav">
			<div id="nav_box">
				<%@ include file="../include/nav.jsp"%>
			</div>
		</nav>

		<section id="container">
			<div id="container_box">
				<section id="content">
					<form role="form" method="post">
						<input type="hidden" name="gdsnum" value="${view.gdsnum}" />
					</form>
					<div class="goods">
						<div class="goodsimg">
							<img src="${view.gdsimg}" />
						</div>
							<div class="goodsinfo">						
								<p class="gdsname"><span>상품명</span>${view.gdsname}</p>
								<p class="catename"><span>카테고리</span>${view.catename}</p>
								<p class="gdsprice">	
									<span>가격</span><fmt:formatNumber pattern="###,###,###" value="${view.gdsprice}" />원
								</p>
								<p class="gdsstock">
									<span>재고</span><fmt:formatNumber pattern="###,###,###" value="${view.gdsstock}" />EA
								</p>
								<p class="cartStock">
									 <span>구입 수량</span>
									 <button type="button" class="plus">+</button>
									 <input type="number" class="numBox" min="1" max="${view.gdsstock}" value="1" readonly="readonly"/>
									 <button type="button" class="minus">-</button>
									 
									<script>
									  $(".plus").click(function(){
									   var num = $(".numBox").val();
									   var plusNum = Number(num) + 1;
									   
									   if(plusNum >= ${view.gdsstock}){
										   $(".numBox").val(num);
									   } else {
										   $(".numBox").val(plusnum);										   
									   }
									 });
									  
									  $(".minus").click(function(){
									   var num = $
									   (".numBox").val();
									   var minusNum = Number(num) - 1;
									   
									   if(minusNum <= 0) {
									    $(".numBox").val(num);
									   } else {
									    $(".numBox").val(minusNum);          
									   }
									  });
									 </script>
									</p>
								<p class="addtocart">
									<button type="button">카트에 담기</button>
								</p>
							</div>
								<div class="gdsdes">${view.gdsdes}</div>
							</div>
							<div id="reply">
								<c:if test="${member== null}">
									<p>후기를 남기시려면 <a href="/member/signin">로그인</a>해주세요</p>
								</c:if>
								<c:if test="${member != null}">
								<section class="replyForm">
									<form role="form" method="post" autocomplete="off">
										<input type="hidden" name="gdsnum" id="gdsnum" value="${view.gdsnum}">
										<div class="input_area">
											<textarea name="repcon" id="repcon"></textarea>
										</div>
										<div class="input_area">
											<button type="button" id="reply_btn">후기 남기기</button>
											
											<script>
											 	
											$("#reply_btn").click(function(){
											 		var formObj = $(".replyForm form[role='form']");
											 		var gdsnum = $("#gdsnum").val();
											 		var repcon = $("#repcon").val();
											 		
											 		var data = {
											 				gdsnum: gdsnum,
											 				repcon: repcon
											 			};			
											 		
													 $.ajax({
													  url:	"/shop/view/registReply",
													  type: "POST",
													  data: data,
													  success: function(){
														  replyList();
														  $("#repcon").val("");
													  	}
													 });			
											 	});
											
											</script>
										</div>
									</form>
								</section>
								</c:if>
							<section class="replyList">
								 <ol>
								 
							<%-- <c:forEach items="${reply}" var="reply">
								  <li>
								       <div class="userinfo">
								       <span class="username">${reply.username}</span>
								       <span class="date"><fmt:formatDate value="${reply.repdate}" pattern="yyyy-MM-dd" /></span>
								      </div>
								      <div class="replyContent">${reply.repcon}</div>
							      </li>
								</c:forEach>
								 			--%>
								</ol>
							</section>
								<script>
										replyList();
								</script>	
									
								<script>
								// 스크립트로 인해 생성된 HTML의 이벤트는 .click() 메서드를 사용할 수 없음
								$(document).on("click", ".delete", function(){
									
									// 사용자에게 삭제 여부를 확인
									var deletConfirm = confirm("정말로 삭제하시겠습니까?"); 
									
									if(deletConfirm) {
									
										var data = {repnum : $(this).attr("data-repnum")};  // ReplyVO 형태로 데이터 생성
										
										$.ajax({
											url : "/shop/view/deleteReply",
											type : "post",
											data : data,
											success :function(result){
												// result의 값에 따라 동작
												console.log(result);
												if(result == 1) {	
													replyList();  // 리스트 새로고침
												}else{
													alert("작성자 본인만 할 수 있습니다.")  // 본인이 아닌 경우										
												}
											},
											error:function(){
												// 로그인하지 않아서 에러가 발생한 경우
												console.log(result);
												alert("로그인하셔야합니다.")
											}
										});
									}
								});
								
								</script>		
							</div>
							
				</section>
				<aside id="aside">
					<%@ include file="../include/aside.jsp" %>
				</aside>
			</div>
			
		</section>

		<footer id="footer">
			<div id="footer_box">
				<%@ include file="../include/footer.jsp"%>
			</div>
		</footer>

	</div>
</body>
</html>