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

-- Create admin user first to use as created_by reference
WITH inserted_role AS (
    SELECT id FROM role WHERE name = 'ADMIN' LIMIT 1
),
inserted_user AS (
    INSERT INTO app_user (id, firstname, lastname, email, password, role_id)
    SELECT 
        gen_random_uuid(),
        'Admin',
        'User',
        'admin@henritrip.com',
        -- In production, use proper password hashing
        'CHANGE_THIS_PASSWORD',
        inserted_role.id
    FROM inserted_role
    RETURNING id
)

-- Create a sample guide using the admin user
INSERT INTO guide (id, title, description, days_count, created_by)
SELECT
    gen_random_uuid(),
    'Paris City Discovery',
    'A comprehensive 3-day tour of Paris, combining cultural visits, local cuisine, and hidden gems. Perfect for first-time visitors who want to explore the city beyond the typical tourist attractions.',
    3,
    inserted_user.id
FROM inserted_user
RETURNING id;

-- Regular user
WITH user_role AS (
    SELECT id FROM role WHERE name = 'USER' LIMIT 1
)
INSERT INTO app_user (id, firstname, lastname, email, password, role_id)
SELECT
    gen_random_uuid(),
    'John',
    'Doe',
    'john.doe@example.com',
    -- In production, use proper password hashing
    'CHANGE_THIS_PASSWORD',
    user_role.id;

-- Add relationships using subqueries
WITH sample_guide AS (
    SELECT id FROM guide WHERE title = 'Paris City Discovery' LIMIT 1
)
-- Add mobilities
INSERT INTO guide_mobility (guide_id, mobility_id)
SELECT sample_guide.id, mobility.id
FROM sample_guide, mobility
WHERE mobility.name IN ('WALK', 'PUBLIC_TRANSPORT');

-- Add seasons
INSERT INTO guide_season (guide_id, season_id)
SELECT sample_guide.id, season.id
FROM sample_guide, season
WHERE season.name IN ('SPRING', 'SUMMER', 'AUTUMN');

-- Add audiences
INSERT INTO guide_audience (guide_id, audience_id)
SELECT sample_guide.id, audience.id
FROM sample_guide, audience
WHERE audience.name IN ('SOLO', 'COUPLE', 'FRIENDS');

-- Add activities
WITH sample_guide AS (
    SELECT id FROM guide WHERE title = 'Paris City Discovery' LIMIT 1
)
INSERT INTO activity (
    id, guide_id, day_number, order_in_day, 
    title, description, category, address, 
    phone, opening_hours, website
)
SELECT
    gen_random_uuid(),
    sample_guide.id,
    day_data.day_number,
    day_data.order_in_day,
    day_data.title,
    day_data.description,
    day_data.category,
    day_data.address,
    day_data.phone,
    day_data.opening_hours,
    day_data.website
FROM sample_guide,
(VALUES
    (1, 1, 'Louvre Museum', 'Discover world-famous artworks including the Mona Lisa', 'CULTURE', 'Rue de Rivoli, 75001 Paris', '+33 1 40 20 50 50', '9:00-18:00', 'www.louvre.fr'),
    (1, 2, 'Luxembourg Gardens', 'Relax in one of Paris most beautiful gardens', 'OUTDOOR', '6th Arrondissement, 75006 Paris', NULL, '7:30-20:30', NULL),
    (2, 1, 'Eiffel Tower', 'Visit the iconic symbol of Paris', 'LANDMARK', 'Champ de Mars, 75007 Paris', '+33 1 44 11 23 23', '9:00-00:45', 'www.toureiffel.paris'),
    (2, 2, 'Le Marais Walking Tour', 'Explore the historic Marais district', 'WALKING', '4th Arrondissement', NULL, NULL, NULL),
    (3, 1, 'Montmartre & Sacré-Cœur', 'Discover the artists quarter', 'CULTURE', '35 Rue du Chevalier de la Barre, 75018 Paris', NULL, '6:00-22:30', NULL)
) as day_data(day_number, order_in_day, title, description, category, address, phone, opening_hours, website);

-- Give access to both users
WITH sample_guide AS (
    SELECT id FROM guide WHERE title = 'Paris City Discovery' LIMIT 1
)
INSERT INTO guide_user_access (guide_id, user_id)
SELECT sample_guide.id, app_user.id
FROM sample_guide, app_user;