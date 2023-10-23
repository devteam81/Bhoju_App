package com.bhoju.app.util;

import android.content.Context;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public class MyGlideModule extends AppGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
//        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
//                .build();
//        glide.getRegistry().replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));

        /*HandshakeCertificates certificates = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        OkHttpClient okHttpClient = new okhttp3.OkHttpClient.Builder()
                .sslSocketFactory(certificates.sslSocketFactory(), certificates.trustManager())
                .build();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));*/
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        super.applyOptions(context, builder);
        RequestOptions options = new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888);
        builder.setDefaultRequestOptions(options);
    }
}