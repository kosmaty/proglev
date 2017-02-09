package com.proglev.util;

import java.util.concurrent.Callable;

public class Unchecked {

    public static Runnable unchecked(Callable c){
        return () -> {
            try {
                c.call();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        };
    }
}
