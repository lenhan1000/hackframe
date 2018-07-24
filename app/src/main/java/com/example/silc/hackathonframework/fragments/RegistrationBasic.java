package com.example.silc.hackathonframework.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.models.User;
import com.hbb20.CountryCodePicker;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationBasic.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationBasic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationBasic extends Fragment {
    private static final String TAG = "RegistrationBasic";
    private static final boolean DEBUG = true;

    private User user;

    private EditText mDisplayNameField;
    private EditText mMobileField;
    private EditText mCarrierField;
    private CountryCodePicker mCountryCodeField;

    // the fragment initialization parameters
    private static final String ARG_USER = "User";

    private OnFragmentInteractionListener mListener;

    public RegistrationBasic() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment RegistrationBasic.
     */
    public static RegistrationBasic newInstance(Parcelable user) {
        RegistrationBasic fragment = new RegistrationBasic();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration_basic, container, false);

        //Views
        mDisplayNameField = view.findViewById(R.id.dname);
        mCountryCodeField = view.findViewById(R.id.countryCode);
        mMobileField = view.findViewById(R.id.mobile);
        mCarrierField = view.findViewById(R.id.carrier);


        //Buttons
        view.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(
                        mDisplayNameField.getText().toString(),
                        mCountryCodeField.getSelectedCountryCode(),
                        mMobileField.getText().toString(),
                        mCarrierField.getText().toString()
                );
            }
        });

        return view;
    }


    public void onButtonPressed(String dname, String code, String mobile, String carrier){
        Log.d(TAG, "nextButtonPressed");
        if(!validateForm()){
            return;
        }
        Log.d(TAG, "nextFragmentInit");
        //Construct a User object
        user.setDisplayName(dname);
        user.setCountryCode(code);
        user.setmPhone(mobile);
        user.setCarrier(carrier);

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
        // TODO: Update argument type and name
        void onFragmentInteraction(String id,User user);
    }

    private boolean validateForm() {
        boolean valid = true;

        String displayName = mDisplayNameField.getText().toString();
        if (TextUtils.isEmpty(displayName)) {
            mDisplayNameField.setError("Required.");
            valid=false;
        }else{
            mDisplayNameField.setError(null);
        }

        String mobile = mMobileField.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            mMobileField.setError("Required.");
            valid=false;
        }else{
            mMobileField.setError(null);
        }

        String carrier = mCarrierField.getText().toString();
        if (TextUtils.isEmpty(carrier)) {
            mCarrierField.setError("Required.");
            valid=false;
        }else{
            mCarrierField.setError(null);
        }

        if (DEBUG){return true;}
        return valid;
    }
}
