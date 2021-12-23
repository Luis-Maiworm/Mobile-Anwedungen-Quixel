package com.example.quizz.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizz.R;
import com.example.quizz.data.enums.Constants;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.PlayerManager;
import com.example.quizz.viewControl.AddPlayerRVAdapter;
import com.example.quizz.viewControl.ProfileRVAdapter;

import java.util.List;

public class ShowPlayerFragment extends Fragment {

    View view;
    ImageView playerIconView;
    TextView playerNameView;

    PlayerManager pManager;
    Player currentPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_player, container, false);

        Bundle bundle = this.getArguments();
        if(bundle != null){
            pManager = bundle.getParcelable(Constants.playerManagerConstant);
            //sets the arrays needed for the recyclerView with the pManager's data
            currentPlayer = pManager.getCurrentPlayer();
        }


        playerIconView = view.findViewById(R.id.playerIconView);
        playerIconView.setImageResource(currentPlayer.getPlayerIcon());

        playerNameView = view.findViewById(R.id.playerNameView);
        playerNameView.setText(currentPlayer.getPlayerName());

        return view;
    }



    public void closeFragment(){

        FragmentTransaction fT = getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.remove(this);
        fT.commit();

        //todo delay? -> nach "add" geht ein klick ins leere

    }



}
