package com.demo.myunittest;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.TestScheduler;

public class RxJavaTest {
    @Test
    public void helloWorld(){
        Single.timer(1, TimeUnit.SECONDS)
                .test()
                .awaitCount(1)
                .assertValue(0L);
    }

    @Test
    public void testByScheduler() {
        final TestScheduler testScheduler = new TestScheduler();
        TestObserver<Long> testObserver = Observable
                .intervalRange(1,5,0,5,TimeUnit.SECONDS,testScheduler)
                .test();
        testObserver.assertNoValues();
        testScheduler.advanceTimeBy(25,TimeUnit.SECONDS);
        testObserver.assertResult(1L,2L,3L,4L,5L);
    }
}
