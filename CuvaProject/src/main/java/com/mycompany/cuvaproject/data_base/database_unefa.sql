-- activa los eventos
SET GLOBAL event_scheduler = ON;

-- crea la base de datos si no existe
create database if not exists database_unefa;

-- usa la base de datos
use database_unefa;


-- tabla de los cargos
create table if not exists Post
(
IDP varchar(50) primary key,
name varchar(50)
);


-- tabla usuario
create table if not exists User
(
Name varchar(100)not null ,
LastName varchar(100)not null,
ID varchar(50) primary key, 
Email varchar(50) not null unique,
Password varchar(128)not null,
salt varchar(32),
FKIDPost varchar(50),
foreign key (FKIDPost) references Post(IDP),
rangee varchar(50) not null
);

-- tabla para la duracion de los ususarios temporales (Invitados)
create table if not exists UserDuration
(
FKIDUser varchar(50),
foreign key (FKIDUser) references User(ID),
dateCreate date default now()
);

-- tabla materia
create table if not exists Subject
(
Code varchar(100) primary key,
Name varchar(100),
Unit_Credit int,
Semester int
);

-- tabla prelacion de materia
create table if not exists priority
(
FKCodeSubject varchar(100),
priority varchar(100),
foreign key (FKcodesubject) references subject(code)
);


-- crea la tabla estudiante
create table if not exists Student
(
ID varchar(50) primary key,
Name varchar(100),
Lastname varchar(100),
career varchar(100),
Unit_Credit int,
Tuition varchar(100)
);

-- tabla reprobados
create table if not exists reprobated
(
FKIDStudent varchar(50),
FKCodeSubject varchar(100),
foreign key (FKIDStudent) references Student(ID),
foreign key (FKCodeSubject) references Subject(code),
grade varchar(50),
period varchar(50),
primary key (FKIDStudent,FKCodeSubject,grade,period)
);

-- tabla bitacora
create table if not exists Bitacora
(
timee timestamp default now() , -- año,mes,dia,hora,minuto,seg
FKIDUser varchar(100),
action varchar(300),
foreign key (FKIDUser) references User(ID)
);

-- crea un evento para que borre automaticamente los registro despues de 30 dias
delimiter //
set sql_safe_updates = 0;
create event if not exists Delete30daysLefts
on schedule every 1 day
starts current_date
do 
begin
delete u,UD from UserDuration UD
inner join User u on u.ID = UD.FKIDUser
where UD.dateCreate = current_date() - interval 30 day;
delete from bitacora where timee < current_date() - interval 30 day;
set sql_safe_updates = 1;
end //
delimiter ;

-- a partir de aqui son los insert manuales de la base de datos

insert into post(IDP,name) values ("Coordinador","Coordinador"),("Director","Director");

set @salt = MD5(RAND());
insert into user (Name,LastName,ID,Email,Password,salt,FKIDPost,rangee) VALUES ("Admin","Admin","Admin","Admin",SHA2(CONCAT('Admin',@salt), 512),@salt,"Coordinador","Admin");


