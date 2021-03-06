package com.example.silc.hackathonframework.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.databinding.ActivityProfileBinding;
import com.example.silc.hackathonframework.fragments.SingleChoiceDialogFragment;
import com.example.silc.hackathonframework.helpers.GeographyDialogWrapper;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.ClientUser;
import com.example.silc.hackathonframework.models.State;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Profile extends AppBarActivity implements View.OnClickListener,
        Http2Request.Http2RequestListener, SingleChoiceDialogFragment.NoticeDialogListener {
    private static final String TAG = "activities.Profile";
    private static final boolean DEBUG = false;
    private static final int SELECT_IMAGE = 1;
    private FloatingActionButton fab;
    private boolean boolFab;
    private int dialog_id;
    private ClientUser clientUser;
    private Context context;

    private TextInputEditText mEmail;
    private TextInputEditText mDisplayName;
    private TextInputEditText mAddress;
    private TextView mCity;
    private TextView mState;
    private TextView mCountry;
    private TextInputEditText mZipCode;
    private GeographyDialogWrapper geoDialog;
    private ActivityProfileBinding binding;

    //Camera
    static final int REQUEST_IMAGE_CAPTURE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_profile,
                mContentFrame,
                true);
        context = this;
        boolFab = false;
        clientUser = new ClientUser();
        fab = binding.editFloatingBtn;
        fab.setOnClickListener(this);
        navigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);

        //Info Views
        mDisplayName = binding.displayName;
        mEmail = binding.email;
        mAddress = binding.address;
        mCity = binding.city;
        mState = binding.state;
        mCountry = binding.country;
        mZipCode = binding.zipCode;


        //Set onClick
        mCountry.setOnClickListener(this);
        mState.setOnClickListener(this);
        mCity.setOnClickListener(this);

        //Wrappers
        geoDialog = new GeographyDialogWrapper(context);

        ClientUser.getUserInfo(context);

        binding.profileImage.setOnClickListener(this);

        File dir = Environment.getExternalStorageDirectory();
        File img = new File(dir, "Pictures/profiles.png");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath(), options);
        binding.profileImage.setImageBitmap(bitmap);
    }

    protected void onResume() {
        super.onResume();
        navigation.getMenu().findItem(R.id.navigation_profile).setChecked(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.editFloatingBtn:
                if (!boolFab) {
                    v.clearAnimation();
                    fab.setImageResource(R.drawable.ic_save_pastel_64dp);
                    mDisplayName.setFocusableInTouchMode(true);
                    mEmail.setFocusableInTouchMode(true);
                    mAddress.setFocusableInTouchMode(true);
                    mZipCode.setFocusableInTouchMode(true);
                    boolFab = true;
                    Toast.makeText(context, "Select to Edit",
                            Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        saveUser();
                    } catch (JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        Toast.makeText(context, "An error occured",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.country:
                if (boolFab) {
                    dialog_id = R.id.country;
                    geoDialog.popCountryDialog();
                }
                break;
            case R.id.state:
                if (boolFab) {
                    dialog_id = R.id.state;
                    geoDialog.popStateDialog();
                }
                break;
            case R.id.city:
                if (boolFab) {
                    dialog_id = R.id.city;
                    geoDialog.popCityDialog();
                }
                break;
            case R.id.profile_image:
                if (boolFab) {
//                    Log.e(TAG, "ACCESSING GALLERY...");
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                    takePhoto();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        binding.profileImage.setImageBitmap(bitmap);
                        Log.e(TAG, "ACCESSED GALLERY");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && data != null){
            Bundle extras = data.getExtras();
            Bitmap bm = (Bitmap) extras.get("data");
            binding.profileImage.setImageBitmap(bm);
            File dir = Environment.getExternalStorageDirectory();
        }
    }

    @Override
    public void onRequestFinished(String id, JSONObject body) {
        if (id.equals(getString(R.string.api_user_info))) {
            try {
                final String msg = body.getString("msg");
                if (body.getBoolean("success")) {
                    JSONObject info = body.getJSONObject("msg");
                    loadInfo(info);
                } else {
                    Log.e(TAG, body.getString("msg"));
                    runOnUiThread(()-> Toast.makeText(context, msg,
                                    Toast.LENGTH_SHORT).show());
                }
            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(context, "An error occured",
                                Toast.LENGTH_SHORT).show());
            }
        } else if (id.equals(getString(R.string.api_user_update))) {
            try {
                final String msg = body.getString("msg");
                if (body.getBoolean("success")) {
                    saveUserUiUpdate();
                } else {
                    Log.e(TAG, body.getString("msg"));
                    runOnUiThread(() -> Toast.makeText(context, msg,
                                    Toast.LENGTH_SHORT).show());
                }
            } catch (JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(() ->Toast.makeText(context, "An error occurred",
                                Toast.LENGTH_SHORT).show());
            }
        }
    }

    @Override
    public void onDialogTextSelect(int id, String dialog) {
        try {
            if (dialog_id == R.id.country) {
                if (!clientUser.getCountry().getString("name").equals(dialog)) {
                    mCountry.setText(dialog);
                    geoDialog.country_id = id + 1;
                    mState.setText("Select");
                    mCity.setText("Select");
                }
            } else if (dialog_id == R.id.state) {
                if (!clientUser.getCountry().getString("name").equals(dialog)) {
                    mState.setText(dialog);
                    geoDialog.state_id = State.stateId(geoDialog.states, dialog);
                    mCity.setText("Select");
                }
            } else if (dialog_id == R.id.city) {
                mCity.setText(dialog);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void loadInfo(JSONObject u) throws JSONException {
        final JSONObject info = u;
        final String email = Utils.getStringSharedPreferences(context,
                context.getString(R.string.user_preference_email),
                "",
                context.getString(R.string.user_preference));
        clientUser.setEmail(email);
        //Set up clientUser object with info
        clientUser.setDisplayName(info.getString("displayName"));
        clientUser.setAddress(info.getString("address"));
        clientUser.setCity(info.getString("city"));
        clientUser.setZipCode(info.getString("zipCode"));
        clientUser.setCountry(info.getString("country"));
        clientUser.setState(info.getString("state"));
        geoDialog.state_id = clientUser.getState().getInt("id");
        geoDialog.country_id = clientUser.getCountry().getInt("id");
        //Run thread to change info on UI
        runOnUiThread(() -> {
            mEmail.setText(email);
            try {
                mDisplayName.setText(clientUser.getDisplayName());
                mAddress.setText(clientUser.getAddress());
                mCity.setText(clientUser.getCity());
                mState.setText(clientUser.getState().getString("name"));
                mCountry.setText(clientUser.getCountry().getString("name"));
                mZipCode.setText(clientUser.getZipCode());
            } catch (JSONException e) {
                Log.e(TAG, "Json parsing err: " + e.getMessage());
            }
        });
    }

    private void saveUser() throws JSONException {
        if (!validate()) {
            return;
        }
        File img = new File(
                Environment.getExternalStorageDirectory(),
                "Pictures/profiles.png");
        try (FileOutputStream out = new FileOutputStream(img)){
            Bitmap bm = ((BitmapDrawable)binding.profileImage.getDrawable()).getBitmap();
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e){
            e.printStackTrace();
            Log.e(TAG,"IO ERROR");
        }
        clientUser.setDisplayName(mDisplayName.getText().toString());
        clientUser.setAddress(mAddress.getText().toString());
        clientUser.setCity(mCity.getText().toString());
        clientUser.setState(geoDialog.state_id,
                mState.getText().toString());
        clientUser.setCountry(geoDialog.country_id,
                mCountry.getText().toString());
        clientUser.setZipCode(mZipCode.getText().toString());
        clientUser.setEmail(Utils.getStringSharedPreferences(context,
                context.getString(R.string.user_preference_email),
                "",
                context.getString(R.string.user_preference)));
        JSONObject body = new JSONObject();
        body.put("info", clientUser.toJSON());
        Http2Request req = new Http2Request(context);
        req.put(getString(R.string.api_base_url), getString(R.string.api_user_update),
                ClientUser.getToken(context), body.toString());
    }

    private void saveUserUiUpdate() {
        runOnUiThread(() -> {
            findViewById(R.id.editFloatingBtn).clearAnimation();
            fab.setImageResource(R.drawable.ic_edit_pastel_64dp);
            mDisplayName.setFocusable(false);
            mEmail.setFocusable(false);
            mAddress.setFocusable(false);
            mZipCode.setFocusable(false);
            boolFab = false;
            Toast.makeText(context, "Saved Successful",
                    Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validate() {
        boolean valid = true;

        String displayName = mDisplayName.getText().toString();
        if (TextUtils.isEmpty(displayName)) {
            mDisplayName.setError("Required.");
            valid = false;
        }

//        String mobile = mMobile.getText().toString();
//        if (TextUtils.isEmpty(mobile)) {
//            mMobile.setError("Required.");
//            valid=false;
//        }
//
//        String carrier = mCarrier.getText().toString();
//        if (TextUtils.isEmpty(carrier)) {
//            mCarrier.setError("Required.");
//            valid=false;
//        }
        String address = mAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            mAddress.setError("Required.");
            valid = false;
        }

        String country = mCountry.getText().toString();
        if (TextUtils.isEmpty(country)) {
            mCountry.setError("Required.");
            valid = false;
        }

        String city = mCity.getText().toString();
        if (city.equals("Select")) {
            mCity.setError("Required.");
            valid = false;
        }

        String state = mState.getText().toString();
        if (state.equals("Select")) {
            mState.setError("Required.");
            valid = false;
        }

        String zipCode = mZipCode.getText().toString();
        if (TextUtils.isEmpty(zipCode)) {
            mZipCode.setError("Required.");
            valid = false;
        } else if (!Utils.isValidZipCode(zipCode)) {
            mZipCode.setError("Invalid Zip.");
            valid = false;
        }

        return DEBUG || valid;
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
