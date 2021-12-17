package com.example.quizz.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizz.R;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.PlayerManager;
import com.example.quizz.viewControl.ProfileRVAdapter;

public class LoginFragment extends Fragment {



    //rivate String [] profileNames = {"hallo", "nabend"};
    //private int [] icons = {R.drawable.cat_icon14_geography, R.drawable.cat_icon17_art};

    private String[] profileNames;
    private int [] icons;

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

               // ?
        view = inflater.inflate(R.layout.fragment_chooseuser, container, false);



        Bundle bundle = this.getArguments();
        if(bundle != null){
            pManager = bundle.getParcelable("playerManager");
            //sets the arrays needed for the recyclerView with the pManager's data
            profileNames = pManager.getProfiles().getPlayerNames();
            icons = pManager.profiles.getPlayerIcons();
        }


        try {

            //send to child
            Bundle bundle2 = new Bundle();
            bundle2.putParcelable("playerManager", pManager);
            childFrag.setArguments(bundle2);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("funktnet");
        }





        recyclerViewLogin = view.findViewById(R.id.recyclerViewProfiles);

        rvAdapter = new ProfileRVAdapter(getActivity(), profileNames, icons, pManager, this);
        childFrag.setRvAdapter(rvAdapter);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2, gridLayoutManager.VERTICAL, false);
        recyclerViewLogin.setLayoutManager(gridLayoutManager);
        recyclerViewLogin.setAdapter(rvAdapter);



        // onClick zum Aufrufen des addPlayer frag
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




        // fügt das addPlayer fragment hinzu, falls es noch keinen spieler gibt
        try {
            if (pManager.getProfiles().getPlayerList().isEmpty()) {
                addAddPlayerFrag();
            }
        } catch (NullPointerException e){

        }

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



      //  ImageButton temp = (ImageButton) view.findViewById(R.id.imageButton2);
        //todo add a NEW fragment each time its called? (atm it remembers the position if the menü and basically just reopens one all the time

        FragmentTransaction fT;
        //calls the FragmentManager from the Activity this fragment is being used on.
        // It then removes the LoginFragment from the Fragments attached to that activity
        fT = getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
        fT.replace(R.id.child_fragment_addPlayer, childFrag);
        fT.commit();
        //ImageViewAnimatedChangeIn(getContext(), temp, R.drawable.ic_baseline_remove_circle_outline_24);
    }

    public void closeAddPlayerFrag(){

        //ImageButton temp = (ImageButton) view.findViewById(R.id.imageButton2);

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

      //  ImageViewAnimatedChangeOut(getContext(), temp, R.drawable.ic_baseline_add_circle_outline_24);

    }


    /*
    public void ImageViewAnimatedChangeIn(Context c, ImageButton v, int new_image){
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.slide_out_imagebutton2);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.slide_in_imagebutton2);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {}
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    public void ImageViewAnimatedChangeOut(Context c, ImageView v, int new_image){
        final Animation anim_out = AnimationUtils.loadAnimation(c, R.anim.slide_out_imagebutton2);
        final Animation anim_in = AnimationUtils.loadAnimation(c, R.anim.slide_in_imagebutton2);
        anim_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setImageResource(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {}
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
*/








}
