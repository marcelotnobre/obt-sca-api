-- INSERÇÕES NA TABELAS DO CONTROLE DE ACESSO

-- usuario
INSERT INTO usuario 
    (id, email, login, tipoAutenticacao, senha, status) 
values 
    (1, 'admin@algamoney.com','admin', 'SCA', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.', true),
    (2, 'bruno.santos@outerboxtech.com.br', 'jose.silva', 'SCA', '$2a$10$JJ0bA6DcfjoanXUEXnqGsudL88LpdQZ8wixKOuwHIxOeWHwMvJMGm', true),
    (3, 'marcelo.nobre@outerboxtech.com.br', 'marcelo.nobre', 'SCA', '$2a$10$/7wDyFFIyYtaHuAywvXwgu5VH6WVI4T17vEqN3ggZNt2syPYSIGE.', true);

-- sistema
INSERT INTO sistema 
    (id,  nome, descricao, urlAPI, urlWEB, urlLOGO, status)
values 
    (1, 'Sistema de Controle de Acesso', 'Sistema de Controle de Acesso', 'https://obt-sca-api.herokuapp.com', 'https://obt-sca-web.herokuapp.com/','https://www.primefaces.org/serenity-react/assets/layout/images/logo-slim.png' ,true),
    (2, 'Sistema de Clinica Médica', 'Sistema de Clinica Médica', 'https://obt-clinica-api.herokuapp.com/', 'https://obt-clinica-api.herokuapp.com/','https://www.primefaces.org/serenity-react/assets/layout/images/logo-slim.png' ,true),
    (3, 'Sistema de Clinica Odontologica', 'Sistema de Clinica Odontologica', '', '','' ,true),
    (4, 'Sistema de Finaceiro', 'Sistema de Finaceiro', '', '','' ,true);

-- permissao
INSERT INTO permissao
    (id, nome, descricao, sistema_id, status)
values
-- Administrador
    (1, 'ROLE_ADMINISTRADOR', 'Permissão para o administrador', 1,true),

-- Paciente
    (2, 'ROLE_CADASTRAR_PACIENTE', 'Permissão para cadastrar um paciente', 2,true),
    (3, 'ROLE_REMOVER_PACIENTE', 'Permissão para remover um paciente', 2,true),
    (4, 'ROLE_PESQUISAR_PACIENTE', 'Permissão para pesquisar um paciente', 2,true),
    (5, 'ROLE_STATUS_PACIENTE', 'Permissão para pesquisar um paciente', 2,true),

-- Permissao
    (6, 'ROLE_CADASTRAR_PERMISSAO', 'Permissão para cadastrar uma permissão', 1, true),
    (7, 'ROLE_PESQUISAR_PERMISSAO', 'Permissão para pesquisar uma permissão', 1,true),
    (8, 'ROLE_STATUS_PERMISSAO', 'Permissão para bloquear/desbloquear uma permissão', 1,true),
    (9, 'ROLE_REMOVER_PERMISSAO', 'Permissão para remover uma permissão', 1,true),

-- Sistema
    (10, 'ROLE_CADASTRAR_SISTEMA', 'Permissão para cadastrar um sistema', 1,true),
    (11, 'ROLE_PESQUISAR_SISTEMA', 'Permissão para pesquisar um sistema', 1,true),
    (12, 'ROLE_STATUS_SISTEMA', 'Permissão para bloquear/desbloquear um sistema', 1,true),
    (13, 'ROLE_REMOVER_SISTEMA', 'Permissão para remover um sistema', 1,true),

-- Perfil
    (14, 'ROLE_CADASTRAR_PERFIL', 'Permissão para cadastrar um perfil', 1,true),
    (15, 'ROLE_PESQUISAR_PERFIL', 'Permissão para pesquisar um perfil', 1,true),
    (16, 'ROLE_STATUS_PERFIL', 'Permissão para bloquear/desbloquear um perfil', 1,true),
    (17, 'ROLE_REMOVER_PERFIL', 'Permissão para remover um perfil', 1,true),

-- Atributo
    (18, 'ROLE_CADASTRAR_ATRIBUTO', 'Permissão para cadastrar um atributo', 2,true),
    (19, 'ROLE_PESQUISAR_ATRIBUTO', 'Permissão para pesquisar um atributo', 2,true),
    (20, 'ROLE_STATUS_ATRIBUTO', 'Permissão para bloquear/desbloquear um atributo', 2,true),
    (21, 'ROLE_REMOVER_ATRIBUTO', 'Permissão para remover um atributo.(Administrador)', 2,true),

-- Dominio
    (22, 'ROLE_CADASTRAR_DOMINIO', 'Permissão para cadastrar um dominio', 2,true),
    (23, 'ROLE_PESQUISAR_DOMINIO', 'Permissão para pesquisar um dominio', 2,true),
    (24, 'ROLE_STATUS_DOMINIO', 'Permissão para bloquear/desbloquear um dominio', 2,true),
    (25, 'ROLE_REMOVER_DOMINIO', 'Permissão para remover um dominio. (Administrador)', 2,true),

-- Usuario
    (26, 'ROLE_CADASTRAR_USUARIO', 'Permissão para cadastrar um usuário', 1,true),
    (27, 'ROLE_PESQUISAR_USUARIO', 'Permissão para pesquisar um usuário', 1,true),
    (28, 'ROLE_STATUS_USUARIO', 'Permissão para bloquear/desbloquear um usuário', 1,true),
    (29, 'ROLE_REMOVER_USUARIO', 'Permissão para remover um usuário', 1,true),

-- Especialidade
    (30, 'ROLE_CADASTRAR_ESPECIALIDADE', 'Permissão para cadastrar uma especialidade', 2,true),
    (31, 'ROLE_PESQUISAR_ESPECIALIDADE', 'Permissão para pesquisar uma especialidade', 2,true),
    (32, 'ROLE_STATUS_ESPECIALIDADE', 'Permissão para bloquear/desbloquear uma especialidade', 2,true),
    (33, 'ROLE_REMOVER_ESPECIALIDADE', 'Permissão para remover uma especialidade', 2,true),

-- Empresa
    (34, 'ROLE_CADASTRAR_CONSULTORIO', 'Permissão para cadastrar uma consutório', 2,true),
    (35, 'ROLE_PESQUISAR_CONSULTORIO', 'Permissão para pesquisar uma consutório', 2,true),
    (36, 'ROLE_STATUS_CONSULTORIO', 'Permissão para bloquear/desbloquear uma consutório', 2,true),
    (37, 'ROLE_REMOVER_CONSULTORIO', 'Permissão para remover uma consutório', 2,true),

-- Profissional
    (38, 'ROLE_CADASTRAR_PROFISSIONAL', 'Permissão para cadastrar uma profissional', 2,true),
    (39, 'ROLE_PESQUISAR_PROFISSIONAL', 'Permissão para pesquisar uma profissional', 2,true),
    (40, 'ROLE_STATUS_PROFISSIONAL', 'Permissão para bloquear/desbloquear uma profissional', 2,true),
    (41, 'ROLE_REMOVER_PROFISSIONAL', 'Permissão para remover uma profissional', 2,true),

-- Funcionario
    (42, 'ROLE_CADASTRAR_FUNCIONARIO', 'Permissão para cadastrar uma funcionario', 2,true),
    (43, 'ROLE_PESQUISAR_FUNCIONARIO', 'Permissão para pesquisar uma funcionario', 2,true),
    (44, 'ROLE_STATUS_FUNCIONARIO', 'Permissão para bloquear/desbloquear uma funcionario', 2,true),
    (45, 'ROLE_REMOVER_FUNCIONARIO', 'Permissão para remover uma funcionario', 2,true),

-- Funcionario
    (46, 'ROLE_CADASTRAR_CONVENIO', 'Permissão para cadastrar uma convenio', 2,true),
    (47, 'ROLE_PESQUISAR_CONVENIO', 'Permissão para pesquisar uma convenio', 2,true),
    (48, 'ROLE_STATUS_CONVENIO', 'Permissão para bloquear/desbloquear uma convenio', 2,true),
    (49, 'ROLE_REMOVER_CONVENIO', 'Permissão para remover uma convenio', 2,true),

-- Agendamento
    (50, 'ROLE_CADASTRAR_AGENDAMENTO', 'Permissão para cadastrar uma agendamento', 2,true),
    (51, 'ROLE_PESQUISAR_AGENDAMENTO', 'Permissão para pesquisar uma agendamento', 2,true),
    (52, 'ROLE_STATUS_AGENDAMENTO', 'Permissão para bloquear/desbloquear uma agendamento', 2,true),
    (53, 'ROLE_REMOVER_AGENDAMENTO', 'Permissão para remover uma agendamento', 2,true),

-- Cargo
    (54, 'ROLE_CADASTRAR_CARGO', 'Permissão para cadastrar um cargo', 2,true),
    (55, 'ROLE_PESQUISAR_CARGO', 'Permissão para pesquisar um cargo', 2,true),
    (56, 'ROLE_STATUS_CARGO', 'Permissão para bloquear/desbloquear um cargo', 2,true),
    (57, 'ROLE_REMOVER_CARGO', 'Permissão para remover um cargo', 2,true),

-- Profissão
    (58, 'ROLE_CADASTRAR_PROFISSAO', 'Permissão para cadastrar umaprofissão', 2,true),
    (59, 'ROLE_PESQUISAR_PROFISSAO', 'Permissão para pesquisar umaprofissão', 2,true),
    (60, 'ROLE_STATUS_PROFISSAO', 'Permissão para bloquear/desbloquear uma profissão', 2,true),
    (61, 'ROLE_REMOVER_PROFISSAO', 'Permissão para remover umaprofissão', 2,true),

-- Procedimento
    (62, 'ROLE_CADASTRAR_PROCEDIMENTO', 'Permissão para cadastrar um procedimento', 2,true),
    (63, 'ROLE_PESQUISAR_PROCEDIMENTO', 'Permissão para pesquisar um procedimento', 2,true),
    (64, 'ROLE_STATUS_PROCEDIMENTO', 'Permissão para bloquear/desbloquear um procedimento', 2,true),
    (65, 'ROLE_REMOVER_PROCEDIMENTO', 'Permissão para remover um procedimento', 2,true),

-- Estatistica
    (66, 'ROLE_VISUALIZAR_ESTATISTICA', 'Permissão para visualizar a estatistica', 2,true);
    
-- menu
INSERT INTO permissao
    (id, nome, descricao, sistema_id, status)
values
    (67, 'ROLE_VISUALIZAR_MENU_BIOMETRIA', 'Permissão para o menu de biometria', 1,true),
    (68, 'ROLE_VISUALIZAR_MENU_AUTENTICACAO_BIOMETRIA', 'Permissão para visualizar o menu de autenticação por biometria', 1,true),
    (69, 'ROLE_VISUALIZAR_MENU_RELATORIO', 'Permissão para visualizar o menu de relatório', 1,true),
    (70, 'ROLE_VISUALIZAR_MENU_RELATORIO_USUARIO', 'Permissão para visualizar o relatório de usuário', 1,true),
    (71, 'ROLE_VISUALIZAR_MENU_RELATORIO_ESTATISTICA', 'Permissão para visualizar o relatório de estatística', 1,true),
    (72, 'ROLE_VISUALIZAR_MENU_LOG', 'Permissão para visualizar o menu de log', 1,true),
    (73, 'ROLE_VISUALIZAR_MENU_LOG_REGISTRO_USUARIO_LOGIN', 'Permissão para visualizar o menu de log do usuário', 1,true),
    (74, 'ROLE_VISUALIZAR_DASHBOARD_ESTATISTICA', 'Permissão para visualizar a dashboard de estatística', 1,true);

-- Perfil
INSERT INTO perfil 
    (id, nome, descricao,sistema_id, status) 
values
    (1, 'Administrador', 'Administrador do Sistema', 1, true),
    (2, 'Gestor', 'Gestor do Sistema',1, true),
    (3, 'Recepcionista', 'Recepcionista',1, true),
    (4, 'Médico', 'Médico',1, true),
    (5, 'Psicologo', 'Psicologo',1, true),
    (6, 'Dentista', 'Dentista',1, true);

-- Perfil do Administrador
INSERT INTO perfil_permissao 
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
    (1, 74);

-- Perfil Usuario - Gestor
INSERT INTO perfil_permissao
    (perfil_id, permissao_id) 
values
    (2, 2),
    (2, 3),
    (2, 4),
    (2, 5),
    (2, 50),
    (2, 51),
    (2, 52),
    (2, 53),
    (2, 66);

-- Perfil Usuario - Recepcionista
INSERT INTO perfil_permissao 
    (perfil_id, permissao_id)
values
    (3, 7),
    (3, 8),
    (3, 9),
    (3, 10);

-- Usuario Perfil
INSERT INTO usuario_perfil 
    (usuario_id, perfil_id ) 
values
    (1, 1),
    (2, 2),
    (3, 3);

-- Usuario Sistema 
INSERT INTO usuario_sistema 
    (usuario_id, sistema_id ) 
values 
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 2),
    (3, 1),
    (3, 2);
