package com.demo.myunittest;

import android.widget.TextView;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;

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
}
