<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
					<form role="form" method="post" autocomplete="off">
						<div class="input_area">
							<label for="userid">아이디
								<input type="email" id="userid" name="userid" placeholder="example@email.com" required="required">
							</label>
						</div>
						
						<div class="input_area">
							<label for="userpass">패스워드
								<input type="password" id="userpass" name="userpass" required="required" />						
							</label>
						</div>
						
						<div class="input_area">
							<label for="username">닉네임
								<input type=text id="username" name="username" placeholder="닉네임을 입력해주세요" required="required" />						
							</label>
						</div>
						
						<div class="input_area">
							<label for="userphon">연락처
								<input type="text" id="userphon" name="userphon" placeholder="연락처를 입력해주세요" required="required" />						
							</label>
						</div>
						
						<button type="submit" id="signup_btn" name="signup_btn">회원가입</button>	
						
					</form>
				</section>
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