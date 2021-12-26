package com.example.quizz.view.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.quizz.R;
import com.example.quizz.data.Constants;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.gameLogic.PlayerManager;

public class ShowPlayerFragment extends Fragment {

    private View view;
    private ImageView playerIconView;
    private ImageButton changeName;
    private TextView playerNameView;
    private EditText editName;
    private PlayerManager pManager;
    private Player currentPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_showplayer, container, false);

        init();
        initVariables();
        initBtn();

        return view;
    }

    private void init(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            pManager = bundle.getParcelable(Constants.playerManagerConstant);
            currentPlayer = pManager.getCurrentPlayer();
        }
    }

    private void initVariables(){
        playerIconView = view.findViewById(R.id.playerIconView);
        playerIconView.setImageResource(currentPlayer.getPlayerIcon());

        playerNameView = view.findViewById(R.id.playerNameView);
        playerNameView.setText(currentPlayer.getPlayerName());

        changeName = view.findViewById(R.id.changeNameBtn);
        editName = view.findViewById(R.id.playerChangeInputView);
    }

    private boolean editFlag = true;
    private void initBtn(){
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editFlag) {

                    playerNameView.setVisibility(View.GONE);
                    changeName.setImageResource(R.drawable.ic_baseline_check_circle_24);

                    editName.setText(playerNameView.getText());
                    editName.setVisibility(View.VISIBLE);

                    editName.setFocusable(true);

                    editFlag = false;
                }
                else if (!editFlag) {
                    //todo if name not appropriate -> already given or such (make alertdialog)

                    try {
                        editName.setVisibility(View.GONE);
                        changeName.setImageResource(R.drawable.ic_baseline_create_24);
                        playerNameView.setVisibility(View.VISIBLE);

                        pManager.renamePlayer(currentPlayer.getPlayerName(), editName.getText().toString());

                        playerNameView.setText(editName.getText().toString());
                    } catch (Exception e){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Error");
                        builder.setMessage(e.getMessage());
                        builder.setPositiveButton("Okay", (dialog, id) -> {
                        });
                        builder.show();
                    }

                    editFlag = true;
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