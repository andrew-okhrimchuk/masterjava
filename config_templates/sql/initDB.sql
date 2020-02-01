
DROP TABLE IF EXISTS groups_user;
DROP SEQUENCE IF EXISTS groups_user_idx;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;
DROP INDEX IF EXISTS email_idx;
DROP INDEX IF EXISTS city_idx;
DROP TABLE IF EXISTS city ;
DROP SEQUENCE IF EXISTS city_seq;
DROP INDEX IF EXISTS groups_idx;
DROP TABLE IF EXISTS groups ;
DROP TYPE IF EXISTS groups_flag;
DROP SEQUENCE IF EXISTS groups_seq;
DROP INDEX IF EXISTS project_idx;
DROP TABLE IF EXISTS project;
DROP SEQUENCE IF EXISTS project_seq;

/*      city      */
CREATE SEQUENCE city_seq START 500;
CREATE TABLE city (
                      id   INTEGER PRIMARY KEY DEFAULT nextval('city_seq'),
                      name TEXT NOT NULL
);
CREATE UNIQUE INDEX city_idx ON city (name);

/*       USERS        */
CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
CREATE SEQUENCE user_seq START 100000;
CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL
);
CREATE UNIQUE INDEX email_idx ON users (email);
ALTER TABLE users ADD COLUMN city_id INTEGER ;
ALTER TABLE users ADD FOREIGN KEY (city_id) REFERENCES city (id);

/*      Projects     */
CREATE SEQUENCE project_seq START 15;
CREATE TABLE project (
                         id   INTEGER PRIMARY KEY DEFAULT nextval('project_seq'),
                         name TEXT NOT NULL,
                         description TEXT NOT NULL
);
CREATE UNIQUE INDEX project_idx ON project (name);


/*     groups     */
CREATE TYPE groups_flag AS ENUM ('registering', 'current', 'finished');

CREATE SEQUENCE groups_seq START 15;
CREATE TABLE groups (
                      id   INTEGER PRIMARY KEY DEFAULT nextval('groups_seq'),
                      name TEXT NOT NULL,
                      flag groups_flag NOT NULL,
                      project_id INTEGER NOT NULL REFERENCES project (id) ON DELETE CASCADE
                    );

CREATE UNIQUE INDEX groups_idx ON groups (name);

/*     groups_user     */
CREATE TABLE groups_user (
                        id   INTEGER PRIMARY KEY DEFAULT nextval('groups_seq'),
                        user_id INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
                        groups_id INTEGER NOT NULL REFERENCES groups (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX groups_user_idx ON groups_user (user_id, groups_id);


