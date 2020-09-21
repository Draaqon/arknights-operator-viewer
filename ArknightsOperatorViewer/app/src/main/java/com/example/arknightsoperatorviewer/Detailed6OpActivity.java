package com.example.arknightsoperatorviewer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Detailed6OpActivity extends AppCompatActivity
{
    ProfileFragment profFrag;
    Trust45Fragment trustFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //set view to 6 star activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed6op);

        //get operator form Activity_Main intent
        OperatorInfo.Operator45Star.Operator6Star op = (OperatorInfo.Operator45Star.Operator6Star) getIncomingIntent();
        //initialize operator variables
        String profString = op.getProfile();
        String clinical = op.getClinicalAnalysis();
        String trust1String = op.getTrust1();
        String trust2String = op.getTrust2();
        String trust3String = op.getTrust3();
        String trust4String = op.getTrust4();
        String promoString = op.getPromo();
        //put profile and trust strings into bundles so it can be sent to the fragments
        Bundle profBundle = new Bundle();
        profBundle.putString("Profile", profString);
        Bundle trustBundle = new Bundle();
        trustBundle.putString("Clinical", clinical);
        trustBundle.putString("Trust 1", trust1String);
        trustBundle.putString("Trust 2", trust2String);
        trustBundle.putString("Trust 3", trust3String);
        trustBundle.putString("Trust 4", trust4String);
        trustBundle.putString("Promo", promoString);

        //initialize fragments
        profFrag = new ProfileFragment();
        profFrag.setArguments(profBundle);
        trustFrag = new Trust45Fragment();
        trustFrag.setArguments((trustBundle));

        //fragment manager to display the fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl6Fragment, profFrag);
        transaction.commit();

        //initialize the buttons to navigate between fragments
        RadioButton profileRadio = findViewById(R.id.radio6Profile);
        RadioButton trustRadio = findViewById(R.id.radio6Trust);

        //listeners for the radio buttons
        profileRadio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fl6Fragment, profFrag);
                transaction.commit();
            }
        });

        trustRadio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fl6Fragment, trustFrag);
                transaction.commit();
            }
        });
    }

    //set the activity's images and text
    private OperatorInfo.Operator45Star.Operator6Star getIncomingIntent()
    {
        //receive the operator data from Activity_Main and then parse it to create the operator profile.
        OperatorInfo.Operator45Star.Operator6Star op = (OperatorInfo.Operator45Star.Operator6Star) getIntent().getSerializableExtra("operator");
        setImage(op);
        return op;
    }

    //nitty gritty text/image setting
    private void setImage(OperatorInfo.Operator45Star.Operator6Star op)
    {
        String name = op.getName();
        if (op.getName().toLowerCase().equals("ch\'en"))
        {
            name = "chen";
        }

        Log.d("NAME:", name);
        //setting operator name text
        TextView opName = findViewById(R.id.txt6Name);
        opName.setText(op.getName().toUpperCase());
        //setting class text
        opName = findViewById(R.id.txt6Class);
        opName.setText(op.getType().toUpperCase());
        //setting operator image
        int id = this.getResources().getIdentifier("com.example.arknightsoperatorviewer:drawable/" + name.toLowerCase(), null, null);
        ImageView imgView = findViewById(R.id.img6Pic);
        imgView.setImageResource(id);
        //setting class image
        imgView = findViewById(R.id.img6Class);
        id = this.getResources().getIdentifier("com.example.arknightsoperatorviewer:drawable/" + op.getType().toLowerCase(), null, null);
        imgView.setImageResource(id);
        //setting skill1
        imgView = findViewById(R.id.img6Skill1);
        id = this.getResources().getIdentifier("com.example.arknightsoperatorviewer:drawable/" + name.toLowerCase() + "_skill1", null, null);
        imgView.setImageResource(id);
        //setting skill2
        imgView = findViewById(R.id.img6Skill2);
        id = this.getResources().getIdentifier("com.example.arknightsoperatorviewer:drawable/" + name.toLowerCase() + "_skill2", null, null);
        imgView.setImageResource(id);
        //setting skill3
        imgView = findViewById(R.id.img6Skill3);
        id = this.getResources().getIdentifier("com.example.arknightsoperatorviewer:drawable/" + name.toLowerCase() + "_skill3", null, null);
        imgView.setImageResource(id);
        //setting affiliation image
        imgView = findViewById(R.id.img6Affiliation);
        id = this.getResources().getIdentifier("com.example.arknightsoperatorviewer:drawable/" + op.getAffiliation().toLowerCase() + "_half", null, null);
        imgView.setImageResource(id);
        //setting stars amount
        String stars = "";
        TextView layoutStars = findViewById(R.id.txt6Stars);
        for(int i = 0; i < op.getStars(); i++)
        {
            stars += "★";
        }
        layoutStars.setText(stars);
    }
}
