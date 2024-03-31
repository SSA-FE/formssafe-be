INSERT INTO member (id, create_time, authority, email, image_url, nickname, oauth_server, oauth_server_id,
                    refresh_token)
VALUES (1, '2024-02-02T13:00:00', 'ROLE_USER', 'test@example.com',
        'https://www.wfla.com/wp-content/uploads/sites/71/2023/05/GettyImages-1389862392.jpg?w=1280&h=720&crop=1',
        'test', 'GOOGLE', '123', 'refresh_token1'),
       (2, '2024-02-02T13:00:00', 'ROLE_USER', 'test2@example.com',
        'https://media-cldnry.s-nbcnews.com/image/upload/t_fit-1240w,f_auto,q_auto:best/rockcms/2022-08/220805-domestic-cat-mjf-1540-382ba2.jpg',
        'test2', 'GOOGLE', '1234', 'refresh_token2');

INSERT INTO form (id, title, detail, image_url, user_id, start_date, end_date, expect_time, privacy_disposal_date,
                  is_email_visible, response_cnt, status, is_deleted, is_temp,
                  create_date, modify_date)
VALUES (1, '설문 조사 제목1', '설문 설명 1', '[
  "https://www.wfla.com/wp-content/uploads/sites/71/2023/05/GettyImages-1389862392.jpg?w=1280&h=720&crop=1",
  "https://www.wfla.com/wp-content/uploads/sites/71/2023/05/GettyImages-1389862392.jpg?w=1280&h=720&crop=1"
]', 1, '2024-02-02T13:00:00', '2024-02-04T13:00:00', 10, null, false,
        0, 'REWARDED', false, false, '2024-02-02T13:00:00', '2024-02-02T13:00:00'),
       (2, '설문 조사 제목2', '설문 설명 2', null, 1, '2024-03-31T03:05:00', '2024-04-04T13:00:00', 10, null, false,
        0, 'NOT_STARTED', false, false, '2024-02-02T13:00:00', '2024-02-02T13:00:00'),
       (3, '설문 조사 제목3', '설문 설명 3', null, 1, '2024-03-31T03:06:00', '2024-04-04T13:00:00', 10, null, false,
        0, 'NOT_STARTED', false, false, '2024-02-02T13:00:00', '2024-02-02T13:00:00'),
       (4, '설문 조사 제목4', '설문 설명 4', null, 1, '2024-03-31T03:06:00', '2024-04-04T13:00:00', 10, null, false,
        0, 'NOT_STARTED', false, false, '2024-02-02T13:00:00', '2024-02-02T13:00:00');

INSERT INTO form_batch_start (id, service_time, form_id)
VALUES (1, '2024-03-31T16:49:00', 2),
       (2, '2024-03-31T16:50:00', 3),
       (3, '2024-03-31T16:50:00', 4);

INSERT INTO form_batch_end (id, service_time, form_id)
VALUES (1, '2024-03-31T16:50:00', 2),
       (2, '2024-03-31T16:51:00', 3),
       (3, '2024-03-31T16:51:00', 4);

INSERT INTO tag (id, tag_name, count)
VALUES (1, 'tag1', 1),
       (2, 'tag2', 1);

INSERT INTO form_tag (id, form_id, tag_id)
VALUES (1, 1, 1),
       (2, 1, 2);

INSERT INTO descriptive_question (id, uuid, form_id, question_type, title, detail, is_privacy, is_required, position)
VALUES (1, 'a1f458a7-063d-48a3-b881-a84c948a1378', 1, 'SHORT', '주관식 단답형 질문1', '주관식 단답형 질문 설명1', false, false, 1),
       (2, 'faae5674-7c0c-4802-9fa4-66437fe495a0', 1, 'LONG', '주관식 장문형 질문2', '주관식 장문형 질문 설명2', false, false, 3);

INSERT INTO objective_question (id, uuid, form_id, question_type, title, detail, question_option, is_privacy,
                                is_required,
                                position)
VALUES (1, '8ca89362-3ba9-4f67-a401-c7a90c03aba8', 1, 'SINGLE', '객관식 단일 질문1', '객관식 단일 질문 설명1',
        json_array(
                json_object('id', 1, 'detail', '1 - 1'),
                json_object('id', 2, 'detail', '1 - 2'),
                json_object('id', 3, 'detail', '1 - 3')),
        false, false, 2),
       (2, '9ede4596-ad8a-4c11-bfc8-b8949636fe91', 1, 'CHECKBOX', '객관식 체크박스 질문2', '객관식 체크박스 질문 설명2',
        json_array(
                json_object('id', 1, 'detail', '2 - 1'),
                json_object('id', 2, 'detail', '2 - 2'),
                json_object('id', 3, 'detail', '2 - 3'))
           , false, false, 4),
       (3, '4d89a276-8dd2-4ee9-b8f0-315c22ed4ff9', 1, 'DROPDOWN', '객관식 드롭다운 질문2', '객관식 드롭다운 질문 설명2',
        json_array(
                json_object('id', 1, 'detail', '3 - 1'),
                json_object('id', 2, 'detail', '3 - 2'),
                json_object('id', 3, 'detail', '3 - 3')),
        false, false, 5);

INSERT INTO reward_category (id, reward_category_name)
VALUES (1, '커피'),
       (2, '상품권'),
       (3, '베이커리'),
       (4, '식품');

INSERT INTO reward (id, form_id, reward_category_id, reward_name, count)
VALUES (1, 1, 1, '스타벅스 아이스 아메리카노 T', 1);

INSERT INTO reward_recipient (id, form_id, user_id)
VALUES (1, 1, 2);