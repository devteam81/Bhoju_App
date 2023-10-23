package com.bhoju.app.util;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import okhttp3.tls.HandshakeCertificates;

import static com.bhoju.app.network.APIConstants.URLs.BASE_URL;
import static com.bhoju.app.network.APIConstants.URLs.KEY;
import static com.bhoju.app.network.APIConstants.URLs.LIVE_CERTIFICATE;
import static com.bhoju.app.network.APIConstants.URLs.MEDIA_CERTIFICATE;
import static com.bhoju.app.network.APIConstants.URLs.TEST_CERTIFICATE;

public class UnsafeOkHttpClient {

    private static final String TAG = UnsafeOkHttpClient.class.getSimpleName();

    public static HandshakeCertificates getUnsafeOkHttpClient(String type) {
        try {
            //New code
            String isgCert ="";
            if(type.equalsIgnoreCase("media"))
            {
                isgCert = MEDIA_CERTIFICATE;
            }else {
                //Live Certificate
                if (BASE_URL.contains(KEY)) {
                    isgCert = LIVE_CERTIFICATE;
                } else {
                    //Test Certificate
                    isgCert = TEST_CERTIFICATE;
                }
            }

            CertificateFactory cf = null;
            try {
                cf = CertificateFactory.getInstance("X.509");
            } catch (CertificateException e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }
            Certificate isgCertificate = null;
            try {
                isgCertificate = cf.generateCertificate(new ByteArrayInputStream(isgCert.getBytes(StandardCharsets.UTF_8)));
            } catch (CertificateException e) {
                UiUtils.log(TAG, Log.getStackTraceString(e));
            }

            HandshakeCertificates certificates = new HandshakeCertificates.Builder()
                    .addTrustedCertificate((X509Certificate) isgCertificate)
                    // Uncomment to allow connection to any site generally, but could possibly cause
                    // noticeable memory pressure in Android apps.
//              .addPlatformTrustedCertificates()
                    .build();
            return certificates;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
