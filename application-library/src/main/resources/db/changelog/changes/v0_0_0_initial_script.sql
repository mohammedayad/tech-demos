--liquibase formatted sql
--changeset ayad:1


/*
     organization Table Creation
 */

CREATE TABLE IF NOT EXISTS organization
(
    id            UUID PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP,
    last_update   TIMESTAMP
);


/*
     school Table Creation
 */

CREATE TABLE IF NOT EXISTS school
(
    id              UUID PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    creation_date   TIMESTAMP,
    last_update     TIMESTAMP,
    organization_id UUID         NOT NULL,

    CONSTRAINT fk_school_organization
        FOREIGN KEY (organization_id)
            REFERENCES organization (id)
);


/*
     users Table Creation
 */
CREATE TABLE IF NOT EXISTS users
(
    id            UUID PRIMARY KEY,
    user_id       VARCHAR(255) NOT NULL UNIQUE,
    name          VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP,
    last_update   TIMESTAMP,
    school_id     UUID         NOT NULL,

    CONSTRAINT fk_user_school
        FOREIGN KEY (school_id)
            REFERENCES school (id)
);


/*
     application Table Creation
 */

CREATE TABLE IF NOT EXISTS application
(
    id              UUID PRIMARY KEY,
    application_id  VARCHAR(255) NOT NULL, -- business id
    name            VARCHAR(255) NOT NULL,
    url             VARCHAR(1000),
    level           VARCHAR(50)  NOT NULL,
    creation_date   TIMESTAMP,
    last_update     TIMESTAMP,
    organization_id UUID,
    school_id       UUID,

    CONSTRAINT fk_application_organization
        FOREIGN KEY (organization_id)
            REFERENCES organization (id),

    CONSTRAINT fk_application_school
        FOREIGN KEY (school_id)
            REFERENCES school (id)
);

-- organization test data
INSERT INTO organization (id,
                          name,
                          creation_date,
                          last_update)
VALUES ('11111111-1111-1111-1111-111111111111',
        'EduCloudwise',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('99999999-1111-1111-1111-111111111111',
        'TechOrg',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (
           'aaaaaaaa-1111-1111-1111-111111111111',
           'PlainOrg',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );


-- school test data
INSERT INTO school (id,
                    name,
                    organization_id,
                    creation_date,
                    last_update)
VALUES ('22222222-2222-2222-2222-222222222221',
        'Cloud College',
        '11111111-1111-1111-1111-111111111111',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('22222222-2222-2222-2222-222222222222',
        'Sun School',
        '11111111-1111-1111-1111-111111111111',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('22222222-2222-2222-2222-222222222223',
        'The Rainbow',
        '11111111-1111-1111-1111-111111111111',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('99999999-2222-2222-2222-222222222222',
        'Empty School',
        '99999999-1111-1111-1111-111111111111',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (
           'aaaaaaaa-2222-2222-2222-222222222222',
           'Plain School',
           'aaaaaaaa-1111-1111-1111-111111111111',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );


-- users test data
INSERT INTO users (id,
                   user_id,
                   name,
                   school_id,
                   creation_date,
                   last_update)
VALUES ('33333333-3333-3333-3333-333333333331',
        'john',
        'John',
        '22222222-2222-2222-2222-222222222221',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('33333333-3333-3333-3333-333333333332',
        'mary',
        'Mary',
        '22222222-2222-2222-2222-222222222222',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('33333333-3333-3333-3333-333333333333',
        'peter',
        'Peter',
        '22222222-2222-2222-2222-222222222223',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('99999999-3333-3333-3333-333333333333',
        'bob',
        'Bob',
        '99999999-2222-2222-2222-222222222222',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       (
           'aaaaaaaa-3333-3333-3333-333333333333',
           'alice',
           'Alice',
           'aaaaaaaa-2222-2222-2222-222222222222',
           CURRENT_TIMESTAMP,
           CURRENT_TIMESTAMP
       );


-- Root Applications test data
INSERT INTO application (id,
                         application_id,
                         name,
                         url,
                         level,
                         creation_date,
                         last_update)
VALUES ('44444444-4444-4444-4444-444444444441',
        'a1',
        'Gmail',
        'www.gmail.com',
        'ROOT',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('44444444-4444-4444-4444-444444444442',
        'a2',
        'Agenda',
        'www.google.com/agenda',
        'ROOT',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('44444444-4444-4444-4444-444444444443',
        'a3',
        'Math4You',
        'www.math4you.com',
        'ROOT',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('44444444-4444-4444-4444-444444444444',
        'a4',
        'Biology Naturally',
        'www.studyapps.com/biology-naturally',
        'ROOT',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);


-- Organization Applications test data ( two organization EduCloudwise and TechOrg)
INSERT INTO application (id,
                         application_id,
                         name,
                         url,
                         level,
                         organization_id,
                         creation_date,
                         last_update)
VALUES ('55555555-5555-5555-5555-555555555551',
        'a2',
        'Calendar',
        'www.google.com/agenda',
        'ORGANIZATION',
        '11111111-1111-1111-1111-111111111111',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('55555555-5555-5555-5555-555555555552',
        'a5',
        'EduCloudwise Intranet',
        'www.educloudwise.com/intranet',
        'ORGANIZATION',
        '11111111-1111-1111-1111-111111111111',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('99999999-4444-4444-4444-444444444441',
        'a2',
        'Organization Calendar',
        'www.techorg.com/calendar',
        'ORGANIZATION',
        '99999999-1111-1111-1111-111111111111',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('99999999-4444-4444-4444-444444444442',
        'a8',
        'Organization Portal',
        'www.techorg.com',
        'ORGANIZATION',
        '99999999-1111-1111-1111-111111111111',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);


-- Cloud College school Applications test data

INSERT INTO application (id,
                         application_id,
                         name,
                         url,
                         level,
                         school_id,
                         creation_date,
                         last_update)
VALUES ('66666666-6666-6666-6666-666666666661',
        'a1',
        'Email',
        'www.outlook.com/mail',
        'SCHOOL',
        '22222222-2222-2222-2222-222222222221',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('66666666-6666-6666-6666-666666666662',
        'a2',
        'Agenda',
        'www.outlook.com/agenda',
        'SCHOOL',
        '22222222-2222-2222-2222-222222222221',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP),
       ('66666666-6666-6666-6666-666666666663',
        'a6',
        'School Site',
        'www.cloudcollege.com',
        'SCHOOL',
        '22222222-2222-2222-2222-222222222221',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);


-- Sun school Applications test data
INSERT INTO application (id,
                         application_id,
                         name,
                         url,
                         level,
                         school_id,
                         creation_date,
                         last_update)
VALUES ('77777777-7777-7777-7777-777777777771',
        'a7',
        'School Site',
        'www.sunschool.com',
        'SCHOOL',
        '22222222-2222-2222-2222-222222222222',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);


-- Rainbow school Applications test data
INSERT INTO application (id,
                         application_id,
                         name,
                         url,
                         level,
                         school_id,
                         creation_date,
                         last_update)
VALUES ('88888888-8888-8888-8888-888888888881',
        'a5',
        'Intranet',
        'www.educloudwise.com/intranet-rainbow',
        'SCHOOL',
        '22222222-2222-2222-2222-222222222223',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

-- End changeset