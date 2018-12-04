package com.example.arnesh07.help;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity {
    EditText Email;
    EditText Password;
    EditText Name;
    EditText Contact;
    Button SignUp,Login;
    private ProgressBar progressBar;

   String email, password, name, contact;

    private FirebaseAuth mAuth;
     private FirebaseDatabase database;
   private DatabaseReference mRef;

   private FirebaseAuth.AuthStateListener authStateListener;


    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mAuth = FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference();

        Contact =  findViewById(R.id.Contact);
        Email = findViewById(R.id.email);
        Name =  findViewById(R.id.Name);
        Password =  findViewById(R.id.Password);
        SignUp=findViewById(R.id.SignUp);
        Login=findViewById(R.id.Login);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                   //Intent intent = new Intent(SignUp.this,TrackMe.class);
                    //startActivity(intent);
                    //finish();
                }
            }
        };
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),com.example.arnesh07.help.Login.class);
                startActivity(i);
            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Email.getText().toString();
                password = Password.getText().toString();
                name = Name.getText().toString();
                contact = Contact.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(name))
                {
                    Toast.makeText(SignUp.this, "Fill All Fields!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(com.example.arnesh07.help.SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(SignUp.this,"Already Registered",Toast.LENGTH_LONG).show();
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(SignUp.this, "Check E-mail and Password", Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(SignUp.this, "Auth Done",Toast.LENGTH_SHORT).show();
                                User user = new User(name,email,contact);
                                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                mRef.child("Users").child(userId)
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(com.example.arnesh07.help.SignUp.this,"Successful",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(com.example.arnesh07.help.SignUp.this,"Failure",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

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
            Toast.makeText(com.example.arnesh07.help.SignUp.this,"Already",Toast.LENGTH_SHORT).show();
        }
    }
}
