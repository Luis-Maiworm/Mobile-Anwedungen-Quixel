package com.example.quizz.viewControl;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizz.R;
import com.example.quizz.gameLogic.PlayerManager;
import com.example.quizz.view.fragments.AddPlayerFragment;
import com.example.quizz.view.fragments.LoginFragment;

public class ProfileRVAdapter extends RecyclerView.Adapter<ProfileRVAdapter.ProfileViewHolder> {

    String[] profileNames;
    int[] profilePictures;
    Context context;
    LayoutInflater inflater;
    FragmentTransaction fT;
    PlayerManager pManager;
    LoginFragment loginFragment;
    AddPlayerFragment childFrag;
    Bundle pBundle;

    // ArrayList contextMenuList;
    Context contextMenuContext;


    public ProfileRVAdapter(Context context , String[] profileNames, int[] profilePictures, @NonNull PlayerManager pManager, LoginFragment frag, AddPlayerFragment childFrag, Bundle pBundle) {
        this.profileNames = profileNames;
        this.profilePictures = profilePictures;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.pManager = pManager;
        this.loginFragment = frag;
        this.childFrag = childFrag;
        this.pBundle = pBundle;
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


    /**
     * Animates the Buttons (changePlayer and deletePlayer) and changes the visibility.
     * @param btn
     * @param firstValue
     * @param secondValue
     */
    private void animateButtons(ImageButton btn, float firstValue, float secondValue){
        btn.setScaleX(firstValue);
        btn.setScaleY(firstValue);
        btn.setAlpha(firstValue);

        btn.animate().scaleX(secondValue).scaleY(secondValue).alpha(secondValue).setInterpolator(new DecelerateInterpolator()).setDuration(500).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(firstValue < secondValue){
                    btn.clearAnimation();
                    btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (firstValue > secondValue){
                    btn.clearAnimation();
                    btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        holder.profileNames.setText(profileNames[position]);
        holder.profilePictures.setImageResource(profilePictures[position]);

        ImageButton deletePlayer = holder.mainLayout.findViewById(R.id.deletePlayerBtn);
        ImageButton changePlayer = holder.mainLayout.findViewById(R.id.changePlayerBtn);

        holder.mainLayout.setOnLongClickListener(v -> {

            animateButtons(deletePlayer, 0f, 1f);
            animateButtons(changePlayer, 0f, 1f);

            return true;        // returns true -> so the onClick doesn't get called
        });

        holder.mainLayout.setOnClickListener(view -> {
            //closes the "deletePlayerButton"
            if(deletePlayer.getVisibility() == View.VISIBLE && changePlayer.getVisibility() == View.VISIBLE){
                animateButtons(deletePlayer, 1f, 0f);
                animateButtons(changePlayer, 1f, 0f);

                return;
            }

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
        });

        deletePlayer.setOnClickListener(v -> {

            //todo asks if the user is sure to delete -> doesn't work properly!!!!!!!!!!!!!!!!!!!!!!!!!!


            AlertDialog.Builder permissionBuilder = new AlertDialog.Builder(context);
            permissionBuilder.setTitle("Careful, Sir/Madame!");
            permissionBuilder.setMessage("You sure you wanna delete this? ;)");
            permissionBuilder.setPositiveButton("I have no choice, so yes!", (dialog, id) -> {
            });

            permissionBuilder.show();


            try {

                pManager.deletePlayer(profileNames[holder.getAdapterPosition()]);

                setData(pManager.getProfiles().getPlayerIcons(), pManager.getProfiles().getPlayerNames());

                //notifyItemChanged(holder.getAdapterPosition());
                notifyDataSetChanged();
            } catch(Exception e){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Error");
                builder.setMessage(e.getMessage());
                builder.setPositiveButton("Okay", (dialog, id) -> {
                });
                builder.show();
                e.printStackTrace();
            }


        });

        changePlayer.setOnClickListener(v -> {

            childFrag = new AddPlayerFragment(pManager.getProfiles().getPlayerWithName(profileNames[holder.getAdapterPosition()]));
            childFrag.setRvAdapter(this);
            childFrag.setArguments(pBundle);


            FragmentTransaction fT;

            fT = loginFragment.getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
            fT.setCustomAnimations(R.anim.scale_up, R.anim.scale_down);
            fT.replace(R.id.child_fragment_addPlayer, childFrag);
            fT.commit();

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
            mainLayout = itemView.findViewById(R.id.mainProfileLayout);
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