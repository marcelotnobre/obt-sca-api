package br.com.obt.sca.api.config.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Profile("dev")
@Configuration
@EnableScheduling
public class SchedulingDevelopment {

	private static final Logger logger = LoggerFactory.getLogger(SchedulingDevelopment.class);

	private static final String TIME_ZONE = "America/Sao_Paulo";

	@Autowired
	protected CacheManager cacheManager;

	// -----------------
	// A cada hora, baseada no relógio, que esteja com 00 minutos e segundos todos
	// os dias.
	// Exemplos: 00:00:00, 01:00:00…
	// @Scheduled(cron = "0 0 * * * *", zone = TIME_ZONE)
	// -----------------
	// * "*/5 * * * * *" = a cada 5 segundos desenvolvimento.
	// @Scheduled(cron = "*/60 * * * * *", zone = TIME_ZONE)
	@Scheduled(cron = "0 0/60 8-17 * * *", zone = TIME_ZONE)
	public void evictAllcachesAtIntervalsDevelopment() {
		evictAllCaches();
	}

	public void evictAllCaches() {
		logger.info("Ambiente de Desenvolvimento : Quantidade de cache {} .",
				cacheManager.getCacheNames().stream().count());
		cacheManager.getCacheNames().forEach(cache -> logger.info("Cache sendo limpo {} .", cache));
		cacheManager.getCacheNames().stream().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
	}

}
