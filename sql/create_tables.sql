CREATE TABLE Unit (
    id UUID PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE Employee (
    id UUID PRIMARY KEY,
    superior UUID REFERENCES Employee(id),
    fullName TEXT NOT NULL,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    contract NUMERIC DEFAULT 37.5
);

CREATE TABLE Shift (
    id UUID PRIMARY KEY,
    unit UUID REFERENCES Unit(id),
    startTime TIMESTAMP NOT NULL,
    endTime TIMESTAMP NOT NULL
);

CREATE TABLE EmployeeShift (
    employee UUID REFERENCES Employee(id),
    shift UUID REFERENCES Shift(id)
);