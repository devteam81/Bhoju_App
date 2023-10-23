package com.bhoju.app.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import com.bhoju.app.network.APIConstants;
import com.bhoju.app.ui.adapter.CardsAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bhoju.app.R;
import com.bhoju.app.model.SettingsItem;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIInterface;

import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.ui.adapter.SettingsAdapter;
import com.bhoju.app.ui.fragment.bottomsheet.ChangePasswordBottomSheet;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefHelper;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONException;
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

public class ProfileViewActivity extends BaseActivity {

    private final String TAG = ProfileViewActivity.class.getSimpleName();

    @BindView(R.id.profileSettingRecycler)
    RecyclerView profileSettingRecycler;

    SettingsAdapter settingsAdapter;
    APIInterface apiInterface;
    private final String[] availableAppLanguages = new String[]{"English", "Spanish"};
    private final String[] availableAppLanguageLocalStr = new String[]{"en", "es"};
    private int currentLanguageIndex, selectedLanguageIndex;
    private String currentLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);


        //Profile settings setup
        profileSettingRecycler.setLayoutManager(new LinearLayoutManager(this));
        profileSettingRecycler.setHasFixedSize(true);
        settingsAdapter = new SettingsAdapter(this, getSettingItems());
        profileSettingRecycler.setAdapter(settingsAdapter);

        currentLanguage = prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, "");
        for (int i = 0; i < availableAppLanguageLocalStr.length; i++) {
            if (currentLanguage.equals(availableAppLanguageLocalStr[i])) {
                currentLanguageIndex = selectedLanguageIndex = i;
                break;
            }
        }
    }


    private ArrayList<SettingsItem> getSettingItems() {
        ArrayList<SettingsItem> settingItems = new ArrayList<>();
        if (PrefUtils.getInstance(this).getStringValue(PrefKeys.LOGIN_TYPE, "").equals(Constants.MANUAL_LOGIN)) {
            settingItems.add(new SettingsItem(getString(R.string.change_password), ""
                    , v -> {
                BottomSheetDialogFragment changePasswordBottomSheet = new ChangePasswordBottomSheet();
                changePasswordBottomSheet.show(getSupportFragmentManager(), changePasswordBottomSheet.getTag());
            }));
        }
        settingItems.add(new SettingsItem(getString(R.string.view_plans), ""
                , v -> startActivity(new Intent(this, PlansActivity.class))));
        settingItems.add(new SettingsItem(getString(R.string.my_plans), ""
                , v -> startActivity(new Intent(this, MyPlansActivity.class))));
      /*  settingItems.add(new SettingsItem(getString(R.string.refer_earn),""
                ,v -> startActivity(new Intent(this, ReferralActivity.class))));*/
        settingItems.add(new SettingsItem(getString(R.string.multiLanguage), ""
                , v -> changeLanguage()));
/*        settingItems.add(new SettingsItem(getString(R.string.wishlist), ""
                , v -> startActivity(new Intent(this, WishListActivity.class))));
        settingItems.add(new SettingsItem(getString(R.string.spam_videos),""
                , v -> startActivity(new Intent(this,SpamVideosActivity.class))));
        settingItems.add(new SettingsItem(getString(R.string.cards), ""
                , v -> startActivity(new Intent(this, PaymentsActivity.class))));
        settingItems.add(new SettingsItem(getString(R.string.paid_videos), ""
                , v -> startActivity(new Intent(this, PaidVideosActivity.class))));
        settingItems.add(new SettingsItem(getString(R.string.history), ""
                , v -> startActivity(new Intent(this, HistoryActivity.class))));
        settingItems.add(new SettingsItem(getString(R.string.delete_acc), ""
                , v -> showDeleteAccountDialog()));*/
        return settingItems;
    }

    private void changeLanguage() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileViewActivity.this, R.style.Theme_AppCompat_Dialog_Alert_ChooseLang);
        builder.setTitle(getString(R.string.update_language));
        builder.setSingleChoiceItems(availableAppLanguages, currentLanguageIndex, (dialog, which) -> selectedLanguageIndex = which);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            currentLanguageIndex = selectedLanguageIndex;
            prefUtils.setValue(PrefKeys.APP_LANGUAGE_STRING, availableAppLanguageLocalStr[currentLanguageIndex]);
            prefUtils.setValue(PrefKeys.APP_LANGUAGE, availableAppLanguages[currentLanguageIndex]);
            setLanguage(availableAppLanguageLocalStr[currentLanguageIndex]);
            Toast.makeText(this, getString(R.string.language_chng), Toast.LENGTH_SHORT).show();
        });
        builder.create().show();
    }

    private void showDeleteAccountDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete_account);
        TextView deleteTextDesc = dialog.findViewById(R.id.sureToDeleteTextDesc);
        EditText password = dialog.findViewById(R.id.deleteAccountPassword);
        View passwordLayout = dialog.findViewById(R.id.deleteAccountLayout);
        if (!PrefUtils.getInstance(this).getStringValue(PrefKeys.LOGIN_TYPE, "").equals(Constants.MANUAL_LOGIN)) {
            password.setVisibility(View.GONE);
            deleteTextDesc.setVisibility(View.GONE);
            passwordLayout.setVisibility(View.GONE);
        }
        Button yes = dialog.findViewById(R.id.yesBtn);
        Button no = dialog.findViewById(R.id.noBtn);
        yes.setOnClickListener(v -> deleteAccount(password.getText().toString()));
        no.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    protected void deleteAccount(String password) {
        UiUtils.showLoadingDialog(this);
        PrefUtils preferences = PrefUtils.getInstance(this);
        Map<String, Object> params = new HashMap<>();
        params.put(Params.ID,  preferences.getIntValue(PrefKeys.USER_ID, -1));
        params.put(Params.TOKEN, preferences.getStringValue(PrefKeys.SESSION_TOKEN, ""));
        params.put(Params.PASSWORD, password);
        params.put(Params.LANGUAGE, preferences.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

        Call<String> call = apiInterface.deleteAccount(APIConstants.APIs.DELETE_ACCOUNT, params);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject deleteAccountResponse = null;
                try {
                    deleteAccountResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (deleteAccountResponse != null)
                    if (deleteAccountResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(ProfileViewActivity.this, deleteAccountResponse.optString(Params.MESSAGE));
                        logOutUserInDevice();
                    } else {
                        UiUtils.showShortToast(ProfileViewActivity.this, deleteAccountResponse.optString(Params.ERROR_MESSAGE));
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(ProfileViewActivity.this);
            }
        });
    }

    private void logOutUserInDevice() {
        PrefHelper.setUserLoggedOut(this);
        Intent restartActivity = new Intent(this, SplashActivity.class);
        restartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(restartActivity);
        this.finish();
    }


    @OnClick(R.id.back_btn)
    protected void backPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }
}
