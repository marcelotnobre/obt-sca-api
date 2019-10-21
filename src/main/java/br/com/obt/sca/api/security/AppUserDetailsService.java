package br.com.obt.sca.api.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;

import br.com.obt.sca.api.model.Permissao;
import br.com.obt.sca.api.model.Usuario;
import br.com.obt.sca.api.service.PermissaoService;
import br.com.obt.sca.api.service.UsuarioService;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PermissaoService permissaoService;

	@Override
	public UserDetails loadUserByUsername(String emailOrLogin) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptional = usuarioService.findByEmailOrLogin(emailOrLogin);
		Usuario usuario = usuarioOptional.orElseThrow(() -> new InvalidGrantException("Usuario ou senha invalido."));

		return new UsuarioSistema(usuario, getPermissoesDoUsuario(usuario));
	}

	private Collection<? extends GrantedAuthority> getPermissoesDoUsuario(Usuario usuario) {

		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		List<Permissao> permissoes = new ArrayList<Permissao>();
		try {
			permissoes = permissaoService.findByPermissoesDoUsuario(usuario.getId());
		} catch (ResourceNotFoundException e) {
			throw new InvalidGrantException("Usuario " + usuario.getLogin()
					+ " ativo, porem nao tem permissoes associadas ao seu perfil. Favor contactar o administrador ou gestor do sistema.");
		}

		for (Permissao permissao : permissoes) {
			authorities.add(new SimpleGrantedAuthority(permissao.getNome()));
		}

		return authorities;
	}

}
