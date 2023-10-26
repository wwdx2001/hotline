package com.sh3h.dataprovider.data.remote;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.sh3h.dataprovider.data.BusEvent;
import com.squareup.otto.Bus;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;


public class UnauthorisedInterceptor implements Interceptor {
    private Bus mBus;

    public UnauthorisedInterceptor(Bus bus) {
        mBus = bus;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        try {
            response = chain.proceed(chain.request());
            if (response.code() == 401) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mBus.post(new BusEvent.AuthenticationError());
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
