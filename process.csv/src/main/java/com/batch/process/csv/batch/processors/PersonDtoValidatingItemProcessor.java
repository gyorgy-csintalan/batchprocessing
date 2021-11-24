package com.batch.process.csv.batch.processors;

import com.batch.process.csv.batch.dto.PersonDTO;
import com.batch.process.csv.model.Company;
import com.batch.process.csv.repository.CompanyRepository;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

import java.util.Optional;

public class PersonDtoValidatingItemProcessor extends BeanValidatingItemProcessor<PersonDTO> {
    private CompanyRepository companyRepository;

    public PersonDtoValidatingItemProcessor(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public PersonDTO process(PersonDTO item) throws ValidationException {
        super.process(item);

        Optional<Company> optional = companyRepository.findById(item.getCompanyId());
        if (optional.isEmpty()) throw new ValidationException(String.format("Invalid companyId. Company with id %d does not exist in database!", item.getCompanyId()));

        return item;
    }
}
