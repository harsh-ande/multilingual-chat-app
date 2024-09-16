package com.project.sdpd.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class ChatRoomActivity extends AppCompatActivity {

    public int num ;

    TextView textView;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        //String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        textView = (TextView)findViewById(R.id.tv1);
        editText = (EditText)findViewById(R.id.et1);
        Button create = (Button)findViewById(R.id.create);
        Button enter = (Button)findViewById(R.id.enter);
        num = 0;
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Intent activityChangeIntent = new Intent(PresentActivity.this, NextActivity.class);

                // currentContext.startActivity(activityChangeIntent);

                //PresentActivity.this.startActivity(activityChangeIntent);

                Random r = new Random();
                num = r.nextInt(1000)+1;

                textView.setText(""+num);
                editText.setText(""+num);
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //Intent activityChangeIntent = new Intent(PresentActivity.this, NextActivity.class);

                // currentContext.startActivity(activityChangeIntent);

                //PresentActivity.this.startActivity(activityChangeIntent);

                num = Integer.parseInt(editText.getText().toString());
                //MainActivity.num = num;
                //MainActivity.globalChat = false;
                //String str = new String((new Integer(num).toString()));
                //Intent returnBtn = getPackageManager().getLaunchIntentForPackage("android.intent.action.MAIN");
                //returnBtn.putExtra("global",false);
                //returnBtn.putExtra("num",num);
                //returnBtn.putExtra("garbage","garbage");
                //startActivity(returnBtn);
                //finish();
                //super.onBackPressed();

                Intent intent = new Intent();
                intent.putExtra("num",num);
                setResult(RESULT_OK,intent);
                finish();
            }
        });



    }
}
