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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationTrackFrag extends Fragment {

    Button help,stop;

    LocationTrackFragListener caller;
    public interface LocationTrackFragListener{
    void sendHelp();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            caller = (LocationTrackFragListener) context;
        }
        catch (ClassCastException e){ throw new ClassCastException(getActivity().toString()); }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.help,container,false);

         help=v.findViewById(R.id.help);
         stop=v.findViewById(R.id.stop);

         help.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
              caller.sendHelp();
             }
         });

        return v;
    }
}
