INSERT INTO room (name)
VALUES
    ('Главный зал'),
    ('Офис'),
    ('Переговорная');

INSERT INTO device (
    device_number,
    room_id,
    operator_name
)
VALUES
    ('100', 1, 'Вася'),
    ('101', 1, 'Коля'),
    ('202', 2, 'Юля'),
    ('203', 2, 'Анна'),
    ('301', 3, 'Иван');