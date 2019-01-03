package com.example.arnesh07.help;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileFrag extends Fragment {
    DatabaseReference mRef;
    profileListener caller;
    FirebaseAuth mAuth;
    public interface profileListener{

     }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            caller = (profileListener) getActivity();
        }
        catch (ClassCastException e){ throw new ClassCastException(getActivity().toString()); }
    }
    TextView name,email,contact;
     String namePro,emailPro,contactPro;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.profile,container,false);
         name=v.findViewById(R.id.name);
         email=v.findViewById(R.id.emailPro);
         contact=v.findViewById(R.id.contact);
        mRef= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            User user2 = dataSnapshot.getValue(User.class);
                            //System.out.println(user.email);
                            name.setText("Name: "+user2.name);
                            email.setText("Email: "+user2.email);
                            contact.setText("Contact: "+user2.contact);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v("Error","Error");
                    }
                });
        return v;
    }
}
