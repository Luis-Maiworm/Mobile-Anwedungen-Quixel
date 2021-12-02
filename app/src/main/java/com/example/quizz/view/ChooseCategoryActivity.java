package com.example.quizz.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.example.quizz.R;
import com.example.quizz.viewControl.CategoryRVAdapter;


public class ChooseCategoryActivity extends AppCompatActivity {

    // Category Names and Icons
    private String[] names;
    private int[] icons = {R.drawable.cat_icon01_generalknowledge, R.drawable.cat_icon02_books, R.drawable.cat_icon03_film,
            R.drawable.cat_icon04_music, R.drawable.cat_icon05_musical, R.drawable.cat_icon06_tv, R.drawable.cat_icon07_vidgames,
            R.drawable.cat_icon08_boardgames, R.drawable.cat_icon09_naturescience, R.drawable.cat_icon10_computer, R.drawable.cat_icon11_math,
            R.drawable.cat_icon12_mythology, R.drawable.cat_icon13_sports, R.drawable.cat_icon14_geography, R.drawable.cat_icon17_art};
    // RV Setup
    RecyclerView recyclerViewCategory;
    CategoryRVAdapter rvAdapter;
    GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);

        // Category Screen RV Setup
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        names = getResources().getStringArray(R.array.category_names);
        rvAdapter = new CategoryRVAdapter(this, names, icons);
        gridLayoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(gridLayoutManager);
        recyclerViewCategory.setAdapter(rvAdapter);
    }
}

