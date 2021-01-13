package com.renyu.commonlibrary.network;

import com.renyu.commonlibrary.network.other.ProgressRequestBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by renyu on 15/10/14.
 */
public class OKHttpUtils {
    private OkHttpClient okHttpClient;

//    private HashMap<String, List<Cookie>> cookieStore;

    protected OKHttpUtils(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    protected OKHttpUtils() {
//        cookieStore = new HashMap<>();
        OkHttpClient.Builder okbuilder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
//                .addInterceptor(new LogInterceptor())
//                .cookieJar(new CookieJar() {
//                    @Override
//                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//                        cookieStore.put(url.host(), cookies);
//                    }
//
//                    @Override
//                    public List<Cookie> loadForRequest(HttpUrl url) {
//                        List<Cookie> cookies = cookieStore.get(url.host());
//                        return cookies != null ? cookies : new ArrayList<Cookie>();
//                    }
//                });
//        setCertificates(okbuilder);
        //https默认信任全部证书
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        okbuilder.hostnameVerifier((hostname, session) -> true).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        okHttpClient = okbuilder.build();
    }

    public interface RequestListener {
        void onStart();

        void onSuccess(String string);

        void onError();
    }

    public interface ProgressListener {
        void updateprogress(int progress, long bytesRead, long contentLength);
    }

    public interface OnDownloadListener {
        void onSuccess(String filePath);

        void onError();
    }

    /**
     * 提供OkHttpClient对象
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * get数据准备
     *
     * @param url
     * @param headers
     * @return
     */
    public Call getPrepare(String url, HashMap<String, String> headers) {
        Request.Builder req_builder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            Iterator<Map.Entry<String, String>> header_it = headers.entrySet().iterator();
            while (header_it.hasNext()) {
                Map.Entry<String, String> header_en = header_it.next();
                req_builder.addHeader(header_en.getKey(), header_en.getValue());
            }
        }
        Request request = req_builder.url(url).tag(url).build();
        return okHttpClient.newCall(request);
    }

