package com.batch.process.csv.batch.writers;

import com.batch.process.csv.model.Person;
import com.batch.process.csv.repository.PersonRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PersonItemWriter implements ItemWriter<Person> {
    @Autowired
    PersonRepository personRepository;

    @Override
    public void write(List<? extends Person> list) throws Exception {
        personRepository.saveAll(list);
    }
}
