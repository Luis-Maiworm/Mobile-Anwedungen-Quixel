package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Toast;
import com.example.quizz.R;
import com.example.quizz.data.Constants;
import com.example.quizz.data.TransferUtility;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.viewControl.CategoryRVAdapter;

/**
 * Screen where the player can choose a category to play
 */
public class ChooseCategoryActivity extends AppCompatActivity {

    // Category Icons
    private String[] names;
    // Category Icons
    private final int[] icons = {R.drawable.cat_icon01_generalknowledge, R.drawable.cat_icon02_books, R.drawable.cat_icon03_film,
            R.drawable.cat_icon04_music, R.drawable.cat_icon05_musical, R.drawable.cat_icon06_tv, R.drawable.cat_icon07_vidgames,
            R.drawable.cat_icon08_boardgames, R.drawable.cat_icon09_naturescience, R.drawable.cat_icon10_computer, R.drawable.cat_icon11_math,
            R.drawable.cat_icon12_mythology, R.drawable.cat_icon13_sports, R.drawable.cat_icon14_geography, R.drawable.cat_icon17_art};
    // RV Setup
    RecyclerView recyclerViewCategory;
    CategoryRVAdapter rvAdapter;
    GridLayoutManager gridLayoutManager;
    // Data Setup
    TransferUtility transferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        transferUtility = new TransferUtility();

        // Category Screen RV Setup
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        names = getResources().getStringArray(R.array.category_names);
        rvAdapter = new CategoryRVAdapter(this, names, icons, "category", transferUtility);
        gridLayoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(gridLayoutManager);
        recyclerViewCategory.setAdapter(rvAdapter);
        // Get Gamemode Info
        transferUtility.setSavedString(getGamemodeFromRV());
        transferUtility.setSavedPlayer(getPlayerFromRV());
    }

    /**
     * Gets the active player instance from the RV class
     * @return the active player | null if there is no data
     */
    private Player getPlayerFromRV() {
        if (getIntent().hasExtra(Constants.playerConstant)) {
            return (Player) getIntent().getSerializableExtra(Constants.playerConstant);
        } else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * Receives the selected Gamemode from the RV Adapter
     */
    private String getGamemodeFromRV() {
        if (getIntent().hasExtra("gamemode")) {
            return getIntent().getStringExtra("gamemode");
        } else {
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}

