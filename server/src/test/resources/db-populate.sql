--Add events to the events table:
INSERT INTO events(name,description,startdate,enddate,interest,location,ticketprice,attendees,interested)
VALUES ('MockEvent', 'Iam a mock event, use me for tests', '2026-12-02', '2026-12-05', 'MOVIES', 'MarsDome1', 50, 0, 0);

INSERT INTO events(name,description,startdate,enddate,interest,location,ticketprice,attendees,interested)
VALUES ('MockEvent2', 'Iam a mock event, use me for tests', '2026-12-02', '2026-12-05', 'MOVIES', 'MarsDome2', 50, 0, 0);

--Add users to the users table:
INSERT INTO users(firstname,lastname,birthDate,height,gender,description)
VALUES ('Mock', 'User', '2003-05-16', 180, 'Male', 'Iam a mock user, use me for tests');

INSERT INTO users(firstname,lastname,birthDate,height,gender,description)
VALUES ('Mock', 'Friend', '2003-05-16', 180, 'Male', 'Iam a friend of Mock user, use me for tests');

--Add interests to the interest table:
INSERT INTO interests(interest)
VALUES ('cars');