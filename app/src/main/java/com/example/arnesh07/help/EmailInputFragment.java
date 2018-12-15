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

public class EmailInputFragment extends Fragment
{
    EditText InputEmail1,InputEmail2,InputEmail3,InputEmail4,InputEmail5;
    Button proceed;
    String Email1,Email2,Email3,Email4,Email5;

    EmailInputFragmentListener caller;

    public interface EmailInputFragmentListener{
      void addEmails(String Email1, String Email2,String Email3,String Email4,String Email5);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            caller = (EmailInputFragmentListener) getActivity();
        }
        catch (ClassCastException e){ throw new ClassCastException(getActivity().toString()); }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.layout_email_input_fragment, container, false);
        InputEmail1=v.findViewById(R.id.inputEmail1);
        InputEmail2=v.findViewById(R.id.inputEmail2);
        InputEmail3=v.findViewById(R.id.inputEmail3);
        InputEmail4=v.findViewById(R.id.inputEmail4);
        InputEmail5=v.findViewById(R.id.inputEmail5);
        proceed=v.findViewById(R.id.proceed);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email1=InputEmail1.getText().toString();
                Email2=InputEmail2.getText().toString();
                Email3=InputEmail3.getText().toString();
                Email4=InputEmail4.getText().toString();
                Email5=InputEmail5.getText().toString();
                caller.addEmails(Email1,Email2,Email3,Email4,Email5);
            }
        });

        return v;
    }

}
