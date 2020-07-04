package br.com.obt.sca.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.obt.sca.api.service.exception.ServiceException;

@Transactional(
        propagation = Propagation.REQUIRED,
        rollbackFor = {ServiceException.class}
)
@Service
public class AnexoService {

}
