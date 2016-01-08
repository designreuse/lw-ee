INSERT INTO logiwebdb.cities (city_id, city_name) VALUES (500, 'testCity');

INSERT INTO logiwebdb.users (user_id, email, password, role) VALUES (5002, 'driver11111@logiweb.com', '12345', 'ROLE_DRIVER');

INSERT INTO logiwebdb.drivers (driver_id, first_name, last_name, personal_number, driver_status,
          current_driver_location_FK, driver_logiweb_account_id) VALUES (501, 'testFirstName', 'testLastName', 11111, 'FREE', 500, 5002);