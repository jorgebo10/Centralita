package ar.com.almundo.test.integration.server;

import static ar.com.almundo.test.integration.call.Employee.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ar.com.almundo.test.integration.call.Employee;
import ar.com.almundo.test.integration.call.dispatcher.Call;
import ar.com.almundo.test.integration.call.dispatcher.Dispatcher;
import ar.com.almundo.test.integration.call.dispatcher.SelectEmployeeByPriorityStrategy;
import ar.com.almundo.test.integration.call.dispatcher.SelectEmployeeStrategy;

public class WebServer {
    private final static Logger logger = Logger.getLogger(WebServer.class.getName());
    private final ExecutorService executorService;
    private final Dispatcher dispatcher;

    public WebServer(final Dispatcher dispatcher, final ExecutorService executorService) {
        this.dispatcher = dispatcher;
        this.executorService = executorService;
    }

    public void stop() {
        executorService.shutdown();
    }

    public void start() throws IOException {
        final ServerSocket serverSocket = new ServerSocket(8080);
        logger.info("Listening on port 8080");

        while (!executorService.isShutdown()) {
            final Socket socket = serverSocket.accept();
            logger.info("Connection accepted");

            Request req  = Request.newRequestFromSocket(socket);
            if (req.isShutdownRequest()) {
                stop();
                return;
            }

            try {
                dispatcher.dispatchCall(new Call(socket));
            } catch (final RejectedExecutionException e) {
                handleRejectedExecution(e);
            }
        }
    }

    private void handleRejectedExecution(final RejectedExecutionException e) {
        if (!executorService.isShutdown()) {
            logger.log(Level.SEVERE, "task submission rejected", e);
        }
    }


    private static Queue<Employee> getAvailableEmployees() {
        final Queue<Employee> availableEmployees = new NoDuplicatesPriorityQueue<>();
        availableEmployees.add(newOperario("Pedro", 1L));
        availableEmployees.add(newOperario("Pablo", 2L));
        availableEmployees.add(newSupervisor("Vilma", 3L));
        availableEmployees.add(newDirector("Betty", 4L));
        return availableEmployees;
    }

    public static void main(final String[] args) throws IOException {
        //let's limit the number of concurrent tasks for resource-management purposes,
        // as in a server application that accepts requests from network
        // clients and would otherwise be vulnerable to overload.
        //TODO: pass thread limit as a parameter
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final SelectEmployeeStrategy selectEmployeeStrategy =
                new SelectEmployeeByPriorityStrategy(getAvailableEmployees());
        final Dispatcher dispatcher = new Dispatcher(selectEmployeeStrategy, executorService);
        new WebServer(dispatcher, executorService).start();
    }
}
