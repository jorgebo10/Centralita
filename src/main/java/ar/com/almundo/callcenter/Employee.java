package ar.com.almundo.callcenter;

import java.io.IOException;
import java.util.Objects;

import ar.com.almundo.callcenter.dispatcher.Call;
import ar.com.almundo.callcenter.utils.Talk;
import ar.com.almundo.callcenter.utils.TimeScheduleTalk;

public class Employee implements Comparable<Employee> {
    private final Long id;

    private final String name;

    private EmployeeType employeeType;

    private Call call;

    private Talk talk;

    private Employee(final String name, final Long id, EmployeeType employeeType) {
        this.name = name;
        this.id = id;
        this.employeeType = employeeType;
        talk = new TimeScheduleTalk(1, 5);
    }

    public static Employee newDirector(final String name, final Long id) {
        return new Employee(name, id, EmployeeType.DIRECTOR);
    }

    public static Employee newOperario(final String name, final Long id) {
        return new Employee(name, id, EmployeeType.OPERATOR);
    }

    public static Employee newSupervisor(final String name, final Long id) {
        return new Employee(name, id, EmployeeType.SUPERVISOR);
    }

    public void setTalk(Talk talk) {
        this.talk = talk;
    }

    public Call getCall() {
        return call;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Employee assignCall(final Call call) {
        Objects.requireNonNull(call);
        this.call = call;
        return this;
    }

    public void startTalking() {
        talk.talk();
    }

    public void hangout() throws IOException {
        Objects.requireNonNull(call);
        this.call.closeCall();
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

     public int getEmployeePriority() {
         return employeeType.getPriority();
     }

    public boolean isOperator() {
        return employeeType == EmployeeType.OPERATOR;
    }

    public boolean isSupervisor() {
        return employeeType == EmployeeType.SUPERVISOR;
    }

    public boolean isDirector() {
        return employeeType == EmployeeType.DIRECTOR;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Employee{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", employeeType=").append(employeeType);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Employee anotherEmployee) {
        return this.getEmployeePriority() >= anotherEmployee.getEmployeePriority() ? 1 : -1;
    }
}