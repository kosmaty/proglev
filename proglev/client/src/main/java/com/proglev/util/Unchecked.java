package com.proglev.util;

import java.util.concurrent.Callable;

public class Unchecked {

    public static Runnable unchecked(CheckedRunnable c){
        return () -> {
            try {
                c.run();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        };
    }

    public static interface CheckedRunnable {
        void run() throws Exception;
    }
}
