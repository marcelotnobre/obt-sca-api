package br.com.obt.sca.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.obt.sca.api.model.LobAnexo;
import br.com.obt.sca.api.repository.anexo.LobAnexoRepository;
import br.com.obt.sca.api.service.exception.ResourceAlreadyExistsException;
import br.com.obt.sca.api.service.exception.ResourceNotFoundException;
import br.com.obt.sca.api.service.exception.ServiceException;

//@formatter:off
@Transactional(
			   propagation = Propagation.REQUIRED,  
			   rollbackFor = { ServiceException.class }
			  )
//@formatter:on
@Service
public class LobAnexoService {

	@Autowired
	private LobAnexoRepository lobAnexoRepository;

	@Transactional(readOnly = false)
	public LobAnexo save(LobAnexo LobAnexo) throws ResourceAlreadyExistsException, ResourceNotFoundException {

		// Optional<AnexoPessoa> anexoPessoaBanco =
		// pessoaService.findById(pessoa.getId());
		// Setando os campos que são alterados na tela.
		// if ((pessoa != null) && (pessoa.getId() != null)) {
		// anexoPessoaBanco.get().setNome(anexoPessoa.getNome());
		// anexoPessoaBanco.get().setLabel(anexoPessoa.getLabel());
		// anexoPessoaBanco.get().setLobAnexo(anexoPessoa.getLobAnexo());
		// anexoPessoaBanco.get().setTamanho(anexoPessoa.getTamanho());
		// anexoPessoaBanco.get().setTipoConteudo(anexoPessoa.getTipoConteudo());
		// anexoPessoaBanco.get().setPessoa(pessoa);
		//
		// BeanUtils.copyProperties(anexoPessoaBanco.get(), pessoa, "id");
		// }

		return lobAnexoRepository.save(LobAnexo);

	}

}
