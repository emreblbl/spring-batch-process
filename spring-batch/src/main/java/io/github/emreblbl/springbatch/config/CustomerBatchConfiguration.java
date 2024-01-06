package io.github.emreblbl.springbatch.config;


import io.github.emreblbl.springbatch.listener.ChunkProcessTimeLogger;
import io.github.emreblbl.springbatch.model.Customer;
import io.github.emreblbl.springbatch.model.CustomerInput;
import io.github.emreblbl.springbatch.processor.CustomItemProcessor;
import io.github.emreblbl.springbatch.reader.CustomerItemReader;
import io.github.emreblbl.springbatch.writer.CustomerItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CustomerBatchConfiguration {

    @Autowired
    private JobLauncher jobLauncher;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final CustomerItemReader customerItemReader;

    private final CustomItemProcessor customerItemProcessor;
    private final CustomerItemWriter customerItemWriter;

    @Autowired
    public CustomerBatchConfiguration( JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, CustomerItemReader customerItemReader, CustomItemProcessor customerItemProcessor, CustomerItemWriter customerItemWriter) {
        this.jobRepository = jobRepository;
        this.platformTransactionManager = platformTransactionManager;
        this.customerItemReader = customerItemReader;
        this.customerItemProcessor = customerItemProcessor;
        this.customerItemWriter = customerItemWriter;
    }

    @Bean
    public Job customerJob() {
        return new JobBuilder("CustomerFileJob", jobRepository)
                .flow(customerStep())
                .end()
                .build();
    }

    @Bean
    public Step customerStep() {
        return new StepBuilder("CustomerFileSteps", jobRepository).<CustomerInput, Customer>chunk(10, platformTransactionManager)
                .reader(customerItemReader)
                .processor(customerItemProcessor)
                .writer(customerItemWriter)
                .listener(new ChunkProcessTimeLogger())
                .build();
    }


}
