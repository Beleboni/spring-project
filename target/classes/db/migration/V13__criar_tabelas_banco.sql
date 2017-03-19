CREATE TABLE banco (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(300) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE produto (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(300) NOT NULL,
    
    codigo_representada TINYINT(10) NULL,
    
     FOREIGN KEY (codigo_representada) REFERENCES representada(codigo),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

