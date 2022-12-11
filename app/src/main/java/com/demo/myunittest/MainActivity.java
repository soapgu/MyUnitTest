package com.demo.myunittest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.demo.myunittest.hilt.SampleSingletonClass;
import com.demo.myunittest.model.UuidResponse;
import com.demo.myunittest.util.HttpClientWrapper;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import okhttp3.Request;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    protected SampleSingletonClass singletonClass;

    private final CompositeDisposable disposables = new CompositeDisposable();
    TextView tv_message;
    private static final String url = "https://httpbin.org/uuid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.tv_message = findViewById(R.id.tv_message);
        findViewById(R.id.button_hilt).setOnClickListener( v -> this.tv_message.setText( singletonClass.echo() ) );
        findViewById(R.id.button_test).setOnClickListener( v -> this.requestUuid());
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