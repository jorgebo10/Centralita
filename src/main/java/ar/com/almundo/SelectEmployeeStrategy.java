package ar.com.almundo;

import java.util.Optional;

public interface SelectEmployeeStrategy {

    Optional<Employee> getNextSelectableEmployee();

    void addSelectableEmployee(final Employee employee);
}
