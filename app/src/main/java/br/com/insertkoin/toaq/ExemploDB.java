package br.com.insertkoin.toaq;

import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ExemploDB extends AppCompatActivity {

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    protected void onStart() {
        super.onStart();
        final TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final String imei = telephonyManager.getImei();
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String user = mAuth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://toaq-b750f.firebaseio.com/users");
        reference.child(user).child("imei").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                String verImei = (String) dataSnapshot.getValue();
                TextView textIMEI = findViewById(R.id.textIMEI);
                    textIMEI.setText(verImei);
                } else {
                TextView textIMEI = findViewById(R.id.textIMEI);
                textIMEI.setText(imei);}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exemplo_db);


        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        Button botaoAdd = findViewById(R.id.buttonAddArtist);

        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String user = mAuth.getCurrentUser().getUid();

                EditText data = findViewById(R.id.editTextName);
                String insertdata = data.getText().toString().trim();

                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("yyyyMMdd");
                date = dateFormat.format(calendar.getTime());

                if(!insertdata.isEmpty()){
                    mDatabase.child(user).child("aulateste01").child("20191105").setValue(insertdata);
                } else {
                    Toast.makeText(ExemploDB.this,"Campo vazio",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
