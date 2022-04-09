CREATE TABLE users (
  username VARCHAR(80) PRIMARY KEY,
  password VARCHAR(80) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
  username VARCHAR(80) NOT NULL,
  authority VARCHAR(80) NOT NULL,
  UNIQUE (username, authority),
  FOREIGN KEY (username) REFERENCES users(username)
);
