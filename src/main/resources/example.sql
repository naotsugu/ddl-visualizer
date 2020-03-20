-- **********************************************
--  Ctrl + p : show preview
--  Ctrl + r : redraw diagram
-- **********************************************

CREATE TABLE owners (
  id         INTEGER PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30),
  address    VARCHAR(255),
  city       VARCHAR(80),
  telephone  VARCHAR(20)
);

CREATE TABLE pets (
  id         INTEGER PRIMARY KEY,
  name       VARCHAR(30),
  birth_date DATE,
  type_id    INTEGER NOT NULL,
  owner_id   INTEGER NOT NULL
);

CREATE TABLE types (
  id   INTEGER PRIMARY KEY,
  name VARCHAR(80)
);

CREATE TABLE visits (
  id          INTEGER PRIMARY KEY,
  pet_id      INTEGER NOT NULL,
  visit_date  DATE,
  description VARCHAR(255)
);

ALTER TABLE pets ADD CONSTRAINT fk_pets_owners
  FOREIGN KEY (owner_id) REFERENCES owners (id);
ALTER TABLE pets ADD CONSTRAINT fk_pets_types
  FOREIGN KEY (type_id) REFERENCES types (id);
ALTER TABLE visits ADD CONSTRAINT fk_visits_pets
  FOREIGN KEY (pet_id) REFERENCES pets (id);
