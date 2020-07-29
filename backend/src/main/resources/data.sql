SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE product;
TRUNCATE location;
TRUNCATE stock;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO product (name, short_name, code, weight, created_at, updated_at) VALUES ('face mask', 'FM','FM-HKTV01', 100, '2020-07-27 13:00','2020-07-27 13:00'),
                                                                        ('potato chips','PC', 'PC-HKTV01', 50,'2020-07-27 13:00','2020-07-27 13:00'),
                                                                        ('coca cola', 'CC','CC-HKTV01', 500, '2020-07-27 13:00', '2020-07-27 13:00'),
                                                                        ('apple juice', 'AJ', 'AJ-HKTV01', 100, '2020-07-27 13:00', '2020-07-27 13:00'),
                                                                        ('orange juice', 'OJ', 'OJ-HKTV01', 100, '2020-07-27 13:00', '2020-07-27 13:00'),
                                                                        ('lemon juice', 'LJ', 'LJ-HKTV01', 100, '2020-07-27 13:00', '2020-07-27 13:00');
INSERT INTO location (name, created_at, updated_at) VALUES ('TKO', '2020-07-27 13:00', '2020-07-27 13:00'),
                                                           ('CWB', '2020-07-27 13:00', '2020-07-27 13:00'),
                                                           ('MK', '2020-07-27 13:00', '2020-07-27 13:00');

INSERT INTO stock (product_id, location_id, quantity, created_at, updated_at) VALUES (1, 1, 150, '2020-07-27 13:00', '2020-07-27 13:00'),
                                                                                     (1, 2, 10, '2020-07-27 13:00', '2020-07-27 13:00'),
                                                                                     (1, 3, 20, '2020-07-27 13:00', '2020-07-27 13:00'),
                                                                                     (2, 1, 150, '2020-07-27 13:00', '2020-07-27 13:00'),
                                                                                     (3, 2, 150, '2020-07-27 13:00', '2020-07-27 13:00'),
                                                                                     (4, 3, 150, '2020-07-27 13:00', '2020-07-27 13:00'),
                                                                                     (5, 1, 50, '2020-07-27 13:00', '2020-07-27 13:00');