package com.example.quizz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizz.R;

public class AddPlayerFragment extends Fragment {


    ImageButton addPlayer;
    View view;
    FragmentTransaction fT;
    static final String TAG = "addingFrag";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_child, container, false);






        return view;
    }



}
