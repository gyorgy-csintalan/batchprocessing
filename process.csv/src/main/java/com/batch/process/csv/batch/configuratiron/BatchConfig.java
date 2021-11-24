package com.batch.process.csv.batch.configuratiron;

import com.batch.process.csv.batch.dto.PersonDTO;
import com.batch.process.csv.batch.listeners.PersonDtoSkipProcessListener;
import com.batch.process.csv.batch.listeners.Setp1StepExecutionListener;
import com.batch.process.csv.batch.policy.PersonDTOSkipPolicy;
import com.batch.process.csv.batch.processors.PersonDtoToPersonProcessor;
import com.batch.process.csv.batch.processors.PersonDtoValidatingItemProcessor;
import com.batch.process.csv.batch.utils.PersonDTOFieldSetMapper;
import com.batch.process.csv.batch.writers.PersonItemWriter;
import com.batch.process.csv.model.Person;
import com.batch.process.csv.repository.CompanyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;


@EnableBatchProcessing
@Configuration
public class BatchConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private CompanyRepository companyRepository;

    private static final String[] PERSON_DTO_TOKENS = {"firstName", "lastName", "age", "email", "companyId"};

    @Bean
    public Job job(Step step1) {
        return jobBuilderFactory.get("ETL-person-job")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(FlatFileItemReader<PersonDTO> personDtoReader,
                      ItemWriter<Person> personWriter,
                      Setp1StepExecutionListener setp1StepExecutionListener,
                      PersonDtoSkipProcessListener personDtoSkipProcessListener) {
        SimpleStepBuilder<PersonDTO, Person> simpleStepBuilder = stepBuilderFactory.get("step1")
               .<PersonDTO, Person>chunk(1)
               .reader(personDtoReader)
               .processor(personDtoProcessor())
               .faultTolerant()
               .skipPolicy(new PersonDTOSkipPolicy())
               .listener(personDtoSkipProcessListener)
               //.skip(ValidationException.class)
               .writer(personWriter);
        simpleStepBuilder.listener(setp1StepExecutionListener);
        return simpleStepBuilder.build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<PersonDTO> personDtoReader(@Value("#{jobParameters['batch.input.file']}")String resourcePath) {
        FlatFileItemReaderBuilder<PersonDTO> flatFileItemReaderBuilder = new FlatFileItemReaderBuilder<>();
        FieldSetMapper<PersonDTO> personDTOFieldSetMapper = new PersonDTOFieldSetMapper();
        return flatFileItemReaderBuilder
                .name("personDtoReader")
                .resource(new FileSystemResource(resourcePath))
                .delimited()
                .names(PERSON_DTO_TOKENS)
                .fieldSetMapper(personDTOFieldSetMapper)
                .build();
    }

/*    @Bean
    @StepScope
    public FlatFileItemReader<PersonDTO> personDtoReader1() {
        FlatFileItemReader<PersonDTO> personDTOFlatFileItemReader = new FlatFileItemReader<>();
        personDTOFlatFileItemReader.setResource(new FileSystemResource("resources/persons.csv"));
        DefaultLineMapper<PersonDTO> personDTOLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames(PERSON_DTO_TOKENS);
        FieldSetMapper<PersonDTO> personDTOFieldSetMapper = new PersonDTOFieldSetMapper();
        personDTOLineMapper.setLineTokenizer(delimitedLineTokenizer);
        personDTOLineMapper.setFieldSetMapper(personDTOFieldSetMapper);
        personDTOFlatFileItemReader.setLineMapper(personDTOLineMapper);

        return personDTOFlatFileItemReader;
    }*/


    @Bean
    public ItemProcessor<PersonDTO, Person> personDtoProcessor() {
        CompositeItemProcessor<PersonDTO, Person> compositeItemProcessor = new CompositeItemProcessor<PersonDTO, Person>();
        compositeItemProcessor.setDelegates(Arrays.asList(PersonDtoBeanValidatingProcessor(), new PersonDtoToPersonProcessor()));
        return compositeItemProcessor;
    }

    @Bean
    public BeanValidatingItemProcessor<PersonDTO> PersonDtoBeanValidatingProcessor() {
        PersonDtoValidatingItemProcessor personDtoValidatingItemProcessor = new PersonDtoValidatingItemProcessor(companyRepository);
        personDtoValidatingItemProcessor.setFilter(false);
        return personDtoValidatingItemProcessor;
    }

    @Bean
    public ItemWriter<Person> personWriter() {
        return new PersonItemWriter();
    }

    @Bean
    public Logger getLogger() {
        return LoggerFactory.getLogger(BatchConfig.class);
    }


}
