package com.example.silc.hackathonframework.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.silc.hackathonframework.R;
import com.example.silc.hackathonframework.activities.Dashboard;
import com.example.silc.hackathonframework.activities.Registration;
import com.example.silc.hackathonframework.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrationCred.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrationCred#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationCred extends Fragment{
    private static final String TAG = "Registration_CRED";
    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;
    private OnFragmentInteractionListener mListener;

    public RegistrationCred() {
        // Required empty public constructor
    }

    public static RegistrationCred newInstance(User user) {
        RegistrationCred fragment = new RegistrationCred();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_registration_cred, container, false);
        // Inflate the layout for this fragment
        mEmailField = getView().findViewById(R.id.email);
        mPasswordField = getView().findViewById(R.id.password);

        getView().findViewById(R.id.signUpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mEmailField.getText().toString() ,mPasswordField.getText().toString());
            }
        });
        return root_view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        void onFragmentInteraction(Uri uri);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in ic_user_black_24dp's information
//                            Log.d(TAG, "createUserWithEmail:success");
//                            FirebaseUser userauth = mAuth.getCurrentUser();
//                            User user = new User(mDisplayNameField.getText().toString(),
//                                    mMobileField.getText().toString(),
//                                    mCarrierField.getText().toString(),
//                                    mZipCodeField.getText().toString(),
//                                    mAddressField.getText().toString(),
//                                    mCountryList.getText().toString(),
//                                    mStateList.getText().toString(),
//                                    mCityList.getText().toString(),
//                                    mCountryCode.getSelectedCountryCode().toString()
//                            );
//                            mDatabase = FirebaseDatabase.getInstance().getReference("Users");
//                            mDatabase.child(userauth.getUid()).setValue(user);
//
//                            Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//                            startActivity(intent);
//                        } else {
//                            // If sign in fails, display a message to the ic_user_black_24dp.
//                            try {
//                                throw task.getException();
//                            }catch (FirebaseAuthWeakPasswordException e){
//                                Toast.makeText(Registration.this, "Weak Password",
//                                        Toast.LENGTH_SHORT).show();
//                            }catch(FirebaseAuthInvalidCredentialsException e){
//                                Toast.makeText(Registration.this, "Invalid Email",
//                                        Toast.LENGTH_SHORT).show();
//                            }catch(FirebaseAuthUserCollisionException e){
//                                Toast.makeText(Registration.this, "Email already used",
//                                        Toast.LENGTH_SHORT).show();
//                            }catch(Exception e){
//                                Log.e(TAG, e.getMessage());
//                            }
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//
//                        }
//
//                    }
//                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }
        return valid;
    }
}
