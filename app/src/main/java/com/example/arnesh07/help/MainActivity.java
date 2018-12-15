package com.example.arnesh07.help;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements signup.signUpListener , login.loginListener , EmailInputFragment.EmailInputFragmentListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FrameLayout container;

     FirebaseAuth mAuth;
     FirebaseDatabase database;
     DatabaseReference mRef;
     FirebaseAuth.AuthStateListener authStateListener;

    signup signupFrag=new signup();
    login loginFrag=new login();
    EmailInputFragment emailInputFragment=new EmailInputFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference();
        container=findViewById(R.id.container);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


        fragmentTransaction.add(R.id.container,signupFrag);
        fragmentTransaction.commit();


    }

    @Override
    public void register(final String Name, final String Contact, final String Email, String Password, final ProgressBar progressBar) {

        if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Contact) || TextUtils.isEmpty(Name))
        {
            Toast.makeText(MainActivity.this, "Fill All Fields!", Toast.LENGTH_LONG).show();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(com.example.arnesh07.help.MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this,"Already Registered",Toast.LENGTH_LONG).show();
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Check E-mail and Password", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, "Auth Done",Toast.LENGTH_SHORT).show();
                        User user = new User(Name,Email,Contact);
                        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        mRef.child("Users").child(userId)
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(com.example.arnesh07.help.MainActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.container,emailInputFragment);
                                    fragmentTransaction.commit();

                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(com.example.arnesh07.help.MainActivity.this,"Failure",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            });
        }
    }

    @Override
    public void goToLogin() {

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,loginFrag);
        fragmentTransaction.commit();
    }

    @Override
    public void checkUser() {
        if(mAuth.getCurrentUser()!=null){
            Toast.makeText(com.example.arnesh07.help.MainActivity.this,"Already Signed-In",Toast.LENGTH_LONG).show();
            //update UI

        }
    }

    @Override
    public void goToSignUp() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,signupFrag);
        fragmentTransaction.commit();
    }

    @Override
    public void login(String email, String password, final ProgressBar mProgressBar) {
        if(email.isEmpty() || password.isEmpty()){
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,"Fill fields properly",Toast.LENGTH_SHORT).show();
        }
        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this,"Successful",Toast.LENGTH_LONG).show();
                        //  Intent i3=new Intent(v.getContext(),TrackMe.class);
                        // i3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // startActivity(i3);
                    }
                    else{
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this,"Incorrect E-mail or Password",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void addEmails(final String Email1,final String Email2,final String Email3,final String Email4,final String Email5) {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference newRef = mRef.child("Users").child(userId);
        newRef.child("Email-1").setValue(Email1);
        newRef.child("Email-2").setValue(Email2);
        newRef.child("Email-3").setValue(Email3);
        newRef.child("Email-4").setValue(Email4);
        newRef.child("Email-5").setValue(Email5).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(com.example.arnesh07.help.MainActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                //update UI

            }
        });

    }
}
