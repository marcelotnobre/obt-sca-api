-- INSERÇÕES NA TABELAS DO CONTROLE DE ACESSO

-- permissao
INSERT INTO permissao
    (id, nome, descricao, sistema_id, status)
values
-- Menu
    (67, 'ROLE_VISUALIZAR_MENU_BIOMETRIA', 'Permissão para o menu de biometria', 1,true),
    (68, 'ROLE_VISUALIZAR_MENU_AUTENTICACAO_BIOMETRIA', 'Permissão para visualizar o menu de autenticação por biometria', 1,true),
    (69, 'ROLE_VISUALIZAR_MENU_RELATORIO', 'Permissão para visualizar o menu de relatório', 1,true),
    (70, 'ROLE_VISUALIZAR_MENU_RELATORIO_USUARIO', 'Permissão para visualizar o relatório de usuário', 1,true),
    (71, 'ROLE_VISUALIZAR_MENU_RELATORIO_ESTATISTICA', 'Permissão para visualizar o relatório de estatística', 1,true),
    (72, 'ROLE_VISUALIZAR_MENU_LOG', 'Permissão para visualizar o menu de log', 1,true),
    (73, 'ROLE_VISUALIZAR_MENU_LOG_REGISTRO_USUARIO_LOGIN', 'Permissão para visualizar o menu de log do usuário', 1,true),
    (74, 'ROLE_VISUALIZAR_DASHBOARD_ESTATISTICA', 'Permissão para visualizar a dashboard de estatística', 1,true);