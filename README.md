# 도서 대여 프로젝트 개요서

개요: Book 테이블, Member 테이블, Rent 테이블 각각의 테이블에 DB를 연결하여 CRUD 기능을 구현,
      도서 대여 프로젝트를 만드는 것이 목표이다.

시작하기 전:
  - 자바 프로젝트 폴더명:Project_BookManagement
  - 패키지명: book_Management
  - 클래스 이름: 
    - 매인 클래스 이름: Main
    - Book 테이블 관련 클래스 이름: Book
    - Book 테이블 생성자: bo ex) Book bo = new Book();
    - Member 테이블 관련 클래스 이름: Member
    - Member 테이블 생성자: mem ex) Member mem = new Member();
    - Rent 테이블 관련 클래스 이름: Rent
    - Rent 테이블 생성자: re ex) Rent re = new Rent();

1. 테이블 내용
 1-1. Book 테이블(테이블 내용 왠만하면 건들지 마세요)
  - 컬럼명:
  - id : index, 양의 정수
  - title : 책 제목, 가변문자열(64), not null
  - isbn : 책 일련번호, 가변문자열(10), 기본키
  - price : 책 가격, 양의 정수
  - publisher : 출판사, 가변문자열(64)
  - pubyear : 출판년도, 양의 정수
  - author : 저자, 가변문자열(16)
  - qty : 전체 책 수량, 양의 정수
  - created : 생성된 시간, datetime
  - updated : 수정된 시간, datetime

 1-2. Member 테이블
  - 컬럼명:
  - id : index, 양의 정수
  - pname : 사람이름, 가변문자열(32), not null
  - userid : 개인아이디, 가변문자열(32), 기본키
  - gender : 성별, 문자열(2)
    (입력 방식: 남 or 여)
  - mobile : 모바일번호, 가변문자열(16)
    (입력 할 때: 010-1234-5678 << 이런식으로 입력해야함)
  - pw : 패스워드, 가변문자열(32), not null
  - created : 생성된 시간, datetime
  - updated : 수정된 시간, datetime

 1-3. Rent 테이블
  - 컬럼명:
  - id : 기본키, 양의 정수, 자동증가
  - book_id : 책 아이디((isbn in Book테이블)을 references 하는 외래키)
  - member_id : 사람 아이디((userid in Member테이블)을 references 하는 외래키)
  - qty : 빌린 책 수량, 양의 정수
  - created : 빌린 날짜, datetime
  - expirydate : 반납 만기 날짜, datetime

2. 테이블 별 구현 해야 할 내용
 2-1. Book클래스 구현(CRUD) - 장기쁨
  - 추가(C:Create), addBook(): 할 필요 없음,
                    만약 추가를 한다면 테이블 내에 있는 id를 가져와서 그 값에 +1한 값을 새로 추가할 데이터의 id로 전달
                    (그 이유: id가 자동증가가 아니기 때문에 id를 매번 입력해주어야 하기 때문에)
  - 출력(R:Read), printBook() : 1. 테이블 내에 있는 모든 값들 출력 // 2. 책 제목 or 책 일련번호 or 저자를 입력받아 그에 해당하는 내용 출력 
  - 수정(U:Update), updateBook() : 1. 한번에 다 바꾸기 // 2. (가능하면) 원하는 내용만 수정
  - 삭제(D:Delete),deleteBook() : 일련번호를 입력받아 그 일련번호에 해당하는 데이터 삭제

Book클래스 추가 사항
- Switch-case문을 쓰든 if-else문을 쓰든 그건 본인 자유
- 수정, 삭제, 출력 이 3메소드에서 책 정보를 찾을 때 모든 경우의 수(정확히는 책 이름, isbn, 저자, 출판사)로 찾고 id를 이용해 원하는 책의 정보만 수정, 삭제, 출력 메소드 제작
- 이때 출력 메소드는 id를 이용 안해도 상관 X
- ex) 삭제 메소드에서 한다는 가정 하에
      switch(str)
      case("이름") : delName();
      case("일련번호") : delIsbn();
      case("저자") : delAuthor();
      case("출판사") : delPublisher();
- 이런 식으로 모든 경우의 수를 각각의 메소드를 만들어 Book 테이블에 있는 추가 메소드 제외 나머지 3개의 메소드를 이런식으로 구현

 2-2. Member클래스 구현(CRUD) - 허예찬
  - 추가 메소드 구현 할 때 id를 1로 선언 후 추가 할 때마다 id를 1씩 증가 시켜줘야함(id가 자동증가가 아니기 때문에)
  - 추가(C:Create), addMember() : 회원가입처럼 값들을 입력받아 그 값들을 테이블에 저장하는 메소드
                    (가능하면 회원가입 시에 만약 입력한 userid가 테이블 내에 있을 경우 break하는 메소드) 
  - 출력(R:Read), printMember() : userid를 입력받아 그 아이디에 해당하는 사람 출력
  - 수정(U:Update), updateMember() : userid를 입력받아 그 아이디에 해당하는 사람의 정보를 수정
  - 삭제(D:Delete), deleteMember() : userid를 입력받아 그 아이디에 해당하는 사람의 정보를 삭제

2-3. Rent클래스 구현(CRUD) - 변제헌
 - 책 대여(C)
   - ID와 PW를 입력 -> 로그인에 성공하면 빌릴 책을 입력 -> 입력한 이름이 포함된 책들의 리스트 출력 -> 그 리스트 중 빌릴 책을 INDEX를 이용해 선택 -> 몇권 빌릴 것인지 입력(이때, Book테이블의 qty가 0이면 빌리기 불가) -> 다 입력이 되면 Rent테이블에 Insert
 - 대여 정보 출력(R)
   - 전체 출력: Rent테이블의 모든 데이터를 출력
   - 선택 출력: 아이디와 이름으로 사람을 찾아 그 사람에 해당하는 데이터 출력, 동명이인이 있을경우 찾을 사람의 index를 입력해 특정한 사람의 데이터를 출력
 - 대여 연장(U)
   - 일괄 연장: 로그인 후 일괄 연장을 할지 말지를 선택, 일괄 연장을 선택하면 일괄 연장 실행 아니면 종료
     - 선택 연장: 일괄 연장을 선택 안하면 선택 연장 실행, 그 사람의 책 중 한권을 선택해 연장
 - 책 반납(D)
   - 로그인 한 후 로그인에 성공하면 Rent 안에 있는 그 사람에 대한 데이터를 출력 -> 출력된 책들 중 원하는 책을 선택해 반납
     
