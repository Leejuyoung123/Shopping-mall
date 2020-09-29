### 스프링 무작정 따라하기  클론코딩() 생각이 안날떄는 다시 재정의
### 기술참조 kuzuro https://kuzuro.blogspot.com/2018/10/3.html


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

