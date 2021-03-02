package com.gmail.carlos.map524_assignment1_carlosfalconett;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.app.Fragment;


import org.w3c.dom.Text;

public class FirstFragment extends Fragment {

    TextView question;
    int textID;
    int colorID;
    Bundle dataBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.first_fragment,container,false);

        question = (TextView) view.findViewById(R.id.questionText);

        dataBundle = getArguments();
        textID = dataBundle.getInt("textID");
        colorID = dataBundle.getInt("colorID");
        //Log.d("colorID", "Value is: " + colorID);

        question.setText(textID);
        question.setBackgroundColor(getResources().getColor(colorID));

        return view;
    }
}
