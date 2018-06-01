package com.senacor.university.graphql.employee;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
public class EmployeeEntity {
    private String id;
    private String firstName;
    private String lastName;
    private int age;
    private Gender gender;
    private String projectId;
}
