-- ============================================
-- Feedback Management System
-- Production-Ready MySQL Database Schema
-- ============================================

-- Create database
CREATE DATABASE IF NOT EXISTS feedback_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE feedback_db;

-- ============================================
-- Drop existing tables (for clean setup)
-- ============================================
DROP TABLE IF EXISTS feedbacks;
DROP TABLE IF EXISTS users;

-- ============================================
-- Table: users
-- Stores user accounts with role-based access
-- ============================================
CREATE TABLE users (
    id          BIGINT          NOT NULL AUTO_INCREMENT  COMMENT 'Primary key',
    name        VARCHAR(100)    NOT NULL                 COMMENT 'Full name of the user',
    email       VARCHAR(150)    NOT NULL                 COMMENT 'Unique email address used for login',
    password    VARCHAR(255)    NOT NULL                 COMMENT 'BCrypt encrypted password',
    role        ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER' COMMENT 'User role for authorization',
    is_deleted  BOOLEAN         NOT NULL DEFAULT FALSE    COMMENT 'Soft delete flag',
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record update timestamp',

    -- Primary Key
    PRIMARY KEY (id),

    -- Unique constraint on email
    CONSTRAINT uk_users_email UNIQUE (email)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User accounts table';

-- ============================================
-- Indexes: users
-- ============================================
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role ON users (role);
CREATE INDEX idx_users_is_deleted ON users (is_deleted);
CREATE INDEX idx_users_created_at ON users (created_at);

-- ============================================
-- Table: feedbacks
-- Stores user feedback submissions
-- ============================================
CREATE TABLE feedbacks (
    id          BIGINT          NOT NULL AUTO_INCREMENT  COMMENT 'Primary key',
    title       VARCHAR(200)    NOT NULL                 COMMENT 'Feedback title/subject',
    message     TEXT            NOT NULL                 COMMENT 'Detailed feedback message',
    rating      TINYINT         NOT NULL                 COMMENT 'Rating from 1 to 5',
    status      ENUM('NEW','REVIEWED','RESOLVED') NOT NULL DEFAULT 'NEW' COMMENT 'Feedback processing status',
    user_id     BIGINT          NOT NULL                 COMMENT 'Foreign key to users table',
    is_deleted  BOOLEAN         NOT NULL DEFAULT FALSE    COMMENT 'Soft delete flag',
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Record creation timestamp',
    updated_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Record update timestamp',

    -- Primary Key
    PRIMARY KEY (id),

    -- Foreign Key: feedbacks.user_id -> users.id
    CONSTRAINT fk_feedbacks_user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    -- Check constraint: rating must be between 1 and 5
    CONSTRAINT chk_feedbacks_rating CHECK (rating BETWEEN 1 AND 5)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User feedback submissions table';

-- ============================================
-- Indexes: feedbacks
-- ============================================
CREATE INDEX idx_feedbacks_user_id ON feedbacks (user_id);
CREATE INDEX idx_feedbacks_title ON feedbacks (title);
CREATE INDEX idx_feedbacks_status ON feedbacks (status);
CREATE INDEX idx_feedbacks_rating ON feedbacks (rating);
CREATE INDEX idx_feedbacks_is_deleted ON feedbacks (is_deleted);
CREATE INDEX idx_feedbacks_created_at ON feedbacks (created_at);
CREATE INDEX idx_feedbacks_user_status ON feedbacks (user_id, status);
CREATE INDEX idx_feedbacks_user_deleted ON feedbacks (user_id, is_deleted);

-- Composite index for search + pagination
CREATE INDEX idx_feedbacks_search ON feedbacks (title, status, is_deleted, created_at DESC);

-- ============================================
-- Sample Data: users
-- Password: "password123" (BCrypt encrypted)
-- ============================================
INSERT INTO users (name, email, password, role) VALUES
    ('Admin User',    'admin@example.com',   '$2a$10$EqKcp1WFKVQISheBxmXNOe9r6YkiVQupMBnMRPx0n7c5n2nFzSuKu', 'ADMIN'),
    ('John Doe',      'john@example.com',    '$2a$10$EqKcp1WFKVQISheBxmXNOe9r6YkiVQupMBnMRPx0n7c5n2nFzSuKu', 'USER'),
    ('Jane Smith',    'jane@example.com',    '$2a$10$EqKcp1WFKVQISheBxmXNOe9r6YkiVQupMBnMRPx0n7c5n2nFzSuKu', 'USER'),
    ('Bob Wilson',    'bob@example.com',     '$2a$10$EqKcp1WFKVQISheBxmXNOe9r6YkiVQupMBnMRPx0n7c5n2nFzSuKu', 'USER');

-- ============================================
-- Sample Data: feedbacks
-- ============================================
INSERT INTO feedbacks (title, message, rating, status, user_id) VALUES
    ('Great Platform',       'The platform is very user-friendly and intuitive. Love the design!',                           5, 'NEW',      2),
    ('Slow Loading',         'Some pages take too long to load, especially the dashboard. Please optimize.',                   2, 'REVIEWED', 3),
    ('Feature Request',      'Would be great to have a dark mode option for the application.',                                4, 'NEW',      2),
    ('Excellent Support',    'Customer support resolved my issue within minutes. Very impressed!',                             5, 'RESOLVED', 4),
    ('Mobile Experience',    'The mobile layout needs improvement. Buttons are too small on phone screens.',                   3, 'REVIEWED', 3),
    ('Documentation',        'API documentation could be more detailed with more code examples.',                              3, 'NEW',      4),
    ('Login Issues',         'Sometimes the login page does not redirect properly after authentication.',                      1, 'REVIEWED', 2),
    ('Great Updates',        'Recent updates have significantly improved the user experience. Thank you!',                     5, 'RESOLVED', 3);
