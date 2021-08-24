delete from room where true;
ALTER SEQUENCE id_seq RESTART WITH 1;

insert into room (number, category, guests, description, img_name, price) VALUES
    (101, 'STANDARD', 1, 'Description 1', '101.jpg', 600),
    (102, 'SUITE', 2, 'Description 2', '102.jpg', 820),
    (103, 'STANDARD', 3, 'Description 3', '103.jpg', 640);
