package com.example.user.buttonexam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 2017-06-23.
 */

public class IntroActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_main);

        TextView t=(TextView) findViewById(R.id.textView);
        t.setTextSize(20);
        t.setText("TOUCH");
        t.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
