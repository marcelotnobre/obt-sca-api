package br.com.obt.sca.api.repository.superclass;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T extends Object, ID extends Object> extends JpaRepository<T, Long> {

    Page<T> findAll(Specification<T> spec, Pageable pageable);

    Long count(Specification<T> spec);

    <T> Collection<T> findByStatusTrue(Class<T> type);

}
