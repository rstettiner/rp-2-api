create table sys_message_lkup
(
    message_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    system_cd varchar(10) ,
    subsystem_cd varchar(10) ,
    message_cd varchar(10) ,
    message_type_cd varchar(10) ,
    message_level_cd varchar(10) ,
    http_status_cd INT ,
    message_text varchar(1000) ,
    status_cd varchar(10) ,
    create_dt timestamp ,
    create_user varchar(50) ,
    update_dt timestamp ,
    update_user varchar(50)
);

create table user
(
    user_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    username varchar(128) ,
    password varchar(128) ,
    name varchar(128)
);
