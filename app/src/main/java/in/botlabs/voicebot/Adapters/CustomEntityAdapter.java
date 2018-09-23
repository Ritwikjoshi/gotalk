package in.botlabs.voicebot.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import in.botlabs.voicebot.ChatBot;
import in.botlabs.voicebot.NewsActivity;
import in.botlabs.voicebot.Objects.suggestionsObject;
import in.botlabs.voicebot.R;
import in.botlabs.voicebot.VideoActivity;

import java.util.List;

public class CustomEntityAdapter extends BaseAdapter {

    private LayoutInflater layoutinflater;
    private List<suggestionsObject> listStorage;
    private Context context;
    SharedPreferences shared_Details;
    SharedPreferences.Editor edit_Details;

    public CustomEntityAdapter(Context context, List<suggestionsObject> customizedListView) {
        this.context = context;
        layoutinflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder listViewHolder;
        if(convertView == null){
            listViewHolder = new ViewHolder();
            convertView = layoutinflater.inflate(R.layout.suggestions_list_item, parent, false);
            listViewHolder.imageInListView = (ImageView)convertView.findViewById(R.id.imageView);
            listViewHolder.nameInListView = convertView.findViewById(R.id.nameEntity);
            listViewHolder.priceInListView = convertView.findViewById(R.id.priceEntity);
            listViewHolder.descriptionInListView = convertView.findViewById(R.id.descriptionEntity);
            listViewHolder.parent = convertView.findViewById(R.id.suggestCardView);

            shared_Details = context.getSharedPreferences("Details", Context.MODE_PRIVATE);
            edit_Details = shared_Details.edit();

            convertView.setTag(listViewHolder);
        }else{
            listViewHolder = (ViewHolder)convertView.getTag();
        }

        if(listStorage.get(position).getDescription() == null)
        {
            listViewHolder.descriptionInListView.setVisibility(View.GONE);
        }
        else
        {
            listViewHolder.descriptionInListView.setVisibility(View.VISIBLE);

        }

        if(listStorage.get(position).getPrice() == null)
        {
            listViewHolder.priceInListView.setVisibility(View.GONE);
        }
        else
        {
            listViewHolder.priceInListView.setVisibility(View.VISIBLE);

        }
        listViewHolder.nameInListView.setText(listStorage.get(position).getName());
        listViewHolder.priceInListView.setText(listStorage.get(position).getPrice() + " ₹");
        listViewHolder.descriptionInListView.setText(listStorage.get(position).getDescription());

        if(listStorage.get(position).getUrlFlag().equals("Image")||listStorage.get(position).getUrlFlag().equals("News")) {
            if(listStorage.get(position).getImageUrl()!= null) {
                listViewHolder.imageInListView.setVisibility(View.VISIBLE);
                GlideApp.with(listViewHolder.imageInListView.getContext())
                        .load(listStorage.get(position).getImageUrl())
                        .into(listViewHolder.imageInListView);

                if(listStorage.get(position).getUrlFlag().equals("News"))
                {
                    listViewHolder.descriptionInListView.setVisibility(View.GONE);
                }
            }
            else
            {
                listViewHolder.imageInListView.setImageDrawable(context.getResources().getDrawable(R.drawable.indigosquarelogo));

            }
        }
        else if(listStorage.get(position).getUrlFlag().equals("Video")){
            listViewHolder.imageInListView.setImageDrawable(context.getResources().getDrawable(R.drawable.youtube));
        }

        listViewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listStorage.get(position).getUrlFlag().equals("Image")) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(listStorage.get(position).getName() + " - " + listStorage.get(position).getPrice() + " ₹");
                    builder.setMessage("Are you sure, you want to order " + listStorage.get(position).getName() + "?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ChatBot.orderFood(listStorage.get(position).getName(), context);
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(listStorage.get(position).getUrlFlag().equals("Video")){

                    edit_Details.putString("videoUrl",listStorage.get(position).getImageUrl());
                    edit_Details.apply();
                    Intent i = new Intent(context, VideoActivity.class);
                    context.startActivity(i);
                }else if(listStorage.get(position).getUrlFlag().equals("News"))
                {
                    edit_Details.putString("newsImage",listStorage.get(position).getImageUrl());
                    edit_Details.putString("newsDescription",listStorage.get(position).getDescription());
                    edit_Details.putString("newsName",listStorage.get(position).getName());
                    edit_Details.apply();

                    Intent i = new Intent(context, NewsActivity.class);
                    context.startActivity(i);
                }

            }
        });

        return convertView;
    }

    static class ViewHolder{
        TextView nameInListView;
        ImageView imageInListView;
        TextView priceInListView;
        TextView descriptionInListView;
        CardView parent;
    }

}

