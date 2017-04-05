package ar.com.almundo.callcenter.dispatcher;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

import net.jcip.annotations.Immutable;

@Immutable
public final class Call {
    private final Socket socket;

    public Call(final Socket socket) {
        this.socket = socket;
    }

    public void closeCall() throws IOException {
        socket.close();
    }

    public void send(final byte[] message) throws IOException {
        socket.getOutputStream().write(message);
    }

    public byte[] receive() throws IOException {
        byte[] buff = null;
        socket.getInputStream().read(buff);
        return  buff;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return Objects.equals(socket, call.socket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket);
    }
}
