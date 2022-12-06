package com.demo.myunittest.util;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import io.reactivex.rxjava3.core.Single;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpClientWrapper {
    private static volatile HttpClientWrapper instance;
    private  OkHttpClient client;


    private HttpClientWrapper()
    {
        client = new OkHttpClient();
    }

    public static HttpClientWrapper getInstance()
    {
        if( instance == null ) {
            synchronized (HttpClientWrapper.class) {
                if( instance == null ){
                    instance = new HttpClientWrapper();
                }
            }
        }
        return instance;
    }

    public <T> Single<T> ResponseJson(Request request , Class<T> classOfT) {
        Call call = client.newCall(request);
        return Single.create(
                emitter -> {
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            emitter.onError(e);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) {
                            try (ResponseBody body = response.body()) {
                                if (response.isSuccessful()) {
                                    try {
                                        assert body != null;
                                        String json = body.string();
                                        Gson gson = new Gson();
                                        T jsonObj = gson.fromJson(json, classOfT);
                                        emitter.onSuccess(jsonObj);
                                    } catch (Exception e) {
                                        emitter.onError(e);
                                    }
                                } else {
                                    emitter.onError(new Exception(String.format("error state code: %s", response.code())));
                                }
                            } catch (Exception e) {
                                emitter.onError(e);
                            }
                        }
                    });
                    emitter.setCancellable(call::cancel);
                }
        );

    }
}
