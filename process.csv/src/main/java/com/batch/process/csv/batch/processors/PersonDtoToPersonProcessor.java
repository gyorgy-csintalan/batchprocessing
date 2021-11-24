package com.batch.process.csv.batch.processors;

import com.batch.process.csv.batch.dto.PersonDTO;
import com.batch.process.csv.model.Person;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

public class PersonDtoToPersonProcessor implements ItemProcessor<PersonDTO, Person> {

    @Override
    public Person process(PersonDTO personDTO) throws Exception {
        Person person = new Person();
        BeanUtils.copyProperties(personDTO, person);

        return person;
    }
}
