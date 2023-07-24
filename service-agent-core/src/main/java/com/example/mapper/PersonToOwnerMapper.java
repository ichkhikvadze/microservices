package com.example.mapper;

import com.example.Owner;
import com.example.domain.Person;

public class PersonToOwnerMapper {

    public static Owner mapPersonToOwner(Person person) {
        return new Owner(0,
                person.getFirstName(),
                person.getLastName(),
                person.getBirthDate(),
                person.getPersonalNo()
        );
    }
}
