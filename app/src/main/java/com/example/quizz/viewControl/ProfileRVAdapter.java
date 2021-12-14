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
    LoginFragment loginFragment;

   // ArrayList contextMenuList;
    Context contextMenuContext;


    public ProfileRVAdapter(Context context , String[] profileNames, int[] profilePictures, @NonNull PlayerManager pManager, LoginFragment frag) {
        this.profileNames = profileNames;
        this.profilePictures = profilePictures;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.pManager = pManager;
        this.loginFragment = frag;
    }

    public void setData(int [] icons, String [] profileNames){
        this.profilePictures = icons;
        this.profileNames = profileNames;
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
                return true;        // returns true -> so the onClick doesn't get called
            }
        });
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                pManager.chooseCurrentPlayer(profileNames[holder.getAdapterPosition()]);        // null pointer
                System.out.println("recycler " + pManager);
                System.out.println("auc recycler " + pManager.getCurrentPlayer());

                // setzt den Text und das bild in der main activity (context = activity vom LoginFragment)
                TextView tx = ((Activity)context).findViewById(R.id.mainProfileLabel);
                tx.setText(pManager.getCurrentPlayer().getPlayerName());
                ImageView iV = ((Activity)context).findViewById(R.id.mainProfileIcon);
                iV.setImageResource(pManager.getCurrentPlayer().getPlayerIcon());
                //todo animation later
                System.out.println(pManager.getCurrentPlayer().getPlayerIcon());
                System.out.println(pManager.getCurrentPlayer().getPlayerName());



                //close fragment, when a profile is chosen
                fT = loginFragment.getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
                fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
                fT.remove(loginFragment);
                fT.commit();

                //todo on click soll fragment schließen
                // und zudem den currentPlayer setzen bzw. diese Daten mitgeben
                // https://developer.android.com/guide/fragments/communicate            !!!
                // https://www.codegrepper.com/code-examples/java/How+to+send+data+from+recyclerview+adapter+to+fragment+in+android     !!

            }

        });




    }

    @Override
    public int getItemCount() {
        if(profileNames != null) return profileNames.length;
        return 0;
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
