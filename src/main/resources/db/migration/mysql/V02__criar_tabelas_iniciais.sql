-- DROP TABLE CONTROLE DE ACESSO 

DROP TABLE IF EXISTS sca_api.anexo;
DROP TABLE IF EXISTS sca_api_auditoria.auditoria;
DROP TABLE IF EXISTS sca_api_auditoria.auditoria_usuario;
DROP TABLE IF EXISTS hibernate_sequence;
DROP TABLE IF EXISTS sca_api.lob_anexo;
DROP TABLE IF EXISTS sca_api.perfil;
DROP TABLE IF EXISTS sca_api.perfil_permissao;
DROP TABLE IF EXISTS sca_api.permissao;
DROP TABLE IF EXISTS sca_api.sistema;
DROP TABLE IF EXISTS sca_api.usuario;
DROP TABLE IF EXISTS sca_api.usuario_perfil;
DROP TABLE IF EXISTS sca_api.usuario_sistema;
DROP TABLE IF EXISTS sca_api.usuario_permissao;


-- CREATE TABLE CONTROLE DE ACESSO
CREATE TABLE sca_api.anexo (
    id BIGINT NOT NULL AUTO_INCREMENT,
    label VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    tamanho BIGINT NOT NULL,
    tipoconteudo VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB;

CREATE TABLE sca_api_auditoria.auditoria (
    rev INTEGER NOT NULL,
    revtstmp BIGINT,
    host_name VARCHAR(255),
    ip VARCHAR(255),
    usuario_email VARCHAR(255) NOT NULL,
    usuario_login VARCHAR(255) NOT NULL,
    PRIMARY KEY (rev)
)  ENGINE=INNODB;

CREATE TABLE sca_api_auditoria.auditoria_usuario (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype TINYINT,
    email VARCHAR(50),
    login VARCHAR(50),
    senha VARCHAR(200),
    tipoautenticacao VARCHAR(50),
    PRIMARY KEY (id , rev)
)  ENGINE=INNODB;

CREATE TABLE hibernate_sequence (
    next_val BIGINT
)  ENGINE=INNODB;
INSERT INTO hibernate_sequence VALUES ( 1 );

CREATE TABLE sca_api.lob_anexo (
    id BIGINT NOT NULL,
    lob LONGBLOB NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB;

CREATE TABLE sca_api.perfil (
    id BIGINT NOT NULL AUTO_INCREMENT,
    status BIT NOT NULL,
    datahorafinalvigencia DATETIME,
    descricao VARCHAR(1000),
    nome VARCHAR(200),
    sistema_id BIGINT NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB;

CREATE TABLE sca_api.perfil_permissao (
    perfil_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,
    PRIMARY KEY (perfil_id , permissao_id)
)  ENGINE=INNODB;

CREATE TABLE sca_api.permissao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    status BIT NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    sistema_id BIGINT NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB;

CREATE TABLE sca_api.sistema (
    id BIGINT NOT NULL AUTO_INCREMENT,
    status BIT NOT NULL,
    descricao VARCHAR(500),
    nome VARCHAR(100),
    urlapi VARCHAR(200),
    urllogo VARCHAR(200),
    urlweb VARCHAR(200),
    PRIMARY KEY (id)
)  ENGINE=INNODB;

CREATE TABLE sca_api.usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    status BIT NOT NULL,
    email VARCHAR(50) NOT NULL,
    login VARCHAR(50) NOT NULL,
    senha VARCHAR(200) NOT NULL,
    tipoautenticacao VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB;

CREATE TABLE sca_api.usuario_perfil (
    perfil_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    PRIMARY KEY (perfil_id , usuario_id)
)  ENGINE=INNODB;

CREATE TABLE sca_api.usuario_sistema (
    usuario_id BIGINT NOT NULL,
    sistema_id BIGINT NOT NULL,
    PRIMARY KEY (sistema_id , usuario_id)
)  ENGINE=INNODB;

CREATE TABLE sca_api.usuario_permissao (
    usuario_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id , permissao_id)
)  ENGINE=INNODB;


-- CREATE INDICES E UNIQUE

ALTER TABLE sca_api.anexo ADD CONSTRAINT UK_ANEXO_NOME_TAM_TPCONTEUDO UNIQUE (nome, tamanho, tipoconteudo);
ALTER TABLE sca_api.perfil ADD CONSTRAINT UK_PERFIL_NOME UNIQUE (nome);
CREATE INDEX INDEX_NOME_PERMISSAO ON sca_api.permissao (nome);
ALTER TABLE sca_api.permissao ADD CONSTRAINT UK_PERMISSAO_NOME UNIQUE (nome);
ALTER TABLE sca_api.sistema ADD CONSTRAINT UK_SISTEMA_NOME UNIQUE (nome);
CREATE INDEX INDEX_EMAIL_USUARIO ON sca_api.usuario (email);
CREATE INDEX INDEX_LOGIN_USUARIO ON sca_api.usuario (login);
CREATE INDEX INDEX_EMAIL_E_SENHA_USUARIO ON sca_api.usuario (email, senha);
CREATE INDEX INDEX_LOGIN_E_SENHA_USUARIO ON sca_api.usuario (login, senha);
ALTER TABLE sca_api.usuario ADD CONSTRAINT UK_USUARIO_EMAIL UNIQUE (email);
ALTER TABLE sca_api.usuario ADD CONSTRAINT UK_USUARIO_LOGIN UNIQUE (login);

-- FOREIGN KEY
ALTER TABLE sca_api_auditoria.auditoria_usuario ADD CONSTRAINT FKd8adfv4n47pekinlhk7xjtugm FOREIGN KEY (rev) REFERENCES sca_api_auditoria.auditoria (rev);
ALTER TABLE sca_api.lob_anexo ADD CONSTRAINT FKmno6foehx2tvts0x7gq1pxipe FOREIGN KEY (id) REFERENCES sca_api.anexo (id);
ALTER TABLE sca_api.perfil ADD CONSTRAINT FK_PERFIL_SISTEMA FOREIGN KEY (sistema_id) REFERENCES sca_api.sistema (id);
ALTER TABLE sca_api.perfil_permissao ADD CONSTRAINT FK_PERFILPERMISSAO_PERFIL FOREIGN KEY (perfil_id) REFERENCES sca_api.perfil (id);
ALTER TABLE sca_api.perfil_permissao ADD CONSTRAINT FK_PERFILPERMISSAO_PERMISSAO FOREIGN KEY (permissao_id) REFERENCES sca_api.permissao (id);
ALTER TABLE sca_api.permissao ADD CONSTRAINT FK_PERMISSAO_SISTEMA FOREIGN KEY (sistema_id) REFERENCES sca_api.sistema (id);
ALTER TABLE sca_api.usuario_perfil ADD CONSTRAINT FK_USUARIOPERFIL_PERFIL FOREIGN KEY (perfil_id) REFERENCES sca_api.perfil (id);
ALTER TABLE sca_api.usuario_perfil ADD CONSTRAINT FK_USUARIOPERFIL_USUARIO FOREIGN KEY (usuario_id) REFERENCES sca_api.usuario (id);
ALTER TABLE sca_api.usuario_sistema ADD CONSTRAINT FK_USUARIOSISTEMA_USUARIO FOREIGN KEY (usuario_id) REFERENCES sca_api.usuario (id);
ALTER TABLE sca_api.usuario_sistema ADD CONSTRAINT FK_USUARIOPERFIL_SISTEMA FOREIGN KEY (sistema_id) REFERENCES sca_api.sistema (id);
ALTER TABLE sca_api.usuario_permissao ADD CONSTRAINT FK_USUARIOPERMISSAO_USUARIO FOREIGN KEY (usuario_id) REFERENCES sca_api.usuario (id);
ALTER TABLE sca_api.usuario_permissao ADD CONSTRAINT FK_USUARIOPERMISSAO_SISTEMA FOREIGN KEY (permissao_id) REFERENCES sca_api.permissao (id);

-- DROP TABLE AUDITORIA

-- FOREIGN KEY
