INSERT INTO USERS (EMAIL, PASSW, ISADMIN, ISBANNED) VALUES ('thorsteinnth@gmail.com', 'mypassword', 1, 0)
INSERT INTO USERS (EMAIL, PASSW, ISADMIN, ISBANNED) VALUES ('gretarg09@gmail.com', 'mypassword', 1, 0)

INSERT INTO ITEM (SKU, DESCRIPTION, PRICE, STOCK) VALUES ('argur', 'Argur is mad', 999, 2)
INSERT INTO ITEM (SKU, DESCRIPTION, PRICE, STOCK) VALUES ('bjarmi', 'Bjarmi is bright', 999, 6)
INSERT INTO ITEM (SKU, DESCRIPTION, PRICE, STOCK) VALUES ('bjarmi-bluehat-redpants', 'Bjarmi is bright. This one has a blue hat and red pants!', 1249, 87)

INSERT INTO SCITEM (ID, ITEM_SKU, USER_ID, QTY) VALUES (1, 'argur', 'thorsteinnth@gmail.com', 100)
INSERT INTO SCITEM (ID, ITEM_SKU, USER_ID, QTY) VALUES (2, 'bjarmi', 'gretarg09@gmail.com', 199)