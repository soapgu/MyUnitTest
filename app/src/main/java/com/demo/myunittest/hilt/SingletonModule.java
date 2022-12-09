package com.demo.myunittest.hilt;



import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class SingletonModule {
    @Provides
    public SampleSingletonClass provideObject(){
        return new SampleSingletonClass();
    }
}
