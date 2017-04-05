package ar.com.almundo;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
class Dispatcher {

    private static final long TEN_SECONDS = 10L;

    private final SelectEmployeeStrategy selectEmployeeStrategy;
    private final ExecutorService executorService;
    private final ScheduledExecutorService timeoutService;
    private final static Logger logger = Logger.getLogger(Dispatcher.class.getName());

    public Dispatcher(final SelectEmployeeStrategy selectEmployeeStrategy, final ExecutorService executorService) {
        this.selectEmployeeStrategy = selectEmployeeStrategy;
        this.executorService = executorService;
        this.timeoutService = Executors.newSingleThreadScheduledExecutor();
    }

    public Optional<Future<Employee>> dispatchCall(final Call call) {
        final Optional<Employee> employee = selectEmployeeStrategy.getNextSelectableEmployee();

        if (employee.isPresent()) {
            return Optional.of(handleCallToEmployee(call, employee.get()));
        } else {
            handleNoEmployeesAvailable();
            return Optional.empty();
        }
    }

    private Future<Employee> handleCallToEmployee(final Call call, final Employee employee) {
        final Future future = this.executorService.submit((Callable<Void>) () -> {
            dispatchCallToEmployee(call, employee);
            return null;
        });
        this.timeoutService.schedule((Callable<Void>) () -> {
            restoreEmployeeOnTimeout(employee, future);
            return null;
        }, TEN_SECONDS, TimeUnit.SECONDS);
        return future;
    }

    private void restoreEmployeeOnTimeout(Employee employee, Future future) {
        future.cancel(true);
        selectEmployeeStrategy.addSelectableEmployee(employee);
    }


    private void dispatchCallToEmployee(final Call call, final Employee employee) {
        employee.assignCall(call);

        logger.log(Level.INFO, Thread.currentThread().getName() + " => " + employee.toString());

        logger.log(Level.INFO, "Employee starting conversation with client...:)");

        logger.log(Level.INFO, "Employee waiting for response from client...:|");

        randomDelay(5, 11);

        logger.log(Level.INFO, "Employee stopping conversation with client...:(!");

        try {
            call.getSocket().close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not close socket");
        } finally {
            logger.log(Level.INFO, Thread.currentThread().getName() + " <= " + employee.toString());
            selectEmployeeStrategy.addSelectableEmployee(employee);
        }
    }

    private void randomDelay(float min, float max) {
        int random = (int) (max * Math.random() + min);
        try {
            Thread.sleep(random * 1000);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Interrupted exception", e);
        }
    }

    private void handleNoEmployeesAvailable() {
        logger.log(Level.WARNING, "No available employees");
    }
}