    /**
     * get请求
     *
     * @param url
     * @param headers
     * @param requestListener
     */
    public void asyncGet(String url, HashMap<String, String> headers, final RequestListener requestListener) {
        if (requestListener != null) {
            requestListener.onStart();
        }
        Call call = getPrepare(url, headers);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestListener != null) {
                    requestListener.onError();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (requestListener != null) {
                    if (response.isSuccessful()) {
                        if (response.body() == null) {
                            requestListener.onError();
                        } else {
                            requestListener.onSuccess(response.body().string());
                        }

                    } else {
                        requestListener.onError();
                    }
                }
            }
        });
    }

    public void asyncGet(String url, HashMap<String, String> headers) {
        asyncGet(url, headers, null);
    }

    public void asyncGet(String url, RequestListener requestListener) {
        asyncGet(url, null, requestListener);
    }

    public void asyncGet(String url) {
        asyncGet(url, new HashMap<>());
    }

    public Response syncGet(String url, HashMap<String, String> headers) {
        Call call = getPrepare(url, headers);
        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                return null;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response syncGet(String url) {
        return syncGet(url, null);
    }

    /**
     * post数据准备
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    private Call postPrepare(String url, HashMap<String, String> params, HashMap<String, String> headers) {
        FormBody.Builder builder = new FormBody.Builder();
        Iterator<Map.Entry<String, String>> params_it = params.entrySet().iterator();
        while (params_it.hasNext()) {
            Map.Entry<String, String> params_en = params_it.next();
            builder.add(params_en.getKey(), params_en.getValue());
        }
        Request.Builder req_builder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            Iterator<Map.Entry<String, String>> header_it = headers.entrySet().iterator();
            while (header_it.hasNext()) {
                Map.Entry<String, String> header_en = header_it.next();
                req_builder.addHeader(header_en.getKey(), header_en.getValue());
            }
        }
        Request request = req_builder.url(url).tag(url).post(builder.build()).build();
        return okHttpClient.newCall(request);
    }

    /**
     * post方式提交json数据
     *
     * @param url
     * @param json
     * @return
     */
    private Call postJsonPrepare(String url, String json, HashMap<String, String> headers) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder req_builder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            Iterator<Map.Entry<String, String>> header_it = headers.entrySet().iterator();
            while (header_it.hasNext()) {
                Map.Entry<String, String> header_en = header_it.next();
                req_builder.addHeader(header_en.getKey(), header_en.getValue());
            }
        }
        Request request = req_builder.url(url).post(body).build();
        return okHttpClient.newCall(request);
    }

    public void asyncPostJson(String url, String json, HashMap<String, String> headers, final RequestListener requestListener) {
        if (requestListener != null) {
            requestListener.onStart();
        }
        Call call = postJsonPrepare(url, json, headers);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestListener != null) {
                    requestListener.onError();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (requestListener != null) {
                    if (response.isSuccessful()) {
                        if (response.body() == null) {
                            requestListener.onError();
                        } else {
                            requestListener.onSuccess(response.body().string());
                        }

                    } else {
                        requestListener.onError();
                    }
                }
            }
        });
    }

    public void asyncPostJson(String url, String json, HashMap<String, String> headers) {
        asyncPostJson(url, json, headers, null);
    }

    public void asyncPostJson(String url, String json, RequestListener requestListener) {
        asyncPostJson(url, json, null, requestListener);
    }

    public void asyncPostJson(String url, String json) {
        asyncPostJson(url, json, null, null);
    }

    public Response syncPostJson(String url, String json, HashMap<String, String> headers) {
        Call call = postJsonPrepare(url, json, headers);
        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                return null;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response syncPostJson(String url, String json) {
        return syncPostJson(url, json, null);
    }

    /**
     * post请求
     *
     * @param url
     * @param params
     * @param headers
     * @param requestListener
     */
    public void asyncPost(String url, HashMap<String, String> params, HashMap<String, String> headers, final RequestListener requestListener) {
        if (requestListener != null) {
            requestListener.onStart();
        }
        Call call = postPrepare(url, params, headers);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestListener != null) {
                    requestListener.onError();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (requestListener != null) {
                    if (response.isSuccessful()) {
                        if (response.body() == null) {
                            requestListener.onError();
                        } else {
                            requestListener.onSuccess(response.body().string());
                        }

                    } else {
                        requestListener.onError();
                    }
                }
            }
        });
    }

    public void asyncPost(String url, HashMap<String, String> params, HashMap<String, String> headers) {
        asyncPost(url, params, headers, null);
    }

    public void asyncPost(String url, HashMap<String, String> params, RequestListener requestListener) {
        asyncPost(url, params, null, requestListener);
    }

    public void asyncPost(String url, HashMap<String, String> params) {
        asyncPost(url, params, null, null);
    }

    public Response syncPost(String url, HashMap<String, String> params, HashMap<String, String> headers) {
        Call call = postPrepare(url, params, headers);
        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                return null;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Response syncPost(String url, HashMap<String, String> params) {
        return syncPost(url, params, null);
    }

    /**
     * 下载数据准备
     *
     * @param url
     * @param dirPath
     * @param downloadListener
     * @param progressListener
     */
    public void prepareDownload(final String url, final String dirPath, final OnDownloadListener downloadListener, final ProgressListener progressListener) {
        if (!new File(dirPath).exists()) {
            new File(dirPath).mkdirs();
        }
        Request request = new Request.Builder().tag(url).url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                downloadListener.onError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                FileOutputStream fos = null;
                boolean isDownloadOK;
                File file = null;
                try {
                    if (response.isSuccessful()) {
                        //文件总长度
                        long fileSize = response.body().contentLength();
                        long fileSizeDownloaded = 0;
                        is = response.body().byteStream();
                        if (url.indexOf("?") != -1) {
                            String url_ = url.substring(0, url.indexOf("?"));
                            file = new File(dirPath + File.separator + url_.substring(url_.lastIndexOf("/") + 1));
                        } else {
                            file = new File(dirPath + File.separator + url.substring(url.lastIndexOf("/") + 1));
                        }
                        if (file.exists()) {
                            file.delete();
                        }
                        file.createNewFile();
                        fos = new FileOutputStream(file);
                        int count = 0;
                        byte[] buffer = new byte[1024];
                        while ((count = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, count);
                            fileSizeDownloaded += count;
                            if (progressListener != null) {
                                progressListener.updateprogress((int) ((100 * fileSizeDownloaded) / fileSize), fileSizeDownloaded, fileSize);
                            }
                        }
                        fos.flush();
                        isDownloadOK = true;
                    } else {
                        isDownloadOK = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    isDownloadOK = false;
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (isDownloadOK) {
                    downloadListener.onSuccess(file.getPath());
                } else {
                    downloadListener.onError();
                }
            }
        });
    }

    /**
     * 异步下载
     *
     * @param url
     * @param dirPath
     * @param requestListener
     * @param progressListener
     */
    public void asyncDownload(String url, String dirPath, final RequestListener requestListener, final ProgressListener progressListener) {
        prepareDownload(url, dirPath, new OKHttpUtils.OnDownloadListener() {
            @Override
            public void onSuccess(String filePath) {
                if (requestListener != null) {
                    requestListener.onSuccess(filePath);
                }
            }

            @Override
            public void onError() {
                if (requestListener != null) {
                    requestListener.onError();
                }
            }
        }, progressListener);
    }

    /**
     * 同步下载
     *
     * @param url
     * @param dirPath
     * @return
     */
    public File syncDownload(String url, String dirPath) {
        Request request = new Request.Builder().tag(url).url(url).build();
        Call call = okHttpClient.newCall(request);
        InputStream is = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            Response response = call.execute();
            if (response != null && response.isSuccessful()) {
                is = response.body().byteStream();
                if (url.indexOf("?") != -1) {
                    String url_ = url.substring(0, url.indexOf("?"));
                    file = new File(dirPath + File.separator + url_.substring(url_.lastIndexOf("/") + 1));
                } else {
                    file = new File(dirPath + File.separator + url.substring(url.lastIndexOf("/") + 1));
                }
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                fos = new FileOutputStream(file);
                int count = 0;
                byte[] buffer = new byte[1024];
                while ((count = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            file = null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                file = null;
            }
        }
        return file;
    }

    /**
     * 上传参数准备
     *
     * @param url
     * @param params
     * @param files
     * @param headers
     * @param listener
     * @return
     */
    private Call uploadPrepare(String url, HashMap<String, String> params, HashMap<String, File> files, HashMap<String, String> headers, ProgressRequestBody.UpProgressListener listener) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType(MultipartBody.FORM);
        if (params != null) {
            Iterator<Map.Entry<String, String>> params_it = params.entrySet().iterator();
            while (params_it.hasNext()) {
                Map.Entry<String, String> params_en = params_it.next();
                multipartBuilder.addFormDataPart(params_en.getKey(), params_en.getValue());
            }
        }
        /**
         * 遍历paths中所有图片绝对路径到builder，并约定key如“upload”作为后台接受多张图片的key
         for (String path : paths) {
         builder.addFormDataPart("upload", null, RequestBody.create(MEDIA_TYPE_PNG, new File(path)));
         */
        if (files != null) {
            Iterator<Map.Entry<String, File>> file_it = files.entrySet().iterator();
            while (file_it.hasNext()) {
                Map.Entry<String, File> entry = file_it.next();
                multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue().getName(), RequestBody.create(MediaType.parse("application/octet-stream"), entry.getValue()));
            }
        }
        RequestBody formBody = multipartBuilder.build();
        formBody = new ProgressRequestBody(formBody, listener);
        Request.Builder builder = new Request.Builder();
        if (headers != null) {
            Iterator<Map.Entry<String, String>> header_it = headers.entrySet().iterator();
            while (header_it.hasNext()) {
                Map.Entry<String, String> entry = header_it.next();
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        return okHttpClient.newCall(builder.url(url).tag(url).post(formBody).build());
    }

    /**
     * 同步上传
     *
     * @param url
     * @param params
     * @param files
     * @param headers
     * @param listener
     * @return
     */
    public String syncUpload(String url, HashMap<String, String> params, HashMap<String, File> files, HashMap<String, String> headers, ProgressRequestBody.UpProgressListener listener) {
        Call call = uploadPrepare(url, params, files, headers, listener);
        try {
            Response response = call.execute();
            if (response != null && response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 异步上传
     *
     * @param url
     * @param params
     * @param files
     * @param headers
     * @param requestListener
     * @param listener
     */
    public void asyncUpload(String url, HashMap<String, String> params, HashMap<String, File> files, HashMap<String, String> headers, final RequestListener requestListener, ProgressRequestBody.UpProgressListener listener) {
        Call call = uploadPrepare(url, params, files, headers, listener);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (requestListener != null) {
                    requestListener.onError();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (requestListener != null) {
                    if (response.isSuccessful()) {
                        if (response.body() == null) {
                            requestListener.onError();
                        } else {
                            requestListener.onSuccess(response.body().string());
                        }

                    } else {
                        requestListener.onError();
                    }
                }
            }
        });
    }

    public void cancel(String tag) {
        synchronized (okHttpClient.dispatcher().getClass()) {
            for (Call call : okHttpClient.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call : okHttpClient.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        }
    }

    public Call putJsonPrepare(String url, String json, HashMap<String, String> headers) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request.Builder req_builder = new Request.Builder();
        if (headers != null && headers.size() > 0) {
            Iterator<Map.Entry<String, String>> header_it = headers.entrySet().iterator();
            while (header_it.hasNext()) {
                Map.Entry<String, String> header_en = header_it.next();
                req_builder.addHeader(header_en.getKey(), header_en.getValue());
            }
        }
        Request request = req_builder.url(url).put(body).build();
        return okHttpClient.newCall(request);
    }

    public Response syncPutJson(String url, String json, HashMap<String, String> headers) {
        Call call = putJsonPrepare(url, json, headers);
        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                return null;
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
