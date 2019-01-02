package com.example.arnesh07.help;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.example.arnesh07.help.MainActivity;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    DatabaseReference mRef;
    FirebaseAuth mAuth;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken=FirebaseInstanceId.getInstance().getToken();
        mRef= FirebaseDatabase.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()==null){
            return;
        }
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
       mRef.child("Users").child(userId).child("token").setValue(refreshedToken);

    }

}
