

#mysql
create database mydb default character set utf8 collate utf8_general_ci;


insert into mydb.tb_user(wdate, username, email, password, phone, realname, roles) values(NOW(), 'user01', 'user01@ddd.com', '1234', '010-0000-0001', '홍길동', 'USER');
insert into mydb.tb_user(wdate, username, email, password, phone, realname, roles) values(NOW(), 'user02', 'user02@ddd.com', '1234', '010-0000-0002', '장길산', 'USER');
insert into mydb.tb_user(wdate, username, email, password, phone, realname, roles) values(NOW(), 'user03', 'user03@ddd.com', '1234', '010-0000-0003', '임꺽정', 'USER');


 insert into tb_board(contents, title,member_id, create_date)values('내용1', '제목1', 1, now());
 insert into tb_board(contents, title,member_id, create_date)values('내용2', '제목2', 2, now());
 insert into tb_board(contents, title,member_id, create_date)values('내용3', '제목3', 3, now());
 insert into tb_board(contents, title,member_id, create_date)values('내용4', '제목4', 1, now());
 
 
     select
        b1_0.id,
        b1_0.contents,
        b1_0.create_date,
        b1_0.member_id,
        b1_0.title 
    from
        tb_board b1_0 
    LIMIT 0, 50
    
    
    
    
    http://localhost:9000/api/board/list?page=0&size=10
    
    
    