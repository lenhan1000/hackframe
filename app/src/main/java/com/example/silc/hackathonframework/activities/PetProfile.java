package com.example.silc.hackathonframework.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.aigestudio.wheelpicker.core.AbstractWheelPicker;
import com.aigestudio.wheelpicker.view.WheelCurvedPicker;
import com.aigestudio.wheelpicker.view.WheelStraightPicker;
import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.databinding.ActivityPetProfileBinding;
import com.example.silc.hackathonframework.helpers.Http2Request;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.Pet;
import com.example.silc.hackathonframework.models.User;
import com.github.shchurov.horizontalwheelview.HorizontalWheelView;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;



public class PetProfile extends AppBarActivity {
    private static final String TAG = "activities.PetProfile";
    private static final int AGE_LIMIT = Pet.getAgeLimit();
    private ActivityPetProfileBinding binding;
    private String petInfoRoute;
    private Pet pet;
    private boolean boolFab;
    private String selectString;
    private double selectDouble;
    private String catBreedRoute;
    private String dogBreedRoute;

    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_SELECT_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(),
                R.layout.activity_pet_profile,
                mContentFrame,
                true);
        pet = new Pet();
        try {
            petInfoRoute = Pet.getPetInfo(this);
        }catch(JSONException e){
            e.printStackTrace();
        }

        boolFab = false;
        setUpFab();
        disableEdit();
    }

    @Override
    public void onRequestFinished(String id, JSONObject res){
        try {
            boolean success = res.getBoolean("success");
            if (!success) Log.d(TAG, res.getString("msg"));
            else {
                if (id == petInfoRoute){
                    pet.setJSON(
                            res.getJSONObject("msg")
                    );
                    binding.setPet(pet);
                }
                else if(id == catBreedRoute || id == dogBreedRoute){
                    final String[] info = Utils.getStringArrayFromJSONArray(
                            res.getJSONArray("msg"));
                    runOnUiThread(()-> popBreed(info));
                }
                else if (id == getString(R.string.api_pet_update_info)){
                    Log.d(TAG, "Saved Successful");
                    runOnUiThread(() -> Toast.makeText(context, "Saved Successfull",
                            Toast.LENGTH_SHORT).show());
                }
            }
        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SELECT_IMAGE && data != null) {
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
        }
    }

    private void setUpFab(){
        //BIND FLOATING BUTTON
        binding.editFloatingBtn.setOnClickListener((View v) -> {
            if(!boolFab){
                binding.editFloatingBtn.clearAnimation();
                binding.editFloatingBtn.setImageResource(R.drawable.ic_save_pastel_64dp);
                enableEdit();
                boolFab = true;
                Toast.makeText(context, "Select to Edit",
                        Toast.LENGTH_SHORT).show();
            } else {
                try{
                    savePet();
                    disableEdit();
                    boolFab = false;
                    binding.editFloatingBtn.setImageResource(R.drawable.ic_edit_pastel_64dp);
                } catch (JSONException e){
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(context, "An error occured",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void enableEdit(){
        binding.name.setFocusableInTouchMode(true);
        binding.age.setOnClickListener((View v) -> popAge());
        binding.birth.setOnClickListener((View v)-> popBirth());

        binding.breed.setOnClickListener((View v) -> {
            catBreedRoute = getString(R.string.api_pet_cat_breed);
            dogBreedRoute = getString(R.string.api_pet_dog_breed);
            pet.getBreedList(context);
        });

        binding.weight.setOnClickListener((View v) -> popWeight());

        binding.spayed.setOnClickListener((View v) -> {
            if(pet.getSpayed()){
                pet.setSpayed(false);
            }else{
                pet.setSpayed(true);
            }
        });

        binding.gender.setOnClickListener((View v) ->{
            if(pet.getGender()){
                pet.setGender(false);
            }else{
                pet.setGender(true);
            }
        });

        binding.profileImage.setOnClickListener((View v) -> takePhoto());
    }

    private void disableEdit(){
        binding.name.setFocusableInTouchMode(false);
        binding.age.setClickable(false);
        binding.birth.setClickable(false);
        binding.breed.setClickable(false);
        binding.weight.setClickable(false);
        binding.spayed.setClickable(false);
        binding.gender.setClickable(false);

        //REMOVE ONCLICKLISTENER
        binding.name.setFocusable(false);
        binding.age.setOnClickListener(null);
        binding.birth.setOnClickListener(null);
        binding.breed.setOnClickListener(null);
        binding.weight.setOnClickListener(null);
        binding.spayed.setOnClickListener(null);
        binding.gender.setOnClickListener(null);
    }

    private void savePet() throws JSONException{
        if(!validate()){
            return;
        }
        pet.setName(binding.name.getText().toString());
        pet.setBreed(binding.breed.getText().toString());
        JSONObject body = new JSONObject();
        body.put("info", pet.toJSON());
        body.put("petId", Pet.getSelectedId(this));
        Http2Request req = new Http2Request(this);
        req.put(req.baseUrl, getString(R.string.api_pet_update_info),
                body.toString(), User.getToken(context));
    }

    private boolean validate(){
        boolean valid  = true;
        String name = binding.name.getText().toString();
        if(name.isEmpty()){
            binding.name.setError("Can't be blank");
            valid = false;
        }
        return valid;
    }

    private void popBreed(String[] list){
        View popView = getLayoutInflater().inflate(
                R.layout.activity_pet_registration_breed,
                null);
        PopupWindow window = new PopupWindow(popView,
                (int) (binding.contentLayout.getWidth()*.9),
                (int) (binding.contentLayout.getHeight()*.9),
                true);
        window.setBackgroundDrawable(getDrawable(R.drawable.popup_window_background));
        final WheelStraightPicker wheelView = popView.findViewById(R.id.infoView);
        wheelView.setData(Arrays.asList(list));
        wheelView.setOnWheelChangeListener(new AbstractWheelPicker.SimpleWheelChangeListener() {
            @Override
            public void onWheelSelected(int i, String s) {
                selectString = s;
            }
        });
        popView.findViewById(R.id.nextButton).setOnClickListener((View v) -> {
            pet.setBreed(selectString);
            window.dismiss();
        });
        window.showAtLocation(binding.contentLayout,
                Gravity.CENTER, 0, 0);
    }

    private void popAge(){
        String[] list = new String[AGE_LIMIT];
        for (int i = 0; i < AGE_LIMIT; i++){
            list[i] = Integer.toString(i);
        }
        View popView = getLayoutInflater().inflate(
                R.layout.activity_pet_registration_age,
                null);
        PopupWindow window = new PopupWindow(popView,
                (int) (binding.contentLayout.getWidth()*.9),
                (int) (binding.contentLayout.getHeight()*.9),
                true);
        window.setBackgroundDrawable(getDrawable(R.drawable.popup_window_background));
        final WheelCurvedPicker wheelView = popView.findViewById(R.id.infoView);
        wheelView.setData(Arrays.asList(list));
        wheelView.setVerticalScrollbarPosition((int) pet.getAge());
        wheelView.setOnWheelChangeListener(new AbstractWheelPicker.SimpleWheelChangeListener() {
            @Override
            public void onWheelSelected(int i, String s) {
                selectDouble = (double) i;
            }
        });
        popView.findViewById(R.id.nextButton).setOnClickListener((View v) -> {
            pet.setAge(selectDouble);
            window.dismiss();
        });
        window.showAtLocation(binding.contentLayout,
                Gravity.CENTER, 0, 0);
    }

    private void popWeight(){
        View popView = getLayoutInflater().inflate(
                R.layout.activity_pet_registration_weight,
                null);
        PopupWindow window = new PopupWindow(popView,
                (int) (binding.contentLayout.getWidth()*.9),
                (int) (binding.contentLayout.getHeight()*.9),
                true);
        window.setBackgroundDrawable(getDrawable(R.drawable.popup_window_background));
        final HorizontalWheelView wheelView = popView.findViewById(R.id.infoView);
        final TextView weight = popView.findViewById(R.id.weight);
        wheelView.setOnlyPositiveValues(true);
        weight.setText(
                String.format("%.2f lbs",pet.getWeight())
        );
        wheelView.setDegreesAngle(pet.getWeight());
        wheelView.setListener(new HorizontalWheelView.Listener(){
            @Override
            public void onRotationChanged(double radians){
                String w = String.format("%.2f lbs", wheelView.getDegreesAngle());
                weight.setText(w);
                selectDouble = wheelView.getDegreesAngle();
            }
        });
        popView.findViewById(R.id.nextButton).setOnClickListener((View v) -> {
            pet.setWeight(selectDouble);
            window.dismiss();
        });
        window.showAtLocation(binding.contentLayout,
                Gravity.CENTER, 0, 0);
    }

    private void popBirth(){
        View popView = getLayoutInflater().inflate(
                R.layout.activity_pet_registration_birth,
                null);
        PopupWindow window = new PopupWindow(popView,
                (int) (binding.contentLayout.getWidth()*.9),
                (int) (binding.contentLayout.getHeight()*.9),
                true);
        window.setBackgroundDrawable(getDrawable(R.drawable.popup_window_background));
        final DatePicker dateView = popView.findViewById(R.id.infoView);
        DateTime dateJ = new DateTime(pet.getBirth().getTime());
        dateView.updateDate(dateJ.year().get(),
                dateJ.monthOfYear().get(),
                dateJ.dayOfMonth().get());
        popView.findViewById(R.id.nextButton).setOnClickListener((View v) -> {
            Date date = new GregorianCalendar(dateView.getYear(),
                    dateView.getMonth() - 1,
                    dateView.getDayOfMonth())
                    .getTime();
            pet.setBirth(date);
            window.dismiss();
        });
        window.showAtLocation(binding.contentLayout,
                Gravity.CENTER, 0, 0);
    }


    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }}
