package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.example.quizz.R;
import com.example.quizz.data.TransferUtility;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.viewControl.CategoryRVAdapter;

public class ChooseGamemodeActivity extends AppCompatActivity {

    // Gamemode Names
    private String[] names;
    // Gamemode Icons
    private int[] icons = {R.drawable.cat_icon10_computer, R.drawable.cat_icon10_computer, R.drawable.cat_icon10_computer};
    // RV Setup
    RecyclerView recyclerViewCategory;
    CategoryRVAdapter rvAdapter;
    GridLayoutManager gridLayoutManager;
    // Player
    Player activePlayer;
    private TransferUtility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_gamemode);

        utility = new TransferUtility();

        activePlayer = (Player) getIntent().getSerializableExtra("Player");
        utility.setSavedPlayer(activePlayer);
        System.out.println("CHOOSE GAMEMODE ACTIVITY: " + utility.getSavedPlayer().getPlayerName());

        // Gamemode Screen RV Setup
        recyclerViewCategory = findViewById(R.id.recyclerViewGamemode);
        names = getResources().getStringArray(R.array.gamemode_names);
        rvAdapter = new CategoryRVAdapter(this, names, icons, "gamemode", utility);
        gridLayoutManager = new GridLayoutManager(this,1, GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(gridLayoutManager);
        recyclerViewCategory.setAdapter(rvAdapter);


    }
}