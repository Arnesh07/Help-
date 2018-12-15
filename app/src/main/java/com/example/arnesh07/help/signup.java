package com.example.arnesh07.help;

import android.content.Context;
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

public class signup extends Fragment {

    EditText Email;
    EditText Password;
    EditText Name;
    EditText Contact;
    Button SignUp,Login;
     ProgressBar progressBar;

    String email, password, name, contact;

    signUpListener caller;


    public interface signUpListener{
        void register(String Name,String Contact,String Email,String Password,ProgressBar progressBar);
        void goToLogin();
        void checkUser();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            caller = (signUpListener) getActivity();
        }
        catch (ClassCastException e){ throw new ClassCastException(getActivity().toString()); }
        }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup,container,false);
        Contact =  view.findViewById(R.id.Contact);
        Email = view.findViewById(R.id.email);
        Name =  view.findViewById(R.id.Name);
        Password = view.findViewById(R.id.Password);
        SignUp=view.findViewById(R.id.SignUp);
        Login=view.findViewById(R.id.Login);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               caller.register(Name.getText().toString(),Contact.getText().toString(),Email.getText().toString(),Password.getText().toString(),progressBar);
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caller.goToLogin();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        caller.checkUser();
    }
}
