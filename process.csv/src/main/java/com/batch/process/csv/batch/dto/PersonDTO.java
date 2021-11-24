package com.batch.process.csv.batch.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PersonDTO {
    private String firstName;
    private String lastName;

    @Max(value = 50, message = "Age must be under int 50.")
    private Integer age;
    private String email;

    @NotNull
    private Long companyId;

    @Override
    public String toString() {
        return "PersonDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", companyId=" + companyId +
                '}';
    }

    public PersonDTO() {
    }


}
