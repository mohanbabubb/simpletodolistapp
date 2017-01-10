package com.systemadminnotes.stodolist.ToDoView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.systemadminnotes.stodolist.R;

public class ProfileActivity extends AppCompatActivity {

    Button btn;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btn=(Button) findViewById(R.id.btnl);
        txt=(TextView) findViewById(R.id.textView5);
        btn.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v){
                txt.setText("Mohanbabu.");
                return false;
            }
        });
    }
}
