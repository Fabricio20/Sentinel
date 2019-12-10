package net.notfab.sentinel.sdk.rpc;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RPCAction {

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private RPCResponse response;

    void signal(RPCResponse response) {
        this.response = response;
        lock.lock();
        this.condition.signalAll();
        lock.unlock();
    }

    public RPCResponse complete() {
        return this.complete(5, TimeUnit.SECONDS);
    }

    public RPCResponse complete(long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            this.condition.await(timeout, timeUnit);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response;
    }

}