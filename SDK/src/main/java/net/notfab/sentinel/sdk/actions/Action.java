package net.notfab.sentinel.sdk.actions;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class Action {

    private static final ExecutorService service = Executors.newFixedThreadPool(30);
    private final ActionRequest request = new ActionRequest();

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private ActionResponse response = new EmptyResponse();

    public Action(String methodName) {
        this.request.setMethod(methodName.toLowerCase());
    }

    void signal(ActionResponse response) {
        this.response = response;
        lock.lock();
        this.condition.signalAll();
        lock.unlock();
    }

    // ----------- Usage

    public void queue() {
        ActionRegistry.getInstance().send(this.request);
    }

    public <T> void queue(Consumer<T> consumer) {
        ActionRegistry.getInstance().ask(this.request, this);
        service.submit(() -> {
            lock.lock();
            try {
                this.condition.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                lock.unlock();
            }
            consumer.accept(this.response.get());
        });
    }

    public <T> void queueList(Consumer<List<T>> consumer) {
        ActionRegistry.getInstance().ask(this.request, this);
        service.submit(() -> {
            lock.lock();
            try {
                this.condition.await(30, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                lock.unlock();
            }
            consumer.accept(this.response.getList());
        });
    }

    /**
     * Sends the Action and blocks the thread for 5 seconds or until a reply.
     *
     * @param <T> - Type.
     * @return Response.
     */
    public <T> T await() {
        return this.await(5, TimeUnit.SECONDS);
    }

    /**
     * Sends the Action and blocks the thread for ? seconds or until a reply.
     *
     * @param <T>      - Type.
     * @param timeout  - Timeout.
     * @param timeUnit - Timeout Unit.
     * @return Response.
     */
    public <T> T await(long timeout, TimeUnit timeUnit) {
        ActionRegistry
                .getInstance().ask(this.request, this);
        lock.lock();
        try {
            this.condition.await(timeout, timeUnit);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response.get();
    }

    public <T> List<T> awaitList() {
        return this.awaitList(5, TimeUnit.SECONDS);
    }

    public <T> List<T> awaitList(long timeout, TimeUnit timeUnit) {
        ActionRegistry
                .getInstance().ask(this.request, this);
        lock.lock();
        try {
            this.condition.await(timeout, timeUnit);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response.getList();
    }

    // -- Requesting

    public Action put(String name, Object value) {
        request.getParameters().put(name.toLowerCase(), value);
        return this;
    }

    public static Action from(String methodName) {
        return new Action(methodName.toLowerCase());
    }

}