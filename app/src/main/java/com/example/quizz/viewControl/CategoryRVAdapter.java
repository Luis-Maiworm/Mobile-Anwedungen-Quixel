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
import com.example.quizz.data.Constants;
import com.example.quizz.data.TransferUtility;
import com.example.quizz.data.playerData.Player;

public class CategoryRVAdapter extends RecyclerView.Adapter<CategoryRVAdapter.CategoryViewHolder> {

    String[] names;
    int[] icons;
    Context context;
    LayoutInflater inflater;
    String identifier;
    String currentGamemode;
    TransferUtility transferUtility;
    Player activePlayer;


    public CategoryRVAdapter(Context context, String[] names, int[] icons, String identifier, TransferUtility transferUtility) {
        this.identifier = identifier;
        this.names = names;
        this.icons = icons;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.transferUtility = transferUtility;
    }



    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {
        View view = inflater.inflate(R.layout.rv_category_grid_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        currentGamemode = transferUtility.getSavedString();
        activePlayer = transferUtility.getSavedPlayer();

        if(identifier.equals("category")) {
            holder.names.setText(names[position]);
            holder.icons.setImageResource(icons[position]);
            System.out.println("RV CATEGORY: " + activePlayer.getPlayerName());

            if(currentGamemode.equals("Quickplay Mode")) {
            holder.mainLayout.setOnClickListener(view -> {
                Intent startGameIntent = new Intent(context, com.example.quizz.gameLogic.gamemodes.Gamemode_standard.class);
                startGameIntent.putExtra("category", names[holder.getAdapterPosition()]);
                startGameIntent.putExtra(Constants.playerConstant, activePlayer);
                context.startActivity(startGameIntent);
            });
        }
            else if(currentGamemode.equals("Endless Mode")) {
                holder.mainLayout.setOnClickListener(view -> {
                Intent startGameIntent = new Intent(context, com.example.quizz.gameLogic.gamemodes.Gamemode_endless.class);
                startGameIntent.putExtra("category", names[holder.getAdapterPosition()]);
                startGameIntent.putExtra(Constants.playerConstant, activePlayer);
                context.startActivity(startGameIntent);
                        });
            }
            else if(currentGamemode.equals("Custom Mode")) {
                holder.mainLayout.setOnClickListener(view -> {
                    Intent startGameIntent = new Intent(context, com.example.quizz.view.ConfigureGamemodeActivity.class);
                    startGameIntent.putExtra("category", names[holder.getAdapterPosition()]);
                    startGameIntent.putExtra("categoryIcon", icons[holder.getAdapterPosition()]);
                    startGameIntent.putExtra(Constants.playerConstant, activePlayer);
                    context.startActivity(startGameIntent);
                });
            }
        }
        else if(identifier.equals("gamemode")) {
            holder.names.setText(names[position]);
            holder.icons.setImageResource(icons[position]);
            holder.mainLayout.setOnClickListener(view -> {
                System.out.println("RV GAMEMODE: " + activePlayer.getPlayerName());
                Intent goToChooseCategory = new Intent(context, com.example.quizz.view.ChooseCategoryActivity.class);
                goToChooseCategory.putExtra("gamemode", names[holder.getAdapterPosition()]);
                goToChooseCategory.putExtra(Constants.playerConstant, activePlayer);
                context.startActivity(goToChooseCategory);
        });
        }
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView names;
        ImageView icons;
        ConstraintLayout mainLayout;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.mainCategoryLayout);
            names = itemView.findViewById(R.id.categoryNameTextView);
            icons = itemView.findViewById(R.id.categoryIcon);

        }
    }
}

