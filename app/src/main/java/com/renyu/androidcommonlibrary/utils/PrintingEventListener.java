package com.renyu.androidcommonlibrary.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 使用说明 https://mp.weixin.qq.com/s/YfHPT-nIYtPmLW91zc6Yyg
 */
public class PrintingEventListener extends EventListener {
    private void printEvent(String name) {
        Log.d("TAGTAGTAG", name);
    }

    @Override
    public void callStart(@NonNull Call call) {
        super.callStart(call);
        printEvent("callStart");
    }

    @Override
    public void dnsStart(@NonNull Call call, @NonNull String domainName) {
        super.dnsStart(call, domainName);
        printEvent("dnsStart");
    }

    @Override
    public void dnsEnd(@NonNull Call call, @NonNull String domainName, @NonNull List<InetAddress> inetAddressList) {
        super.dnsEnd(call, domainName, inetAddressList);
        printEvent("dnsEnd");
    }

    @Override
    public void connectStart(@NonNull Call call, @NonNull InetSocketAddress inetSocketAddress, @NonNull Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        printEvent("connectStart");
    }

    @Override
    public void secureConnectStart(@NonNull Call call) {
        super.secureConnectStart(call);
        printEvent("secureConnectStart");
    }

    @Override
    public void secureConnectEnd(@NonNull Call call, Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        printEvent("secureConnectEnd");
    }

    @Override
    public void connectEnd(@NonNull Call call, @NonNull InetSocketAddress inetSocketAddress, @NonNull Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        printEvent("connectEnd");
    }

    @Override
    public void connectFailed(@NonNull Call call, @NonNull InetSocketAddress inetSocketAddress, @NonNull Proxy proxy, Protocol protocol, @NonNull IOException ioe) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
        printEvent("connectFailed");
    }

    @Override
    public void connectionAcquired(@NonNull Call call, @NonNull Connection connection) {
        super.connectionAcquired(call, connection);
        printEvent("connectionAcquired");
    }

    @Override
    public void connectionReleased(@NonNull Call call, @NonNull Connection connection) {
        super.connectionReleased(call, connection);
        printEvent("connectionReleased");
    }

    @Override
    public void requestHeadersStart(@NonNull Call call) {
        super.requestHeadersStart(call);
        printEvent("requestHeadersStart");
    }

    @Override
    public void requestHeadersEnd(@NonNull Call call, @NonNull Request request) {
        super.requestHeadersEnd(call, request);
        printEvent("requestHeadersEnd");
    }

    @Override
    public void requestBodyStart(@NonNull Call call) {
        super.requestBodyStart(call);
        printEvent("requestBodyStart");
    }

    @Override
    public void requestBodyEnd(@NonNull Call call, long byteCount) {
        super.requestBodyEnd(call, byteCount);
        printEvent("requestBodyEnd");
    }

    @Override
    public void requestFailed(@NonNull Call call, @NonNull IOException ioe) {
        super.requestFailed(call, ioe);
        printEvent("requestFailed");
    }

    @Override
    public void responseHeadersStart(@NonNull Call call) {
        super.responseHeadersStart(call);
        printEvent("responseHeadersStart");
    }

    @Override
    public void responseHeadersEnd(@NonNull Call call, @NonNull Response response) {
        super.responseHeadersEnd(call, response);
        printEvent("responseHeadersEnd");
    }

    @Override
    public void responseBodyStart(@NonNull Call call) {
        super.responseBodyStart(call);
        printEvent("responseBodyStart");
    }

    @Override
    public void responseBodyEnd(@NonNull Call call, long byteCount) {
        super.responseBodyEnd(call, byteCount);
        printEvent("responseBodyEnd");
    }

    @Override
    public void responseFailed(@NonNull Call call, @NonNull IOException ioe) {
        super.responseFailed(call, ioe);
        printEvent("responseFailed");
    }

    @Override
    public void callEnd(@NonNull Call call) {
        super.callEnd(call);
        printEvent("callEnd");
    }

    @Override
    public void callFailed(@NonNull Call call, @NonNull IOException ioe) {
        super.callFailed(call, ioe);
        printEvent("callFailed");
    }
}
