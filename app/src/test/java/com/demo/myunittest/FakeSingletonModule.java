package com.demo.myunittest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.demo.myunittest.hilt.SampleSingletonClass;
import com.demo.myunittest.hilt.SingletonModule;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.testing.TestInstallIn;

@Module
@TestInstallIn(components = SingletonComponent.class,
replaces = SingletonModule.class)
public class FakeSingletonModule {
    @Provides
    public SampleSingletonClass provideObject(){
        SampleSingletonClass retValue = mock(SampleSingletonClass.class);
        when(retValue.echo()).thenReturn("mocked hilt object echo");
        return retValue;
    }
}
