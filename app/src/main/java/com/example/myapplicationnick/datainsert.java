package com.example.myapplicationnick;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class datainsert extends AppCompatActivity
{
    EditText txtname, txtage, txtphone, txtheight;
    Button btnsave;
    DatabaseReference reff;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datainsert);
        txtname=(EditText)findViewById(R.id.txtname);
        txtage=(EditText)findViewById(R.id.txtage);
        btnsave=(Button)findViewById(R.id.btnsave);
        member = new Member();
        reff= FirebaseDatabase.getInstance().getReference().child("Member");
        btnsave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // int agea=Integer.parseInt(txtage.getText().toString().trim());

                member.setName(txtname.getText().toString().trim());
                member.setAge(txtage.getText().toString().trim());
                reff.push().setValue(member);
                Toast.makeText(datainsert.this, "Data inserted sucessfully", Toast.LENGTH_LONG).show();
            }
        });
        // test
    }
}
