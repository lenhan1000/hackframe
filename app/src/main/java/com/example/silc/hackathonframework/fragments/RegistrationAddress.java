package com.example.silc.hackathonframework.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.activities.Registration;
import com.example.silc.hackathonframework.helpers.Utils;
import com.example.silc.hackathonframework.models.City;
import com.example.silc.hackathonframework.models.Country;
import com.example.silc.hackathonframework.models.State;
import com.example.silc.hackathonframework.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationAddress.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationAddress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationAddress extends Fragment implements SingleChoiceDialogFragment.NoticeDialogListener, View.OnClickListener{
    private static final String TAG = "RegistrationAddress";
    private static final boolean DEBUG = false;

    private int dialog_id;
    private int country_id;
    private int state_id;
    private User user;
    private ArrayList<State> states;



    private EditText mAddressField;
    private EditText mZipCodeField;
    private TextView mCountryList;
    private TextView mStateList;
    private TextView mCityList;

    // the fragment initialization parameters
    private static final String ARG_USER = "User";


    private OnFragmentInteractionListener mListener;

    public RegistrationAddress() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment RegistrationAddress.
     */
    public static RegistrationAddress newInstance(Parcelable user) {
        RegistrationAddress fragment = new RegistrationAddress();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(ARG_USER);
        }

        country_id = 0;
        state_id = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_address, container, false);

        //Views
        mCountryList = view.findViewById(R.id.country);
        mCityList = view.findViewById(R.id.city);
        mStateList = view.findViewById(R.id.state);
        mAddressField = view.findViewById(R.id.address);
        mZipCodeField = view.findViewById(R.id.zipcode);

        //Button
        view.findViewById(R.id.nextButton).setOnClickListener(this);
        mCountryList.setOnClickListener(this);
        mStateList.setOnClickListener(this);
        mCityList.setOnClickListener(this);

        return view;
    }

    public void onButtonPressed(String address,
                                String country, String state,
                                String city, String zipcode) {
        Log.d(TAG, "nextButtonPressed");
        if(!validateForm()){
            return;
        }
        Log.d(TAG, "nextFragmentInit");
        //Construct a User object
        user.setAddress(address);
        user.setCountry(country);
        user.setState(state);
        user.setCity(city);
        user.setZipCode(zipcode);

        //Pass object and start new fragment
        this.mListener.onFragmentInteraction(TAG, user);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String id, User user);
    }

    @Override
    public void onClick(View v){
        int i = v.getId();
        if (i == R.id.country){
            dialog_id = R.id.country;
            pop_country_dialog();

        }else if (i == R.id.state) {
            dialog_id = R.id.state;
            if (country_id > 0)
                pop_state_dialog(country_id);
            else
                Toast.makeText(getActivity(), "Select Country First",
                        Toast.LENGTH_SHORT).show();
        }else if (i == R.id.city) {
            dialog_id = R.id.city;
            if(state_id > 0 || country_id > 0)
                pop_city_dialog(state_id);
            else
                Toast.makeText(getActivity(), "Select Country or State First",
                        Toast.LENGTH_SHORT).show();
        }else if (i== R.id.nextButton){
            onButtonPressed(
                    mAddressField.getText().toString(),
                    mCountryList.getText().toString(),
                    mStateList.getText().toString(),
                    mCityList.getText().toString(),
                    mZipCodeField.getText().toString());
        }
    }

    @Override
    public void onDialogTextSelect(int id, String dialog){
        if (dialog_id == R.id.country){
            mCountryList.setText(dialog);
            country_id = id + 1;
            Log.d(TAG, "Country ID: " + Integer.toString(country_id));
        }else if (dialog_id == R.id.state){
            mStateList.setText(dialog);
            state_id = State.stateId(states, dialog);
            Log.d(TAG, "State ID: " + Integer.toString(state_id));
        }else if (dialog_id == R.id.city){
            mCityList.setText(dialog);
        }
    }

    private boolean validateForm() {
        boolean valid = true;
        return valid;
    }

    private void pop_country_dialog(){
        ArrayList<Country> countries = null;
        String jsonStr = Utils.loadJSONFromAsset(getActivity(), "countries.json");
        try {
            countries = Country.jsonToCountry(Utils
                    .getArrayListFromJSONArray(new JSONObject(jsonStr)
                            .getJSONArray("countries")));
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }
        ArrayList<String> countries_name = Country.toArrayStrings(countries);
        SingleChoiceDialogFragment dialog = new SingleChoiceDialogFragment();
        Bundle data = new Bundle();
        data.putStringArrayList("list", countries_name);
        data.putString("title", "Select a Country");
        dialog.setArguments(data);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        dialog.show(ft,null);
    }

    private void pop_state_dialog(int cid){
        String jsonStr = Utils.loadJSONFromAsset(getActivity(), "states.json");
        try {
            states = State.jsonToState(Utils
                    .getArrayListFromJSONArray(new JSONObject(jsonStr)
                            .getJSONArray("states")));
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return;
        }
        states = State.countryToStates(states, cid);
        ArrayList<String> states_name = State.toArrayStrings(states);
        SingleChoiceDialogFragment dialog = new SingleChoiceDialogFragment();
        Bundle data = new Bundle();
        data.putStringArrayList("list", states_name);
        data.putString("title", "Select a State/Region");
        dialog.setArguments(data);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft,null);
    }

    private void pop_city_dialog(int sid){
        ArrayList<City> cities;
        String jsonStr = Utils.loadJSONFromAsset(getActivity(), "cities.json");
        try {
            cities = City.jsonToCity(Utils
                    .getArrayListFromJSONArray(new JSONObject(jsonStr)
                            .getJSONArray("cities")));
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return;
        }
        cities = City.stateToCities(cities, sid);
        ArrayList<String> cities_name = City.toArrayStrings(cities);
        SingleChoiceDialogFragment dialog = new SingleChoiceDialogFragment();
        Bundle data = new Bundle();
        data.putStringArrayList("list", cities_name);
        data.putString("title", "Select a City");
        dialog.setArguments(data);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        dialog.show(ft,null);
    }
}
