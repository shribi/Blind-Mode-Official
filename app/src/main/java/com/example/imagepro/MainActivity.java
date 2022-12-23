package com.example.imagepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;

public class MainActivity<pubic> extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private Intent intent;
    public Button button;
    public static boolean blindModeOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blindModeOn = true;
                textToSpeech.speak("Welcome to blind mode",TextToSpeech.QUEUE_FLUSH,null,null);
                startActivity(new Intent(MainActivity.this, CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle bundle) {
                ArrayList<String> matches;
                matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String res;

                if(matches!=null || blindModeOn){
                    res=matches.get(0);

                    if(res.equals("start") || blindModeOn){
                        textToSpeech.speak("Welcome to blind mode",TextToSpeech.QUEUE_FLUSH,null,null);
                        startActivity(new Intent(MainActivity.this, CameraActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));

                    }
                    else{
                        textToSpeech.speak("Sorry To Enable Blind Mode Say ON",TextToSpeech.QUEUE_FLUSH,null,null);

                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                speechRecognizer.startListening(intent);
                            }

                    }

                }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR || blindModeOn){
                    textToSpeech.speak("To Enable Blind Mode Say ON",TextToSpeech.QUEUE_FLUSH,null,null);
                    speechRecognizer.startListening(intent);
                }
            }
        });

    }
}
