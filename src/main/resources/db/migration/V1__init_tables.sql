-- V1__init_tables.sql
-- Roles table
CREATE TABLE role (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT UNIQUE NOT NULL
);

-- Mobility options
CREATE TABLE mobility (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT UNIQUE NOT NULL
);

-- Seasons
CREATE TABLE season (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT UNIQUE NOT NULL
);

-- Audiences
CREATE TABLE audience (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT UNIQUE NOT NULL
);

-- Users
CREATE TABLE app_user (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    firstname TEXT NOT NULL,
    lastname TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role_id UUID REFERENCES role(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Guides
CREATE TABLE guide (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title TEXT NOT NULL,
    description TEXT,
    days_count INT NOT NULL CHECK (days_count >= 1),
    created_by UUID REFERENCES app_user(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Guide <-> mobility many-to-many
CREATE TABLE guide_mobility (
    guide_id UUID REFERENCES guide(id) ON DELETE CASCADE,
    mobility_id UUID REFERENCES mobility(id) ON DELETE CASCADE,
    PRIMARY KEY (guide_id, mobility_id)
);

-- Guide <-> season many-to-many
CREATE TABLE guide_season (
    guide_id UUID REFERENCES guide(id) ON DELETE CASCADE,
    season_id UUID REFERENCES season(id) ON DELETE CASCADE,
    PRIMARY KEY (guide_id, season_id)
);

-- Guide <-> audience many-to-many
CREATE TABLE guide_audience (
    guide_id UUID REFERENCES guide(id) ON DELETE CASCADE,
    audience_id UUID REFERENCES audience(id) ON DELETE CASCADE,
    PRIMARY KEY (guide_id, audience_id)
);

-- Guide <-> User many-to-many
CREATE TABLE guide_user_access (
    guide_id UUID REFERENCES guide(id) ON DELETE CASCADE,
    user_id UUID REFERENCES app_user(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (guide_id, user_id)
);

-- Activities
CREATE TABLE activity (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    guide_id UUID REFERENCES guide(id) ON DELETE CASCADE,
    day_number INT NOT NULL,
    order_in_day INT NOT NULL,
    title TEXT NOT NULL,
    description TEXT,
    category TEXT NOT NULL,
    address TEXT NOT NULL,
    phone TEXT,
    opening_hours TEXT,
    website TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_activity_guide_day_order ON activity(guide_id, day_number, order_in_day);
