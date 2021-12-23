package com.example.quizz.fragments;

import android.app.AlertDialog;
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
import com.example.quizz.data.enums.Constants;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.PlayerManager;
import com.example.quizz.viewControl.AddPlayerRVAdapter;
import com.example.quizz.viewControl.ProfileRVAdapter;

public class AddPlayerFragment extends Fragment {

    private Button addPlayerBtn;
    private View view;

    static final String TAG = "addingFrag";
    RecyclerView recyclerViewIcons;
    AddPlayerRVAdapter rvAdapter;

    private GridLayoutManager gridLayoutManager;

    private Player playerToCreate;
    private PlayerManager pManager;
    private EditText editText;
    private ProfileRVAdapter profAdapter;

    public void setRvAdapter(ProfileRVAdapter profAdapter){
        this.profAdapter = profAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_child, container, false);

        init();
        initButton();

        return view;
    }



    private void init(){
        Bundle bundle = this.getArguments();

        if(bundle != null) {
            pManager = bundle.getParcelable(Constants.playerManagerConstant);       //todo null pointer evtl?
        }

        playerToCreate = new Player(); //todo remove player id from constructor

        recyclerViewIcons = view.findViewById(R.id.recyclerViewIcons);

        rvAdapter = new AddPlayerRVAdapter(getActivity(), playerToCreate);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2, gridLayoutManager.VERTICAL, false);
        recyclerViewIcons.setLayoutManager(gridLayoutManager);
        recyclerViewIcons.setAdapter(rvAdapter);
    }

    private void initButton(){
        addPlayerBtn = view.findViewById(R.id.submitPlayerBtn);

        addPlayerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    editText = view.findViewById(R.id.editTextTextPersonName2);
                    String name = editText.getText().toString();
                    playerToCreate.setPlayerName(name);         // problem: immer der gleiche player auf dem operiert wird!

                    if(playerToCreate.getPlayerIcon() == 0){
                        throw new Exception("Select Icon");
                    }

                    pManager.createNewPlayer(playerToCreate);

                    profAdapter.setData(pManager.getProfiles().getPlayerIcons(), pManager.getProfiles().getPlayerNames());
                    // profAdapter.notifyItemChanged(pManager.getProfiles().getPlayerListSize());      //todo
                    profAdapter.notifyDataSetChanged();     //todo change notify (notifyDataSetChanged -> performance problem)
                    closeFragment();

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Error");
                    builder.setMessage(e.getMessage());
                    builder.setPositiveButton("Okay", (dialog, id) -> {
                    });
                    builder.show();
                }
            }

        });
    }


    private void closeFragment(){

        FragmentTransaction fT = getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.remove(this);
        fT.commit();
        //todo delay? -> nach "add" geht ein klick ins leere
    }



}
