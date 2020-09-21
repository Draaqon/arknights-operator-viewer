package com.example.arknightsoperatorviewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Trust45Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Trust45Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View view;

    public Trust45Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Trust45Fragment newInstance(String param1, String param2) {
        Trust45Fragment fragment = new Trust45Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_45trust, container, false);
        //set each TextView's content to the data provided in arguments.
        TextView test = view.findViewById(R.id.txt45Clinical);
        test.setText(getArguments().getString("Clinical"));
        test = view.findViewById(R.id.txt45Trust1);
        test.setText(getArguments().getString("Trust 1"));
        test = view.findViewById(R.id.txt45Trust2);
        test.setText(getArguments().getString("Trust 2"));
        test = view.findViewById(R.id.txt45Trust3);
        test.setText(getArguments().getString("Trust 3"));
        test = view.findViewById(R.id.txt45Trust4);
        test.setText(getArguments().getString("Trust 4"));
        test = view.findViewById(R.id.txt45Promo);
        test.setText(getArguments().getString("Promo"));
        return view;
    }
}