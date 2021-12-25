package com.example.quizz.fragments;

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
import com.example.quizz.data.enums.Constants;
import com.example.quizz.data.playerData.PlayerManager;
import com.example.quizz.viewControl.ProfileRVAdapter;

public class LoginFragment extends Fragment {

    private String[] profileNames;
    private int [] icons;

    public static String TAG = "Login";

    private RecyclerView recyclerViewLogin;
    private ProfileRVAdapter rvAdapter;
    private GridLayoutManager gridLayoutManager;
    private View view;
    private ImageButton closeFragment, addProfileBtn;

    private AddPlayerFragment childFrag = new AddPlayerFragment();

    private PlayerManager pManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chooseuser, container, false);

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
            Bundle bundle2 = new Bundle();
            bundle2.putParcelable(Constants.playerManagerConstant, pManager);
            childFrag.setArguments(bundle2);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("funktnet");
        }

        recyclerViewLogin = view.findViewById(R.id.recyclerViewProfiles);

        rvAdapter = new ProfileRVAdapter(getActivity(), profileNames, icons, pManager, this, childFrag);
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

        }
    }


    @Override
    public void onDestroyView(){
        super.onDestroyView();

        if(view.findViewById(R.id.child_fragment_addPlayer).getVisibility() != View.GONE){
            FragmentTransaction fT;
            fT = getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
            //todo check ob jedes mal aufs neue nötig (oder ob z.B. EINE transaction für alles verwendet werden kann usw
            fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
            fT.remove(childFrag);
            fT.commit();


            View b = view.findViewById(R.id.child_fragment_addPlayer);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    b.setVisibility(View.GONE);
                }
            }, 300);
        }


    }


    public void addAddPlayerFrag(){
        View b = view.findViewById(R.id.child_fragment_addPlayer);
        b.setVisibility(View.VISIBLE);

        //todo add a NEW fragment each time its called? (atm it remembers the position if the menü and basically just reopens one all the time
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
