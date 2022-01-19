package xyz.thinktest.fastestapi.http.metadata;

/**
 *Consumer
 */
@FunctionalInterface
public interface HttpSendable<In1, In2> {

    /**
     * http send request
     */
    void run(In1 in1, In2 in2);
}
