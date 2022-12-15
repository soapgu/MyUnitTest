package com.demo.myunittest;

import static org.junit.Assert.assertEquals;

import android.widget.TextView;

import com.demo.myunittest.hilt.SampleSingletonClass;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;

@RunWith(RobolectricTestRunner.class)
@HiltAndroidTest
@Config(application = HiltTestApplication.class)
public class HiltTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Inject
    SampleSingletonClass singletonClass;

    @Test
    public void testSingleton() {
        Assert.assertNull(singletonClass);
        hiltRule.inject();
        Assert.assertNotNull( singletonClass );
        Assert.assertEquals( "mocked hilt object echo",singletonClass.echo() );
    }

    @Test
    public void testActivity(){
        try (ActivityController<MainActivity> controller = Robolectric.buildActivity(MainActivity.class)) {
            controller.setup();
            MainActivity activity = controller.get();
            Assert.assertNotNull("inject single in Activity",activity.singletonClass);
            Assert.assertEquals( "mocked hilt object echo",activity.singletonClass.echo() );
        }
    }
}
