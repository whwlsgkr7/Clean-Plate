-- 테스트 계정
insert into user_account (user_id, pwd, nick_name, email, address, created_at, created_by, modified_at, modified_by)
values ('qqq', '1234', 'hi', 'user123@example.com', '우리집', now(), '내가만듬', now(), '내가 수정함');

-- 테스트 food
insert into food (food_name, user_id, quantity, category, storage, expiration, created_at, created_by, modified_at, modified_by) values
    ('사과', 'qqq', 1, '과일', '실온', '2024-04-08', now(), 'qqq', now(), 'qqq');
insert into food (food_name, user_id, quantity, category, storage, expiration, created_at, created_by, modified_at, modified_by) values
    ('바나나', 'qqq', 1, '과일', '실온', '2024-04-05', now(), 'qqq', now(), 'qqq');
insert into food (food_name, user_id, quantity, category, storage, expiration, created_at, created_by, modified_at, modified_by) values
    ('포도', 'qqq', 1, '과일', '실온', '2024-04-04', now(), 'qqq', now(), 'qqq');
insert into food (food_name, user_id, quantity, category, storage, expiration, created_at, created_by, modified_at, modified_by) values
    ('배', 'qqq', 1, '과일', '실온', '2024-04-03', now(), 'qqq', now(), 'qqq');
insert into food (food_name, user_id, quantity, category, storage, expiration, created_at, created_by, modified_at, modified_by) values
    ('귤', 'qqq', 1, '과일', '실온', '2024-04-02', now(), 'qqq', now(), 'qqq');
insert into food (food_name, user_id, quantity, category, storage, expiration, created_at, created_by, modified_at, modified_by) values
    ('오렌지', 'qqq', 1, '과일', '실온', '2024-04-06', now(), 'qqq', now(), 'qqq');

