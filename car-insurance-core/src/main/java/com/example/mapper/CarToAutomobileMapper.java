package com.example.mapper;

import com.example.Automobile;
import com.example.Car;
import com.example.enums.AutomobileType;
import com.example.enums.InsuranceType;
import com.example.enums.OdometerUnit;

import java.math.BigDecimal;

public class CarToAutomobileMapper {

    public static Automobile mapCarToAutomobile(Car car,
                                         String licencePlate,
                                         BigDecimal fullInsurancePrice,
                                         BigDecimal insurancePension,
                                         InsuranceType insuranceType,
                                         BigDecimal franchisePrice) {
        return new Automobile(
                0,
                car.getReleaseDate(),
                car.getVinCode(),
                AutomobileType.valueOf(car.getType().toString()),
                car.getOdometerValue(),
                OdometerUnit.valueOf(car.getOdometerUnit().toString()),
                licencePlate,
                fullInsurancePrice,
                insurancePension,
                insuranceType,
                franchisePrice,
                null);
    }
}
