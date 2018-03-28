package com.example.jan.groupproject;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.AsyncTask;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;






public class Jugar extends AppCompatActivity {


    //:::::::::::::::: PUBLIC Pre-define Buttons ::::::::::::::::::::
    //ImageButton ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8, ib9;
    ImageButton[] ib;
    ImageView   ivLife3, ivLife2, ivLife1;
    TextView    tvScore;

    //:::::::::::::::: GLOBAL Variables ::::::::::::::::::::
    int randIb;  // 0 - 8 to randomly pick button to be visible
    int randPic;  // 0 - 3 (depending on options) to pick which picture shows up
    int[] BTstate = new int[9];

    //::::::::::::::::::: TimerTaskSynch[] for each button :::::::::::::
    private TimerTask[] timerTaskBt = new TimerTask[9];
    private Timer[] timerBt = new Timer[9];
    private Timer timerAsync;
    private TimerTask timerTaskAsync;
    private Timer timerTime;
    private TimerTask   timerTaskTime;

    // Function to set a timer until button is INVISIBLE
    public void checkBt(int a, int b, final int randIb, final int randPic) {
        // Get access to see lifes
        final ImageView   ivLife1 = (ImageView) findViewById(R.id.ivLife1);

        final Handler handler = new Handler();
        timerBt[randIb] = new Timer();
        timerTaskBt[randIb] = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        // loose life if button = Trump after a seconds
                        if (1 <= randPic && randPic <= 8) {
                            reduceLife();

                            // change score if button = trump and life < 0
                            if (ivLife1.getVisibility() == View.VISIBLE)
                                changeScore(9); // randpic has to be changed cause otherwise +100 will be added
                        }
                        // close button after 3s (any button)
                        ib[randIb].setVisibility(View.INVISIBLE);
                        // cancel Timers
                        timerBt[randIb].cancel();
                        timerTaskBt[randIb].cancel();
                    }
                });
            }
        };
        // initialize schedule for timer a = first timer, b = time for 2nd round and after
        timerBt[randIb].schedule(timerTaskBt[randIb], a, b);
    }

    // Function for reducing life
    public void reduceLife() {
        // Access Lifes
        ImageView   ivLife3 = (ImageView) findViewById(R.id.ivLife3);
        ImageView   ivLife2 = (ImageView) findViewById(R.id.ivLife2);
        ImageView   ivLife1 = (ImageView) findViewById(R.id.ivLife1);


        // Get Life values according if Imageview is VISIBLE
        if (ivLife3.getVisibility() == View.VISIBLE)
            ivLife3.setVisibility(View.INVISIBLE);
        else if (ivLife2.getVisibility() == View.VISIBLE)
            ivLife2.setVisibility(View.INVISIBLE);
        else if (ivLife1.getVisibility() == View.VISIBLE)
            ivLife1.setVisibility(View.INVISIBLE);

    }

    // Function for adding points to score
    public void changeScore(int randPic) {

        final TextView    tvScore = (TextView)  findViewById(R.id.tvScore);
        String scoreString = tvScore.getText().toString();
        int score = Integer.parseInt(scoreString);

        if (1 <= randPic && randPic <= 8) // hit trump
            score+=100;
        else if (9 <= randPic && randPic <= 10) // hit bomb
            score-=100;

        String newScoreString = Integer.toString(score);
        tvScore.setText(newScoreString);
    }

    // Function to call random Button
    public int[] randomBt(int randIb, int randPic) {
        // Check weather button is active
        if (ib[randIb].getVisibility() == View.INVISIBLE) {
            // randPic initializes which Image pops up (Trump = 1-8, Bomb = 2)
            if (1 <= randPic && randPic <= 8) {
                ib[randIb].setImageResource(R.mipmap.trump);
                ib[randIb].setVisibility(View.VISIBLE);
                BTstate[randIb]=randPic;
                checkBt(4000, 900000000, randIb, randPic); // Call Function to set button invisible when not pressed

            }
            else if (9 <= randPic && randPic <= 10) {
                ib[randIb].setImageResource(R.mipmap.bomb);
                ib[randIb].setVisibility(View.VISIBLE);
                BTstate[randIb]=randPic;
                checkBt(4000, 900000000, randIb, randPic); // Call Function to set button invisible when not pressed

            }
        }
        // BTstate[] contains information about the image that popped up
    return BTstate;
    }

    // Initialize a schedule when button pops up (called from every OnClickListener!)


    public int[] openBt(int a, int b) { // a = first round, b = 2nd and after; a == b recommended
        // Access lifes
        final ImageView   ivLife1 = (ImageView) findViewById(R.id.ivLife1);

        // Initialize handler
        final Handler handler = new Handler();
        // Initialize timer
        timerAsync = new Timer();
        timerTaskAsync = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        // stops schedule when no more life (no more buttons pop up!)
                        if (ivLife1.getVisibility() == View.INVISIBLE) {
                            timerAsync.cancel();
                            timerTaskAsync.cancel();
                            endGame();

                            //****Change activity with bundle
                        }
                        // if there are lifes create random position to pop up and random pic
                        else {
                            Random rand = new Random();
                            randIb = rand.nextInt(9); // Random Number betwween 0 and 8
                            randPic = rand.nextInt(10) + 1; // Random Number between 1 and 10 (Trump or Bomb)
                            BTstate = randomBt(randIb, randPic); // Call Function to start Button
                        }
                    }
                });
            }
        };
        timerAsync.schedule(timerTaskAsync, a, b);
        return BTstate;
    }

    public void time() { // a = first round, b = 2nd and after; a == b recommended
        final TextView   tvTime = (TextView) findViewById(R.id.tvTime);

        // Initialize handler
        final Handler handler = new Handler();
        // Initialize timer
        timerTime = new Timer();
        timerTaskTime = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override public void run() {
                        String scoreString = tvTime.getText().toString();
                        int time0 = Integer.parseInt(scoreString);

                        int time1 = time0 + 1;

                        String timeString = Integer.toString(time1);
                        //String.format("%02d:%02d", time1 / 60, time1 % 60);
                        tvTime.setText(timeString);
                    }
                });
            }
        };
        timerTime.schedule(timerTaskTime, 1000, 1000);
    }

    public int level() {
        final TextView   tvTime = (TextView) findViewById(R.id.tvTime);

        String Time = tvTime.getText().toString();
        int t = Integer.parseInt(Time);
        //int delay = 4000 - 50 * t;
        double delay = 1000 * Math.exp(-(0.0001)*Math.pow(t,2)) + 200;
        int delayInt = (int) delay;


        return delayInt;
    }

    public void endGame(){
        final TextView   tvScore = (TextView) findViewById(R.id.tvScore);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {

                Intent intent = new Intent(Jugar.this, FinDeJugar.class);

                Bundle b1 = new Bundle();
                String scoreString = tvScore.getText().toString();

                int scoreInt = Integer.parseInt(scoreString);
                b1.putInt("Score", scoreInt);
                intent.putExtras(b1);

                startActivity(intent);
            }
        }, 1000);
    }




    // Function to close random Button
    public void closeBt(int randIb) {
        ib[randIb].setVisibility(View.INVISIBLE);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugar);

        //============== Find IDs ==================
        ib = new ImageButton[9];
        ib[0] = (ImageButton) findViewById(R.id.ib1);
        ib[1] = (ImageButton) findViewById(R.id.ib2);
        ib[2] = (ImageButton) findViewById(R.id.ib3);
        ib[3] = (ImageButton) findViewById(R.id.ib4);
        ib[4] = (ImageButton) findViewById(R.id.ib5);
        ib[5] = (ImageButton) findViewById(R.id.ib6);
        ib[6] = (ImageButton) findViewById(R.id.ib7);
        ib[7] = (ImageButton) findViewById(R.id.ib8);
        ib[8] = (ImageButton) findViewById(R.id.ib9);
        final ImageView   ivLife3 = (ImageView) findViewById(R.id.ivLife3);
        final ImageView   ivLife2 = (ImageView) findViewById(R.id.ivLife2);
        final ImageView   ivLife1 = (ImageView) findViewById(R.id.ivLife1);
        TextView    tvScore = (TextView)  findViewById(R.id.tvScore);
        TextView    tvTime = (TextView) findViewById(R.id.tvTime);


        // Clock
        time();

        //Initial time delay (a=b recommended)
        openBt(4000,4000);


        // Set OnClickListeners - TODO Forloop
        /*
        for (int i=0; i==8; i++){
            final int j = i;
            ib[j].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeScore(BTstate[j]);
                    closeBt(1);
                    timerBt[j].cancel();
                    timerTaskBt[j].cancel();
                    if (BTstate[j] == 2)
                        reduceLife();
                    if (ivLife1.getVisibility() == View.INVISIBLE) {
                        timerAsync.cancel();
                        timerTaskAsync.cancel();
                    }
                    else {
                        timerAsync.cancel();
                        timerTaskAsync.cancel();
                        openBt(1000, 1000);
                    }
                }
            });

        }
        */

        // Set OnClickListeners
        ib[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScore(BTstate[0]);
                closeBt(0);
                timerBt[0].cancel();
                timerTaskBt[0].cancel();
                timerBt[0].cancel();
                timerTaskBt[0].cancel();
                if (9 <= BTstate[0] && BTstate[0] <= 10)
                    reduceLife();
                if (ivLife1.getVisibility() == View.INVISIBLE) {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    endGame();
                }
                else {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    int delay = level();
                    BTstate = openBt(delay, delay);
                }
            }
        });

        ib[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScore(BTstate[1]);
                closeBt(1);
                timerBt[1].cancel();
                timerTaskBt[1].cancel();
                timerBt[1].cancel();
                timerTaskBt[1].cancel();
                if (9 <= BTstate[1] && BTstate[1] <= 10)
                    reduceLife();
                if (ivLife1.getVisibility() == View.INVISIBLE) {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    endGame();
                }
                else {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    int delay = level();
                    BTstate = openBt(delay, delay);
                }
            }
        });
        ib[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScore(BTstate[2]);
                closeBt(2);
                timerBt[2].cancel();
                timerTaskBt[2].cancel();
                timerBt[2].cancel();
                timerTaskBt[2].cancel();
                if (9 <= BTstate[2] && BTstate[2] <= 10)
                    reduceLife();
                if (ivLife1.getVisibility() == View.INVISIBLE) {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    endGame();
                }
                else {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    int delay = level();
                    BTstate = openBt(delay, delay);
                }
            }
        });
        ib[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScore(BTstate[3]);
                closeBt(3);
                timerBt[3].cancel();
                timerTaskBt[3].cancel();
                timerBt[3].cancel();
                timerTaskBt[3].cancel();
                if (9 <= BTstate[3] && BTstate[3] <= 10)
                    reduceLife();
                if (ivLife1.getVisibility() == View.INVISIBLE) {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    endGame();
                }
                else {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    int delay = level();
                    BTstate = openBt(delay, delay);
                }
            }
        });
        ib[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScore(BTstate[4]);
                closeBt(4);
                timerBt[4].cancel();
                timerTaskBt[4].cancel();
                timerBt[4].cancel();
                timerTaskBt[4].cancel();
                if (9 <= BTstate[4] && BTstate[4] <= 10)
                    reduceLife();
                if (ivLife1.getVisibility() == View.INVISIBLE) {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    endGame();
                }
                else {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    int delay = level();
                    BTstate = openBt(delay, delay);
                }
            }
        });
        ib[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScore(BTstate[5]);
                closeBt(5);
                timerBt[5].cancel();
                timerTaskBt[5].cancel();
                timerBt[5].cancel();
                timerTaskBt[5].cancel();
                if (9 <= BTstate[5] && BTstate[5] <= 10)
                    reduceLife();
                if (ivLife1.getVisibility() == View.INVISIBLE) {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    endGame();
                }
                else {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    int delay = level();
                    BTstate = openBt(delay, delay);
                }
            }
        });
        ib[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScore(BTstate[6]);
                closeBt(6);
                timerBt[6].cancel();
                timerTaskBt[6].cancel();
                timerBt[6].cancel();
                timerTaskBt[6].cancel();
                if (9 <= BTstate[6] && BTstate[6] <= 10)
                    reduceLife();
                if (ivLife1.getVisibility() == View.INVISIBLE) {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    endGame();
                }
                else {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    int delay = level();
                    BTstate = openBt(delay, delay);
                }
            }
        });
        ib[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScore(BTstate[7]);
                closeBt(7);
                timerBt[7].cancel();
                timerTaskBt[7].cancel();
                timerBt[7].cancel();
                timerTaskBt[7].cancel();
                if (9 <= BTstate[7] && BTstate[7] <= 10)
                    reduceLife();
                if (ivLife1.getVisibility() == View.INVISIBLE) {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    endGame();
                }
                else {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    int delay = level();
                    BTstate = openBt(delay, delay);
                }
            }
        });
        ib[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeScore(BTstate[8]);
                closeBt(8);
                timerBt[8].cancel();
                timerTaskBt[8].cancel();
                timerBt[8].cancel();
                timerTaskBt[8].cancel();
                if (9 <= BTstate[8] && BTstate[8] <= 10)
                    reduceLife();
                if (ivLife1.getVisibility() == View.INVISIBLE) {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    endGame();
                }
                else {
                    timerAsync.cancel();
                    timerTaskAsync.cancel();
                    int delay = level();
                    BTstate = openBt(delay, delay);
                }
            }
        });

            }

        }











