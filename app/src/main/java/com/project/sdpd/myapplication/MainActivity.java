package com.project.sdpd.myapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity{
    private static final int SIGN_IN_REQUEST_CODE = 1;
    private static final int SECOND_ACTIVITY_RESULT_CODE = 2;
    private FirebaseListAdapter<ChatMessage> adapter;
    private long prevTime;
    private String prefLang;
    public boolean globalChat;
    public int num;
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    private EditText mVoiceInputTv;
    private FloatingActionButton mSpeakBtn;
    private FloatingActionButton fab;

    /*
    private TextToSpeech tts;
    private FloatingActionButton btnSpeak;
    private EditText txtText;
    private String lastMessage;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        globalChat = true;
        //lastMessage="";

        prefLang = "en";
        //prevTime = System.currentTimeMillis();

        mVoiceInputTv = (EditText) findViewById(R.id.input);
        mSpeakBtn = (FloatingActionButton) findViewById(R.id.fab2);
        mSpeakBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("GG","Inside fab2 onClick");
                startVoiceInput();
            }
        });
        /*
        tts = new TextToSpeech(this, this);
        btnSpeak = (FloatingActionButton) findViewById(R.id.fab);
        //txtText = (EditText) findViewById(R.id.txtText);
        // button on click event
        */

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            super.startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            // Load chat room contents
            displayChatMessages();
        }

        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("GG","Inside fab onClick");
                EditText input = (EditText)findViewById(R.id.input);
                //Log.d("GG2","After fab onCLick, input.getText = "+input.getText().toString());
                //if(input.getText().toString()==null)
                //{
                    //speakOut();Log.d("GG","Inside2 fab onClick, String being spoken = "+lastMessage);
                //}
                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                //FirebaseDatabase.getInstance().getReference("Users/").or
                if(globalChat)
                {
                    //lastMessage = input.getText().toString();
                    FirebaseDatabase.getInstance()
                            .getReference("Common/")
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName(),"en",false,0)
                            );
                }
                else
                {
                    //lastMessage = input.getText().toString();
                    FirebaseDatabase.getInstance()
                            .getReference(""+num+"/Common/")
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName(),"en",true,num)
                            );
                    FirebaseDatabase.getInstance()
                            .getReference("/Common/")
                            .push()
                            .setValue(new ChatMessage(input.getText().toString(),
                                    FirebaseAuth.getInstance()
                                            .getCurrentUser()
                                            .getDisplayName(),"en",true,num)
                            );
                }



                // Clear the input
                input.setText("");
            }
        });
    }


    private void startVoiceInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        if(prefLang=="te" || prefLang=="ta" || prefLang=="ml" || prefLang=="hi" || prefLang=="gu")
        {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, ""+prefLang+"-IN");
        }
        else
        {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, ""+prefLang);
        }
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }

       // mVoiceInputTv.setText("HELLO");
    }

    /*
    private void speakOut() {

        String text = lastMessage;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
        }
    }
    */
    private void displayChatMessages() {

            ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
            if(globalChat)
            {
                adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                        R.layout.message, FirebaseDatabase.getInstance().getReference(""+prefLang+"/")) {
                    @Override
                    protected void populateView(View v, ChatMessage model, int position) {
                        //if(model.getMessageText()!="")
                        //{
                        //  && model.getMessageTime()>prevTime
                        TextView messageText = (TextView)v.findViewById(R.id.message_text);
                        TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                        TextView messageTime = (TextView)v.findViewById(R.id.message_time);
                        //lastMessage = model.getMessageText();
                        Intent data = getIntent();
                        messageText.setText(model.getMessageText());
                        messageUser.setText(model.getMessageUser());
                        //lastMessage=model.getMessageText();
                        // Format the date before showing it
                        messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                                model.getMessageTime()));
                        //prevTime = model.getMessageTime();
                        //}

                    }
                };
            }
            else
            {
                adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                        R.layout.message, FirebaseDatabase.getInstance().getReference(""+num+"/"+prefLang+"/")) {
                    @Override
                    protected void populateView(View v, ChatMessage model, int position) {
                        //if(model.getMessageText()!="") {
                            //  && model.getMessageTime()>prevTime
                            TextView messageText = (TextView) v.findViewById(R.id.message_text);
                            TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                            TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                            //lastMessage = model.getMessageText();
                            Intent data = getIntent();
                            messageText.setText(model.getMessageText());
                            messageUser.setText(model.getMessageUser());
                            //lastMessage = model.getMessageText();
                            // Format the date before showing it
                            messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                                    model.getMessageTime()));
                            //prevTime = model.getMessageTime();
                        //}

                    }
                };
            }


            listOfMessages.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show();
                displayChatMessages();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }
        if(requestCode==SECOND_ACTIVITY_RESULT_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                globalChat = false;
                num = data.getIntExtra("num",99);
                displayChatMessages();
            }
        }
        if(requestCode==REQ_CODE_SPEECH_INPUT)
        {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                mVoiceInputTv.setText(result.get(0));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });
        }

        if(item.getItemId() == R.id.english) {
            prefLang = "en";
            displayChatMessages();
        }

        if(item.getItemId() == R.id.french) {
            prefLang = "fr";
            displayChatMessages();
        }

        if(item.getItemId() == R.id.hindi) {
            prefLang = "hi";
            displayChatMessages();
        }

        if(item.getItemId() == R.id.malayalam) {
            prefLang = "ml";
            displayChatMessages();
        }

        if(item.getItemId() == R.id.gujarati) {
            prefLang = "gu";
            displayChatMessages();
        }

        if(item.getItemId() == R.id.spanish) {
            prefLang = "es";
            displayChatMessages();
        }

        if(item.getItemId() == R.id.tamil) {
            prefLang = "ta";
            displayChatMessages();
        }

        if(item.getItemId() == R.id.telugu) {
            prefLang = "te";
            displayChatMessages();
        }

        if(item.getItemId() == R.id.createRoom) {
            //globalChat=false;
            Intent intent = new Intent(this, ChatRoomActivity.class);
            //EditText editText = (EditText) findViewById(R.id.et1);
            //String message = editText.getText().toString();
            //intent.putExtra(EXTRA_MESSAGE, message);
            startActivityForResult(intent,SECOND_ACTIVITY_RESULT_CODE);



        }


        return true;
    }
    /*
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.forLanguageTag(prefLang));

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                fab.setEnabled(true);
                speakOut();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
    */
}
