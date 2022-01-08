package com.example.quizz.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizz.R;
import com.example.quizz.data.Constants;
import com.example.quizz.gameLogic.PlayerManager;
import com.example.quizz.viewControl.ProfileRVAdapter;

public class LoginFragment extends Fragment {

    private String[] profileNames;
    private int [] icons;

    private RecyclerView recyclerViewLogin;
    private ProfileRVAdapter rvAdapter;
    private GridLayoutManager gridLayoutManager;
    private View view;
    private ImageButton addProfileBtn;

    private AddPlayerFragment childFrag = new AddPlayerFragment();

    private PlayerManager pManager;

    private Bundle pBundle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        init();
        initButton();
        finish();

        return view;
    }


    private void init(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            pManager = bundle.getParcelable(Constants.playerManagerConstant);
            //sets the arrays needed for the recyclerView with the pManager's data
            profileNames = pManager.getProfiles().getPlayerNames();
            icons = pManager.profiles.getPlayerIcons();
        }

        try {
            //send to child
            pBundle = new Bundle();
            pBundle.putParcelable(Constants.playerManagerConstant, pManager);
            childFrag.setArguments(pBundle);

        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerViewLogin = view.findViewById(R.id.recyclerViewProfiles);

        rvAdapter = new ProfileRVAdapter(getActivity(), profileNames, icons, pManager, this, childFrag, pBundle);
        childFrag.setRvAdapter(rvAdapter);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2, gridLayoutManager.VERTICAL, false);
        recyclerViewLogin.setLayoutManager(gridLayoutManager);
        recyclerViewLogin.setAdapter(rvAdapter);
    }
    private void initButton(){
        // onClick zum Aufrufen des addPlayer frag
        addProfileBtn = (ImageButton) view.findViewById(R.id.imageButton2);

        addProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(view.findViewById(R.id.child_fragment_addPlayer).getVisibility() != View.GONE){
                    closeAddPlayerFrag();
                } else{
                    addAddPlayerFrag();
                }

            }   //todo bei child einfügen das wieder zu
        });
    }
    private void finish(){
        //opens addPlayerFragment if no user has been created yet
        try {
            if (pManager.getProfiles().getPlayerList().isEmpty()) {
                addAddPlayerFrag();
            }
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();
        if(view.findViewById(R.id.child_fragment_addPlayer).getVisibility() != View.GONE){
            closeAddPlayerFrag();
        }
    }


    public void addAddPlayerFrag(){
        View b = view.findViewById(R.id.child_fragment_addPlayer);
        b.setVisibility(View.VISIBLE);

        FragmentTransaction fT;
        fT = getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.replace(R.id.child_fragment_addPlayer, childFrag);
        fT.commit();
    }

    public void closeAddPlayerFrag(){
        FragmentTransaction fT;
        fT = getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.remove(childFrag);
        fT.commit();

        View b = view.findViewById(R.id.child_fragment_addPlayer);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                b.setVisibility(View.GONE);
            }
        }, 500);

    }



}
