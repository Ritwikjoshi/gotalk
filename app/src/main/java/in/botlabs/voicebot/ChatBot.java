package in.botlabs.voicebot;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import in.botlabs.voicebot.Adapters.CustomEntityAdapter;
import in.botlabs.voicebot.Helper.WatsonCall;


import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.CouchDbException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.botlabs.voicebot.Adapters.ChatArrayAdapter;
import in.botlabs.voicebot.Helper.ImageFilePath;
import in.botlabs.voicebot.Model.ComplaintModel;
import in.botlabs.voicebot.Model.NewsModel;
import in.botlabs.voicebot.Objects.ChatMessage;

import in.botlabs.voicebot.Objects.suggestionsObject;
import in.botlabs.voicebot.Retrofit.API;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import in.botlabs.voicebot.Retrofit.retroClient;

public class ChatBot extends AppCompatActivity {


    private static final String TAG = "ChatActivity";
//    private static final String ChatBotUrl ="https://gateway-syd.watsonplatform.net/assistant/api";

    private static ChatArrayAdapter chatArrayAdapter;
    private ImageView sendButton, mic;
    private EditText chatText;
    private ListView messageListView;
    static private boolean side = true;
    final int REQ_CODE_SPEECH_INPUT = 0;
    Assistant service;
    MessageOptions options;
    MessageResponse response;
    private static final int CAMERA_REQUEST = 1;
    TextToSpeech tts;
    static String fortts = "", ImageUrl, openAppUrl;
    static com.ibm.watson.developer_cloud.assistant.v1.model.Context responseContext = null;
    int QUERY_FLAG = 0;
    ImageView camera;
    static String fooditem = "", description = "", username = "", seatnumber = "", status = "False";
    String RESPONSE_FLAG = "False";
    static String nearbyImageUrl;
    SharedPreferences shared_Details, shared_seat;
    SharedPreferences.Editor edit_Details, edit_seat;
    static String prefs, seat, genre;
    GridView gridview;
    ImageView closeGrid;
    static LinearLayout gridLinear;

    ArrayList<suggestionsObject> items = new ArrayList<suggestionsObject>();
    String picturePath;
    Uri imageUri;
    private File output = null;

    NewsModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        getWindow().setBackgroundDrawableResource(R.drawable.back);

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


// clear FLAG_TRANSLUCENT_STATUS flag:
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        getWindow().setStatusBarColor(ContextCompat.getColor(ChatBot.this, R.color.colorPrimaryDark));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        service = new Assistant("2018-02-16");
        service.setUsernameAndPassword("03eb2ad5-8f4a-4104-9a2c-0434d485fc13", "2Q2e4al2pWzg");

        mic = (ImageView) findViewById(R.id.mic);
        chatText = (EditText) findViewById(R.id.chatText);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageListView = (ListView) findViewById(R.id.messageListView);
        gridview = findViewById(R.id.gridview);
        closeGrid = findViewById(R.id.closegrid);
        gridLinear = findViewById(R.id.gridLinear);

//        camera = findViewById(R.id.camera);

        shared_Details = getSharedPreferences("Details", Context.MODE_PRIVATE);
        edit_Details = shared_Details.edit();

        shared_seat = getSharedPreferences("SeatInfo",MODE_PRIVATE);
        edit_seat = shared_seat.edit();

        username = shared_Details.getString("name", "Bhavya");
        prefs = shared_Details.getString("preference", "Veg");
        genre = shared_Details.getString("movies", "Sufi");
        seat = shared_seat.getString("seatNo","0");

        sendChatMessageToTheScreen();

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        messageListView.setAdapter(chatArrayAdapter);

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {


            }

            @Override
            public void onDone(String utteranceId) {

                if (QUERY_FLAG == 0 && !(RESPONSE_FLAG.equals("True"))) {

                    promptSpeechInput();
                } else {
                    RESPONSE_FLAG = "False";
                }
            }

            @Override
            public void onError(String utteranceId) {

            }
        });

       // sendChatMessageFromTheCall("Hi!\nI'm ZooZoo Bot.\nI can assist you with:\n 1.Your queries\n2.Latest Offers\n3.Offers on nearby places\n4.Register a complaint", 2);
