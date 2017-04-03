package ar.com.almundo;

import java.net.Socket;
import java.util.Objects;

import net.jcip.annotations.Immutable;

@Immutable
public class Call extends Socket {
    private final Socket socket;
    private final Request request;

    public static Call newCallOnSocketWithRequest(final Socket socket, final Request req) {
        return new Call(socket, req);
    }

    private Call(final Socket socket, final Request request) {
        this.socket = socket;
        this.request = request;
    }

    public Socket getSocket() {
        //warning> Socket internal might be changed
        return socket;
    }

    public Request getRequest() {
        return request;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return Objects.equals(socket, call.socket)
                && Objects.equals(request, call.request);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, request);
    }
}
