package ar.com.almundo.callcenter.dispatcher;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.almundo.callcenter.Employee;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class Dispatcher {

    private static final long TEN_SECONDS_TIMEOUT = 10L;

    private final SelectEmployeeStrategy selectEmployeeStrategy;
    private final ExecutorService executorService;
    private final ScheduledExecutorService timeoutService;
    private final static Logger logger = Logger.getLogger(Dispatcher.class.getName());

    public Dispatcher(final SelectEmployeeStrategy selectEmployeeStrategy, final ExecutorService executorService) {
        this(selectEmployeeStrategy, executorService, Executors.newSingleThreadScheduledExecutor());
    }

    public Dispatcher(final SelectEmployeeStrategy selectEmployeeStrategy, final ExecutorService executorService,
                      final ScheduledExecutorService timeoutService) {
        this.selectEmployeeStrategy = selectEmployeeStrategy;
        this.executorService = executorService;
        this.timeoutService = timeoutService;
    }

    public Optional<Future<Employee>> dispatchCall(final Call call) {
        final Optional<Employee> employee = selectEmployeeStrategy.getNextSelectableEmployee();

        if (employee.isPresent()) {
            return Optional.of(handleCallToEmployee(employee.get().assignCall(call)));
        } else {
            handleNoEmployeesAvailable();
            return Optional.empty();
        }
    }

    private Future<Employee> handleCallToEmployee(final Employee employee) {
        final Future<Employee> future = this.executorService.submit(() -> {
            dispatchCallToEmployee(employee);
            return employee;
        });
        this.timeoutService.schedule(() -> {
            restoreEmployeeOnTimeout(employee, future);
            return employee;
        }, TEN_SECONDS_TIMEOUT, TimeUnit.SECONDS);
        return future;
    }

    private void restoreEmployeeOnTimeout(Employee employee, Future future) {
        future.cancel(true);
        selectEmployeeStrategy.addSelectableEmployee(employee);
    }


    private void dispatchCallToEmployee(final Employee employee) {

        logger.log(Level.INFO, Thread.currentThread().getName() + " => " + employee.toString());

        employee.startTalking();

        try {
            employee.hangout();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not close socket");
        } finally {
            logger.log(Level.INFO, Thread.currentThread().getName() + " <= " + employee.toString());
            selectEmployeeStrategy.addSelectableEmployee(employee);
        }
    }

    private void handleNoEmployeesAvailable() {
        logger.log(Level.WARNING, "No available employees");
    }
}