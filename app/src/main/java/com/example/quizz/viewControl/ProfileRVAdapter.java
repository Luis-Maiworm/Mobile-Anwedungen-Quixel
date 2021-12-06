package com.example.quizz.viewControl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizz.R;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.data.playerData.PlayerManager;
import com.example.quizz.fragments.LoginFragment;
import com.example.quizz.view.GameActivity;
import com.example.quizz.view.MainMenuActivity;

import java.util.ArrayList;

public class ProfileRVAdapter extends RecyclerView.Adapter<ProfileRVAdapter.ProfileViewHolder> {

    String profileNames[];
    int profilePictures[];
    Context context;
    LayoutInflater inflater;
    FragmentTransaction fT;
    PlayerManager pManager;

   // ArrayList contextMenuList;
    Context contextMenuContext;


    public ProfileRVAdapter(Context context , String[] profileNames, int[] profilePictures, @NonNull PlayerManager pManager) {
        this.profileNames = profileNames;
        this.profilePictures = profilePictures;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.pManager = pManager;
    }


    @NonNull
    @Override
    public ProfileRVAdapter.ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_profile_grid_layout, parent, false);
        return new ProfileRVAdapter.ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.profileNames.setText(profileNames[position]);
        holder.profilePictures.setImageResource(profilePictures[position]);

        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("lüüüüüpppppppppppt");


                //todo let a button appear "changePlayer" ->
                // also: make it wiggle? AND let a button appear "deletePlayer"
                // also: make it visible that the player is selected
                return false;
            }
        });
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    pManager.createNewPlayer("nabend");     //todo : weg? -> create NUR im childfrag
                    System.out.println(pManager);
                } catch (Exception e) {
                    e.printStackTrace();
                }



                pManager.chooseCurrentPlayer(profileNames[holder.getAdapterPosition()]);

                System.out.println(pManager);
                System.out.println(pManager.getCurrentPlayer());


                if(pManager != null && pManager.getCurrentPlayer() != null){        //todo


                    System.out.println(pManager.getCurrentPlayer().getPlayerName());



                 //   TextView testView = get.findViewById(R.id.textViewTest);
                   // testView.setText(pManager.getCurrentPlayer().getPlayerName() + pManager.getCurrentPlayer().getPlayerID());
                   // testView.setText("nabend");
                }




                /*
                fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
                fT.remove(fT.);
                fT.commit();*/

                //todo on click soll fragment schließen
                // und zudem den currentPlayer setzen bzw. diese Daten mitgeben
                // https://developer.android.com/guide/fragments/communicate            !!!
                // https://www.codegrepper.com/code-examples/java/How+to+send+data+from+recyclerview+adapter+to+fragment+in+android     !!

            }

        });




    }

    @Override
    public int getItemCount() {
        return profileNames.length;
    }

    public class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView profileNames;
        ImageView profilePictures;
        ConstraintLayout mainLayout;


        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);

            profileNames = itemView.findViewById(R.id.categoryNameTextView);
            profilePictures = itemView.findViewById(R.id.profileIcon);
            mainLayout = itemView.findViewById(R.id.mainCategoryLayout);
        }
    }



}
