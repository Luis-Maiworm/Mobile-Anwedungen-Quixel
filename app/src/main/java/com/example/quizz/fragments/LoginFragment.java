package com.example.quizz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizz.R;
import com.example.quizz.data.playerData.PlayerManager;
import com.example.quizz.viewControl.ProfileRVAdapter;

public class LoginFragment extends Fragment{



    private String [] profileNames = {"hallo", "nabend"};
    private int [] icons = {R.drawable.cat_icon14_geography, R.drawable.cat_icon17_art};

    public static String TAG = "Login";

    RecyclerView recyclerViewLogin;
    ProfileRVAdapter rvAdapter;
    GridLayoutManager gridLayoutManager;
    View view;
    ImageButton closeFragment, addPlayer;

    AddPlayerFragment childFrag = new AddPlayerFragment();

    PlayerManager pManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chooseuser, container, false);

        recyclerViewLogin = view.findViewById(R.id.recyclerViewProfiles);


        Bundle bundle = this.getArguments();
        if(bundle != null){
            pManager = bundle.getParcelable("playerManager");
        }





        rvAdapter = new ProfileRVAdapter(getActivity(), profileNames, icons, pManager);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2, gridLayoutManager.VERTICAL, false);
        recyclerViewLogin.setLayoutManager(gridLayoutManager);
        recyclerViewLogin.setAdapter(rvAdapter);




        addPlayer = (ImageButton) view.findViewById(R.id.imageButton2);

        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(view.findViewById(R.id.child_fragment_addPlayer).getVisibility() != View.GONE){
                    closeAddPlayerFrag();
                } else{
                    addAddPlayerFrag();
                }


            }   //todo bei child einfügen das wieder zu
        });





        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){


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

        FragmentTransaction fT;
        //calls the FragmentManager from the Activity this fragment is being used on.
        // It then removes the LoginFragment from the Fragments attached to that activity
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
