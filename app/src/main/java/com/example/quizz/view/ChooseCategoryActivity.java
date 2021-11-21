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
    private int[] icons = {R.drawable.element1, R.drawable.element2};
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

