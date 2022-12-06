package com.demo.myunittest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.demo.myunittest.model.UuidResponse;
import com.demo.myunittest.util.HttpClientWrapper;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private final CompositeDisposable disposables = new CompositeDisposable();
    TextView tv_message;
    private static final String url = "https://httpbin.org/uuid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tv_message = findViewById(R.id.tv_message);
        findViewById(R.id.button_test).setOnClickListener( v -> requestUuid() );
    }

    public void requestUuid() {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        disposables.add(
            HttpClientWrapper.getInstance().ResponseJson(request, UuidResponse.class)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe( uuid -> this.tv_message.setText(uuid.getUuid())
                            ,throwable -> this.tv_message.setText(throwable.getMessage()))
        );
    }
}