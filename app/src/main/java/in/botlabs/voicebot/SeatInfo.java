package in.botlabs.voicebot;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SeatInfo extends AppCompatActivity {
    Button submit;
    EditText seatNoEt;
    String seatNo;

    SharedPreferences shared;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_info);
        submit = findViewById(R.id.seat_submit);
        seatNoEt= findViewById(R.id.seatNoEt);

        shared = getSharedPreferences("SeatInfo", MODE_PRIVATE);
        edit = shared.edit();

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
                    edit.putString("seatNo",seatNo);
                    edit.apply();
                    Intent i = new Intent(SeatInfo.this,DataInfo.class);
                    startActivity(i);
                }
            }
        });
    }
}
