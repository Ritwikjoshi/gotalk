package in.botlabs.voicebot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class SeatInfo extends AppCompatActivity {
    Button submit;
    EditText seatNoEt;
    String seatNo, alphabet = "A";
    Spinner alphabetSpinner;
    ArrayList<String> alphabets = new ArrayList <String>();
    ArrayAdapter<String> alphabetAdapter;

    SharedPreferences shared;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_info);
        submit = findViewById(R.id.seat_submit);
        seatNoEt= findViewById(R.id.seatNoEt);
        alphabetSpinner = findViewById(R.id.alphabetSpinner);

        shared = getSharedPreferences("SeatInfo", MODE_PRIVATE);
        edit = shared.edit();

        addAlphabets();

        alphabetAdapter = new ArrayAdapter<String>(SeatInfo.this, R.layout.prefs_list_item, alphabets);
        alphabetSpinner.setAdapter(alphabetAdapter);

        if(!shared.getString("seatNo","").equals(""))
        {
            Intent i = new Intent(SeatInfo.this,DataInfo.class);
            startActivity(i);
        }

        alphabetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                alphabet = alphabetSpinner.getItemAtPosition(i).toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seatNo = seatNoEt.getText().toString().trim();

                if(seatNo.equals(""))
                {
                    Toast.makeText(SeatInfo.this, "Enter a valid seat number.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    edit.putString("seatNo",seatNo+alphabet);
                    edit.apply();
                    Intent i = new Intent(SeatInfo.this,DataInfo.class);
                    startActivity(i);
                }
            }
        });


    }

    public void addAlphabets()
    {
        alphabets.add("A");
        alphabets.add("B");
        alphabets.add("C");
        alphabets.add("D");
        alphabets.add("E");
        alphabets.add("F");
        alphabets.add("G");
        alphabets.add("H");
        alphabets.add("J");
        alphabets.add("K");
    }
}
