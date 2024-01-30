INSERT INTO restaurant.restaurants(id, name, active)
values ('aae35075-5bbe-4441-ae1a-668de4f6dc81', 'restaurant_1', TRUE);
INSERT INTO restaurant.restaurants(id, name, active)
values ('aae35075-5bbe-4441-ae1a-668de4f6dc82', 'restaurant_2', FALSE);

INSERT INTO restaurant.products(id, name, price, available)
VALUES('aae35075-5bbe-4441-ae1a-668de4f6dc83', 'product_1', 25.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
VALUES('aae35075-5bbe-4441-ae1a-668de4f6dc84', 'product_2', 50.00, TRUE);
INSERT INTO restaurant.products(id, name, price, available)
VALUES('aae35075-5bbe-4441-ae1a-668de4f6dc85', 'product_3', 20.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
VALUES('aae35075-5bbe-4441-ae1a-668de4f6dc86', 'product_4', 40.00, TRUE);

INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ('aae35075-5bbe-4441-ae1a-668de4f6dc87', 'aae35075-5bbe-4441-ae1a-668de4f6dc81', 'aae35075-5bbe-4441-ae1a-668de4f6dc83');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ('aae35075-5bbe-4441-ae1a-668de4f6dc88', 'aae35075-5bbe-4441-ae1a-668de4f6dc81', 'aae35075-5bbe-4441-ae1a-668de4f6dc84');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ('aae35075-5bbe-4441-ae1a-668de4f6dc89', 'aae35075-5bbe-4441-ae1a-668de4f6dc82', 'aae35075-5bbe-4441-ae1a-668de4f6dc85');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
VALUES ('aae35075-5bbe-4441-ae1a-668de4f6dc80', 'aae35075-5bbe-4441-ae1a-668de4f6dc82', 'aae35075-5bbe-4441-ae1a-668de4f6dc86');