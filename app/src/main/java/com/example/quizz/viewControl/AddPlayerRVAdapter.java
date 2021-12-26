package com.example.quizz.viewControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quizz.R;
import com.example.quizz.data.playerData.Player;

/**
 *Recyler View für die Icon Auswahl bei Spielererstellung
 */
public class AddPlayerRVAdapter extends RecyclerView.Adapter<AddPlayerRVAdapter.AddPlayerViewHolder> {

    //String names[];
    int icons[] = {R.drawable.cat_icon01_generalknowledge, R.drawable.cat_icon02_books, R.drawable.cat_icon03_film,
            R.drawable.cat_icon04_music, R.drawable.cat_icon05_musical, R.drawable.cat_icon06_tv, R.drawable.cat_icon07_vidgames,
            R.drawable.cat_icon08_boardgames, R.drawable.cat_icon09_naturescience, R.drawable.cat_icon10_computer, R.drawable.cat_icon11_math,
            R.drawable.cat_icon12_mythology, R.drawable.cat_icon13_sports, R.drawable.cat_icon14_geography, R.drawable.cat_icon17_art};

    //todo different icons?

    Context context;
    LayoutInflater inflater;
    Player playerToCreate;

    private int selectedItemIndex = RecyclerView.NO_POSITION;


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
        View view = inflater.inflate(R.layout.rv_addplayer_grid_layout, parent, false);
        return new AddPlayerViewHolder(view);
    }

    /**
     * onClick sets the icon for the playerToCreate.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull AddPlayerViewHolder holder, int position) {
        holder.iconView.setImageResource(icons[position]);

        holder.itemView.clearFocus();
        holder.itemView.setSelected(selectedItemIndex == position);
        System.out.println(selectedItemIndex == position);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = holder.getAdapterPosition();


                notifyItemChanged(selectedItemIndex);
                selectedItemIndex = holder.getLayoutPosition();
                notifyItemChanged(selectedItemIndex);


                playerToCreate.setPlayerIcon(icons[index]);

            }
        });
    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    public class AddPlayerViewHolder extends RecyclerView.ViewHolder {

        ImageView iconView;
        ConstraintLayout mainLayout;

        public AddPlayerViewHolder(@NonNull View itemView) {
            super(itemView);

            iconView = itemView.findViewById(R.id.fixProfileIcon);
            mainLayout = itemView.findViewById(R.id.mainPlayerLayout);
        }
    }


}

