INSERT INTO Unit (name) VALUES 
    ('Malmi'),
    ('Olari'),
    ('Helsinginkatu'),
    ('Itäkeskus'),
    ('Joensuu'),
    ('Jyväskylä'),
    ('Kuopio'),
    ('Tampere'),
    ('Turku'),
    ('Petikko'),
    ('Tammisto');

INSERT INTO Employee (fullName, username, password) VALUES
    ('Esko Esimies', 'esimes1', 'password'),
    ('Asko Aamunkoi', 'aamuas1', 'salasana');

INSERT INTO Employee (superior, fullName, username, password) VALUES
    ((SELECT id FROM Employee WHERE username = 'esimes1'), 'Ilkka Immeinen', 'immeil1', 'passord'),
    ((SELECT id FROM Employee WHERE username = 'aamuas1'), 'Katariina Kurki', 'kurkka1', 'losenord');

INSERT INTO Shift VALUES
    ('75bcfd18-45e8-41e2-ad8a-64b28c640aff', (SELECT id FROM Unit WHERE name = 'Malmi'), '2017-04-17 10:00:00', '2017-04-17 18:00:00'),
    ('e4546c52-2afb-4b08-a295-315a9dfeb342', (SELECT id FROM Unit WHERE name = 'Malmi'), '2017-04-17 12:00:00', '2017-04-17 19:00:00'),
    ('a3e3c511-3e60-4e2e-adca-6a51293ba4f7', (SELECT id FROM Unit WHERE name = 'Tammisto'), '2017-04-17 10:00:00', '2017-04-17 19:00:00'),
    ('373a1233-8197-4b70-a3f6-00bb44b9f614', (SELECT id FROM Unit WHERE name = 'Petikko'), '2017-04-18 10:00:00', '2017-04-18 18:00:00');

INSERT INTO EmployeeShift VALUES
    ((SELECT id FROM Employee WHERE username = 'esimes1'), '75bcfd18-45e8-41e2-ad8a-64b28c640aff'),
    ((SELECT id FROM Employee WHERE username = 'immeil1'), 'e4546c52-2afb-4b08-a295-315a9dfeb342'),
    ((SELECT id FROM Employee WHERE username = 'aamuas1'), 'a3e3c511-3e60-4e2e-adca-6a51293ba4f7'),
    ((SELECT id FROM Employee WHERE username = 'kurkka1'), 'a3e3c511-3e60-4e2e-adca-6a51293ba4f7'),
    ((SELECT id FROM Employee WHERE username = 'kurkka1'), '373a1233-8197-4b70-a3f6-00bb44b9f614');