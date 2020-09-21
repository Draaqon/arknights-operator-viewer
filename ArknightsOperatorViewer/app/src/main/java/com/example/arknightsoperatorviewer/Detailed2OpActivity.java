package com.example.arknightsoperatorviewer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Detailed2OpActivity extends AppCompatActivity
{
    ProfileFragment profFrag;
    TrustFragment trustFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed2op);

        OperatorInfo.Operator2Star op = getIncomingIntent();
        String profString = op.getProfile();
        String clinical = op.getClinicalAnalysis();
        String trust1String = op.getTrust1();
        String trust2String = op.getTrust2();
        String trust3String = op.getTrust3();
        String trust4String = op.getTrust4();
        Bundle profBundle = new Bundle();
        profBundle.putString("Profile", profString);
        Bundle trustBundle = new Bundle();
        trustBundle.putString("Clinical", clinical);
        trustBundle.putString("Trust 1", trust1String);
        trustBundle.putString("Trust 2", trust2String);
        trustBundle.putString("Trust 3", trust3String);
        trustBundle.putString("Trust 4", trust4String);

        //send profile data to the fragment
        profFrag = new ProfileFragment();
        profFrag.setArguments(profBundle);
        trustFrag = new TrustFragment();
        trustFrag.setArguments((trustBundle));

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl2Fragment, profFrag);
        transaction.commit();

        RadioButton profileRadio = findViewById(R.id.radio2Profile);
        RadioButton trustRadio = findViewById(R.id.radio2Trust);

        profileRadio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fl2Fragment, profFrag);
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
                transaction.replace(R.id.fl2Fragment, trustFrag);
                transaction.commit();
            }
        });


    }

    private OperatorInfo.Operator2Star getIncomingIntent()
    {
        OperatorInfo.Operator2Star op = (OperatorInfo.Operator2Star) getIntent().getSerializableExtra("operator");
        setImage(op);
        return op;
    }

    private void setImage(OperatorInfo op)
    {
        //setting operator name text
        TextView opName = findViewById(R.id.txt2Name);
        opName.setText(op.getName().toUpperCase());
        //setting class text
        opName = findViewById(R.id.txt2Class);
        opName.setText(op.getType().toUpperCase());
        //setting operator image
        int id = this.getResources().getIdentifier("com.example.arknightsoperatorviewer:drawable/" + op.getName().toLowerCase(), null, null);
        ImageView imgView = findViewById(R.id.img2Pic);
        imgView.setImageResource(id);
        //setting class pic
        imgView = findViewById(R.id.img2Class);
        id = this.getResources().getIdentifier("com.example.arknightsoperatorviewer:drawable/" + op.getType().toLowerCase(), null, null);
        imgView.setImageResource(id);
        //setting affiliation image
        imgView = findViewById(R.id.img2Affiliation);
        id = this.getResources().getIdentifier("com.example.arknightsoperatorviewer:drawable/" + op.getAffiliation().toLowerCase() + "_half", null, null);
        imgView.setImageResource(id);
        //setting stars amount
        String stars = "";
        TextView layoutStars = findViewById(R.id.txt2Stars);
        for(int i = 0; i < op.getStars(); i++)
        {
            stars += "★";
        }
        layoutStars.setText(stars);
        //setting profile fragment text

    }
}
