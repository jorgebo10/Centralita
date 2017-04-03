package ar.com.almundo;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
class Dispatcher {

    private static final long TEN_SECONDS = 10000000000L;

    private final Queue<Employee> availableEmployeesQueue;
    private final ExecutorService executorService;
    private final static Logger logger = Logger.getLogger(Dispatcher.class.getName());

    public Dispatcher(final Queue<Employee> queue, final ExecutorService executorService) {
        this.availableEmployeesQueue = new PriorityBlockingQueue<>(queue);
        this.executorService = executorService;
    }

    public void dispatchCall(final Call call) {
        final Optional<Employee> employee = Optional.ofNullable(this.availableEmployeesQueue.poll());

        if (employee.isPresent()) {
            long endNanos = System.nanoTime() + TEN_SECONDS;
            final Future<Employee> future = this.executorService.submit(() -> handleCallToEmployee(call, employee.get()));
            long timeLeft = endNanos - System.nanoTime();
            try {
                future.get(timeLeft, TimeUnit.NANOSECONDS);
            } catch (InterruptedException | ExecutionException e) {
                logger.log(Level.SEVERE, "Execution exception", e);
            } catch (final TimeoutException e) {
                future.cancel(true);
                logger.log(Level.WARNING, "Call exceded maximum allow timeout", e);
            } finally {
                availableEmployeesQueue.add(employee.get());
            }
        } else {
            handleNoEmployeesAvailable();
        }
    }

    private Employee handleCallToEmployee(final Call call, final Employee employee) {
        employee.assignCall(call);

        //employee.sayHello('Hello')
        logger.log(Level.INFO, "Employee starting conversation with client...:)");

        //String response = employee.waitForResponse()
        logger.log(Level.INFO, "Employee waiting for response from client...:|");

        randomDelay(2, 5);

        //employee.sayGoodbye("Bye")
        logger.log(Level.INFO, "Employee stopping conversation with client...:(!");

        return employee;
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