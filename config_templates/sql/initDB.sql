DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;
DROP INDEX IF EXISTS email_idx;
DROP INDEX IF EXISTS project_idx;
DROP TABLE IF EXISTS project;
DROP SEQUENCE IF EXISTS project_seq;
DROP INDEX IF EXISTS city_idx;
DROP TABLE IF EXISTS city CASCADE;
DROP SEQUENCE IF EXISTS city_seq;
DROP INDEX IF EXISTS groups_idx;
DROP TABLE IF EXISTS groups CASCADE;
DROP SEQUENCE IF EXISTS groups_seq;




/*users*/
CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
CREATE SEQUENCE user_seq START 100000;
CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT NOT NULL,
  email     TEXT NOT NULL,
  flag      user_flag NOT NULL
);
CREATE UNIQUE INDEX email_idx ON users (email);

/*city*/
CREATE SEQUENCE city_seq START 500;
CREATE TABLE city (
                       id   INTEGER PRIMARY KEY DEFAULT nextval('city_seq'),
                       name TEXT NOT NULL
);
CREATE UNIQUE INDEX city_idx ON city (name);


/*groups*/
CREATE SEQUENCE groups_seq START 15;
CREATE TABLE groups (
                      id   INTEGER PRIMARY KEY DEFAULT nextval('groups_seq'),
                      name TEXT NOT NULL);

CREATE UNIQUE INDEX groups_idx ON groups (name);
INSERT INTO groups
VALUES (10, 'REGISTERING'),
       (11, 'CURRENT'),
       (12, 'FINISHED');


/*Projects*/
CREATE SEQUENCE project_seq START 15;
CREATE TABLE project (
                        id   INTEGER PRIMARY KEY DEFAULT nextval('project_seq'),
                        name TEXT NOT NULL,
                        description TEXT NOT NULL,
                        groups_id INTEGER NOT NULL,
                        /*city_id INTEGER NOT NULL,*/
                        FOREIGN KEY (groups_id) REFERENCES groups (id)
                        /*FOREIGN KEY (city_id) REFERENCES city (id)*/
                      );

CREATE UNIQUE INDEX project_idx ON project (name);
