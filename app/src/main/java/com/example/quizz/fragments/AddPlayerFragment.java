package com.example.quizz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizz.R;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.PlayerManager;
import com.example.quizz.viewControl.AddPlayerRVAdapter;

public class AddPlayerFragment extends Fragment {


    ImageButton addPlayer;
    View view;
    FragmentTransaction fT;
    static final String TAG = "addingFrag";
    RecyclerView recyclerViewIcons;
    AddPlayerRVAdapter rvAdapter;
    ImageButton imageButtonTemP;

    GridLayoutManager gridLayoutManager;

    Player playerToCreate;
    PlayerManager pManager;
    EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_child, container, false);


        try {
            Bundle bundle = this.getArguments();

            pManager = bundle.getParcelable("playerManager");       //todo null pointer evtl?
            System.out.println(pManager);

        } catch(Exception e){
            e.printStackTrace();
        }

        //todo create new Player -> maybe also a new method "createNewPlayer(Player player)"..

        /*
        pManager.createNewPlayer("TestName");
        pManager.getProfiles().getPlayerWithName("TestName").setPlayerIcon(R.drawable.cat_icon01_generalknowledge);
        profileNames = pManager.getPlayerNames();
        icons = pManager.getPlayerIcons();
        */

        playerToCreate = new Player("Bsp", 0); //todo remove player id from constructor

        recyclerViewIcons = view.findViewById(R.id.recyclerViewIcons);

        rvAdapter = new AddPlayerRVAdapter(getActivity(), playerToCreate);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2, gridLayoutManager.VERTICAL, false);
        recyclerViewIcons.setLayoutManager(gridLayoutManager);
        recyclerViewIcons.setAdapter(rvAdapter);



        Button button = view.findViewById(R.id.enterButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo check if properties are correct, and then Create the Player.
                // createPlayer(playerToCreate);
                try{
                    editText = view.findViewById(R.id.editTextTextPersonName2);
                    String name = editText.getText().toString();
                    playerToCreate.setPlayerName(name);

                    if(playerToCreate.getPlayerIcon() == 0){
                        throw new Exception("Select Icon");
                    }
                    System.out.println(playerToCreate.playerName);
                    System.out.println(playerToCreate.getPlayerIcon());
                    System.out.println(pManager);
                    pManager.createNewPlayer(playerToCreate);


                    // todo -> HIER notify adapter
                } catch (Exception e) {
                    System.out.println(e.getMessage());     //todo wenn name schon vergeben ist
                }


            }   //todo bei child einfügen das wieder zu
        });




        //todo hier wird nur eine Instanz des PlayerManager benötigt.
        // in dem recycler view für die icons muss lediglich ein integer wert zurück gegeben
        // werden.. dieser setzt das icon für den player


        return view;
    }



}
