package com.example.quizz.viewControl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizz.R;
import com.example.quizz.data.enums.Constants;
import com.example.quizz.view.GameActivity;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.CategoryViewHolder> {

    String names[];
    int icons[];
    Context context;
    LayoutInflater inflater;

    //private int lastPosition = -1;

    public CategoryRVAdapter(Context context, String names[], int icons[]) {
        this.names = names;
        this.icons = icons;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_category_grid_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.names.setText(names[position]);
        holder.icons.setImageResource(icons[position]);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startGameIntent = new Intent(context, GameActivity.class);
                startGameIntent.putExtra(Constants.categoryConstant, names[holder.getAdapterPosition()]);
                context.startActivity(startGameIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView names;
        ImageView icons;
        ConstraintLayout mainLayout;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            names = itemView.findViewById(R.id.categoryNameTextView);
            icons = itemView.findViewById(R.id.profileIcon);
            mainLayout = itemView.findViewById(R.id.mainCategoryLayout);
        }
    }


}

