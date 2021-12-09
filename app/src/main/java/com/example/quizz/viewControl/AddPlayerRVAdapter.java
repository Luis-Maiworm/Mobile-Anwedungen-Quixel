package com.example.quizz.viewControl;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizz.R;
import com.example.quizz.data.playerData.Player;
import com.example.quizz.view.GameActivity;

/**
 *Recyler View für die Icon Auswahl bei Spielererstellung
 */
public class AddPlayerRVAdapter extends RecyclerView.Adapter<AddPlayerRVAdapter.AddPlayerViewHolder> {

    //String names[];
    int icons[] = {R.drawable.cat_icon01_generalknowledge, R.drawable.cat_icon02_books, R.drawable.cat_icon03_film,
            R.drawable.cat_icon04_music, R.drawable.cat_icon05_musical, R.drawable.cat_icon06_tv, R.drawable.cat_icon07_vidgames,
            R.drawable.cat_icon08_boardgames, R.drawable.cat_icon09_naturescience, R.drawable.cat_icon10_computer, R.drawable.cat_icon11_math,
            R.drawable.cat_icon12_mythology, R.drawable.cat_icon13_sports, R.drawable.cat_icon14_geography, R.drawable.cat_icon17_art};

    Context context;
    LayoutInflater inflater;
    Player playerToCreate;
    int selectedItem;
    //private int lastPosition = -1;

    public AddPlayerRVAdapter(Context context, Player playerToCreate) {
       // this.names = names;
       // this.icons = icons;
        this.playerToCreate = playerToCreate;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }



    @NonNull
    @Override
    public AddPlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_category_grid_layout, parent, false);
        return new AddPlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddPlayerViewHolder holder, int position) {
        holder.icons.setImageResource(icons[position]);



        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                playerToCreate.setPlayerIcon(icons[holder.getAdapterPosition()]);




                if(holder.getOldPosition() != RecyclerView.NO_POSITION && holder.getOldPosition() != holder.getAdapterPosition()){
                   // holder.getOldPosition().
                }


                holder.itemView.setBackgroundColor(Color.RED);




                selectedItem = holder.getAdapterPosition();





                //todo setzt Icon...dieses wird dann zusammen mit dem Namen in dem AddPlayerFragment übergeben und alles zusammen an das LoginFragment übergeben
                // dort wird dann der RecyclerView geupdatet, sodass der neu erstellte Benutzer direkt sichtbar wird!!!
            }
        });


    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    public class AddPlayerViewHolder extends RecyclerView.ViewHolder {

        ImageView icons;
        ConstraintLayout mainLayout;



        public AddPlayerViewHolder(@NonNull View itemView) {
            super(itemView);

            icons = itemView.findViewById(R.id.profileIcon);
            mainLayout = itemView.findViewById(R.id.mainCategoryLayout);
        }
    }


}

