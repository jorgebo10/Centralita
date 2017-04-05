package ar.com.almundo.callcenter.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Objects;

import net.jcip.annotations.Immutable;

@Immutable
final class Request {
    private final String message;
    // TODO: Add other fields according to application protocol

    public static Request newRequestFromSocket(Socket socket) throws IOException {
        final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        // TODO: Parse message according to an application protocol
        return new Request(in.readLine());
    }

    private Request(final String message) {
        Objects.requireNonNull(message);
        this.message = message;
    }

    public boolean isShutdownRequest() {
        return message.equals("shutdown");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return Objects.equals(message, request.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Request{");
        return sb.append("message='")
                .append(message)
                .append('\'')
                .append('}')
                .toString();
    }
}
