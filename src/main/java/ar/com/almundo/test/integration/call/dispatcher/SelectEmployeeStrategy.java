package ar.com.almundo.test.integration.call.dispatcher;

import java.util.Optional;

import ar.com.almundo.test.integration.call.Employee;

public interface SelectEmployeeStrategy {

    Optional<Employee> getNextSelectableEmployee();

    void addSelectableEmployee(final Employee employee);
}
