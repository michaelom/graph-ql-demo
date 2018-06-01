package com.senacor.university.graphql.employee;

import com.senacor.university.graphql.project.Project;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
public class Employee {
    private String id;
    private String firstName;
    private String lastName;
    private int age;
    private Gender gender;
    private String projectId;
    private Project project;
}
