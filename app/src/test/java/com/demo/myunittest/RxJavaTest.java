package com.demo.myunittest;

import com.demo.myunittest.model.UuidResponse;
import com.demo.myunittest.util.HttpClientWrapper;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;
import io.reactivex.rxjava3.schedulers.TestScheduler;
import okhttp3.Request;
import static org.junit.Assert.*;

public class RxJavaTest {
    @Test
    public void helloWorld(){
        Single.timer(1, TimeUnit.SECONDS)
                .test()
                .awaitCount(1)
                .assertValue(0L);
    }

    @Test
    public void testHttp() {
        String url = "https://httpbin.org/uuid";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        TestObserver<UuidResponse> testObserver = HttpClientWrapper.getInstance().ResponseJson(request, UuidResponse.class)
                .test();
        testObserver.assertNoValues();
        testObserver.awaitCount(1);
        testObserver.assertComplete();
        assertEquals( 36 ,testObserver.values().get(0).getUuid().length() );
    }

    @Test
    public void testByScheduler() {
        final TestScheduler testScheduler = new TestScheduler();
        TestObserver<Long> testObserver = Observable
                .intervalRange(1,5,0,5,TimeUnit.SECONDS,testScheduler)
                .test();
        testObserver.assertNoValues();
        testScheduler.advanceTimeBy(20,TimeUnit.SECONDS);
        testObserver.assertResult(1L,2L,3L,4L,5L);
    }
}
