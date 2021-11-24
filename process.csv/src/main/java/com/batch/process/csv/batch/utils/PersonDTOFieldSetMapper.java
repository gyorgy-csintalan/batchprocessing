package com.batch.process.csv.batch.utils;

import com.batch.process.csv.batch.dto.PersonDTO;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class PersonDTOFieldSetMapper implements FieldSetMapper<PersonDTO> {
    @Override
    public PersonDTO mapFieldSet(FieldSet fieldSet) throws BindException {
        if (fieldSet == null) return null;

        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName(fieldSet.readString("firstName"));
        personDTO.setLastName(fieldSet.readString("lastName"));
        personDTO.setAge(fieldSet.readInt("age"));
        personDTO.setEmail(fieldSet.readString("email"));
        personDTO.setCompanyId(fieldSet.readLong("companyId"));

        return personDTO;
    }
}
