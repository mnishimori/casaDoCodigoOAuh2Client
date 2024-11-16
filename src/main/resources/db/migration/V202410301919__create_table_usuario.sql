create table usuario(
	id int auto_increment primary key,
	nome varchar(100),
	login varchar(100),
	senha varchar(50),
	login_bookserver varchar(100) null,
	senha_bookserver varchar(50) null
);
