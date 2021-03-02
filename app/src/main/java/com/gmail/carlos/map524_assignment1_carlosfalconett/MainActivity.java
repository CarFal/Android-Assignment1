package com.gmail.carlos.map524_assignment1_carlosfalconett;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Color;
import  android.os.Bundle;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private int prevAVG = 0;
    AlertDialog.Builder builder;
    AlertDialog.Builder avgBuilder;
    private Menu menu;
    Button falseButton;
    Button trueButton;
    ProgressBar pb;
    ArrayList<Integer> colorList = new ArrayList<Integer>();
    ArrayList<Quiz> questionList = new ArrayList<Quiz>();
    int progress = 0;
    int rightAnswers = 0;
    static int language = 1;
    StorageManager manager;
    boolean can = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(this);
        avgBuilder = new AlertDialog.Builder(this);
        falseButton = (Button) findViewById(R.id.falseButton);
        trueButton = (Button) findViewById(R.id.trueButton);
        manager = new StorageManager();
        //question = (TextView) findViewById(R.id.questionText);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setMax(5);

        if(savedInstanceState != null){
            language = savedInstanceState.getInt("lang");
            if (language == 2) {
                Locale locale = new Locale("es");
                Locale.setDefault(locale);

                Configuration config = new Configuration();
                config.locale = locale;

                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                Toast.makeText(getApplicationContext(), "Spanish", Toast.LENGTH_SHORT).show();
                language = 2;

                finish();
                startActivity(getIntent());
            } else {
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;

                language = 1;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                Toast.makeText(getApplicationContext(), "English", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        }
        updateMenuTitle();

        questionList.add(new Quiz(R.string.question1, true));
        questionList.add(new Quiz(R.string.question2, false));
        questionList.add(new Quiz(R.string.question3, false));
        questionList.add(new Quiz(R.string.question4, false));
        questionList.add(new Quiz(R.string.question5, true));

        colorList.add(R.color.Red);
        colorList.add(R.color.Blue);
        colorList.add(R.color.Green);
        colorList.add(R.color.Brown);
        colorList.add(R.color.Gray);

        Collections.shuffle(questionList);
        Collections.shuffle(colorList);

        Bundle bundle = new Bundle();
        bundle.putInt("textID", questionList.get(progress).showQuestion());
        bundle.putInt("colorID", colorList.get(progress));

        Fragment firstFragment = new FirstFragment();

        firstFragment.setArguments(bundle);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.questionContainer, firstFragment);
        ft.commit();

        //question.setText(questionList.get(progress).showQuestion());
        pb.setProgress(progress);

        falseButton.setOnClickListener(v -> {

            if(checkIfTrue(questionList.get(progress),false)) {
                Toast.makeText(this, R.string.correctAnswer, Toast.LENGTH_SHORT).show();
                if(progress < 5 && can){
                    rightAnswers++;
                }

            }
            else{
                Toast.makeText(this, R.string.wrongAnswer, Toast.LENGTH_SHORT).show();
            }
            if(progress < 4){
                progress++;

                FirstFragment fragmentObj = (FirstFragment) fm.findFragmentById(R.id.questionContainer);
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.remove(fragmentObj);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);


                FirstFragment secondFragment = new FirstFragment();

                bundle.clear();
                bundle.putInt("textID", questionList.get(progress).showQuestion());
                bundle.putInt("colorID", colorList.get(progress));
                secondFragment.setArguments(bundle);
                transaction.replace(R.id.questionContainer, secondFragment);
                transaction.commit();


                pb.setProgress(progress);
            }
            else{
                if(can){
                    File file = null;
                    String data = rightAnswers + ",";
                    manager.saveInternalFile(this, data);
                }

                can = false;
                pb.setProgress(5);
                builder.setMessage("The quiz is over. Your results are:" + rightAnswers + "/5")
                        .setCancelable(false)
                        .setTitle("End of Quiz")
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                can = true;
                                progress = 0;
                                rightAnswers = 0;
                                pb.setProgress(progress);

                                Collections.shuffle(questionList);
                                Collections.shuffle(colorList);

                                FirstFragment fragmentObj = (FirstFragment) fm.findFragmentById(R.id.questionContainer);
                                FragmentTransaction transaction = fm.beginTransaction();
                                transaction.remove(fragmentObj);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

                                FirstFragment secondFragment = new FirstFragment();

                                bundle.clear();
                                bundle.putInt("textID", questionList.get(progress).showQuestion());
                                bundle.putInt("colorID", colorList.get(progress));
                                secondFragment.setArguments(bundle);
                                transaction.replace(R.id.questionContainer, secondFragment);
                                transaction.commit();

                            }
                        }).setNegativeButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });

        trueButton.setOnClickListener(v -> {

            if(checkIfTrue(questionList.get(progress), true)) {
                Toast.makeText(this, R.string.correctAnswer, Toast.LENGTH_SHORT).show();
                if(progress < 5 && can){
                    rightAnswers++;
                }

            }
            else{
                Toast.makeText(this, R.string.wrongAnswer, Toast.LENGTH_SHORT).show();

            }
            if(progress < 4){
                progress++;

                FirstFragment fragmentObj = (FirstFragment) fm.findFragmentById(R.id.questionContainer);
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.remove(fragmentObj);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

                FirstFragment secondFragment = new FirstFragment();

                bundle.clear();
                bundle.putInt("textID", questionList.get(progress).showQuestion());
                bundle.putInt("colorID", colorList.get(progress));
                secondFragment.setArguments(bundle);
                transaction.replace(R.id.questionContainer, secondFragment);
                transaction.commit();

                pb.setProgress(progress);
            }
            else {
                if(can){
                    File file = null;
                    String data = rightAnswers + ",";
                    manager.saveInternalFile(this, data);
                }

                can = false;

                pb.setProgress(5);
                builder.setMessage("The quiz is over. Your results are:" + rightAnswers + "/5")
                        .setCancelable(false)
                        .setTitle("End of Quiz")
                        .setPositiveButton("Repeat", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                can = true;
                                progress = 0;
                                rightAnswers = 0;
                                pb.setProgress(progress);

                                Collections.shuffle(questionList);
                                Collections.shuffle(colorList);

                                FirstFragment fragmentObj = (FirstFragment) fm.findFragmentById(R.id.questionContainer);
                                FragmentTransaction transaction = fm.beginTransaction();
                                transaction.remove(fragmentObj);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);


                                FirstFragment secondFragment = new FirstFragment();

                                bundle.clear();
                                bundle.putInt("textID", questionList.get(progress).showQuestion());
                                bundle.putInt("colorID", colorList.get(progress));
                                secondFragment.setArguments(bundle);
                                transaction.replace(R.id.questionContainer, secondFragment);
                                transaction.commit();
                            }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);
        this.menu = menu;
        updateMenuTitle();
        return true;
    }

    public void updateMenuTitle() {
        if(menu != null){
            MenuItem lang = menu.findItem(R.id.changeLang);
            MenuItem avg = menu.findItem(R.id.avgResults);
            MenuItem rst = menu.findItem(R.id.rstResult);
            if(language == 1) {
                lang.setTitle(R.string.sp_Lang);
                avg.setTitle(R.string.avgRes);
                rst.setTitle(R.string.rstRes);
            } else if(language == 2){
                lang.setTitle(R.string.eng_Lang);
                avg.setTitle(R.string.avgRes);
                rst.setTitle(R.string.rstRes);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.changeLang: {
                if (item.getTitle().equals("Spanish")) {
                    Locale locale = new Locale("es");
                    Locale.setDefault(locale);

                    Configuration config = new Configuration();
                    config.locale = locale;

                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    Toast.makeText(getApplicationContext(), "Spanish", Toast.LENGTH_SHORT).show();
                    language = 2;

                    finish();
                    startActivity(getIntent());
                } else {
                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;

                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    Toast.makeText(getApplicationContext(), "English", Toast.LENGTH_SHORT).show();
                    language = 1;

                    finish();
                    startActivity(getIntent());
                }
                break;
            }
            case R.id.avgResults: {
                int avg = 0;
                int total = 0;
                if (manager.loadInternalFile(this) != null) {
                    String data = manager.loadInternalFile(this);

                    String[] dataArray = data.split(",");

                    if(dataArray[0] != null) {
                        int[] results = new int[dataArray.length];

                        for (int i = 0; i < dataArray.length; i++) {
                            if (dataArray[i] != null) {
                                try {
                                    results[i] = Integer.parseInt(dataArray[i]);
                                } catch (Exception e) {
                                    results[i] = prevAVG;
                                    Log.d("Value NumberException", dataArray[i]);
                                }

                                total = results[i] + total;
                            }

                        }

                        avg = total / results.length;
                        prevAVG = avg;
                        avgBuilder.setMessage("Your average score is: " + avg)
                                .setCancelable(false)
                                .setTitle("Average Result.")
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        AlertDialog alert = avgBuilder.create();
                        alert.show();

                        //Toast.makeText(this, "Your average score is:" + avg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "The quiz has not been completed before. Please finish before asking for an average.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.rstResult: {
                prevAVG = 0;
                File file = null;
                manager.ResetFile(this);
                //Toast.makeText(this, "It is Reseting", Toast.LENGTH_LONG);
                break;
            }
        }

        return true;
    }

    public boolean checkIfTrue(Quiz q, boolean value) {
      boolean correct = false;

        if(q.showAnswer() == value){
            correct = true;
        }

      return correct;

    };

    public Bundle setBundle() {
        Bundle tempBundle = new Bundle();
        return tempBundle;

    }
}
