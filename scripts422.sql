CREATE TABLE person (
person_id SERIAL PRIMARY KEY,
name TEXT NOT NULL,
age INTEGER NOT NULL,
driver_license BOOLEAN NOT NULL
);
CREATE TABLE car (
car_id SERIAL PRIMARY KEY,
brand TEXT NOT NULL,
model TEXT NOT NULL,
price DECIMAL(10, 2) NOT NULL
);
CREATE TABLE person_car (
person_id INTEGER REFERENCES person(person_id),
car_id INTEGER REFERENCES car(car_id),
PRIMARY KEY (person_id, car_id)
);
