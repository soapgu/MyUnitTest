package com.demo.myunittest;

import android.os.Looper;
import android.widget.TextView;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import com.demo.myunittest.model.UuidResponse;
import com.demo.myunittest.util.HttpClientWrapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

import java.util.UUID;

import io.reactivex.rxjava3.core.Single;

@RunWith(RobolectricTestRunner.class)
public class RobolectricTest {

    @Test
    public void helloWorld(){
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();
            TextView textView = activity.findViewById(R.id.tv_message);
            assertEquals( "Hello World!",textView.getText() );
        }
    }

    @Test
    public void testClickAndMockResponse(){
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            try( MockedStatic<HttpClientWrapper> mocked = mockStatic(HttpClientWrapper.class) ) {
                HttpClientWrapper instance = mock(HttpClientWrapper.class);
                UuidResponse response = new UuidResponse();
                UUID uuid = UUID.randomUUID();
                response.setUuid(uuid.toString());
                when(instance.ResponseJson(any(), any()))
                        .thenReturn(Single.just(response));
                mocked.when(HttpClientWrapper::getInstance)
                        .thenReturn(instance);
                MainActivity activity = controller.get();
                TextView textView = activity.findViewById(R.id.tv_message);
                activity.findViewById(R.id.button_test).performClick();
                shadowOf(Looper.getMainLooper()).idle();
                assertEquals( uuid.toString(),textView.getText() );
            }
        }
    }
}
