package com.example.arnesh07.help;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email2,password2;
    Button Login2,SignUp2;
    FirebaseAuth mAuth;
    String email,password;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email2=findViewById(R.id.email2);
        mAuth=FirebaseAuth.getInstance();
        password2=findViewById(R.id.password2);
        Login2=findViewById(R.id.Login2);
        SignUp2=findViewById(R.id.SignUp2);
        mProgressBar=findViewById(R.id.bar);
        mProgressBar.setVisibility(View.GONE);

        SignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2=new Intent(v.getContext(),SignUp.class);
                startActivity(i2);
            }
        });
        Login2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                email=email2.getText().toString().trim();
                password=password2.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty()){
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(Login.this,"Fill fields properly",Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this,"Successful",Toast.LENGTH_LONG).show();
                          //  Intent i3=new Intent(v.getContext(),TrackMe.class);
                           // i3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                           // startActivity(i3);
                        }
                        else{
                            mProgressBar.setVisibility(View.GONE);
                            Toast.makeText(Login.this,"Incorrect E-mail or Password",Toast.LENGTH_LONG).show();
                        }
                        }
                    });
                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(mAuth.getCurrentUser()!=null){
            //Intent intent = new Intent(SignUp.class,TrackMe.class);
            // startActivity(intent);
            //finish();
            Toast.makeText(com.example.arnesh07.help.Login.this,"Already",Toast.LENGTH_SHORT).show();
        }
    }
}
