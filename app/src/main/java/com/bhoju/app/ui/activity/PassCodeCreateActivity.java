package com.bhoju.app.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bhoju.app.R;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class PassCodeCreateActivity extends AppCompatActivity {

    @BindViews({R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9})
    List<View> btnNumPads;

    @BindView(R.id.otp_view)
    OtpTextView otp_view;

    @BindView(R.id.btn_clear)
    CardView btn_clear;

    @BindView(R.id.btn_tick)
    CardView btn_tick;

    /*@BindView(R.id.btn_back)
    ImageView btn_back;*/

    @BindView(R.id.txt_remove_pin)
    TextView txt_remove_pin;

    private final int MAX_LENGTH = 6;
    private String codeString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_code_create);
        ButterKnife.bind(this);

        if(!getIntent().getExtras().getBoolean("screen"))
        {
            txt_remove_pin.setVisibility(View.GONE);
        }

        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }
            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.
                //Toast.makeText(PassCodeActivity.this, "The OTP is " + otp,  Toast.LENGTH_SHORT).show();
            }
        });

        txt_remove_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils prefUtils = PrefUtils.getInstance(PassCodeCreateActivity.this);
                prefUtils.setValue(PrefKeys.IS_PASS,false);
                prefUtils.setValue(PrefKeys.PASS_CODE,"");
                Toast.makeText(PassCodeCreateActivity.this, "PassCode has been removed successfully.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeString.length() > 0) {
                    //remove last character of code
                    codeString = removeLastChar(codeString);
                    otp_view.setOTP(codeString);
                }
            }
        });

        btn_tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeString.length() == MAX_LENGTH) {
                    setIsPass();
                    finish();
                    Toast.makeText(PassCodeCreateActivity.this, "PassCode is set successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }

    @OnClick({R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9})
    public void onClick(CardView button) {
        getStringCode(button.getId());
        otp_view.setOTP(codeString);
    }

    private void getStringCode(int buttonId) {
        if(codeString.length()<MAX_LENGTH) {
            //switch (buttonId) {
            if(buttonId == R.id.btn0)
                codeString += "0";
            else if(buttonId == R.id.btn1)
                codeString += "1";
            else if(buttonId == R.id.btn2)
                codeString += "2";
            else if(buttonId == R.id.btn3)
                codeString += "3";
            else if(buttonId == R.id.btn4)
                codeString += "4";
            else if(buttonId == R.id.btn5)
                codeString += "5";
            else if(buttonId == R.id.btn6)
                codeString += "6";
            else if(buttonId == R.id.btn7)
                codeString += "7";
            else if(buttonId == R.id.btn8)
                codeString += "8";
            else if(buttonId == R.id.btn9)
                codeString += "9";
            //}
        }
    }

    private String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }

    private void setIsPass() {
        PrefUtils prefUtils = PrefUtils.getInstance(PassCodeCreateActivity.this);
        prefUtils.setValue(PrefKeys.IS_PASS,true);
        prefUtils.setValue(PrefKeys.PASS_CODE,codeString);
    }
}