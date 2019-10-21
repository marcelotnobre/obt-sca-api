package br.com.obt.sca.api.config.threadexecutor;

//@Configuration
//@EnableAsync
public class ThreadPoolTaskConfig {

    // @Bean(name = "threadPoolTaskExecutor")
    // public TaskExecutor getAsyncExecutor() {
    // ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // executor.setCorePoolSize(5);
    // executor.setMaxPoolSize(20);
    // executor.setWaitForTasksToCompleteOnShutdown(true);
    //// executor.setQueueCapacity(100);
    // executor.setThreadNamePrefix("Async-");
    // executor.afterPropertiesSet();
    //// executor.initialize();
    // return executor;
    // }

    // @Bean("customFixedThreadPool")
    // public ExecutorService customFixedThreadPool() {
    // return Executors.newFixedThreadPool(2, new
    // CustomizableThreadFactory("customFixedThreadPool"));
    //
    // }

}