//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                File dir =
//                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//
//                output = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpeg");
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
//                startActivityForResult(intent, CAMERA_REQUEST);
//            }
//        });

        chatText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (chatText.getText().toString().length() > 0) {
                    mic.setVisibility(View.GONE);
                    sendButton.setVisibility(View.VISIBLE);
                } else {
                    mic.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (chatText.getText().toString().length() > 0) {
                    mic.setVisibility(View.GONE);
                    sendButton.setVisibility(View.VISIBLE);
                } else {
                    mic.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (chatText.getText().toString().length() > 0) {
                    mic.setVisibility(View.GONE);
                    sendButton.setVisibility(View.VISIBLE);
                } else {
                    mic.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.GONE);
                }
            }

        });

        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessageToTheScreen();
                }
                return false;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (chatText.getText().toString().length() > 0) {
                    if(chatText.getText().toString().trim().toLowerCase().contains("news"))
                    {
                        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString(), picturePath, null));
                        chatText.setText("");

                        buildConnection();

                    }
                    else {
                        sendChatMessageToTheScreen();
                        QUERY_FLAG = 1;
                    }
                }
            }
        });

        messageListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messageListView.setAdapter(chatArrayAdapter);


        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
                QUERY_FLAG = 0;
            }
        });

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                messageListView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        closeGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridLinear.setVisibility(View.GONE);
            }
        });

    }

    private boolean sendChatMessageToTheScreen() {
        String data = chatText.getText().toString().trim();
        if(!data.equals("")) {
            chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString(), picturePath, null));
        }
        InputData input = new InputData.Builder(data).build();
        if (responseContext == null) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            responseContext = gson.fromJson("{\"username\":\""+username+"\",\"preference\":\"" + prefs.toLowerCase() + "\",\"genre\":\""+genre.toLowerCase()+"\",\"seatnumber\":\"" + seat + "\"}", com.ibm.watson.developer_cloud.assistant.v1.model.Context.class);
            //send seat number name prefs in context here
            options = new MessageOptions.Builder("7bc662e1-dca9-44ea-9d0e-9dd75a0bacad")
                    .input(input).context(responseContext)
                    .build();
        } else {
            options = new MessageOptions.Builder("7bc662e1-dca9-44ea-9d0e-9dd75a0bacad")
                    .input(input).context(responseContext)
                    .build();
        }

        new AsyncCaller().execute();
        chatText.setText("");

        return true;
    }


    public static boolean sendChatMessageFromTheCall(String reply, int call) {
        if (call == 1) {
            chatArrayAdapter.add(new ChatMessage(!side, reply, ImageUrl, openAppUrl));
        } else if (call == 2) {
            chatArrayAdapter.add(new ChatMessage(!side, reply, null, null));

        } else {
            chatArrayAdapter.add(new ChatMessage(!side, reply, nearbyImageUrl, null));

        }
        return true;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Up");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "speech_not_supported", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if ((resultCode == RESULT_OK) && (null != data)) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    chatText.setText(result.get(0));
                    if (chatText.getText().toString().length() > 0) {
                        sendChatMessageToTheScreen();
                        chatText.setText("");
                    }
                }
            }
            break;

            case CAMERA_REQUEST: {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.fromFile(output), "image/jpeg");
                imageUri = Uri.fromFile(output);
                picturePath = ImageFilePath.getPath(ChatBot.this.getApplicationContext(), imageUri);
                System.out.println("image Uri" + picturePath);

                if (picturePath != null) {
                    chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString(), picturePath, null));
                    new WatsonCall(ChatBot.this).execute(picturePath);
                    picturePath = null;
                } else {
                    Toast.makeText(ChatBot.this, "No Image Captured!", Toast.LENGTH_SHORT).show();
                }

            }


        }


    }

    public void getWatson(String outlet, Context context) {
        QUERY_FLAG = 1;
        InputData input = new InputData.Builder(outlet).build();

        if (responseContext == null) {
            options = new MessageOptions.Builder("8d565a8e-f1d0-42a7-b8c1-bb9da40bc234")
                    .input(input)
                    .build();
        } else {
            options = new MessageOptions.Builder("8d565a8e-f1d0-42a7-b8c1-bb9da40bc234")
                    .input(input).context(responseContext)
                    .build();
        }

        new AsyncCaller().execute();

    }

    public class AsyncCaller extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                response = service.message(options).execute();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread

            try {

                fortts = "";
                if (response != null) {

//        System.out.println(response);

                    if (response.getOutput().getText().size() > 0) {
                        for (int i = 0; i < response.getOutput().getText().size(); i++) {
                            if (!(response.getOutput().getText().get(i) == null)) {
                                String res = response.getOutput().getText().get(i);
                                System.out.println(res);
                                if (!(response.getContext().get("url") == null)) {
                                    ImageUrl = response.getContext().get("url").toString().trim();
                                    System.out.println(ImageUrl);

                                }
                                if (!(response.getContext().get("openapp") == null)) {
                                    openAppUrl = response.getContext().get("openapp").toString().trim();
                                    System.out.println(openAppUrl);
                                }
                                if(!(response.getContext().get("menu") == null))
                                {
                                    JsonObject jsonContext = new JsonParser().parse(response.getContext().toString()).getAsJsonObject();
                                    JsonArray jsonMenu =  jsonContext.get("menu").getAsJsonArray();

                                    items.clear();
                                    for(int j=0 ;j< jsonMenu.size();j++) {
                                        JsonObject entityJson = jsonMenu.get(j).getAsJsonObject();
                                        items.add(new suggestionsObject(entityJson.get("url").getAsString(),entityJson.get("price").getAsString(),entityJson.get("description").getAsString(),entityJson.get("itemname").getAsString(),"Image"));
                                    }
                                    CustomEntityAdapter customAdapter = new CustomEntityAdapter(ChatBot.this, items);
                                    gridview.setAdapter(customAdapter);
                                    gridLinear.setVisibility(View.VISIBLE);
                                    gridview.bringToFront();
                                }

                                if(!(response.getContext().get("playlist") == null)) {
                                    JsonObject jsonContext = new JsonParser().parse(response.getContext().toString()).getAsJsonObject();
                                    JsonArray jsonMenu = jsonContext.get("playlist").getAsJsonArray();

                                    JsonObject jsonmovieContext = new JsonParser().parse(response.getContext().toString()).getAsJsonObject();
                                    JsonArray jsonplay =  jsonContext.get("playlist").getAsJsonArray();

                                    items.clear();
                                    for(int j=0 ;j< jsonMenu.size();j++) {
                                        JsonObject entityJson = jsonMenu.get(j).getAsJsonObject();
                                        items.add(new suggestionsObject(entityJson.get("url").getAsString(),entityJson.get("videoname").getAsString(), "Video"));
                                    }
                                    CustomEntityAdapter customAdapter = new CustomEntityAdapter(ChatBot.this, items);
                                    gridview.setAdapter(customAdapter);
                                    gridLinear.setVisibility(View.VISIBLE);
                                    gridview.bringToFront();
                                }

                                if(!(response.getContext().get("options") == null)) {

                                    JsonObject jsonContext = new JsonParser().parse(response.getContext().toString()).getAsJsonObject();
                                    JsonArray jsoninstruct = jsonContext.get("options").getAsJsonArray();

                                    ArrayList<String> instruct = new ArrayList<String>();
                                    for(int j=0 ;j< jsoninstruct.size();j++) {
                                       instruct.add(jsoninstruct.get(j).getAsString());
                                    }



                                    chatArrayAdapter.add(new ChatMessage(!side, instruct));

                                }
                                else

                                sendChatMessageFromTheCall(res, 1);
                                fortts = fortts + res + ",";

                                ImageUrl = null;

                            }
                        }
                    }

                    responseContext = response.getContext();
                    if (responseContext.get("url") != null) {
                        responseContext = null;
                    }
                    if (response.getContext().get("exit") == null) {
                        RESPONSE_FLAG = "False";
                    } else if (response.getContext().get("exit").toString().equals("True")) {
                        RESPONSE_FLAG = "True";
                    }

//                    if (response.getContext().get("orderfood") != null) {
//                        if (response.getContext().get("orderfood").toString().equals("True")) {
//                            try {
//                                fooditem = response.getContext().get("fooditem").toString();
//                                new AsyncCloudant().execute();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }

                    System.out.println(response);
                    if (QUERY_FLAG == 0) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, System.currentTimeMillis() + "");

                        if(gridLinear.getVisibility() == View.GONE) {
                            tts.speak(fortts, TextToSpeech.QUEUE_FLUSH, map);
                        }
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onPause() {
        if (tts != null) {
            tts.stop();
        }
        super.onPause();
    }

    private static class AsyncCloudant extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {


            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                CloudantClient client = ClientBuilder.account("b0d8c6c3-d4c4-4029-95f8-a9dcba368f8f-bluemix")
                        .username("b0d8c6c3-d4c4-4029-95f8-a9dcba368f8f-bluemix")
                        .password("150e8a78ab2aa45424603cf097b8a8ccdfbc6b09ac3994ee784e092319519da0")
                        .build();

                System.out.println("Server Version: " + client.serverVersion());

                Database db = client.database("food-orders", false);

                ComplaintModel complaint = new ComplaintModel(fooditem, username, seat, status);

                db.save(complaint);
            } catch (CouchDbException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // this method will be running on UI thread
            System.out.println("complaint saved");
            responseContext = null;
        }

    }

    public static void orderFood(String foodItem, Context context)
    {
        fooditem = foodItem;

        gridLinear.setVisibility(View.GONE);

        sendChatMessageFromTheCall("Your order for "+foodItem +" has been placed Successfully.",2);

        Toast.makeText(context, foodItem +" ordered Successfully.", Toast.LENGTH_SHORT).show();
        new AsyncCloudant().execute();
    }

    @Override
    public void onBackPressed() {

    }

    private void buildConnection() {

        API api = retroClient.getApiService();



        api.getNews().enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {

                //Dismiss Dialog

                if (response.isSuccessful()) {

                    model = response.body();

                    items.clear();

                    for(int i = 0; i< model.getArticles().size(); i++) {
                        items.add(new suggestionsObject(model.getArticles().get(i).getUrlToImage(), model.getArticles().get(i).getTitle(),model.getArticles().get(i).getDescription(), "News"));
                    }

                    CustomEntityAdapter customAdapter = new CustomEntityAdapter(ChatBot.this, items);
                    gridview.setAdapter(customAdapter);
                    gridLinear.setVisibility(View.VISIBLE);
                    gridview.bringToFront();


                }
                else
                {
                    Log.d("response",response.message());
                }
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

                Log.d("Error Message", "Unable to submit post to API.");
            }

        });
    }


}

//rohit@edugorilla.com