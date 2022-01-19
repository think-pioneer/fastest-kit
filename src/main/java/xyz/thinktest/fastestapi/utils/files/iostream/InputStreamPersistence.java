package xyz.thinktest.fastestapi.utils.files.iostream;

import java.util.Objects;

/**
 * @Date: 2021/12/9
 */
@FunctionalInterface
interface InputStreamPersistence<In> {
    void to(In in);

    default InputStreamPersistence<In> from(InputStreamPersistence<? super In> after){
        Objects.requireNonNull(after);
        return (In in) -> {to(in); after.to(in);};
    }
}
