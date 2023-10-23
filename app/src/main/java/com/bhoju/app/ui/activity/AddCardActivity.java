package com.bhoju.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bhoju.app.network.APIConstants;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bhoju.app.R;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.ConfigParser;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.Constants;
import static com.bhoju.app.network.APIConstants.Params;

public class AddCardActivity extends BaseActivity {

    private final String TAG = AddCardActivity.class.getSimpleName();

    public static final String ADDED_CARD = "addedCard";
    private static final char SEPARATOR = ' ';
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cardNumb)
    EditText cardNumb;
    @BindView(R.id.cardExpiryDate)
    EditText cardExpiryDate;
    @BindView(R.id.cardCvv)
    EditText cardCvv;
    @BindView(R.id.cardNumberInput)
    EditText cardNumberInput;
    @BindView(R.id.cardExpiryInput)
    EditText cardExpiryInput;
    @BindView(R.id.cardCvvInput)
    EditText cardCvvInput;
    @BindView(R.id.addCard)
    Button addCard;
    @BindView(R.id.card)
    TextInputLayout cardLayout;
    @BindView(R.id.expiry)
    TextInputLayout expiryLayout;
    @BindView(R.id.cvv)
    TextInputLayout cvvLayout;
    private int prevLength = -1;
    private APIInterface apiInterface;
    private Stripe stripe;
    private View.OnClickListener addCardListener = v -> {
        String number = cardNumberInput.getText().toString().replace(String.valueOf(SEPARATOR), "");
        String expiry = cardExpiryInput.getText().toString();
        String cvv = cardCvvInput.getText().toString();
        if (expiry.length() < 5) {
            UiUtils.showShortToast(this, getString(R.string.enter_valid_expiry_date));
            return;
        }
        if (expiry.substring(0, 2).contains("/") || expiry.substring(3, 5).contains("/")) {
            UiUtils.showShortToast(this, getString(R.string.enter_valid_expiry_date_for_the_card));
            return;
        }
        Card card = new Card(number,
                Integer.parseInt(expiry.substring(0, 2)),
                Integer.parseInt(expiry.substring(3, 5)),
                cvv);
        if (!card.validateCard()) {
            UiUtils.showShortToast(this, getString(R.string.Enter_valid_card_details));
            return;
        }
        UiUtils.showLoadingDialog(this);
        stripe.createToken(card, new TokenCallback() {
            @Override
            public void onError(Exception error) {
                UiUtils.hideLoadingDialog();
                Toast.makeText(AddCardActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Token token) {
                addCardInBackend(token.getId());
            }
        });
    };
    private TextWatcher cardNumberWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (SEPARATOR == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            // Insert char where needed.
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                // Only if its a digit where there should be a space we insert a space
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(SEPARATOR)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(SEPARATOR));
                }
            }

            cardNumb.setText(s.toString());
        }
    };
    private TextWatcher cardExpiryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String validate = s.toString();
            int length = validate.length();


            //add '/' at 2 char entry
            if (length == 2 && prevLength == 1) {
                cardExpiryInput.setText(String.format("%s/", validate));
                cardExpiryInput.setSelection(3);
                prevLength = 3;
                return;
            }
            if (length == 2 && prevLength == 3) {
                cardExpiryInput.setText(validate.substring(0, 1));
                cardExpiryInput.setSelection(1);
                prevLength = 1;
                return;
            }
            prevLength = length;
            cardExpiryDate.setText(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher cardCvvWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            cardCvv.setText(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_card);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setUpStripe();

        setUpCardWidget();
    }

    private void setUpStripe() {
        String stripePublicKey = ConfigParser.getConfig(null).getPaymentCreds().getStripePubKey();
        if (stripePublicKey.equals(""))
            stripePublicKey = getResources().getString(R.string.stripepk);
        stripe = new Stripe(this, stripePublicKey);
    }

    private void setUpCardWidget() {
        cardNumberInput.addTextChangedListener(cardNumberWatcher);
        cardExpiryInput.addTextChangedListener(cardExpiryWatcher);
        cardCvvInput.addTextChangedListener(cardCvvWatcher);
        addCard.setOnClickListener(addCardListener);
        cardNumberInput.requestFocus();
    }

    private void addCardInBackend(String cardToken) {
        PrefUtils preferences = PrefUtils.getInstance(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, preferences.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, preferences.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, preferences.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(Params.CARD_TOKEN, cardToken);
        params.put(Params.LANGUAGE, preferences.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.addCard(APIConstants.APIs.ADD_CARD, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject addCardResponse = null;
                try {
                    addCardResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (addCardResponse != null)
                    if (addCardResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(AddCardActivity.this, addCardResponse.optString(Params.MESSAGE));
                        JSONObject data = addCardResponse.optJSONObject(Params.DATA);
                        Intent cardData = new Intent();
                        cardData.putExtra(ADDED_CARD, data.toString());
                        setResult(Activity.RESULT_OK, cardData);
                        finish();
                    } else {
                        UiUtils.showShortToast(AddCardActivity.this, addCardResponse.optString(Params.ERROR_MESSAGE));
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(AddCardActivity.this);
            }
        });
    }
}
