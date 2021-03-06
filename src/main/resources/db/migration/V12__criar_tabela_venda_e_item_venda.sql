CREATE TABLE venda (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    data_criacao DATETIME NOT NULL,
    valor_frete DECIMAL(10,2),
    valor_desconto DECIMAL(10,2),
    valor_total DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    observacao VARCHAR(200),
    data_hora_entrega DATETIME,
    codigo_cliente BIGINT(20) NOT NULL,
    codigo_usuario BIGINT(20) NOT NULL,
    codigo_banco BIGINT(20) NOT NULL,
    FOREIGN KEY (codigo_cliente) REFERENCES cliente(codigo),
    FOREIGN KEY (codigo_banco) REFERENCES banco(codigo),
    FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE item_venda (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    quantidade INTEGER NOT NULL,
    observacoes VARCHAR(300),
    valor_unitario DECIMAL(10,2) NOT NULL,
    codigo_cerveja BIGINT(20) NOT NULL,
    codigo_venda BIGINT(20) NOT NULL,
    FOREIGN KEY (codigo_cerveja) REFERENCES cerveja(codigo),
    FOREIGN KEY (codigo_venda) REFERENCES venda(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE pedido (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    data_criacao DATETIME NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    observacao VARCHAR(200),
	codigo_representada BIGINT(20) NOT NULL,
	codigo_banco BIGINT(20) NOT NULL,
	codigo_ BIGINT(20) NOT NULL,
    
    codigo_cliente BIGINT(20) NOT NULL,
    codigo_usuario BIGINT(20) NOT NULL,
    FOREIGN KEY (codigo_banco) REFERENCES banco(codigo),
    FOREIGN KEY (codigo_representada) REFERENCES representada(codigo),
    FOREIGN KEY (codigo_cliente) REFERENCES cliente(codigo),
    FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE item_pedido (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    quantidade INTEGER NOT NULL,
    observacoes VARCHAR(300),
    valor_unitario DECIMAL(10,2) NOT NULL,
    codigo_pedido BIGINT(20) NOT NULL,
    FOREIGN KEY (codigo_pedido) REFERENCES pedido(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE comissao (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    data_criacao DATETIME NOT NULL,
    total_venda DECIMAL(10,2),
    total_entregue DECIMAL(10,2),
    percentual DECIMAL(10,2) NOT NULL,
    codigo_venda BIGINT(20) NOT NULL,
    FOREIGN KEY (codigo_venda) REFERENCES venda(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
