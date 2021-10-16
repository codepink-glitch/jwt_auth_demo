insert into user_details (non_expired, non_locked, credentials_non_expired, enabled, password, username)
values (true, true, true, true, '$2a$10$nPMsQNwJo/HtuqJ.VaFkIerZh3J0PR25iWU.kSEfiFRbErzJyHiza', 'first_user');

insert into user_details (non_expired, non_locked, credentials_non_expired, enabled, password, username)
values (true, true, true, true, '$2a$10$LHcMqCu5NsmTLAFLpCfG0emVejoTzlDbQB2NGgMdLtQEA3rDGb39. ', 'second_user');

insert into user_details (non_expired, non_locked, credentials_non_expired, enabled, password, username)
values (true, true, true, true, '$2a$10$Cy.D1DVlezRW8S2h7NaLXulTEcaWsLn1xpMuULp09KbyFc4ca7ZHe ', 'third_user');


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


