package in.botlabs.voicebot.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.botlabs.voicebot.Objects.ChatMessage;
import in.botlabs.voicebot.R;

/**
 * Created by yashkothari on 12/03/18.
 */

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;
    private ImageView chatImage;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private Context context;
    ProgressBar progress;
    AppCompatButton availBtn;
    Bitmap image;
    URL newurl;

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.left&&(chatMessageObj.imageUrl == null)) {
            row = inflater.inflate(R.layout.right, parent, false);
        }
        else if(chatMessageObj.left&&chatMessageObj.imageUrl!=null)
        {

            row = inflater.inflate(R.layout.image_right, parent, false);

            chatImage = row.findViewById(R.id.imgmsgr);
            progress = row.findViewById(R.id.imageProgress);
            progress.setVisibility(View.GONE);
            chatImage.setVisibility(View.GONE);

            File file = new File(chatMessageObj.imageUrl);
            Uri imageUri = Uri.fromFile(file);
            Glide.with(chatImage.getContext())
                    .load(imageUri) // Uri of the picture
             .into(chatImage);
            chatImage.setVisibility(View.VISIBLE);

            System.out.println(chatMessageObj.imageUrl);


        }else if((!chatMessageObj.left)&&(chatMessageObj.imageUrl == null)){
            row = inflater.inflate(R.layout.left, parent, false);
        }
        else{
            row = inflater.inflate(R.layout.image_left, parent, false);
            chatImage = row.findViewById(R.id.imgmsgr);
            progress = row.findViewById(R.id.imageProgress);
            progress.setVisibility(View.GONE);
            chatImage.setVisibility(View.GONE);
            try {
                newurl = new URL(chatMessageObj.imageUrl);
                chatImage.setVisibility(View.VISIBLE);

                Glide.with(chatImage.getContext())
                        .load(chatMessageObj.imageUrl)
                        .into(chatImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        availBtn = row.findViewById(R.id.availOffer);
        if(availBtn!=null) {
            if (chatMessageObj.openapp != null) {
                availBtn.setVisibility(View.VISIBLE);
            } else {
                availBtn.setVisibility(View.GONE);
            }
        }



        chatText = (TextView) row.findViewById(R.id.msgr);
        if(chatText!=null) {
            chatText.setText(chatMessageObj.message.replace("\\n", "\n"));
        }

        if(availBtn != null) {
            availBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "opening Vodafone application", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return row;
    }

}
