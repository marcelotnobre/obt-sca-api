-- noinspection SqlNoDataSourceInspectionForFile

-- INSERÇÕES NA TABELAS DO CONTROLE DE ACESSO

-- usuario
INSERT INTO sca_api.usuario
    (id, email, login, tipoAutenticacao, senha, status) 
values 
    (1, 'fabrica@outerboxtech.com.br','admin', 'SCA', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.', true),
    (2, 'bruno.santos@outerboxtech.com.br', 'jose.silva', 'SCA', '$2a$10$JJ0bA6DcfjoanXUEXnqGsudL88LpdQZ8wixKOuwHIxOeWHwMvJMGm', true),
    (3, 'marcelo.nobre@outerboxtech.com.br', 'marcelo.nobre', 'SCA', '$2a$10$/7wDyFFIyYtaHuAywvXwgu5VH6WVI4T17vEqN3ggZNt2syPYSIGE.', true);


-- sistema
INSERT INTO sca_api.sistema
(id,  nome, descricao, urlAPI, urlWEB, urlLOGO, status)
values
    (1, 'Sistema de Controle de Acesso', 'Sistema de Controle de Acesso', 'https://obt-sca-api.herokuapp.com', 'https://obt-sca-web.herokuapp.com/','https://www.primefaces.org/serenity-react/assets/layout/images/logo-slim.png' ,true),
    (2, 'Sistema de Clinica Odontológica', 'Sistema de Clinica Médica', 'https://obt-clinica-api.herokuapp.com/', 'https://obt-clinica-api.herokuapp.com/','https://www.primefaces.org/serenity-react/assets/layout/images/logo-slim.png' ,true);

-- permissao SCA
INSERT INTO sca_api.permissao
(id, nome, descricao, sistema_id, status)
values
-- Administrador
(1, 'ROLE_ADMINISTRADOR', 'Permissão para o administrador', 1,true),

-- Permissao
(2, 'ROLE_CRUD_PERMISSAO', 'Permissão para cadastrar/atualizar/desativar/ativar uma permissão', 1, true),
(3, 'ROLE_DESATIVAR_PERMISSAO', 'Permissão para desativar uma permissão', 1,true),
(4, 'ROLE_ATIVAR_PERMISSAO', 'Permissão para ativar uma permissão', 1,true),

-- Sistema
(5, 'ROLE_CRUD_SISTEMA', 'Permissão para cadastrar/atualizar/desativar/ativar um sistema', 1,true),
(6, 'ROLE_DESATIVAR_SISTEMA', 'Permissão para desativar um sistema', 1,true),
(7, 'ROLE_ATIVAR_SISTEMA', 'Permissão para ativar um sistema', 1,true),

-- Perfil
(8, 'ROLE_CRUD_PERFIL', 'Permissão para cadastrar/atualizar/desativar/ativar um perfil', 1,true),
(9, 'ROLE_DESATIVAR_PERFIL', 'Permissão para desativar um perfil', 1,true),
(10, 'ROLE_ATIVAR_PERFIL', 'Permissão para ativar um perfil', 1,true),

-- Usuario
(11, 'ROLE_CRUD_USUARIO', 'Permissão para cadastrar/atualizar/desativar/ativar um usuário', 1,true),
(12, 'ROLE_DESATIVAR_USUARIO', 'Permissão para desativar um usuário', 1,true),
(13, 'ROLE_ATIVAR_USUARIO', 'Permissão para ativar um usuário', 1,true);


-- permissao CLINICA
INSERT INTO sca_api.permissao
(id, nome, descricao, sistema_id, status)
values
-- Paciente
(14, 'ROLE_CRUD_PACIENTE', 'Permissão para cadastrar/atualizar/desativar/ativar um paciente', 2, true),
(15, 'ROLE_DESATIVAR_PACIENTE', 'Permissão para desativar um paciente', 2, true),
(16, 'ROLE_ATIVAR_PACIENTE', 'Permissão para ativar um paciente', 2, true),

-- Especialidade
(17, 'ROLE_CRUD_ESPECIALIDADE', 'Permissão para cadastrar/atualizar/desativar/ativar uma especialidade', 2, true),
(18, 'ROLE_DESATIVAR_ESPECIALIDADE', 'Permissão para desativar uma especialidade', 2, true),
(19, 'ROLE_ATIVAR_ESPECIALIDADE', 'Permissão para ativar uma especialidade', 2, true),

-- Consultorio
(20, 'ROLE_CRUD_CONSULTORIO', 'Permissão para cadastrar/atualizar/desativar/ativar um consutório', 2, true),
(21, 'ROLE_DESATIVAR_CONSULTORIO', 'Permissão para desativar um consutório', 2, true),
(22, 'ROLE_ATIVAR_CONSULTORIO', 'Permissão para ativar um consutório', 2, true),

-- Profissional
(23, 'ROLE_CRUD_PROFISSIONAL', 'Permissão para cadastrar/atualizar/desativar/ativar um profissional', 2, true),
(24, 'ROLE_DESATIVAR_PROFISSIONAL', 'Permissão para desativar um profissional', 2, true),
(25, 'ROLE_ATIVAR_PROFISSIONAL', 'Permissão para ativar um profissional', 2, true),

-- Convenio
(26, 'ROLE_CRUD_CONVENIO', 'Permissão para cadastrar/atualizar/desativar/ativar uma convenio', 2, true),
(27, 'ROLE_DESATIVAR_CONVENIO', 'Permissão para desativar um convenio', 2, true),
(28, 'ROLE_ATIVAR_CONVENIO', 'Permissão para ativar um convenio', 2, true),

-- Agendamento
(29, 'ROLE_CRUD_AGENDAMENTO', 'Permissão para cadastrar/atualizar/desativar/ativar uma agendamento', 2, true),
(30, 'ROLE_DESATIVAR_AGENDAMENTO', 'Permissão para desativar um agendamento', 2, true),
(31, 'ROLE_ATIVAR_AGENDAMENTO', 'Permissão para ativar um agendamento', 2, true),

-- Procedimento
(32, 'ROLE_CRUD_PROCEDIMENTO', 'Permissão para cadastrar/atualizar/desativar/ativar um procedimento', 2, true),
(33, 'ROLE_DESATIVAR_PROCEDIMENTO', 'Permissão para desativar um procedimento', 2, true),
(34, 'ROLE_ATIVAR_PROCEDIMENTO', 'Permissão para ativar um procedimento', 2, true),

-- Estatistica
(35, 'ROLE_VISUALIZAR_ESTATISTICA', 'Permissão para visualizar a estatistica', 2, true);

-- menu
INSERT INTO sca_api.permissao
(id, nome, descricao, sistema_id, status)
values
    (36, 'ROLE_VISUALIZAR_MENU_BIOMETRIA', 'Permissão para o menu de biometria', 1,true),
    (37, 'ROLE_VISUALIZAR_MENU_AUTENTICACAO_BIOMETRIA', 'Permissão para visualizar o menu de autenticação por biometria', 1,true),
    (38, 'ROLE_VISUALIZAR_MENU_RELATORIO', 'Permissão para visualizar o menu de relatório', 1,true),
    (39, 'ROLE_VISUALIZAR_MENU_RELATORIO_USUARIO', 'Permissão para visualizar o relatório de usuário', 1,true),
    (40, 'ROLE_VISUALIZAR_MENU_RELATORIO_ESTATISTICA', 'Permissão para visualizar o relatório de estatística', 1,true),
    (41, 'ROLE_VISUALIZAR_MENU_LOG', 'Permissão para visualizar o menu de log', 1,true),
    (42, 'ROLE_VISUALIZAR_MENU_LOG_REGISTRO_USUARIO_LOGIN', 'Permissão para visualizar o menu de log do usuário', 1,true),
    (43, 'ROLE_VISUALIZAR_DASHBOARD_ESTATISTICA', 'Permissão para visualizar a dashboard de estatística', 1,true);

INSERT INTO sca_api.permissao
(id, nome, descricao, sistema_id, status)
values
-- Atributo
(44, 'ROLE_CRUD_ATRIBUTO', 'Permissão para cadastrar/atualizar/desativar/ativar um atributo', 2, true),
(45, 'ROLE_DESATIVAR_ATRIBUTO', 'Permissão para desativar um atributo', 2, true),
(46, 'ROLE_ATIVAR_ATRIBUTO', 'Permissão para ativar um atributo', 2, true),

-- Dominio
(47, 'ROLE_CRUD_DOMINIO', 'Permissão para cadastrar/atualizar/desativar/ativar um dominio', 2, true),
(48, 'ROLE_DESATIVAR_DOMINIO', 'Permissão para desativar um dominio', 2, true),
(49, 'ROLE_ATIVAR_DOMINIO', 'Permissão para ativar um dominio', 2, true),

-- Cargo
(50, 'ROLE_CRUD_CARGO', 'Permissão para cadastrar/atualizar/desativar/ativar um cargo', 2, true),
(51, 'ROLE_DESATIVAR_CARGO', 'Permissão para desativar um cargo', 2, true),
(52, 'ROLE_ATIVAR_CARGO', 'Permissão para ativar um cargo', 2, true),

-- Profissão
(53, 'ROLE_CRUD_PROFISSAO', 'Permissão para cadastrar/atualizar/desativar/ativar uma profissão', 2, true),
(54, 'ROLE_DESATIVAR_PROFISSAO', 'Permissão para desativar uma profissão', 2, true),
(55, 'ROLE_ATIVAR_PROFISSAO', 'Permissão para ativar uma profissão', 2, true),

-- Grupo
(56, 'ROLE_CRUD_GRUPO', 'Permissão para cadastrar/atualizar/desativar/ativar uma grupo', 2, true),
(57, 'ROLE_DESATIVAR_GRUPO', 'Permissão para desativar um grupo', 2, true),
(58, 'ROLE_ATIVAR_GRUPO', 'Permissão para ativar um grupo', 2, true),

-- Contato
(59, 'ROLE_CRUD_CONTATO', 'Permissão para cadastrar/atualizar/desativar/ativar um contato', 2, true),
(60, 'ROLE_DESATIVAR_CONTATO', 'Permissão para desativar um contato', 2, true),
(61, 'ROLE_ATIVAR_CONTATO', 'Permissão para ativar um contato', 2, true),

-- Pessoa
(62, 'ROLE_CRUD_PESSOA', 'Permissão para cadastrar/atualizar/desativar/ativar uma pessoa', 2, true),
(63, 'ROLE_DESATIVAR_PESSOA', 'Permissão para desativar uma pessoa', 2, true),
(64, 'ROLE_ATIVAR_PESSOA', 'Permissão para ativar uma pessoa', 2, true),

-- PessoaFisica
(65, 'ROLE_CRUD_PESSOA_FISICA', 'Permissão para cadastrar/atualizar/desativar/ativar uma pessoa física', 2, true),
(66, 'ROLE_DESATIVAR_PESSOA_FISICA', 'Permissão para desativar uma pessoa física', 2, true),
(67, 'ROLE_ATIVAR_PESSOA_FISICA', 'Permissão para ativar uma pessoa física', 2, true),

-- PessoaJuridica
(68, 'ROLE_CRUD_PESSOA_JURIDICA', 'Permissão para cadastrar/atualizar/desativar/ativar uma pessoa jurídica', 2, true),
(69, 'ROLE_DESATIVAR_PESSOA_JURIDICA', 'Permissão para desativar uma pessoa jurídica', 2, true),
(70, 'ROLE_ATIVAR_PESSOA_JURIDICA', 'Permissão para ativar uma pessoa jurídica', 2, true),

-- Clinica
(71, 'ROLE_CRUD_CLINICA', 'Permissão para cadastrar/atualizar/desativar/ativar uma clinica', 2, true),
(72, 'ROLE_DESATIVAR_CLINICA', 'Permissão para desativar uma clinica', 2, true),
(73, 'ROLE_ATIVAR_CLINICA', 'Permissão para ativar uma clinica', 2, true),

-- Laboratorio
(74, 'ROLE_CRUD_LABORATORIO', 'Permissão para cadastrar/atualizar/desativar/ativar um laboratório', 2, true),
(75, 'ROLE_DESATIVAR_LABORATORIO', 'Permissão para desativar um laboratório', 2, true),
(76, 'ROLE_ATIVAR_LABORATORIO', 'Permissão para ativar um laboratório', 2, true),

-- ConvenioPadrao
(77, 'ROLE_CRUD_CONVENIO_PADRAO', 'Permissão para cadastrar/atualizar/desativar/ativar um convênio padrão', 2, true),
(78, 'ROLE_DESATIVAR_CONVENIO_PADRAO', 'Permissão para desativar um convênio padrão', 2, true),
(79, 'ROLE_ATIVAR_CONVENIO_PADRAO', 'Permissão para ativar um convênio padrão', 2, true),

-- ProcedimentoPadrao
(80, 'ROLE_CRUD_PROCEDIMENTO_PADRAO', 'Permissão para cadastrar/atualizar/desativar/ativar um procedimento padrão', 2, true),
(81, 'ROLE_DESATIVAR_PROCEDIMENTO_PADRAO', 'Permissão para desativar um procedimento padrão', 2, true),
(82, 'ROLE_ATIVAR_PROCEDIMENTO_PADRAO', 'Permissão para ativar um procedimento padrão', 2, true),

-- CartaoCredito
(83, 'ROLE_CRUD_CARTAO_CREDITO', 'Permissão para cadastrar/atualizar/desativar/ativar um cartão de crédito', 2, true),
(84, 'ROLE_DESATIVAR_CARTAO_CREDITO', 'Permissão para desativar um cartão de crédito', 2, true),
(85, 'ROLE_ATIVAR_CARTAO_CREDITO', 'Permissão para ativar um cartão de crédito', 2, true),

-- Consulta
(86, 'ROLE_CRUD_CONSULTA', 'Permissão para cadastrar/atualizar/desativar/ativar uma consulta', 2, true),
(87, 'ROLE_DESATIVAR_CONSULTA', 'Permissão para desativar uma consulta', 2, true),
(88, 'ROLE_ATIVAR_CONSULTA', 'Permissão para ativar uma consulta', 2, true),

-- ProfissionalAdministrador
(89, 'ROLE_CRUD_PROFISSIONAL_ADMINISTRADOR', 'Permissão que indica se o profissional é administrador', 2, true);

-- Perfil
INSERT INTO sca_api.perfil
(id, nome, descricao,sistema_id, status)
values
    (1, 'Administrador', 'Administrador do Sistema', 1, true),
    (2, 'Gestor', 'Gestor do Sistema',1, true),
    (3, 'Recepcionista', 'Recepcionista',1, true),
    (4, 'Profissional', 'Dentista, Cirurgião Dentista, Auxiliar de dentista, etc.',1, true),
    (5, 'Profissional Administrador', 'Dentista, Cirurgião Dentista, Auxiliar de dentista, etc.',1, true);

-- Perfil do Administrador
INSERT INTO sca_api.perfil_permissao
(perfil_id, permissao_id)
values
    (1, 1),
    (1, 2),
    (1, 3),
    (1, 4),
    (1, 5),
    (1, 6),
    (1, 7),
    (1, 8),
    (1, 9),
    (1, 10),
    (1, 11),
    (1, 12),
    (1, 13),
    (1, 14),
    (1, 15),
    (1, 16),
    (1, 17),
    (1, 18),
    (1, 19),
    (1, 20),
    (1, 21),
    (1, 22),
    (1, 23),
    (1, 24),
    (1, 25),
    (1, 26),
    (1, 27),
    (1, 28),
    (1, 29),
    (1, 30),
    (1, 31),
    (1, 32),
    (1, 33),
    (1, 34),
    (1, 35),
    (1, 36),
    (1, 37),
    (1, 38),
    (1, 39),
    (1, 40),
    (1, 41),
    (1, 42),
    (1, 43),
    (1, 44),
    (1, 45),
    (1, 46),
    (1, 47),
    (1, 48),
    (1, 49),
    (1, 50),
    (1, 51),
    (1, 52),
    (1, 53),
    (1, 54),
    (1, 55),
    (1, 56),
    (1, 57),
    (1, 58),
    (1, 59),
    (1, 60),
    (1, 61),
    (1, 62),
    (1, 63),
    (1, 64),
    (1, 65),
    (1, 66),
    (1, 67),
    (1, 68),
    (1, 69),
    (1, 70),
    (1, 71),
    (1, 72),
    (1, 73),
    (1, 74),
    (1, 75),
    (1, 76),
    (1, 77),
    (1, 78),
    (1, 79),
    (1, 80),
    (1, 81),
    (1, 82),
    (1, 83),
    (1, 84),
    (1, 85),
    (1, 86),
    (1, 87),
    (1, 88),
    (1, 89);

-- Perfil Usuario - Gestor
INSERT INTO sca_api.perfil_permissao
(perfil_id, permissao_id)
values
    (2, 14),
    (2, 15),
    (2, 16),
    (2, 29),
    (2, 30),
    (2, 31),
    (2, 35);

-- Perfil Usuario - Recepcionista
INSERT INTO sca_api.perfil_permissao
(perfil_id, permissao_id)
values
    (3, 2),
    (3, 3),
    (3, 4),
    (3, 5);

-- Usuario Perfil
INSERT INTO sca_api.usuario_perfil
(usuario_id, perfil_id )
values
    (1, 1),
    (2, 2),
    (3, 3);

-- Perfil do Profissional Administrador (Faz tudo menos aquilo que é responsabilidade da OBT)
INSERT INTO sca_api.perfil_permissao
(perfil_id, permissao_id)
values
    (5, 14),
    (5, 15),
    (5, 16),
    (5, 20),
    (5, 23),
    (5, 24),
    (5, 25),
    (5, 26),
    (5, 27),
    (5, 28),
    (5, 29),
    (5, 30),
    (5, 31),
    (5, 32),
    (5, 33),
    (5, 34),
    (5, 35),
    (5, 36),
    (5, 37),
    (5, 38),
    (5, 39),
    (5, 40),
    (5, 41),
    (5, 42),
    (5, 43),
    (5, 56),
    (5, 57),
    (5, 58),
    (5, 59),
    (5, 60),
    (5, 61),
    (5, 71),
    (5, 74),
    (5, 75),
    (5, 76),
    (5, 83),
    (5, 84),
    (5, 85),
    (5, 86),
    (5, 87),
    (5, 88),
    (5, 89);

-- Usuario Sistema
INSERT INTO sca_api.usuario_sistema
(usuario_id, sistema_id )
values
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 2),
    (3, 1),
    (3, 2);
