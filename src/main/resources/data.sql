
INSERT INTO p_store_category (category_id, category_name, is_active, created_at, modified_at)
SELECT '11111111-1111-1111-1111-111111111111', '한식', TRUE, now(), now()
    WHERE NOT EXISTS (
  SELECT 1 FROM p_store_category WHERE category_name = '한식'
);


INSERT INTO p_store_category (category_id, category_name, is_active, created_at, modified_at)
SELECT '22222222-2222-2222-2222-222222222222', '중식', TRUE, now(), now()
    WHERE NOT EXISTS (
  SELECT 1 FROM p_store_category WHERE category_name = '중식'
);


INSERT INTO p_store_category (category_id, category_name, is_active, created_at, modified_at)
SELECT '33333333-3333-3333-3333-333333333333', '분식', TRUE, now(), now()
    WHERE NOT EXISTS (
  SELECT 1 FROM p_store_category WHERE category_name = '분식'
);


INSERT INTO p_store_category (category_id, category_name, is_active, created_at, modified_at)
SELECT '44444444-4444-4444-4444-444444444444', '치킨', TRUE, now(), now()
    WHERE NOT EXISTS (
  SELECT 1 FROM p_store_category WHERE category_name = '치킨'
);


INSERT INTO p_store_category (category_id, category_name, is_active, created_at, modified_at)
SELECT '55555555-5555-5555-5555-555555555555', '피자', TRUE, now(), now()
    WHERE NOT EXISTS (
  SELECT 1 FROM p_store_category WHERE category_name = '피자'
);