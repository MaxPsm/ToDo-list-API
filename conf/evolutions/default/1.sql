-- todolist schema

-- !Ups

CREATE TABLE todo (
    id int not null auto_increment,
    name varchar(90) not null,
    is_complete boolean,
    PRIMARY KEY (id)
);


-- !Downs

DROP TABLE todo;