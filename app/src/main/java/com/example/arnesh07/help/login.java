package com.example.arnesh07.help;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class login extends Fragment {

    EditText email2,password2;
    Button Login2,SignUp2;
    String email,password;
    ProgressBar mProgressBar;

    loginListener caller;

    public interface loginListener{
        void goToSignUp();
        void checkUser();
        void login(String email,String password,ProgressBar mProgressBar);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            caller = (loginListener) getActivity();
        }
        catch (ClassCastException e){ throw new ClassCastException(getActivity().toString()); }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_login,container,false);
        email2=v.findViewById(R.id.email2);
        password2=v.findViewById(R.id.password2);
        Login2=v.findViewById(R.id.Login2);
        SignUp2=v.findViewById(R.id.SignUp2);
        mProgressBar=v.findViewById(R.id.bar);
        mProgressBar.setVisibility(View.GONE);

        SignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caller.goToSignUp();
            }
        });
        Login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                email = email2.getText().toString().trim();
                password = password2.getText().toString().trim();

                caller.login(email,password,mProgressBar);
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        caller.checkUser();
    }
}
