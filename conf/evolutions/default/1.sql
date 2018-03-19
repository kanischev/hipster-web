# --- !Ups
CREATE TABLE "user_account" (
  "id" BIGSERIAL,
  "login" VARCHAR(1000),
  "password_hash" VARCHAR(1000),
  "password_salt" VARCHAR(1000),
  "email" VARCHAR(1000),
  "first_name" VARCHAR(1000),
  "middle_name" VARCHAR(1000),
  "last_name" VARCHAR(1000),
  "register_date" TIMESTAMP,
  "temporal_password" BOOLEAN,
  "disabled" BOOLEAN,
  "mail_verified" BOOLEAN,
  "verification_code" VARCHAR(1000),
  "last_login" TIMESTAMP,
  PRIMARY KEY ("id"),
  UNIQUE ("email")
);

CREATE TABLE "role" (
  "id" VARCHAR(255) UNIQUE NOT NULL,
  "description" TEXT,
  PRIMARY KEY("id")
);

CREATE TABLE "user_role" (
  "user_account_id" BIGINT NOT NULL REFERENCES "user_account"(id),
  "role_id" VARCHAR(255) NOT NULL REFERENCES "role"(id),
  PRIMARY KEY("user_account_id", "role_id")
);

CREATE TABLE "user_auth_token" (
  "id" VARCHAR(255),
  "user_id" BIGINT REFERENCES "user_account"(id),
  "host" VARCHAR(255),
  "last_request_path" TEXT,
  "creation_time" TIMESTAMP NOT NULL,
  "last_request_time" TIMESTAMP,
  "expiry_time" TIMESTAMP NOT NULL,
  PRIMARY KEY ("id")
);

INSERT INTO role VALUES ('ADMIN', 'Администратор'), ('USER', 'Пользователь');

# --- !Downs
DELETE FROM role;
DROP TABLE "user_auth_token";
DROP TABLE "user_role";
DROP TABLE "user_account";
DROP TABLE "role";
