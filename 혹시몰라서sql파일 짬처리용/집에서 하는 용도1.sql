create database project;
use project;

create table Book(
	id int unsigned, # id
    title varchar(64) not null, # 책 제목
    isbn varchar(10) primary key, # 책 일련번호
    price int unsigned, # 책 가격
    publisher varchar(64), # 책 출판사
    pubyear int unsigned, # 출판년도
    author varchar(16), # 저자명
    qty int unsigned, # 책 수량
    created datetime default current_timestamp, 
    updated datetime default current_timestamp
);

desc Book;
commit;
select * from Book order by id;

create table Member(
	id int unsigned,
    pname varchar(32) not null, # 사람 이름
    userid varchar(32) primary key, # 개인아이디
    gender char(2), # 성별
    mobile varchar(16), # 모바일번호
    pw varchar(32) not null, # 패스워드
    created datetime default current_timestamp, 
    updated datetime default current_timestamp
);

desc Member;
select * from Member;

# id, book_id, member_id, created(빌린날짜), returned(반납한 날짜), 
create table Rent(
	id int unsigned auto_increment primary key, 
    book_id varchar(10), # 책 아이디(Book -> isbn)
    member_id varchar(32), # 사람 아이디(Member -> userid)
    qty int unsigned, # 책 수량
    created datetime default current_timestamp, 
    expirydate datetime default current_timestamp
);

desc Rent;
select * from Rent;

commit;

select r.id, b.title, r.book_id, m.pname ,r.member_id, r.qty ,r.created, r.expirydate
from Rent r, Book b ,Member m
where r.book_id = b.isbn and r.member_id = m.userid;

alter table rent add foreign key(book_id) references book(isbn);
alter table rent add foreign key(member_id) references member(userid);

drop table Rent;
drop table Book;
drop table Member;