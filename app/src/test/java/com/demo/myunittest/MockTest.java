package com.demo.myunittest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.demo.myunittest.model.UuidResponse;
import com.demo.myunittest.util.HttpClientWrapper;
import com.demo.myunittest.util.TestMock;

import org.junit.Test;
import org.mockito.MockedStatic;

import static org.junit.Assert.*;

import java.util.UUID;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.observers.TestObserver;
import okhttp3.Request;

public class MockTest {

    /*
    @Mock private TestMock mockObject;

    @Before
    public void init(){
        MockitoAnnotations.openMocks(this);
    }
     */

    @Test
    public void helloWorld(){
        TestMock mockObject = mock(TestMock.class);
        when(mockObject.echo()).thenReturn("changed mock value");
        assertEquals( "changed mock value",mockObject.echo() );
    }

    @Test
    public void helloStatic() {
        try (MockedStatic<TestMock> mocked = mockStatic(TestMock.class)) {
            mocked.when(TestMock::staticOutput).thenReturn("changed static mock value");
            assertEquals( "changed static mock value", TestMock.staticOutput() );
        }
    }
    @Test
    public void testResponseJson() {
        try( MockedStatic<HttpClientWrapper> mocked = mockStatic(HttpClientWrapper.class) ){
            HttpClientWrapper instance = mock(HttpClientWrapper.class);
            UuidResponse response = new UuidResponse();
            UUID uuid = UUID.randomUUID();
            response.setUuid(uuid.toString());
            when(instance.ResponseJson(any(), any()))
                    .thenReturn(Single.just(response));
            mocked.when( HttpClientWrapper::getInstance )
                    .thenReturn(instance);
            String url = "https://httpbin.org/uuid";
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            TestObserver<UuidResponse> testObserver = HttpClientWrapper.getInstance().ResponseJson(request, UuidResponse.class)
                    .test();
            testObserver.awaitCount(1);
            testObserver.assertComplete();
            assertEquals(uuid.toString(), testObserver.values().get(0).getUuid());
        }
    }
}
