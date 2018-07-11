package nom.ignorance.component.redis.spring.lock;

public class LockWaitTimeOutException extends Exception {
    public LockWaitTimeOutException() {
    }

    public LockWaitTimeOutException(String message) {
        super(message);
    }

    public LockWaitTimeOutException(String message, Throwable cause) {
        super(message, cause);
    }
}
