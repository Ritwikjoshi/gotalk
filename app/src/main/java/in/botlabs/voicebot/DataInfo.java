package in.botlabs.voicebot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class DataInfo extends AppCompatActivity {
    Spinner prefsSpin, moviesSpin;
    ArrayAdapter<String> prefsAdapter;
    ArrayAdapter<String> movieprefsAdapter;
    ArrayList<String> prefsList = new ArrayList <String>();
    ArrayList<String> movieprefsList = new ArrayList <String>();

    AppCompatButton submit;
    EditText nameEt;
    String name,preference = "Veg", movies = "Sufi";

    SharedPreferences shared_user;
    SharedPreferences.Editor edit_user;

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_info);

        prefsSpin = findViewById(R.id.food_list);
        submit = findViewById(R.id.submitUserInfo);
        nameEt = findViewById(R.id.name);
        moviesSpin = findViewById(R.id.movieList);

        shared_user = getSharedPreferences("Details", MODE_PRIVATE);
        edit_user = shared_user.edit();

        prefsList.add("Veg");
        prefsList.add("Nonveg");
        prefsList.add("Jain");
        prefsList.add("Diet");

        movieprefsList.add("Sufi");
        movieprefsList.add("Bollywood");

        prefsAdapter = new ArrayAdapter<String>(DataInfo.this, R.layout.prefs_list_item, prefsList);
        prefsSpin.setAdapter(prefsAdapter);

        movieprefsAdapter = new ArrayAdapter<String>(DataInfo.this, R.layout.prefs_list_item, movieprefsList);
        moviesSpin.setAdapter(movieprefsAdapter);

        prefsSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                preference = prefsSpin.getItemAtPosition(position).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        moviesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                movies = moviesSpin.getItemAtPosition(i).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEt.getText().toString().trim();
                if(name.equals(""))
                {
                    Toast.makeText(DataInfo.this, "Name must not be empty.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    edit_user.putString("name",name);
                    edit_user.putString("preference",preference);
                    edit_user.putString("movies",movies);
                    edit_user.apply();
                    Intent i = new Intent(DataInfo.this,ChatBot.class);
                    startActivity(i);
                }
            }
        });
    }
}
