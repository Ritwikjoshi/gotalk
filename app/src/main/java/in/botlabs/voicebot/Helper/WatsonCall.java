package in.botlabs.voicebot.Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import in.botlabs.voicebot.ChatBot;

/**
 * Created by BD on 22-01-2018.
 */


public class WatsonCall extends AsyncTask<String, Integer, String> {

    ClassifyOptions classify;
    String picturePath;
    VisualRecognition service;
    Context baseContext;


    public WatsonCall(Context context)
    {
        baseContext = context;
    }

    @Override
    protected String doInBackground(String... path) {

        String picturePath = new String(path[0]);

        System.out.println(picturePath);


        File file = new File(picturePath);

        System.out.println(file.getName());

        if(file.exists()) {
            IamOptions options = new IamOptions.Builder()
                    .apiKey("TeKLPSOSdF8sv2o2ETjPnVugUbWuybU4mwExT6Yg1Rqa")
                    .build();
            service = new VisualRecognition("2018-03-19", options);

            try {
//                InputStream imagesStream = new FileInputStream(picturePath);
                classify = new ClassifyOptions.Builder()
                        .imagesFile(file).classifierIds(Arrays.asList("brand_classifier_1721251831")).build();
                System.out.println("yahaaaa call hora");

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        try {
            ClassifiedImages result = service.classify(classify).execute();
            System.out.println(result.toString() + "yahaaaa");

            return result.toString();
            }
             catch (Exception e)
            {
                e.printStackTrace();
            }


        }

        return null;
    }

    // method onPostExecute Getting values from given Json Response obtained in result

    @Override
    protected void onPostExecute(String s) {

        System.out.println(s);
        if(s == null)
        {
            Toast.makeText(baseContext, "Unable to Upload Image!", Toast.LENGTH_SHORT).show();
        }

        try {
            JSONObject response = new JSONObject(s);
            JSONArray res = response.getJSONArray("images");
            JSONObject jobj = res.getJSONObject(0);
            JSONArray classifiers = jobj.getJSONArray("classifiers");
            JSONObject forclasses = classifiers.getJSONObject(0);
            JSONArray classes = forclasses.getJSONArray("classes");

            ArrayList<String> outletList = new ArrayList <String>();
            ArrayList<Float> confidenceList = new ArrayList <Float>();
            for(int i=0;i<classes.length();i++) {
                JSONObject forvalues = classes.getJSONObject(i);
                String outlet = forvalues.getString("class");
                String confidence = forvalues.getString("score");

                outletList.add(outlet);
                confidenceList.add(Float.parseFloat(confidence));
            }

            float maxConfidence = confidenceList.get(0);
            int maxIndex = 0;

            for(int i =0;i<confidenceList.size();i++)
            {
                if(maxConfidence<confidenceList.get(i))
                {
                    maxConfidence = confidenceList.get(i);
                    maxIndex = i;
                }
            }

            String maxOutlet;
            maxOutlet = outletList.get(maxIndex);

            System.out.println(maxOutlet);


            if(maxConfidence>=0.8) {
                ((ChatBot) baseContext).getWatson(maxOutlet, baseContext);

            }
            else
            {
                ((ChatBot) baseContext).sendChatMessageFromTheCall("No offers available",2);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}