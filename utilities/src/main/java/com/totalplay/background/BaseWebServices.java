package com.totalplay.background;

import android.annotation.SuppressLint;
import android.util.Base64;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.totalplay.utilities.BuildConfig;
import com.totalplay.utils.GsonUtils;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("unused")
public class BaseWebServices {

    protected static Retrofit settingsRetrofit(String url) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(180, TimeUnit.SECONDS);
        builder.connectTimeout(180, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        builder.addInterceptor(chain -> {
            Request.Builder builder1 = chain.request().newBuilder();
            builder1.headers(getJsonHeader());
            return chain.proceed(builder1.build());
        });

        return createRetrofit(builder, url);
    }

    protected static Retrofit settingsRetrofitWithOutToken(String url) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(180, TimeUnit.SECONDS);
        builder.connectTimeout(180, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return createRetrofit(builder, url);
    }

    protected static Retrofit settingsFilesRetrofit(String url) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(120, TimeUnit.SECONDS);
        builder.connectTimeout(120, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);

        builder.addInterceptor(chain -> {
            Request.Builder builder1 = chain.request().newBuilder();
            builder1.headers(getJsonImageHeader());
            return chain.proceed(builder1.build());
        });

        return createRetrofit(builder, url);
    }

    private static Retrofit createRetrofit(OkHttpClient.Builder builder, String url) {
        Retrofit retrofit;
        if (BuildConfig.DEBUG) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(getUnsafeOkHttpClient(builder))
                    .addConverterFactory(GsonConverterFactory.create(GsonUtils.gsonForDeserialization()))
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(GsonUtils.gsonForDeserialization()))
                    .build();
        }
        return retrofit;
    }

    private static Headers getJsonHeader() {
        String username = BaseWebServicesConstants.SECRET_NAME;
        String password = BaseWebServicesConstants.SECRET_WORD;
        String credentials = username + ":" + password;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Headers.Builder builder = new Headers.Builder();
        builder.add("Content-Type", "application/json");
        builder.add("Accept", "application/json");
        builder.add("Authorization", basic);
        return builder.build();
    }

    private static Headers getJsonImageHeader() {
        String username = BaseWebServicesConstants.SECRET_NAME;
        String password = BaseWebServicesConstants.SECRET_WORD;
        String credentials = username + ":" + password;
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Headers.Builder builder = new Headers.Builder();
        builder.add("Authorization", basic);
        return builder.build();
    }

    @SuppressLint("TrustAllX509TrustManager")
    private static OkHttpClient getUnsafeOkHttpClient(OkHttpClient.Builder builder) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();


            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier((hostname, session) -> true);

            builder.addInterceptor(new LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("Request")
                    .response("Response")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .build());

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
