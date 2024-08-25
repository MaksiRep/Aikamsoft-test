CREATE TABLE customer
(
    id         SERIAL PRIMARY KEY,
    firstname  VARCHAR NOT NULL,
    lastname VARCHAR NOT NULL
);

CREATE TABLE product
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR NOT NULL,
    price INTEGER NOT NULL
);

CREATE TABLE purchase
(
    id           SERIAL PRIMARY KEY,
    customer_id  INTEGER NOT NULL,
    product_id   INTEGER NOT NULL,
    purchase_date DATE    NOT NULL
);

ALTER TABLE purchase
    ADD CONSTRAINT FK_PURCHASE_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);
ALTER TABLE purchase
    ADD CONSTRAINT FK_PURCHASE_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id);