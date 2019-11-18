package br.com.insertkoin.toaq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    Button openCamera;
    Button buttonSignOut;
    private FirebaseAuth mAuth;
    ListView listViewAulas;
    DatabaseReference mDatabase;
    List<String> listaAulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        //final String loggedUser = mAuth.getCurrentUser().getUid();
        //final DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://toaq-b750f.firebaseio.com/users");

        buttonSignOut = findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                updateUI(null);
            }
        });

        openCamera = findViewById(R.id.buttonOpenCamera);
        openCamera.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, QRCamera.class);
            MainActivity.this.startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://toaq-b750f.firebaseio.com/users");
            listViewAulas = findViewById(R.id.listViewAulas);
            listaAulas = new ArrayList<>();
            String loggedUser = mAuth.getCurrentUser().getUid();

            reference.child(loggedUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaAulas.clear();

                    for (DataSnapshot aulaSnapshot : dataSnapshot.child("10016646").getChildren()) {

                        String aula = aulaSnapshot.getValue(String.class);
                        listaAulas.add(aula);

                    }

                    AulaLista aulaListaAdapter = new AulaLista(MainActivity.this, listaAulas);
                    listViewAulas.setAdapter(aulaListaAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
    }

}
