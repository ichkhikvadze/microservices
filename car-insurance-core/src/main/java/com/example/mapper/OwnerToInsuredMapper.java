package com.example.mapper;

import com.example.Insured;
import com.example.Owner;

public class OwnerToInsuredMapper {

    public static Insured mapOwnerToInsured(Owner owner) {
        return new Insured(
                0,
                owner.getFirstName(),
                owner.getLastName(),
                owner.getBirthDate(),
                owner.getPersonalNo()
        );
    }
}
