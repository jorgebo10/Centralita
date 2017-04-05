package ar.com.almundo.test.integration.server;

import java.util.PriorityQueue;

class NoDuplicatesPriorityQueue<E> extends PriorityQueue<E> {

    @Override
    public boolean offer(E e) {
        boolean isAdded = false;
        if (!super.contains(e)) {
            isAdded = super.offer(e);
        }
        return isAdded;
    }
}