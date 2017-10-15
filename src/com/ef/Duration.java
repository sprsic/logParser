package com.ef;

/**
 * Written with love
 *
 * @author Sasa Prsic 13/10/2017
 */
public enum Duration {
    HOURLY("hourly") {
        @Override
        int getDuration() {
            return 1;
        }
    },
    DAILY("daily") {
        @Override
        int getDuration() {
            return 24;
        }
    };

    private final String duration;

    abstract int getDuration();

    Duration(String duration) {
        this.duration = duration;
    }

    public static Duration forCode(String code) {
        for (Duration d : Duration.values()) {
            if (d.duration.equals(code)) {
                return d;
            }
        }
        throw new IllegalArgumentException("The provided argument " + code + " not recognisable!");
    }
}
