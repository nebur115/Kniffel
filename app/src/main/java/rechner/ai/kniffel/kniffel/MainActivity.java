package rechner.ai.kniffel.kniffel;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    boolean bonus;
    String Offset;
    Date firstTakenTime;
    TextView Summe;
    int gesammtsumme;
    float[] gamePlan;
    Move currentMove;
    ImageView[] Dices = new ImageView[5];
    ImageView[] buttons = new ImageView[6];
    TextView[] ShownState = new TextView[13];
    TextView[] Labels = new TextView[14];
    TextView Bonus;
    int[] ActiveDiceImages = new int[7];
    int[] UnactiveDiceImages = new int[7];
    int[] InputDices = new int[5];
    int[] DiceValues = new int[6];
    boolean[] activeDice = new boolean[5];
    int status;
    int freieFelder;
    static ArrayList<Integer>[] generallProbabiltiesInt;
    static ArrayList<Float>[] generallProbabiltiesFloat;
    static ArrayList<Integer>[] genereallOptions;
    int nextDice = 0;

    Button MainButton;
    State currentstate;
    int decision;
    boolean gamePlanNotGenerated = false;
    boolean gameAcitve = true;

    ProgressBar HorizontalprogressBar;
    ProgressBar CyclicProgressbar;
    TextView ProgressText;
    Button Wurfeln;
    static DecimalFormat df = new DecimalFormat("0.00");



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Offset = "";

        Dices[0] = findViewById(R.id.dice1);
        Dices[1] = findViewById(R.id.dice2);
        Dices[2] = findViewById(R.id.dice3);
        Dices[3] = findViewById(R.id.dice4);
        Dices[4] = findViewById(R.id.dice5);
        MainButton = findViewById(R.id.button);
        MainButton.setVisibility(View.INVISIBLE);
        Summe = findViewById(R.id.Summe);
        Wurfeln = findViewById(R.id.Wurfeln);
        Wurfeln.setVisibility(View.VISIBLE);
        gesammtsumme = 0;
        ActiveDiceImages[0] = R.drawable.clear;
        ActiveDiceImages[1] = R.drawable.active1;
        ActiveDiceImages[2] = R.drawable.active2;
        ActiveDiceImages[3] = R.drawable.active3;
        ActiveDiceImages[4] = R.drawable.active4;
        ActiveDiceImages[5] = R.drawable.active5;
        ActiveDiceImages[6] = R.drawable.active6;

        bonus = false;


        UnactiveDiceImages[0] = R.drawable.unactiveclear;
        UnactiveDiceImages[1] = R.drawable.unactive1;
        UnactiveDiceImages[2] = R.drawable.unactive2;
        UnactiveDiceImages[2] = R.drawable.unactive3;
        UnactiveDiceImages[4] = R.drawable.unactive4;
        UnactiveDiceImages[5] = R.drawable.unactive5;
        UnactiveDiceImages[6] = R.drawable.unactive6;

        buttons[0] = findViewById(R.id.diceButton1);
        buttons[1] = findViewById(R.id.diceButton2);
        buttons[2] = findViewById(R.id.diceButton3);
        buttons[3] = findViewById(R.id.diceButton4);
        buttons[4] = findViewById(R.id.diceButton5);
        buttons[5] = findViewById(R.id.diceButton6);

        ShownState[0] = findViewById(R.id.one);
        ShownState[1] = findViewById(R.id.two);
        ShownState[2] = findViewById(R.id.three);
        ShownState[3] = findViewById(R.id.four);
        ShownState[4] = findViewById(R.id.five);
        ShownState[5] = findViewById(R.id.six);
        ShownState[6] = findViewById(R.id.pasch3);
        ShownState[7] = findViewById(R.id.pasch4);
        ShownState[8] = findViewById(R.id.fullhouse);
        ShownState[9] = findViewById(R.id.klStr);
        ShownState[10] = findViewById(R.id.grStl);
        ShownState[11] = findViewById(R.id.Kniffel);
        ShownState[12] = findViewById(R.id.chance);

        Labels[0] = findViewById(R.id.Label1);
        Labels[1] = findViewById(R.id.Label2);
        Labels[2] = findViewById(R.id.label3);
        Labels[3] = findViewById(R.id.label4);
        Labels[4] = findViewById(R.id.Label5);
        Labels[5] = findViewById(R.id.Label6);
        Labels[6] = findViewById(R.id.Label7);
        Labels[7] = findViewById(R.id.Label8);
        Labels[8] = findViewById(R.id.Label9);
        Labels[9] = findViewById(R.id.Label10);
        Labels[10] = findViewById(R.id.Label11);
        Labels[11] = findViewById(R.id.Label12);
        Labels[12] = findViewById(R.id.Label13);
        Labels[13] = findViewById(R.id.Label14);


        HorizontalprogressBar = findViewById(R.id.HorizontalProgressBar);
        CyclicProgressbar = findViewById(R.id.progressBar2);
        ProgressText = findViewById(R.id.ProgressText);
        Bonus = findViewById(R.id.bonus);

        HorizontalprogressBar.setVisibility(View.INVISIBLE);
        CyclicProgressbar.setVisibility(View.INVISIBLE);
        ProgressText.setVisibility(View.INVISIBLE);

        GenerategenereallOptions();
        GenerategenerallProbabilities();

        freieFelder = 13;
        Offset = "";
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Kniffel", 0);


        if (!pref.contains("GamePlan")) {



            firstTakenTime = Calendar.getInstance().getTime();


            Wurfeln.setVisibility(View.INVISIBLE);
            MainButton.setVisibility(View.INVISIBLE);

            for (ImageView dice : Dices) {
                dice.setVisibility(View.INVISIBLE);
            }


            for (TextView label : Labels) {
                label.setVisibility(View.INVISIBLE);
            }


            Bonus.setVisibility(View.GONE);

            gamePlanNotGenerated = true;

            gamePlanNotGenerated = false;
            gameAcitve = false;

            HorizontalprogressBar.setVisibility(View.VISIBLE);
            CyclicProgressbar.setVisibility(View.VISIBLE);
            ProgressText.setVisibility(View.VISIBLE);
            HorizontalprogressBar.setProgress(0);


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    gamePlan = new float[868352];
                    status = 0;


                    boolean[] allgefuellt = new boolean[13];

                    for (int i = 0; i < 13; i++) {
                        allgefuellt[i] = true;
                    }


                    for (int i = 0; i < 106; i++) {
                        status++;
                        final State tempState = new State(allgefuellt, i);
                        if (i > 62) {
                            gamePlan[tempState.getNumber()] = 35;

                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                ((ProgressBar) findViewById(R.id.HorizontalProgressBar)).setProgress(status);
                                ((TextView) findViewById(R.id.ProgressText)).setText(df.format(((float) status / 8192) * 100) + " % \n" + status + " / 430080  \n" + 0 + " / 13 \n" + 35);
                            }
                        });

                    }



                    for (int i = 1; i < 14; i++) {

                        final int currentI = i;

                        ExecutorService es = Executors.newFixedThreadPool(10);

                        for (int j = 0; j < 8192; j++) {

                            final boolean[] gefuellt = new boolean[13];

                            String sgefuellt = Integer.toBinaryString(j);
                            for (int k = 0; k < 13; k++) {
                                gefuellt[k] = false;
                            }
                            int countgefuelt = 0;
                            for (int k = 0; k < sgefuellt.length(); k++) {
                                if (sgefuellt.charAt(sgefuellt.length() - k - 1) == '1') {
                                    gefuellt[k] = true;
                                    countgefuelt++;
                                } else {
                                    gefuellt[k] = false;

                                }
                            }

                            if (countgefuelt == 13 - i) {

                                es.execute(new NodeGeneratingRunnable(gefuellt) {
                                    @Override
                                    public void run() {

                                        int maxuppervalue = 0;

                                        for (int i = 0; i < 6; i++) {
                                            if (gefuellt[i]) {
                                                maxuppervalue = maxuppervalue + ((i + 1) * 5);

                                            }
                                        }

                                        for (int k = 0; k < 106; k++) {

                                            if (maxuppervalue < k) {
                                                break;
                                            }

                                            boolean[] tempgefuellt = new boolean[13];

                                            for (int l = 0; l < tempgefuellt.length; l++) {
                                                tempgefuellt[l] = getgefuellt()[l];
                                            }

                                            final State tempState = new State(tempgefuellt, k);

                                            gamePlan[tempState.getNumber()] = new Move(tempState.getState(), getGamePlan()).Value();

                                            status++;

                                            if (status % 5 == 0) {

                                                if (status % 20 == 0){
                                                    Offset = Offset + "\n";
                                                    if (status % 400 == 0) Offset = "";
                                                }

                                                runOnUiThread(new Runnable() {
                                                    public void run() {

                                                        Date currentTime = Calendar.getInstance().getTime();
                                                        long milis2 = currentTime.getTime();
                                                        long milis1 = firstTakenTime.getTime();
                                                        long timeremaining = (milis2 - milis1) / status * (430080 - status);
                                                        long remainingSeconds = timeremaining / 1000 % 60;
                                                        long remainingMinutes = timeremaining / (60 * 1000) % 60;
                                                        long remaingingHours = timeremaining / (60 * 60 * 1000);

                                                        ((ProgressBar) findViewById(R.id.HorizontalProgressBar)).setProgress(status);
                                                        ((TextView) findViewById(R.id.ProgressText)).setText(df.format((float) status / 4300.80) + " % \n" + status + " / 430080  \n" + currentI + " / 13 \n" + remaingingHours + ":" + remainingMinutes + ":" + remainingSeconds + "\n" + Offset);

                                                    }
                                                });

                                            }

                                        }



                                    }
                                });


                            }

                        }


                        es.shutdown();


                        try {
                            es.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }


                    gameAcitve = true;



                    String json;
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("Kniffel", 0); // 0 - for private mode
                    SharedPreferences.Editor editor = pref.edit();
                    Gson gson = new Gson();
                    json = gson.toJson(gamePlan);


                    editor.putString("GamePlan", json);
                    editor.apply();


                    decision = 0;
                    currentstate = new State(0);
                    currentMove = new Move(currentstate, gamePlan);


                    runOnUiThread(new Runnable() {
                        public void run() {
                            HorizontalprogressBar.setVisibility(View.GONE);
                            ProgressText.setVisibility(View.GONE);
                            CyclicProgressbar.setVisibility(View.GONE);
                            Summe.setText(Float.toString(gesammtsumme));
                            Wurfeln.setVisibility(View.VISIBLE);

                            for (int i = 0; i < buttons.length; i++) {
                                buttons[i].setVisibility(View.VISIBLE);
                            }


                            for (int i = 0; i < Dices.length; i++) {
                                Dices[i].setVisibility(View.VISIBLE);
                            }


                            for (int i = 0; i < Labels.length; i++) {
                                Labels[i].setVisibility(View.VISIBLE);
                            }


                        }
                    });


                }
            });



        } else {


            Gson gson = new Gson();
            String json = pref.getString("GamePlan", "");
            gamePlan = gson.fromJson(json, float[].class);


            decision = 0;
            currentstate = new State(0);
            currentMove = new Move(currentstate, gamePlan);
            Summe.setText(Float.toString(gesammtsumme));
            Blink();
        }


        for (int i = 0; i < activeDice.length; i++) {
            activeDice[i] = true;
        }

        Wurfeln.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < activeDice.length; i++) {
                    Random random = new Random();
                    if (activeDice[i]) {
                        InputDices[i] = random.nextInt(6);
                        diceClick(i, true);
                    }
                }

                Wurfeln.setVisibility(View.INVISIBLE);
            }
        });

        Dices[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diceClick(0, true);
            }
        });
        Dices[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diceClick(1, true);
            }
        });
        Dices[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diceClick(2, true);
            }
        });
        Dices[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diceClick(3, true);
            }
        });
        Dices[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diceClick(4, true);
            }
        });


        buttons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick(0);
            }
        });

        buttons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick(1);
            }
        });
        buttons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick(2);
            }
        });
        buttons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick(3);
            }
        });
        buttons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick(4);
            }
        });
        buttons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick(5);
            }
        });


        MainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gameAcitve) {
                    if (freieFelder == 0) {
                        for (int i = 0; i < Dices.length; i++) {
                            Dices[i].setVisibility(View.VISIBLE);

                        }
                        for (int i = 0; i < ShownState.length; i++) {
                            ShownState[i].setText("");
                        }
                        MainButton.setVisibility(View.INVISIBLE);
                        Wurfeln.setVisibility(View.VISIBLE);
                        gesammtsumme = 0;
                        Bonus.setText("");
                        Summe.setText(Float.toString(gesammtsumme));
                        freieFelder = 13;
                    } else {

                        decision++;
                        diceClick(0, false);
                        switch (decision) {
                            case 1:
                                InputDices = convertDiceValue(currentMove.Decission0ne(DiceValues));
                                break;

                            case 2:
                                InputDices = convertDiceValue(currentMove.DecissionTwo(DiceValues));
                                break;

                            case 3:
                                freieFelder--;
                                boolean[] bestDecission = currentMove.FinalDeccissionDifference(DiceValues).getBooleanArray();
                                for (int i = 0; i < bestDecission.length; i++) {
                                    if (bestDecission[i]) {
                                        if (i < 6) {
                                            ShownState[i].setText(Integer.toString(DiceValues[i] * (i + 1)));
                                            gesammtsumme = gesammtsumme + (DiceValues[i] * (i + 1));
                                        } else {
                                            switch (i) {
                                                case 6:
                                                    //Dreier Pash
                                                    for (int die : DiceValues) {
                                                        if (die >= 3) {
                                                            ShownState[i].setText(Integer.toString(alleaugenzählen(DiceValues)));
                                                            gesammtsumme = gesammtsumme + (alleaugenzählen(DiceValues));
                                                            break;
                                                        } else {
                                                            ShownState[i].setText("--");
                                                        }
                                                    }
                                                    break;
                                                case 7:
                                                    //Vierer Pash
                                                    for (int die : DiceValues) {
                                                        if (die >= 4) {
                                                            ShownState[i].setText(Integer.toString(alleaugenzählen(DiceValues)));
                                                            gesammtsumme = gesammtsumme + (alleaugenzählen(DiceValues));
                                                            break;
                                                        } else {
                                                            ShownState[i].setText("--");
                                                        }
                                                    }
                                                    break;
                                                case 8:
                                                    //Fullhouse
                                                    boolean two = false;
                                                    boolean three = false;
                                                    for (int die : DiceValues) {
                                                        if (die == 2) {
                                                            two = true;
                                                        }
                                                        if (die == 3) {
                                                            three = true;
                                                        }

                                                    }
                                                    if (two && three) {
                                                        ShownState[i].setText("25");
                                                        gesammtsumme = gesammtsumme + 25;
                                                    } else {
                                                        ShownState[i].setText("--");
                                                    }
                                                    break;
                                                case 9:
                                                    //kl. Straße
                                                    int counter = 0;
                                                    for (int die : DiceValues) {
                                                        if (die > 0) {
                                                            counter++;
                                                            if (counter > 3) {
                                                                break;
                                                            }
                                                        } else {
                                                            counter = 0;
                                                        }
                                                    }
                                                    if (counter > 3) {
                                                        ShownState[i].setText("30");
                                                        gesammtsumme = gesammtsumme + 30;
                                                    } else {
                                                        ShownState[i].setText("--");
                                                    }
                                                    break;
                                                case 10:
                                                    //gr. Straße
                                                    int counter2 = 0;
                                                    for (int die : DiceValues) {
                                                        if (die > 0) {
                                                            counter2++;
                                                            if (counter2 > 4) {
                                                                break;
                                                            }
                                                        } else {
                                                            counter2 = 0;
                                                        }
                                                    }
                                                    if (counter2 > 4) {
                                                        ShownState[i].setText("40");
                                                        gesammtsumme = gesammtsumme + 40;
                                                    } else {
                                                        ShownState[i].setText("--");
                                                    }
                                                    break;
                                                case 11:
                                                    //Kniffel
                                                    for (int die : DiceValues) {
                                                        if (die == 5) {
                                                            ShownState[i].setText("50");
                                                            gesammtsumme = gesammtsumme + 50;
                                                            break;
                                                        } else {
                                                            ShownState[i].setText("--");
                                                        }
                                                    }
                                                    break;
                                                case 12:
                                                    //Chance
                                                    ShownState[i].setText(Integer.toString(alleaugenzählen(DiceValues)));
                                                    gesammtsumme = gesammtsumme + alleaugenzählen(DiceValues);
                                                    break;
                                            }
                                        }
                                    }

                                }


                                currentstate = new State(currentMove.FinalDeccissionState(DiceValues));
                                if (!bonus && currentstate.getObererValue() > 62) {
                                    Bonus.setText("35");
                                    gesammtsumme = gesammtsumme + 35;
                                    bonus = true;
                                }



                                currentMove = new Move(currentstate, gamePlan);
                                for (int i = 0; i < InputDices.length; i++) {
                                    InputDices[i] = 0;
                                }
                                Summe.setText(Integer.toString(Math.round(gesammtsumme)));

                                MainButton.setVisibility(View.INVISIBLE);
                                Wurfeln.setVisibility(View.VISIBLE);



                                decision = 0;
                                break;

                        }
                        UpdateDice();
                        if (freieFelder == 0) {
                            for (int i = 0; i < Dices.length; i++) {
                                Dices[i].setVisibility(View.INVISIBLE);
                            }
                            MainButton.setVisibility(View.VISIBLE);
                            Wurfeln.setVisibility(View.INVISIBLE);
                            MainButton.setText("Neues Spiel");
                            decision = 0;
                            bonus = false;
                            currentstate = new State(0);
                            currentMove = new Move(currentstate, gamePlan);
                        }
                    }
                }
            }
        });
    }

    public void UpdateDice() {
        boolean noDiceActive = true;
        for (int i = 0; i < Dices.length; i++) {
            if (InputDices[i] > 0) {
                Dices[i].setImageResource(UnactiveDiceImages[InputDices[i]]);
                activeDice[i] = false;
            } else {
                Dices[i].setImageResource(ActiveDiceImages[0]);
                activeDice[i] = true;
                noDiceActive = false;
                MainButton.setVisibility(View.INVISIBLE);
                Wurfeln.setVisibility(View.VISIBLE);
            }
        }
        if (noDiceActive) {
            decision = 2;
            diceClick(0, false);
        } else {
            Blink();
        }

    }


    public void diceClick(int pPosition, boolean pRealClick) {
        if ((activeDice[pPosition] && gameAcitve) || !pRealClick) {
            if (pRealClick) {
                InputDices[pPosition] = (InputDices[pPosition] % 6) + 1;
            }
            Dices[pPosition].setImageResource(ActiveDiceImages[InputDices[pPosition]]);

            boolean allDiceselected = true;
            MainButton.setVisibility(View.VISIBLE);

            for (int i = 0; i < InputDices.length; i++) {
                if (InputDices[i] == 0) {
                    MainButton.setVisibility(View.INVISIBLE);

                    allDiceselected = false;
                    if (decision < 2) {
                        MainButton.setText("wegnehmen");
                    }
                }

            }
            DiceValues = convertInputDices();
            if (decision == 2 && allDiceselected) {
                boolean[] bestDecission = currentMove.FinalDeccissionDifference(DiceValues).getBooleanArray();
                for (int i = 0; i < bestDecission.length; i++) {
                    if (bestDecission[i]) switch (i) {
                        case 0:
                            MainButton.setText("Einsen");
                            break;
                        case 1:
                            MainButton.setText("Zweien");
                            break;
                        case 2:
                            MainButton.setText("Dreien");
                            break;
                        case 3:
                            MainButton.setText("Vieren");
                            break;
                        case 4:
                            MainButton.setText("Fünfen");
                            break;
                        case 5:
                            MainButton.setText("Sechsen");
                            break;
                        case 6:
                            MainButton.setText("3. Pash");
                            break;
                        case 7:
                            MainButton.setText("4. Pash");
                            break;
                        case 8:
                            MainButton.setText("Fullhouse");
                            break;
                        case 9:
                            MainButton.setText("kl. Straße");
                            break;
                        case 10:
                            MainButton.setText("gr. Straße");
                            break;
                        case 11:
                            MainButton.setText("Kniffel");
                            break;
                        case 12:
                            MainButton.setText("Chance");
                            break;

                    }
                }
            }

        }


    }

    public int[] convertDiceValue(int[] pInput) {
        int[] input = pInput;
        int[] r = new int[5];
        for (int i = 0; i < r.length; i++) {
            r[i] = 0;
            for (int j = 0; j < input.length; j++) {
                if (input[j] > 0) {
                    r[i] = j + 1;
                    input[j]--;
                    break;
                }
            }
        }
        return r;
    }

    public int[] convertInputDices() {
        int[] r = new int[6];
        for (int i = 0; i < r.length; i++) {
            r[i] = 0;
            for (int j = 0; j < InputDices.length; j++) {
                if (InputDices[j] == i + 1) {
                    r[i]++;
                }
            }
        }
        return r;
    }


    public float[] getGamePlan() {
        float[] r = new float[868352];
        for (int i = 0; i < gamePlan.length; i++) {
            r[i] = gamePlan[i];
        }
        return r;
    }


    static void GenerategenerallProbabilities() {

        generallProbabiltiesInt = new ArrayList[462];
        generallProbabiltiesFloat = new ArrayList[462];
        int count = 0;


        for (int i = 0; i < generallProbabiltiesInt.length; i++) {
            float test = 0;
            generallProbabiltiesFloat[i] = new ArrayList<Float>();
            generallProbabiltiesInt[i] = new ArrayList<Integer>();

            int[] postDeccissionDice = PostDecissionDice.diceFromNumber(i);
            for (int j = 0; j < 252; j++) {

                int[] preDecissionDice = PreDecissionDice.diceFromNumber(j);
                int[] difference = new int[6];
                int dice = 0;
                boolean possible = true;
                int denominator = 1;
                for (int k = 0; k < difference.length; k++) {
                    difference[k] = preDecissionDice[k] - postDeccissionDice[k];
                    if (difference[k] < 0) {
                        possible = false;

                    } else {
                        denominator = denominator * factorial(difference[k]);
                        dice = dice + difference[k];
                    }
                }
                if (possible) {
                    test = test + (float) (Math.pow(0.166666666666666666, dice) * factorial(dice) / denominator);
                    generallProbabiltiesFloat[i].add((float) (Math.pow(0.166666666666666666, dice) * factorial(dice) / denominator));
                    generallProbabiltiesInt[i].add(j);
                    count++;
                }
            }
            if (test < 1) {
                test = test;
            }
        }

        count = count;
    }


    static void GenerategenereallOptions() {
        genereallOptions = new ArrayList[252];

        for (int i = 0; i < genereallOptions.length; i++) {
            genereallOptions[i] = new ArrayList<Integer>();
            int[] diceGiven = PreDecissionDice.diceFromNumber(i);
            for (int j = 0; j < 462; j++) {
                int[] diceNeeded = PostDecissionDice.diceFromNumber(j);
                boolean possible = true;
                for (int k = 0; k < diceGiven.length; k++) {
                    if (diceNeeded[k] > diceGiven[k]) {
                        possible = false;
                    }
                }
                if (possible) {
                    genereallOptions[i].add(j);
                }
            }
        }


    }


    static int factorial(int n) {
        int val = 1;
        for (int i = 1; i <= n; i++) {
            val *= i;
        }
        return val;
    }


    public void Blink() {


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                nextDice = 0;

                runOnUiThread(new Runnable() {
                    public void run() {
                        for (int i = 0; i < buttons.length; i++) {
                            buttons[i].setVisibility(View.VISIBLE);
                        }
                    }
                });


                while (true) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (InputDices[nextDice] > 0) {
                        nextDice = 0;
                        boolean allDice = true;
                        for (int i = 0; i < InputDices.length; i++) {
                            if (InputDices[i] == 0) {
                                allDice = false;
                                break;
                            }
                            nextDice++;
                        }
                        if (allDice) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    for (int i = 0; i < buttons.length; i++) {
                                        buttons[i].setVisibility(View.GONE);
                                    }
                                }
                            });

                            break;
                        }
                    }


                    Dices[nextDice].setImageResource(R.drawable.unactiveclear);


                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    if (InputDices[nextDice] > 0) {
                        nextDice = 0;
                        boolean allDice = true;
                        for (int i = 0; i < InputDices.length; i++) {
                            if (InputDices[i] == 0) {
                                allDice = false;
                                break;
                            }
                            nextDice++;
                        }
                        if (allDice) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    for (int i = 0; i < buttons.length; i++) {
                                        buttons[i].setVisibility(View.GONE);
                                    }
                                }
                            });
                            break;
                        }
                    }
                    Dices[nextDice].setImageResource(R.drawable.clear);
                }


            }


        });
    }


    public void buttonClick(int button) {
        InputDices[nextDice] = button + 1;
        diceClick(nextDice, false);
    }


    private int alleaugenzählen(int[] Dice) {
        int r = 0;
        for (int i = 0; i < Dice.length; i++) {
            r = r + Dice[i] * (i + 1);
        }
        return r;
    }

}



