insert into user_details (non_expired, non_locked, credentials_non_expired, enabled, password, username)
values (true, true, true, true, '$2a$10$0pdaXLgs8JnnUAnoBpRUr.2t58nR7WxdHZItE3FrPlasGh5wwc.iO', 'first_user');

insert into user_details (non_expired, non_locked, credentials_non_expired, enabled, password, username)
values (true, true, true, true, '$2a$10$RpNj2Cee13LJ17qTYmOyMu1wEKDCkhj.jXTB2YG6WUJp8RcGfyP46', 'second_user');

insert into user_details (non_expired, non_locked, credentials_non_expired, enabled, password, username)
values (true, true, true, true, '$2a$10$un2cesIzEeXGVnOtrtmiX.uJruwQkijNgrvcyDU5nEdwBMwk2ps3C', 'third_user');


insert into authority (authority_name, user_details_id)
values ('USER',
            (
            select user_details_id from user_details
            where username = 'first_user'
            ));

insert into authority (authority_name, user_details_id)
values ('USER',
        (
            select user_details_id from user_details
            where username = 'second_user'
            ));

insert into authority (authority_name, user_details_id)
values ('USER',
        (
            select user_details_id from user_details
            where username = 'third_user'
            ));


