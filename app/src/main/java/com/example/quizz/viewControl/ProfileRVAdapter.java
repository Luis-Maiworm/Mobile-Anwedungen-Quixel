package com.example.quizz.viewControl;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizz.R;
import com.example.quizz.data.playerData.PlayerManager;
import com.example.quizz.fragments.AddPlayerFragment;
import com.example.quizz.fragments.LoginFragment;

public class ProfileRVAdapter extends RecyclerView.Adapter<ProfileRVAdapter.ProfileViewHolder> {

    String profileNames[];
    int profilePictures[];
    Context context;
    LayoutInflater inflater;
    FragmentTransaction fT;
    PlayerManager pManager;
    LoginFragment loginFragment;
    AddPlayerFragment childFrag;

   // ArrayList contextMenuList;
    Context contextMenuContext;


    public ProfileRVAdapter(Context context , String[] profileNames, int[] profilePictures, @NonNull PlayerManager pManager, LoginFragment frag, AddPlayerFragment childFrag) {
        this.profileNames = profileNames;
        this.profilePictures = profilePictures;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.pManager = pManager;
        this.loginFragment = frag;
        this.childFrag = childFrag;
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

        ImageButton deletePlayer = (ImageButton) holder.mainLayout.findViewById(R.id.deletePlayerBtn);
        ImageButton changePlayer = (ImageButton) holder.mainLayout.findViewById(R.id.changePlayerBtn);

        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                System.out.println("lol");
                //todo let a button appear "changePlayer" ->
                // also: make it wiggle? AND let a button appear "deletePlayer"
                // also: make it visible that the player is selected
                deletePlayer.setScaleX(0);
                deletePlayer.setScaleY(0);
                deletePlayer.setAlpha(0f);
                deletePlayer.animate().scaleX(100).scaleY(100).alpha(1f).setDuration(100).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        deletePlayer.clearAnimation();
                        deletePlayer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {


                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                changePlayer.animate();


                changePlayer.setVisibility(View.VISIBLE);

                return true;        // returns true -> so the onClick doesn't get called
            }
        });
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //closes the "deletePlayerButton"
                if(deletePlayer.getVisibility() == View.VISIBLE && changePlayer.getVisibility() == View.VISIBLE){
                    deletePlayer.setVisibility(View.GONE);
                    changePlayer.setVisibility(View.GONE);
                    return;
                }

                System.out.println("haha");
                pManager.chooseCurrentPlayer(profileNames[holder.getAdapterPosition()]);        // null pointer

                // setzt den Text und das bild in der main activity (context = activity vom LoginFragment)
                TextView tx = ((Activity)context).findViewById(R.id.mainProfileLabel);
                tx.setText(pManager.getCurrentPlayer().getPlayerName());
                ImageView iV = ((Activity)context).findViewById(R.id.mainProfileIcon);
                iV.setImageResource(pManager.getCurrentPlayer().getPlayerIcon());
                //todo animate later?

                // für den ImageButton im Main menü
                ImageButton openProfileChooser = ((Activity) context).findViewById(R.id.fragmentBtn);
                ImageViewAnimatedChangeOut(context, openProfileChooser, R.drawable.ic_baseline_check_circle_24);


                //close fragment, when a profile is chosen
                fT = loginFragment.getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
                fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
                fT.remove(loginFragment);
                fT.commit();
            }

        });
        deletePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //todo asks if the user is sure to delete -> doesn't work properly
                AlertDialog.Builder permissionBuilder = new AlertDialog.Builder(context);
                permissionBuilder.setTitle("Error");
                permissionBuilder.setMessage("You sure you wanna delete this? ;)");
                permissionBuilder.setPositiveButton("Okay", (dialog, id) -> {

                });
                permissionBuilder.setNegativeButton("Nevermind.", (dialog, id) -> {

                });
                permissionBuilder.show();


                try {

                    pManager.deletePlayer(profileNames[holder.getAdapterPosition()]);

                    setData(pManager.getProfiles().getPlayerIcons(), pManager.getProfiles().getPlayerNames());

                    //todo delete Player + updates recyclerView (arrays)
                    notifyItemChanged(holder.getAdapterPosition());
                    // notifyDataSetChanged();
                } catch(Exception e){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Error");
                    builder.setMessage(e.getMessage());
                    builder.setPositiveButton("Okay", (dialog, id) -> {
                    });
                    builder.show();
                }


            }
        });

        changePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo playerFragment

                FragmentTransaction fT;

                fT = loginFragment.getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
                fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
                fT.replace(R.id.child_fragment_addPlayer, childFrag);
                fT.commit();

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


    public static void ImageViewAnimatedChangeOut(Context c, ImageButton v, int new_image){
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


}
