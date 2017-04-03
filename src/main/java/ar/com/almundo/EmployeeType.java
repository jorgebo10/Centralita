package ar.com.almundo;

import net.jcip.annotations.Immutable;

@Immutable
public enum EmployeeType {
    OPERATOR(1),
    SUPERVISOR(2),
    DIRECTOR(3);

    private final int priority;

    EmployeeType(final int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
