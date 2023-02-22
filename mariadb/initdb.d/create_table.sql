
create table passengers (
        id int auto_increment,
        name varchar(100) not null,
        nationality varchar(100) not null,
        birthdate date,
        primary key (id)
);

create table airports (
        code varchar(10) not null,
        full_name varchar(100) not null,
        nationality varchar(100) not null,
        lat float,
        lon float,
        primary key (code)
);

create table journeys (
        id int auto_increment,
        passenger_id int,
        airport_from varchar(10) not null,
        airport_to varchar(10) not null,
        start_seconds long,
        status tinyint,
        primary key (id),
        foreign key (passenger_id) references passengers(id) 
        on update cascade,
        foreign key (airport_from) references airports(code) 
        on update cascade,
        foreign key (airport_to) references airports(code) 
        on update cascade
);
