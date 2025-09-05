-- V2__seed_data.sql
-- Seed initial users, guides, activities, and access rights

-- Users
INSERT INTO app_user (id, firstname, lastname, email, password, role, created_at, modified_at)
VALUES
  (1, 'Admin', 'User', 'admin@test.com', '$2a$10$adminhashedpassword', 'ADMIN', NOW(), NOW()),
  (2, 'Regular', 'User', 'user@test.com', '$2a$10$userhashedpassword', 'USER', NOW(), NOW());

-- Guides
INSERT INTO guide (id, title, description, created_at, modified_at)
VALUES
  (1, 'Guide 1', 'Description for Guide 1', NOW(), NOW());

-- Activities
INSERT INTO activity (id, name, description, guide_id, created_at, modified_at)
VALUES
  (1, 'Activity 1', 'Activity 1 description', 1, NOW(), NOW()),
  (2, 'Activity 2', 'Activity 2 description', 1, NOW(), NOW()),
  (3, 'Activity 3', 'Activity 3 description', 1, NOW(), NOW());

-- User access to guides
INSERT INTO guide_user_access (user_id, guide_id, created_at, modified_at)
VALUES
  (2, 1, NOW(), NOW());