insert into Subject (code, name, Unit_Credit, Semester) VALUES 
("ADG-25132", "EDUCACIÓN AMBIENTAL", 2, "1"),
("ADG-25123", "HOMBRE, SOCIEDAD, CIENCIA Y TEC", 2, "1"),
("IDM-24113", "INGLES I", 2, "1"),
("MAT-21212", "DIBUJO", 1, "1"),
("MAT-21215", "MATEMATICA I", 4, "1"),
("MAT-21524", "GEOMETRÍA ANALITICA", 3, "1"),
("DIN-21113", "DEFENSA INTEGRAL DE LA NACION I", 2, "1"),
("ADG-25131", "SEMINARIO I", 1, "2"),
("IDM-24123", "INGLES II", 2, "2"),
("QUF-22014", "QUÍMICA GENERAL", 4, "2"),
("QUF-23015", "FÍSICA I", 5, "2"),
("MAT-21225", "MATEMATICA II", 5, "2"),
("MAT-21114", "ALGEBRA LINEAL", 4, "2"),
("DIN-21123", "DEFENSA INTEGRAL DE LA NACION II", 2, "2"),
("ADG-25141", "SEMINARIO II", 1, "3"),
("QUF-23025", "FISICA II", 5, "3"),
("MAT-21235", "MATEMATICA III", 5, "3"),
("MAT-21414", "PROBABILIDADES Y ESTADISTICA", 4, "3"),
("SYC-22113", "PROGRAMACIÓN", 3, "3"),
("DIN-21133", "DEFENSA INTEGRAL DE LA NACION III", 2, "3"),
("AGG-22313", "SISTEMAS ADMINISTRATIVOS", 3, "4"),
("SYC-32114", "TEORÍA DE LOS SISTEMAS", 3, "4"),
("MAT-31214", "LOGICA MATEMATICA", 3, "4"),
("SYC-32225", "LENGUAJES DE PROGRAMACIÓN I", 4, "4"),
("DIN-31143", "DEFENSA INTEGRAL DE LA NACION IV", 2, "4"),
("AGL-30214", "SISTEMAS DE PRODUCCIÓN", 3, "5"),
("SYC-32414", "PROCESAMIENTO DE DATOS", 3, "5"),
("MAT-31714", "CALCULO NUMÉRICO", 3, "5"),
("MAT-31114", "TEORÍA DE GRAFOS", 3, "5"),
("SYC-32235", "LENGUAJES DE PROGRAMACIÓN II", 4, "5"),
("DIN-31153", "DEFENSA INTEGRAL DE LA NACION V", 2, "5"),
("MAT-30925", "INVESTIGACIÓN DE OPERACIONES", 4, "6"),
("ELN-30514", "CIRCUITOS LÓGICOS", 3, "6"),
("SYC-32514", "ANÁLISIS DE SISTEMAS", 3, "6"),
("SYC-32614", "BASES DE DATOS", 3, "6"),
("SYC-32245", "LENGUAJES DE PROGRAMACIÓN III", 4, "6"),
("DIN-31163", "DEFENSA INTEGRAL DE LA NACION VI", 2, "6"),
("MAT-30935", "OPTIMIZACIÓN NO LINEAL", 4, "7"),
("MAT-31414", "PROCESOS ESTOCÁSTICOS", 3, "7"),
("SYC-30525", "ARQUITECTURA DEL COMPUTADOR", 4, "7"),
("SYC-32524", "DISEÑO DE SISTEMAS", 3, "7"),
("SYC-30834", "SISTEMAS OPERATIVOS", 3, "7"),
("DIN-31173", "DEFENSA INTEGRAL DE LA NACION VII", 2, "7"),
("SYC-32714", "IMPLANTACIÓN DE SISTEMAS", 3, "8"),
("ADG-30214", "METODOLOGIA DE LA INVESTIGACION", 3, "8"),
("MAT-30945", "SIMULACIÓN Y MODELOS", 4, "8"),
("SYC-31644", "REDES", 3, "8"),
("ADG-30224", "GERENCIA DE LA INFORMÁTICA", 3, "8"),
("DIN-31183", "DEFENSA INTEGRAL DE LA NACION VIII", 2, "8"),
("MAT-31314", "TEORÍA DE DECISIONES", 3, "9"),
("SYC-32814", "AUDITORÍA DE SISTEMAS", 3, "9"),
("CJU-37314", "MARCO LEGAL PARA EL EJERCICIO DE LA INGENIERÍA", 4, "10"),
("TTC-31154", "TELEPROCESOS", 3, "10"),
("TGR-30010", "TRABAJO ESPECIAL DE GRADO", 0, "10");

insert into priority(FKCodeSubject,priority) values
("IDM-24123", "IDM-24113"),
("QUF-22014", "MAT-21215"),
("QUF-22014", "MAT-21524"),
("QUF-23015", "MAT-21215"),
("QUF-23015", "MAT-21524"),
("MAT-21225", "MAT-21215"),
("MAT-21225", "MAT-21524"),
("MAT-21114", "MAT-21215"),
("MAT-21114", "MAT-21524"),
("DIN-21123", "DIN-21113"),
("ADG-25141", "ADG-25131"),
("QUF-23025", "QUF-23015"),
("QUF-23025", "MAT-21225"),
("MAT-21235", "MAT-21225"),
("MAT-21414", "MAT-21225"),
("SYC-22113", "MAT-21114"),
("DIN-21133", "DIN-21123"),
("MAT-31214", "MAT-21114"),
("SYC-32225", "SYC-22113"),
("DIN-31143", "DIN-21133"),
("AGL-30214", "AGG-22313"),
("SYC-32414", "SYC-22113"),
("MAT-31714", "MAT-21235"),
("MAT-31114", "MAT-31214"),
("MAT-31114", "MAT-21414"),
("SYC-32235", "SYC-32225"),
("DIN-31153", "DIN-31143"),
("MAT-30925", "MAT-31714"),
("ELN-30514", "MAT-31214"),
("SYC-32514", "SYC-32114"),
("SYC-32614", "SYC-32414"),
("SYC-32245", "SYC-32235"),
("DIN-31163", "DIN-31153"),
("MAT-30935", "MAT-30925"),
("MAT-31414", "MAT-30925"),
("MAT-31414", "MAT-31114"),
("SYC-30525", "ELN-30514"),
("SYC-32524", "SYC-32514"),
("DIN-31173", "DIN-31163"),
("SYC-32714", "SYC-32524"),
("MAT-30945", "MAT-30935"),
("MAT-30945", "MAT-31414"),
("SYC-31644", "SYC-30834"),
("DIN-31183", "DIN-31173"),
("MAT-31314", "MAT-30945"),
("SYC-32814", "SYC-32714"),
("TTC-31154", "SYC-31644");
