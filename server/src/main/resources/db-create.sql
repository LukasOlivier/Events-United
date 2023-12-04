drop table if exists tickets;
drop table if exists interestedUsers;

drop table if exists interests;
drop table if exists userFriends;

drop table if exists events;
drop table if exists userInterests cascade;
drop table if exists users;

create table events
(
    id          IDENTITY NOT NULL PRIMARY KEY,
    name        varchar(50) not null,
    description varchar     not null,
    startDate   date        not null,
    endDate     date        not null,
    interest    varchar(30) not null,
    location    varchar(30) not null,
    ticketPrice int         not null,
    attendees   int         not null,
    interested  int         not null
);

create table users
(
    userId       IDENTITY NOT NULL PRIMARY KEY,
    firstname   varchar(100) not null,
    lastname    varchar(100) not null,
    birthDate   date         not null,
    height      int          not null,
    gender      varchar(25)  not null,
    description varchar(300) not null
);

create table tickets
(
    eventId int not null,
    userId  int not null,
    primary key (eventId, userId),
    foreign key (eventId) references events (id),
    foreign key (userId) references users (userId)
);

create table interestedUsers
(
    eventId int not null,
    userId  int not null,
    primary key (eventId, userId),
    foreign key (eventId) references events (id),
    foreign key (userId) references users (userId)
);

create table userInterests
(
    userId   int          not null,
    interest varchar(100) not null,
    primary key (userId, interest),
    foreign key (userId) references users (userId)
);

create table interests
(
    id       IDENTITY NOT NULL PRIMARY KEY,
    interest varchar(50) not null,
    primary key (id)
);
create table userFriends
(
    userId   int not null,
    friendId int not null,
    primary key (userId, friendId),
    foreign key (userId) references users (userId),
    foreign key (friendId) references users (userId)
);