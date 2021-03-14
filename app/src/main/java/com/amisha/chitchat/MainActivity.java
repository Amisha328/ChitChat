package com.amisha.chitchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int flag = 0;
    String message = "";
    String Username="";
    // ChildEventListener childEventListener;
    String UserMessage = "";
    Animation animation;


    EditText text;
    ImageButton attach;
    ImageButton send;
    RecyclerView recyclerView;
    // ProgressBar progressBar;

    ArrayList<ChatMessage> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.editText);
        attach = findViewById(R.id.imageButton);
        send = findViewById(R.id.imageButton2);
        recyclerView = findViewById(R.id.recyclerView);
        arrayList = new ArrayList<ChatMessage>();
        animation = AnimationUtils.loadAnimation(this, R.anim.slideup);
        recyclerView.setAnimation(animation);

        pref = getSharedPreferences("Username", Context.MODE_PRIVATE);
        editor = pref.edit();


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("message");
        firebaseAuth = FirebaseAuth.getInstance();

        send.setEnabled(false);

        Bundle bundle = getIntent().getExtras();
        Username = bundle.getString("name_key");

        //Enable send button when there is text to send
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    send.setEnabled(true);
                } else {
                    send.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserMessage = text.getText().toString();

                ChatMessage obj = new ChatMessage();
                obj.setName(Username);
                obj.setMessage(UserMessage);

                String childId = databaseReference.push().getKey();
                ChatMessage chatMessage = new ChatMessage();
                databaseReference.child(childId).setValue(obj);

                text.setText("");
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage txt = snapshot.getValue(ChatMessage.class);
                    arrayList.add(txt);
                }

                CustomAdapter adapter = new CustomAdapter(MainActivity.this, arrayList);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            message = null;
            editor.putString("logout_key", message);
            editor.commit();
            flag = 1;

            Intent i = new Intent(MainActivity.this, SignInPage.class);
            startActivity(i);
            finish();
        }

            return super.onOptionsItemSelected(item);

    };

    @Override
    public void onBackPressed() {
        if(flag!=1)
        {
            message = "not pressed";
            editor.putString("logout_key", message);
            editor.commit();
        }
        else
        {
            editor.putString("username_key", Username);
            editor.commit();
        }
        super.onBackPressed();
    }
}
