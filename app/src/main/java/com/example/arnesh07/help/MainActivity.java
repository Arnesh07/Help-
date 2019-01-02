package com.example.arnesh07.help;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AlarmManagerCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements signup.signUpListener , login.loginListener , EmailInputFragment.EmailInputFragmentListener , trackMeFrag.trackMeListener ,TimePickerFrag.timePickerListener , LocationTrackFrag.LocationTrackFragListener  {

     FragmentManager fragmentManager;
     FragmentTransaction fragmentTransaction;
     FrameLayout container;

    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback=new LocationCallback();
    LocationTrackingService mService;
    boolean mBound = false;
     String token;

    FirebaseAuth mAuth;
     FirebaseDatabase database;
     DatabaseReference mRef;
     FirebaseAuth.AuthStateListener authStateListener;
     public static String myEmail ;
     public static int count=0;
     public String latitude;
     public String longitude;
     public String email1;
     public String email2;
     public String email3;
     public String email4;
     public String email5;
    UserEmails userEmails=new UserEmails();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    MyReceiver myReceiver;

     signup signupFrag=new signup();
     login loginFrag=new login();
     EmailInputFragment emailInputFragment=new EmailInputFragment();
     trackMeFrag trackMeFrag=new trackMeFrag();
     DialogFragment timePickerFrag = new TimePickerFrag();
     LocationTrackFrag LocationTrackFrag=new LocationTrackFrag();

     final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION=101;
    final int MY_PERMISSIONS_REQUEST_INTERNET=102;

    final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationTrackingService.LocalBinder binder = (LocationTrackingService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email1=null;
        mAuth = FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference();
        container=findViewById(R.id.container);

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container,signupFrag);
        fragmentTransaction.commit();

        myReceiver = new MyReceiver();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationTrackingService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationTrackingService.ACTION_BROADCAST));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        mService.stopForeground(true);
        mService.removeLocationUpdates();
        mService.stopSelf();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }

        super.onStop();
    }

    @Override
    public void register(final String Name, final String Contact, final String Email, String Password, final ProgressBar progressBar) {

        if(TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Contact) || TextUtils.isEmpty(Name))
        {
            Toast.makeText(MainActivity.this, "Fill All Fields!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            myEmail=Email;
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(com.example.arnesh07.help.MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this,"Already Registered",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Check E-mail and Password", Toast.LENGTH_SHORT).show();
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
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            Toast.makeText(MainActivity.this,"You need to Sign-In",Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        //updateUI
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, trackMeFrag);
                        fragmentTransaction.commit();
                        return;
                    }
                    else{
                        Toast.makeText(MainActivity.this,"You need to Sign-In",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            };
        }
        Toast.makeText(MainActivity.this,"Already Signed In",Toast.LENGTH_SHORT).show();
        //updateUI
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, trackMeFrag);
        fragmentTransaction.commit();


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
                        Toast.makeText(MainActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container,trackMeFrag);
                        fragmentTransaction.commit();
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
        email1=Email1;
        email2=Email2;
        email3=Email3;
        email4=Email4;
        email5=Email5;
       // UserEmails userEmails=new UserEmails(Email1,Email2,Email3,Email4,Email5);
      /*  userEmails.setEmail1(Email1);
        userEmails.setEmail2(Email2);
        userEmails.setEmail3(Email3);
        userEmails.setEmail4(Email4);
        userEmails.setEmail5(Email5);  */
       // ((UserEmails)getApplication()).setEmail1(email1);

        //Log.v("addEmailsCheck",email1);
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference newRef = mRef.child("Users").child(userId);
        newRef.child("email1").setValue(Email1);
        newRef.child("email2").setValue(Email2);
        newRef.child("email3").setValue(Email3);
        newRef.child("email4").setValue(Email4);
        newRef.child("email5").setValue(Email5).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(com.example.arnesh07.help.MainActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                //update UI
                token=FirebaseInstanceId.getInstance().getToken();
                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                mRef.child("Users").child(userId).child("token").setValue(token);
                count=0;
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User2 user = snapshot.getValue(User2.class);
                                    //System.out.println(user.email);
                                    Log.v("EmailFound",user.email);
                                    Log.v("tokenFound",user.token);
                                   // Log.v("email1",userEmails.getEmail1());
                                    if(user.email.equals(email1)||user.email.equals(email2)||user.email.equals(email3)||user.email.equals(email4)||user.email.equals(email5)){
                                        Log.v("email","matched");
                                        if(count==0) {
                                            mRef.child("Users").child(userId).child("token1").setValue(user.token);
                                            count++;
                                        }
                                        else if(count==1) {
                                            mRef.child("Users").child(userId).child("token2").setValue(user.token);
                                            count++;
                                        }
                                        else if(count==2) {
                                            mRef.child("Users").child(userId).child("token3").setValue(user.token);
                                            count++;
                                        }
                                        else if(count==3) {
                                            mRef.child("Users").child(userId).child("token4").setValue(user.token);
                                            count++;
                                        }
                                        else if(count==4) {
                                            mRef.child("Users").child(userId).child("token5").setValue(user.token);
                                            count++;
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.v("Error","Error");
                            }
                        });

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,trackMeFrag);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public void callTimePicker() {
        timePickerFrag.show(fragmentManager,"TimePickerFrag");


    }

    @Override
    public void startCountdown(int secondsRemaining) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after timer is completed.
                Log.v("Timer","executed");
                // A handler that can be used to post to the main thread
                Handler mainHandler = new Handler(Looper.getMainLooper());

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        makeAToast();
                        mService.removeLocationUpdates();
                        unbindService(mServiceConnection);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container,trackMeFrag);
                        fragmentTransaction.commit();
                    }
                };
                mainHandler.post(myRunnable);
            }
        }, secondsRemaining*1000);
    }

    public void makeAToast(){
        Toast.makeText(MainActivity.this,"Timer Completed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToHelp() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,LocationTrackFrag)
               // .addToBackStack(null)
                .commit();
    }

    @Override  //@TargetApi(26)
    public void getLocation() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);

        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mService.requestLocationUpdates();
            //Calling service to request location updates.
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.INTERNET},
                    MY_PERMISSIONS_REQUEST_INTERNET);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this,"Please Grant Permission",Toast.LENGTH_LONG);
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
            case MY_PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this,"Please Grant Permission",Toast.LENGTH_LONG);
                }
                return;
            }
        }
    }


    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationTrackingService.EXTRA_LOCATION);
            if (location != null) {
                //Toast.makeText(MainActivity.this, Utils.getLocationText(location),
                     //   Toast.LENGTH_SHORT).show();
                latitude=Double.toString(location.getLatitude());
                longitude=Double.toString(location.getLongitude());
              // mRef.child("Users").child(userId).child("latitude").setValue(Double.toString(location.getLatitude()));
               // mRef.child("Users").child(userId).child("longitude").setValue(Double.toString(location.getLongitude()));
                Log.v("Latitude",Double.toString(location.getLatitude()));
                Log.v("Longitude",Double.toString(location.getLongitude()));
            }
        }
    }

    @Override
    public void sendHelp() {
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
         //Log.v("EMAIL1234",((UserEmails)getApplication()).getEmail1());
        /* count=0;
        FirebaseDatabase.getInstance().getReference().child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User2 user = snapshot.getValue(User2.class);
                            //System.out.println(user.email);
                            Log.v("EmailFound",user.email);
                            Log.v("tokenFound",user.token);
                            Log.v("email1",userEmails.getEmail1());
                            if(user.email.equals(email1)||user.email.equals(email2)||user.email.equals(email3)||user.email.equals(email4)||user.email.equals(email5)){
                                Log.v("email","matched");
                                if(count==0) {
                                    mRef.child("Users").child(userId).child("token1").setValue(user.token);
                                    count++;
                                }
                                else if(count==1) {
                                    mRef.child("Users").child(userId).child("token2").setValue(user.token);
                                    count++;
                                }
                                else if(count==2) {
                                    mRef.child("Users").child(userId).child("token3").setValue(user.token);
                                    count++;
                                }
                                else if(count==3) {
                                    mRef.child("Users").child(userId).child("token4").setValue(user.token);
                                    count++;
                                }
                                else if(count==4) {
                                    mRef.child("Users").child(userId).child("token5").setValue(user.token);
                                    count++;
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("Error","Error");
                    }
                });*/

        mRef.child("Users").child(userId).child("latitude").setValue(latitude);
        mRef.child("Users").child(userId).child("longitude").setValue(longitude);

    }

    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container,signupFrag);
        fragmentTransaction.commit();

    }
}
