package com.example.myapplicationnick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity
{
    EditText txtname, txtage, txtphone, txtheight;
    Button btnsave;
    DatabaseReference reff;
    Member member;
    Button btnLogout;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtname=(EditText)findViewById(R.id.txtname);
        txtage=(EditText)findViewById(R.id.txtage);
        btnsave=(Button)findViewById(R.id.btnsave);
        btnLogout = findViewById(R.id.logout);
        member = new Member();

        mAuth = FirebaseAuth.getInstance();

        String user_id = mAuth.getCurrentUser().getUid();

        reff = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        btnsave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // int agea=Integer.parseInt(txtage.getText().toString().trim());



                member.setName(txtname.getText().toString().trim());
                member.setAge(txtage.getText().toString().trim());
                reff.setValue(member);
                Toast.makeText(HomeActivity.this, "Data inserted sucessfully", Toast.LENGTH_LONG).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(HomeActivity.this, SignupActivity.class);
                startActivity(intToMain);
            }
        });
        // test
    }
}
