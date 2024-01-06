package io.github.emreblbl.springbatch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerInput {

    private String id;

    private String firstName;

    private String lastName;

    private String gender;

    private String birthday;

    private String address;

    private String country;

}
