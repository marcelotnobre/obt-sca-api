package br.com.obt.sca.api.config.threadexecutor;

import br.com.obt.sca.api.model.Usuario;
import br.com.obt.sca.api.service.UsuarioService;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;

public class ThreadEnviarEmail implements Runnable {

	protected UsuarioService usuarioService;

	protected Usuario usuario;

	public ThreadEnviarEmail(UsuarioService usuarioService, Usuario usuario) {
		this.usuario = usuario;
		this.usuarioService = usuarioService;
	}

	@Override
	public void run() {
		try {
			usuarioService.enviarEmailUsuario(this.usuario);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
		}

	}

}
