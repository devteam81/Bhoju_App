package com.bhoju.app.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import com.bhoju.app.R;
import com.bhoju.app.model.Card;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIConstants;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.ui.adapter.CardsAdapter;
import com.bhoju.app.util.ParserUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.Constants;
import static com.bhoju.app.network.APIConstants.Params;

public class PaymentsActivity extends BaseActivity implements CardsAdapter.CardListener {

    private final String TAG = PaymentsActivity.class.getSimpleName();
    private static final int GET_ADDED_CARD = 100;
    @BindView(R.id.addCard)
    View addCard;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cardsRecycler)
    RecyclerView cardsRecycler;
    @BindView(R.id.noResultLayout)
    View noResultLayout;
    CardsAdapter cardsAdapter;
    ArrayList<Card> cards = new ArrayList<>();
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_payments);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setUpCards();
    }

    private void setUpCards() {
        cardsRecycler.setHasFixedSize(true);
        cardsRecycler.setLayoutManager(new LinearLayoutManager(this));
        cardsAdapter = new CardsAdapter(this, cards, this);
        cardsRecycler.setItemAnimator(new DefaultItemAnimator());
        cardsRecycler.setAdapter(cardsAdapter);
        getCards();
    }

    private void getCards() {
        UiUtils.showLoadingDialog(this);
        PrefUtils preferences = PrefUtils.getInstance(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID, preferences.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, preferences.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.SUB_PROFILE_ID, preferences.getIntValue(PrefKeys.ACTIVE_SUB_PROFILE, -1));
        params.put(Params.LANGUAGE, preferences.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.getCards(APIConstants.APIs.GET_CARDS, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                cards.clear();
                UiUtils.hideLoadingDialog();
                JSONObject cardsResponse = null;
                try {
                    cardsResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (cardsResponse != null)
                    if (cardsResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        JSONArray cardsArray = cardsResponse.optJSONArray(Params.DATA);
                        for (int i = 0; i < cardsArray.length(); i++) {
                            try {
                                JSONObject cardObj = cardsArray.getJSONObject(i);
                                Card card = ParserUtils.parseCardData(cardObj);
                                cards.add(card);
                            } catch (Exception e) {
                                UiUtils.log(TAG, Log.getStackTraceString(e));
                            }
                        }
                        notifyDataChange();
                    } else {
                        UiUtils.showShortToast(PaymentsActivity.this, cardsResponse.optString(Params.ERROR_MESSAGE));
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(PaymentsActivity.this);
            }
        });
    }

    private void notifyDataChange() {
        cardsAdapter.notifyDataSetChanged();
        boolean isEmpty = cards.size() == 0;
        noResultLayout.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        cardsRecycler.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.addCard)
    public void onViewClicked() {
        Intent i = new Intent(this, AddCardActivity.class);
        startActivityForResult(i, GET_ADDED_CARD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GET_ADDED_CARD && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                JSONObject addedCard;
                try {
                    addedCard = new JSONObject(data.getStringExtra(AddCardActivity.ADDED_CARD));
                    Card card = ParserUtils.parseCardData(addedCard);
                    cards.add(card);
                    notifyDataChange();
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCardDeleted(boolean isEmpty, int position) {
        getCards();
    }
}
