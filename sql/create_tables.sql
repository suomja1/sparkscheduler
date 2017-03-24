CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE Unit (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1mc(),
    name TEXT NOT NULL
);

CREATE TABLE Employee (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1mc(),
    superior UUID REFERENCES Employee(id),
    fullName TEXT NOT NULL,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    contract NUMERIC DEFAULT 37.5
);

CREATE TABLE Shift (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v1mc(),
    unit UUID REFERENCES Unit(id),
    startTime TIMESTAMP NOT NULL,
    endTime TIMESTAMP NOT NULL
);

CREATE TABLE EmployeeShift (
    employee UUID REFERENCES Employee(id),
    shift UUID REFERENCES Shift(id),
    PRIMARY KEY (employee, shift)
);