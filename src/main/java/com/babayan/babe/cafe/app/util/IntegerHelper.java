package com.babayan.babe.cafe.app.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Random;

/**
 * Utility methods for data manipulations with integers.
 *
 * @author by artbabayan
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntegerHelper {
    static Random random = new Random();

    /**
     * Checks is positive current number
     */
    public static boolean isPositive(int num) {
        return Math.signum(num) > 0;
    }

    /**
     * Generates random 'count' length integer
     */
    public static int generateRandomNumber(int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++)
            sb.append((char) ('0' + random.nextInt(10)));
        return Integer.parseInt(sb.toString());
    }

}
