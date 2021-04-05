package com.example.catsanddogs.sdk;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DebouncedRunnableTest  {
    private DebouncedRunnable debouncedRunnable;
    private static final long DELAY_MILLIS = 2000L;
    private static final int[] EXPECTED_RESULTS = {3, 1, 10};
    private int count;

    @Before
    @SuppressWarnings("unchecked")
    public void before() {
        this.debouncedRunnable = new DebouncedRunnable(
                new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        System.out.println("count = " + count);
                    }
                },
                "CATS_AND_DOGS_UNIT_TEST",
                DELAY_MILLIS
        );
    }

    @Test (timeout = 60000)
    public void testDebounceEmits() {
        int i = 0;
        count = 0;
        String testName = "testDebounceEmits";
        System.out.println("--------" + testName + " started--------");

        while (i < 10) {
            /*
             * Call debouncedRunnable.run() 10 times in 5000ms, and expected the count variable just print 3 times at time = 0, time = 2000 and time = 4000ms
             */
            try {
                this.debouncedRunnable.run();
                // call the function each 500ms
                Thread.sleep(500L);
                i++;
                System.out.println(testName + ": call debouncedRunnable.run() " + i + " times");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertTrue(count == EXPECTED_RESULTS[0]);
        System.out.println("--------" + testName + " completed--------");
    }

    @Test (timeout = 60000)
    public void testDebounceEmitsJustOneTime() {
        int i = 0;
        count = 0;
        String testName = "testDebounceEmitsJustOneTime";
        System.out.println("--------" + testName + " started--------");

        while (i < 10) {
            /*
             * Call debouncedRunnable.run() 10 times in 1000ms, and expected the count variable just print only 1 times at time = 0
             */
            try {
                this.debouncedRunnable.run();
                // call the function each 100ms
                Thread.sleep(100L);
                i++;
                System.out.println(testName + ": call debouncedRunnable.run() " + i + " times");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertTrue(count == EXPECTED_RESULTS[1]);
        System.out.println("--------" + testName + " completed--------");
    }

    @Test (timeout = 60000)
    public void testDebounceEmitsNormallyEveryTwoSecs() {
        int i = 0;
        count = 0;
        String testName = "testDebounceEmitsNormallyAfterTwoSecs";
        System.out.println("--------" + testName + " started--------");

        while (i < 10) {
            /*
             * Call debouncedRunnable.run() 10 times in 25000ms, and expected the count variable just print 10 times at time = 0, 2000, 4000, 6000...
             */
            try {
                this.debouncedRunnable.run();
                //make enough time for next emission
                Thread.sleep(2500L);
                i++;
                System.out.println(testName + ": call debouncedRunnable.run() " + i + " times");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertTrue(count == EXPECTED_RESULTS[2]);
        System.out.println("--------" + testName + " completed--------");
    }

}