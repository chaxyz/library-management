INSERT INTO `library`.`categories` (`name`)
VALUES
    ('Fiction'),
    ('Non-Fiction'),
    ('Science Fiction'),
    ('Fantasy'),
    ('History');

INSERT INTO `library`.`books` (`name`, `path`, `categories_id`, `status`)
VALUES
    ('The Lord of the Rings', NULL, 1, 'returned'),
    ('Pride and Prejudice', NULL, 1, 'returned'),
    ('Sapiens: A Brief History of Humankind', NULL, 2, 'returned'),
    ('The Hitchhiker's Guide to the Galaxy', NULL, 3, 'returned'),
    ('Harry Potter and the Sorcerer's Stone', NULL, 4, 'returned');