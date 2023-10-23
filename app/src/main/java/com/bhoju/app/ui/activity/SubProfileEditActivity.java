package com.bhoju.app.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bhoju.app.network.APIConstants;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.bhoju.app.R;
import com.bhoju.app.network.APIClient;
import com.bhoju.app.network.APIInterface;
import com.bhoju.app.util.NetworkUtils;
import com.bhoju.app.util.UiUtils;
import com.bhoju.app.util.sharedpref.PrefKeys;
import com.bhoju.app.util.sharedpref.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bhoju.app.network.APIConstants.*;
import static com.bhoju.app.util.UiUtils.animationObject;

public class SubProfileEditActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private final String TAG = SubProfileEditActivity.class.getSimpleName();

    public static final String ID = "subProfileIdUnderChange";
    public static final String NAME = "userName";
    public static final String PICTURE = "picture";
    public static final String COUNT = "count";
    public static final String IS_EDITING = "isEditing";
    private static final int PICK_IMAGE = 100;
    String name = "";
    String picture = "";
    int subProfileIdUnderChange;

    @BindView(R.id.subProfileName)
    EditText subProfileName;
    @BindView(R.id.deleteSubProfile)
    View deleteSubProfile;
    @BindView(R.id.subProfilePicture)
    ImageView subProfilePicture;
    private int STORAGE_PERMISSION = 124;


    APIInterface apiInterface;
    PrefUtils prefUtils;
    private Uri fileToUpload = null;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_sub_profile);
        ButterKnife.bind(this);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefUtils = PrefUtils.getInstance(this);

        Intent intent = getIntent();
        if (intent != null) {
            isEditMode = intent.getBooleanExtra(IS_EDITING, false);
            subProfileIdUnderChange = intent.getIntExtra(ID, 0);
            deleteSubProfile.setVisibility(isEditMode && subProfileIdUnderChange != 0 ? View.VISIBLE : View.GONE);
            name = intent.getStringExtra(NAME);
            picture = intent.getStringExtra(PICTURE);
            setUpDataOnToViews();
        } else {
            UiUtils.showShortToast(this, getString(R.string.something_went_wrong));
            finish();
        }
    }



    private void setUpDataOnToViews() {
        try {
            subProfileName.setText(subProfileIdUnderChange == 0 ? null : name);
            subProfileName.setHint(subProfileIdUnderChange == 0 ? getString(R.string.enter_a_name) : null);
            if (subProfileIdUnderChange != 0)
                subProfileName.setSelection(name.length());
            Glide.with(this)
                    .load(picture)
                    .transition(GenericTransitionOptions.with(animationObject))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                    .into(subProfilePicture);
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
        }
    }


    private RequestBody getPartFor(String stuff) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, stuff);
    }

    @OnClick({R.id.cancelEdit, R.id.saveEdit, R.id.editPicture, R.id.deleteSubProfile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelEdit:
                finish();
                break;
            case R.id.saveEdit:
                if (validateFields()) {
                    if (!isEditMode || subProfileIdUnderChange == 0) {
                        addSubProfile();
                    } else {
                        editSubProfile();
                    }
                }
                break;
            case R.id.editPicture:
                String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                if (EasyPermissions.hasPermissions(this, perms)) {
                    callImagePicker();
                } else {
                    getPermission();
                }
                break;
            case R.id.deleteSubProfile:
                deleteSubProfileConfirm();
                break;
        }
    }

    private void getPermission() {
        EasyPermissions.requestPermissions(this, getString(R.string.needPermissionToAccessYourStoarge),
                STORAGE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void deleteSubProfileConfirm() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirmation)
                .setMessage(R.string.are_you_sure_to_your_sub_profile)
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                    UiUtils.showLoadingDialog(this);
                    Map<String, Object> params = new HashMap<>();
                    params.put(Params.ID, id);
                    params.put(Params.TOKEN, token);
                    params.put(Params.SUB_PROFILE_ID, subProfileId);
                    params.put(Params.DELETE_SUB_PROFILE, subProfileIdUnderChange);
                    params.put(Params.LANGUAGE, prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, ""));

                    Call<String> call = apiInterface.deleteSubProfile(APIConstants.APIs.DELETE_SUB_PROFILE, params);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            UiUtils.hideLoadingDialog();
                            JSONObject subProfileDeleteResponse = null;
                            try {
                                subProfileDeleteResponse = new JSONObject(response.body());
                            } catch (Exception e) {
                                UiUtils.log(TAG, Log.getStackTraceString(e));
                            }
                            if (subProfileDeleteResponse != null) {
                                if (subProfileDeleteResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                                    PrefUtils.getInstance(SubProfileEditActivity.this).removeKey(PrefKeys.ACTIVE_SUB_PROFILE);
                                    UiUtils.showShortToast(SubProfileEditActivity.this, subProfileDeleteResponse.optString(Params.MESSAGE) + ". Restarting the app..");
                                    restartApp();
                                } else {
                                    UiUtils.showShortToast(SubProfileEditActivity.this, subProfileDeleteResponse.optString(Params.ERROR_MESSAGE));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            UiUtils.hideLoadingDialog();
                            NetworkUtils.onApiError(SubProfileEditActivity.this);
                        }
                    });
                })
                .setNegativeButton(getString(R.string.no), null)
                .setIcon(R.mipmap.ic_launcher)
                .create().show();
    }

    private boolean validateFields() {
        if (subProfileName.getText().toString().length() == 0) {
            UiUtils.showShortToast(this, getString(R.string.names_cant_be_empty));
            return false;
        }
        return true;
    }

    private void editSubProfile() {
        UiUtils.showLoadingDialog(this);

        MultipartBody.Part multipartBody = null;

        if (fileToUpload != null) {
            String filePath = getRealPathFromURIPath(fileToUpload, this);
            File file = new File(filePath);
            UiUtils.log("TAG","Filename "+ file.getName());

            // create RequestBody instance tempFrom file
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/*"), file);

            // MultipartBody.Part is used to send also the actual file name
            multipartBody =
                    MultipartBody.Part.createFormData(Params.PICTURE, file.getAbsolutePath(), requestFile);
        }

        Map<String, RequestBody> params = new HashMap<>();
        params.put(Params.ID, getPartFor(String.valueOf(id)));
        params.put(Params.TOKEN, getPartFor(token));
        params.put(Params.SUB_PROFILE_ID, getPartFor(String.valueOf(subProfileIdUnderChange)));
        params.put(Params.NAME, getPartFor(subProfileName.getText().toString()));
        params.put(Params.LANGUAGE, getPartFor(prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, "")));

        Call<String> call = apiInterface.editSubProfile(APIs.EDIT_SUB_PROFILE, params, multipartBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject updateProfileResponse = null;
                try {
                    updateProfileResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (updateProfileResponse != null)
                    if (updateProfileResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(SubProfileEditActivity.this, updateProfileResponse.optString(Params.MESSAGE));
                        finish();
                    } else {
                        UiUtils.showShortToast(SubProfileEditActivity.this, updateProfileResponse.optString(Params.ERROR_MESSAGE));
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(SubProfileEditActivity.this);
            }
        });
    }

    private void addSubProfile() {
        UiUtils.showLoadingDialog(this);

        MultipartBody.Part multipartBody = null;

        if (fileToUpload != null) {
            String filePath = getRealPathFromURIPath(fileToUpload, this);
            File file = new File(filePath);

            // create RequestBody instance tempFrom file
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("image/*"), file);

            // MultipartBody.Part is used to send also the actual file name
            multipartBody =
                    MultipartBody.Part.createFormData(Params.PICTURE, file.getAbsolutePath(), requestFile);

        }

        Map<String, RequestBody> params = new HashMap<>();
        params.put(Params.ID, getPartFor(String.valueOf(id)));
        params.put(Params.TOKEN, getPartFor(token));
        params.put(Params.NAME, getPartFor(subProfileName.getText().toString()));
        params.put(Params.LANGUAGE, getPartFor(prefUtils.getStringValue(PrefKeys.APP_LANGUAGE_STRING, "")));

        Call<String> call = apiInterface.addSubProfile(APIs.ADD_SUB_PROFILE, params, multipartBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                UiUtils.hideLoadingDialog();
                JSONObject addSubProfileResponse = null;
                try {
                    addSubProfileResponse = new JSONObject(response.body());
                } catch (Exception e) {
                    UiUtils.log(TAG, Log.getStackTraceString(e));
                }
                if (addSubProfileResponse != null)
                    if (addSubProfileResponse.optString(Params.SUCCESS).equals(Constants.TRUE)) {
                        UiUtils.showShortToast(SubProfileEditActivity.this, addSubProfileResponse.optString(Params.MESSAGE) + ". Restarting app..");
                        PrefUtils.getInstance(SubProfileEditActivity.this)
                                .removeKey(PrefKeys.ACTIVE_SUB_PROFILE);
                        restartApp();
                        Toast.makeText(SubProfileEditActivity.this,getString(R.string.sub_profile),Toast.LENGTH_SHORT).show();
                    } else {
                        UiUtils.showShortToast(SubProfileEditActivity.this, addSubProfileResponse.optString(Params.ERROR_MESSAGE));
                    }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                UiUtils.hideLoadingDialog();
                NetworkUtils.onApiError(SubProfileEditActivity.this);
            }
        });
    }

    private void restartApp() {
        Intent restart = new Intent(SubProfileEditActivity.this, SplashActivity.class);
        restart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(restart);
        SubProfileEditActivity.this.finish();
    }

    private void callImagePicker() {
        try {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
            openGalleryIntent.setType("image/*");
            startActivityForResult(openGalleryIntent, PICK_IMAGE);
        } catch (Exception e) {
            UiUtils.log(TAG, Log.getStackTraceString(e));
            UiUtils.showShortToast(this, getString(R.string.sorry_no_apps_to_perfrom));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            fileToUpload = data.getData();
            Glide.with(this)
                    .load(fileToUpload)
                    .transition(GenericTransitionOptions.with(animationObject))
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).signature(new ObjectKey(0)))
                    .into(subProfilePicture);
        }

    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String value = cursor.getString(idx);
            cursor.close();
            return value;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        callImagePicker();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
