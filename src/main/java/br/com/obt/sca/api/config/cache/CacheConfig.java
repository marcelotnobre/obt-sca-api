package br.com.obt.sca.api.config.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching(mode = AdviceMode.PROXY)
public class CacheConfig {

//	protected String s;
//	protected String s2;

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		List<Cache> cacheList = new ArrayList<Cache>();
		// cacheList.add(new
		// ConcurrentMapCache("anexoPacienteService.findByIdPaciente"));

		// so like that you can create as many as you want
		cacheManager.setCaches(cacheList);
		return cacheManager;
	}

//	@Bean
//	public EhCacheManagerFactoryBean ehCacheManagerFactory() {
//		EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//		cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
//		cacheManagerFactoryBean.setShared(true);
//		return cacheManagerFactoryBean;
//	}
//
//	@Bean
//	public EhCacheCacheManager ehCacheCacheManager() {
//		EhCacheCacheManager cacheManager = new EhCacheCacheManager();
//		cacheManager.setCacheManager(ehCacheManagerFactory().getObject());
//		cacheManager.setTransactionAware(true);
//		return cacheManager;
//	}

}
