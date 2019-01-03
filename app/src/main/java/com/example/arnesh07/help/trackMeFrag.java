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

public class trackMeFrag extends Fragment {

    trackMeListener caller;

    public interface trackMeListener{
        void callTimePicker();
        void signOut();
        void displayProfile();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            caller = (trackMeListener) getActivity();
        }
        catch (ClassCastException e){ throw new ClassCastException(getActivity().toString()); }
    }

    Button trackMe;
    Button signOut;
    Button profile;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.trackme,container,false);

        trackMe=v.findViewById(R.id.trackMe);
        signOut=v.findViewById(R.id.signOut);
        profile=v.findViewById(R.id.profile);

        trackMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caller.callTimePicker();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caller.signOut();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caller.displayProfile();
            }
        });
        return v;
    }
}
