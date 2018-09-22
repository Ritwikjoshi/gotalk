package in.botlabs.voicebot;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import in.botlabs.voicebot.Helper.WatsonCall;
import in.botlabs.voicebot.Retrofit.retroClient;


import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.org.lightcouch.CouchDbException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import in.botlabs.voicebot.Adapters.ChatArrayAdapter;
import in.botlabs.voicebot.Helper.ImageFilePath;
import in.botlabs.voicebot.Model.ComplaintModel;
import in.botlabs.voicebot.Model.ResultModel;
import in.botlabs.voicebot.Objects.ChatMessage;
import in.botlabs.voicebot.Retrofit.API;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBot extends AppCompatActivity {


    private static final String TAG = "ChatActivity";
//    private static final String ChatBotUrl ="https://gateway-syd.watsonplatform.net/assistant/api";

    private ChatArrayAdapter chatArrayAdapter;
    private ImageView sendButton, mic;
    private EditText chatText;
    private ListView messageListView;
    private boolean side = true;
    final int REQ_CODE_SPEECH_INPUT = 0;
    Assistant service;
    MessageOptions options;
    MessageResponse response;
    private static final int CAMERA_REQUEST = 1;
    TextToSpeech tts;
    String fortts = "", ImageUrl, openAppUrl;
    com.ibm.watson.developer_cloud.assistant.v1.model.Context responseContext = null;
    int QUERY_FLAG = 0;
    ImageView camera;
    String category = "", description = "", username = "", age = "", status = "False";
    String RESPONSE_FLAG = "False";
    String nearbyImageUrl;
    SharedPreferences shared_Details, shared_seat;
    SharedPreferences.Editor edit_Details, edit_seat;
    String prefs, seat;

    String picturePath;
    Uri imageUri;
    private File output = null;


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
        camera = findViewById(R.id.camera);

        shared_Details = getSharedPreferences("Details", Context.MODE_PRIVATE);
        edit_Details = shared_Details.edit();

        shared_seat = getSharedPreferences("SeatInfo",MODE_PRIVATE);
        edit_seat = shared_seat.edit();

        username = shared_Details.getString("name", "Bhavya");
        prefs = shared_Details.getString("preference", "Veg");
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
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File dir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

                output = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpeg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });

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
                    sendChatMessageToTheScreen();
                    QUERY_FLAG = 1;
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

    }

    private boolean sendChatMessageToTheScreen() {
        String data = chatText.getText().toString().trim();
        if(!data.equals("")) {
            chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString(), picturePath, null));
        }
        InputData input = new InputData.Builder(data).build();
        if (responseContext == null) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            responseContext = gson.fromJson("{\"username\":\""+username+"\",\"preference\":\"" + prefs + "\",\"seatnumber\":\"" + seat + "\"}", com.ibm.watson.developer_cloud.assistant.v1.model.Context.class);
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


    public boolean sendChatMessageFromTheCall(String reply, int call) {
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

                    if (response.getContext().get("submitcomplaint") != null) {
                        if (response.getContext().get("submitcomplaint").toString().equals("True")) {
                            try {
                                category = response.getContext().get("category").toString();
                                description = response.getContext().get("description").toString();
                                new AsyncCloudant().execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    System.out.println(response);
                    if (QUERY_FLAG == 0) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, System.currentTimeMillis() + "");

                        tts.speak(fortts, TextToSpeech.QUEUE_FLUSH, map);
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

    private class AsyncCloudant extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {


            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                CloudantClient client = ClientBuilder.account("59cde6d4-f925-4a3d-85bb-9a1314f29e06-bluemix")
                        .username("59cde6d4-f925-4a3d-85bb-9a1314f29e06-bluemix")
                        .password("86ddf880d33529aa6367fc90ef808f63bd76530df5818352b01e608bbf1c8e16")
                        .build();

                System.out.println("Server Version: " + client.serverVersion());

                Database db = client.database("user_complains", false);


                ComplaintModel complaint = new ComplaintModel(category, description, username, age, status);


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


}

final class MyEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public MyEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}
