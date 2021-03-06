package com.senacor.university.graphql.domain.employee;

import com.coxautodev.graphql.tools.GraphQLResolver;
import com.senacor.university.graphql.domain.project.Project;
import com.senacor.university.graphql.domain.project.ProjectRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class EmployeeResolver implements GraphQLResolver<Employee> {

    private ProjectRepository repository;

    public EmployeeResolver(ProjectRepository repository) {
        this.repository = repository;
    }

    public Optional<Project> project(Employee employee) {
        return repository.findById(employee.getProjectId());
    }

    public Email email(Employee employee, EmailType emailType) {
        Email businessEmail = employee.getEmail();
        switch (emailType) {
            case PRIVATE:
                String address = businessEmail.getValue();
                return Email.from(address.replace("@senacor.com", "@googlemail.com"));
            case BUSINESS:
                return businessEmail;
            default: throw new AssertionError("unreachable");
        }
    }
}