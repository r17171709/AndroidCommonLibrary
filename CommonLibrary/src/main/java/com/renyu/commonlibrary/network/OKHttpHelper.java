package com.renyu.commonlibrary.network;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Response;

/**
 * Created by RG on 2015/10/15.
 */
public class OKHttpHelper {

    public interface StartListener {
        void onStart();
    }

    public interface RequestListener {
        void onSuccess(String string);
        void onError();
    }

    public interface ProgressListener {
        void updateprogress(int progress, long bytesRead, long contentLength);
    }

    /**
     * 一般使用
     * @param url
     * @param params
     * @param startListener
     * @param requestListener
     */
    public void commonPostRequest(String url, HashMap<String, String> params, final StartListener startListener, final RequestListener requestListener) {
        if (startListener!=null) {
            startListener.onStart();
        }
        OKHttpUtils.getInstance().asyncPost(url, params, response -> {
            try {
                String string = response.body().string();
                Observable.just(string).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                    if (requestListener != null) {
                        requestListener.onSuccess(s);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                    if (requestListener != null) {
                        requestListener.onError();
                    }
                });
            }
        }, () -> {
            Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                if (requestListener != null) {
                    requestListener.onError();
                }
            });
        });
    }

    /**
     * 有head的一般使用
     * @param url
     * @param params
     * @param heads
     * @param startListener
     * @param requestListener
     */
    public void commonPostWithHeadRequest(String url, HashMap<String, String> params, HashMap<String, String> heads, final StartListener startListener, final RequestListener requestListener) {
        if (startListener!=null) {
            startListener.onStart();
        }
        OKHttpUtils.getInstance().asyncPost(url, params, heads, response -> {
            try {
                String string = response.body().string();
                Observable.just(string).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                    if (requestListener != null) {
                        requestListener.onSuccess(s);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                    if (requestListener != null) {
                        requestListener.onError();
                    }
                });
            }
        }, () -> Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
            if (requestListener != null) {
                requestListener.onError();
            }
        }));
    }

    public Response syncPostRequest(String url, HashMap<String, String> params) {
        return OKHttpUtils.getInstance().syncPost(url, params);
    }

    public Response syncPostWithHeadRequest(String url, HashMap<String, String> params, HashMap<String, String> heads) {
        return OKHttpUtils.getInstance().syncPost(url, params, heads);
    }

    /**
     * 一般调用
     * @param url
     * @param startListener
     * @param requestListener
     */
    public void commonGetRequest(String url, final StartListener startListener, final RequestListener requestListener) {
        if (startListener!=null) {
            startListener.onStart();
        }
        OKHttpUtils.getInstance().asyncGet(url, response -> {
            try {
                String string = response.body().string();
                Observable.just(string).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                    if (requestListener != null) {
                        requestListener.onSuccess(s);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                    if (requestListener != null) {
                        requestListener.onError();
                    }
                });
            }
        }, () -> {
            Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                if (requestListener != null) {
                    requestListener.onError();
                }
            });
        });
    }

    public Response syncGetRequest(String url, HashMap<String, String> headers) {
        return OKHttpUtils.getInstance().syncGet(url, headers);
    }

    public void asyncUpload(HashMap<String, File> files, String url, HashMap<String, String> params, final StartListener startListener, final RequestListener requestListener) {
        if (startListener!=null) {
            startListener.onStart();
        }
        OKHttpUtils.getInstance().asyncUpload(url, params, files, response -> {
            try {
                String string = response.body().string();
                Observable.just(string).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                    if (requestListener != null) {
                        requestListener.onSuccess(s);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                    if (requestListener != null) {
                        requestListener.onError();
                    }
                });
            }
        }, () -> Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
            if (requestListener != null) {
                requestListener.onError();
            }
        }));
    }

    public Response syncUpload(HashMap<String, File> files, String url, HashMap<String, String> params) {
        return OKHttpUtils.getInstance().syncUpload(url, params, files);
    }

    public File syncDownloadFile(String url, String dirPath) {
        return OKHttpUtils.getInstance().syncDownload(url, dirPath);
    }

    public void downloadFile(String url, String dirPath, final RequestListener requestListener, final ProgressListener progressListener) {
        OKHttpUtils.getInstance().download(url, dirPath, new OKHttpUtils.OnDownloadListener() {
            @Override
            public void onSuccess(String filePath) {
                Observable.just(filePath).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                    if (requestListener != null) {
                        requestListener.onSuccess(s);
                    }
                });
            }

            @Override
            public void onError() {
                Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(s -> {
                    if (requestListener != null) {
                        requestListener.onError();
                    }
                });
            }
        }, (bytesRead, contentLength, done) -> {
//                System.out.println(bytesRead);
//                System.out.println(contentLength);
//                System.out.println(done);
//                Log.d("OKHttpHelper", (100 * bytesRead) / contentLength + " " + done);
            Flowable.just("").onBackpressureDrop()
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(o -> {
                        if (progressListener!=null) {
                            progressListener.updateprogress((int) ((100 * bytesRead) / contentLength), bytesRead, contentLength);
                        }
                    });
        });
    }

    public void cancel(String tag) {
        OKHttpUtils.getInstance().cancel(tag);
    }
}
