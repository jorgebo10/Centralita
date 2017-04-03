package ar.com.almundo;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
class Dispatcher {
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
            this.executorService.execute(() -> handleCallToEmployee(call, employee.get()));
        } else {
            handleNoEmployeesAvailable();
        }
    }

    private void handleCallToEmployee(final Call call, final Employee employee) {
        employee.assignCall(call);

        //employee.sayHello('Hello')
        logger.log(Level.INFO, "Employee starting conversation with client...:)");

        //String response = employee.waitForResponse()
        logger.log(Level.INFO, "Employee waiting for response from client...:|");

        randomDelay(2, 5);

        //employee.sayGoodbye("Bye")
        logger.log(Level.INFO, "Employee stopping conversation with client...:(!");

        availableEmployeesQueue.add(employee);
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