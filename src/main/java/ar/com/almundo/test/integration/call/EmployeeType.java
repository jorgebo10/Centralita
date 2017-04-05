package ar.com.almundo.test.integration.call;

import net.jcip.annotations.Immutable;

@Immutable
enum EmployeeType {
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
