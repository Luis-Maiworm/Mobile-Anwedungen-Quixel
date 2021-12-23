package com.example.quizz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quizz.R;
import com.example.quizz.data.enums.Constants;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.PlayerManager;

public class ShowPlayerFragment extends Fragment {

    private View view;
    private ImageView playerIconView;
    private TextView playerNameView;

    private PlayerManager pManager;
    private Player currentPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_player, container, false);

        init();
        initVariables();

        return view;
    }

    private void init(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            pManager = bundle.getParcelable(Constants.playerManagerConstant);
            currentPlayer = pManager.getCurrentPlayer();
        }
    }

    private void initVariables(){
        playerIconView = view.findViewById(R.id.playerIconView);
        playerIconView.setImageResource(currentPlayer.getPlayerIcon());

        playerNameView = view.findViewById(R.id.playerNameView);
        playerNameView.setText(currentPlayer.getPlayerName());
    }


    private void closeFragment(){

        FragmentTransaction fT = getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.remove(this);
        fT.commit();

        //todo delay? -> nach "add" geht ein klick ins leere

    }



}
