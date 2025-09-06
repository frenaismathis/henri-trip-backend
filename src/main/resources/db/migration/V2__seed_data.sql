-- V2__seed_data.sql

-- Roles (using UUID generation for consistency)
INSERT INTO role (id, name) VALUES 
    (gen_random_uuid(), 'ADMIN'),
    (gen_random_uuid(), 'USER');

-- Mobilities
INSERT INTO mobility (id, name) VALUES 
    (gen_random_uuid(), 'CAR'),
    (gen_random_uuid(), 'BIKE'),
    (gen_random_uuid(), 'WALK'),
    (gen_random_uuid(), 'PUBLIC_TRANSPORT'),
    (gen_random_uuid(), 'MOTO');

-- Seasons
INSERT INTO season (id, name) VALUES 
    (gen_random_uuid(), 'SUMMER'),
    (gen_random_uuid(), 'SPRING'),
    (gen_random_uuid(), 'AUTUMN'),
    (gen_random_uuid(), 'WINTER'),
    (gen_random_uuid(), 'ALL_SEASONS');

-- Audiences
INSERT INTO audience (id, name) VALUES 
    (gen_random_uuid(), 'FAMILY'),
    (gen_random_uuid(), 'SOLO'),
    (gen_random_uuid(), 'GROUP'),
    (gen_random_uuid(), 'FRIENDS'),
    (gen_random_uuid(), 'COUPLE'),
    (gen_random_uuid(), 'BUSINESS');

-- Create admin user
INSERT INTO app_user (id, firstname, lastname, email, password, role_id)
SELECT gen_random_uuid(), 'Admin', 'User', 'admin@henritrip.com', 'CHANGE_THIS_PASSWORD', id
FROM role WHERE name = 'ADMIN';

-- Create regular user
INSERT INTO app_user (id, firstname, lastname, email, password, role_id)
SELECT gen_random_uuid(), 'John', 'Doe', 'john.doe@example.com', 'CHANGE_THIS_PASSWORD', id
FROM role WHERE name = 'USER';

-- Create sample guide
INSERT INTO guide (id, title, description, days_count, created_by)
SELECT 
    gen_random_uuid(),
    'Paris City Discovery',
    'A comprehensive 3-day tour of Paris, combining cultural visits, local cuisine, and hidden gems.',
    3,
    id
FROM app_user WHERE email = 'admin@henritrip.com';

-- Add guide relationships
INSERT INTO guide_mobility (guide_id, mobility_id)
SELECT 
    (SELECT id FROM guide WHERE title = 'Paris City Discovery'),
    id
FROM mobility WHERE name IN ('WALK', 'PUBLIC_TRANSPORT');

INSERT INTO guide_season (guide_id, season_id)
SELECT 
    (SELECT id FROM guide WHERE title = 'Paris City Discovery'),
    id
FROM season WHERE name IN ('SPRING', 'SUMMER', 'AUTUMN');

INSERT INTO guide_audience (guide_id, audience_id)
SELECT 
    (SELECT id FROM guide WHERE title = 'Paris City Discovery'),
    id
FROM audience WHERE name IN ('SOLO', 'COUPLE', 'FRIENDS');

-- Add activities
INSERT INTO activity (id, guide_id, day_number, order_in_day, title, description, category, address, phone, opening_hours, website)
SELECT 
    gen_random_uuid(),
    (SELECT id FROM guide WHERE title = 'Paris City Discovery'),
    1,
    1,
    'Louvre Museum',
    'Discover world-famous artworks including the Mona Lisa',
    'CULTURE',
    'Rue de Rivoli, 75001 Paris',
    '+33 1 40 20 50 50',
    '9:00-18:00',
    'www.louvre.fr';

INSERT INTO activity (id, guide_id, day_number, order_in_day, title, description, category, address, opening_hours)
SELECT 
    gen_random_uuid(),
    (SELECT id FROM guide WHERE title = 'Paris City Discovery'),
    1,
    2,
    'Luxembourg Gardens',
    'Relax in one of Paris most beautiful gardens',
    'OUTDOOR',
    '6th Arrondissement, 75006 Paris',
    '7:30-20:30';

-- Give access to all users
INSERT INTO guide_user_access (guide_id, user_id)
SELECT 
    (SELECT id FROM guide WHERE title = 'Paris City Discovery'),
    id
FROM app_user;