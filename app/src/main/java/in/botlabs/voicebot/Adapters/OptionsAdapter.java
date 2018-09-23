package in.botlabs.voicebot.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.List;

import in.botlabs.voicebot.ChatBot;
import in.botlabs.voicebot.Objects.OptionsObject;
import in.botlabs.voicebot.R;

/**
 * Created by Sadruddin on 12/24/2017.
 */

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.GroceryViewHolder>{
    private List<OptionsObject> optionsList;
    Context context;

    public OptionsAdapter(List<OptionsObject> optionsList, Context context){
        this.optionsList= optionsList;
        this.context = context;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View optionsView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(optionsView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {
        holder.txtview.setText(optionsList.get(position).getOption());

        Glide.with(holder.imageView.getContext())
                .load(optionsList.get(position).getUrl())
                .into(holder.imageView);

        holder.instructionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatBot.sendChatMessageToTheScreen(2,holder.txtview.getText().toString().trim());
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;
        CardView instructionCard;
        public GroceryViewHolder(View view) {
            super(view);
            txtview=view.findViewById(R.id.instructionText);
            imageView = view.findViewById(R.id.optionsImage);
            instructionCard = view.findViewById(R.id.instructionCard);
        }
    }
}
