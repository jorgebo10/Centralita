package ar.com.almundo.callcenter.dispatcher;

import java.util.Optional;

import ar.com.almundo.callcenter.Employee;

public interface SelectEmployeeStrategy {

    Optional<Employee> getNextSelectableEmployee();

    void addSelectableEmployee(final Employee employee);
}
