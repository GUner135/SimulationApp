package software_project.simulationapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private ImageView ivGreen1, ivGreen2, ivGreen3, ivGreen4, ivGreen5, ivGreen6, ivRed1, ivRed2,
            ivRed3, ivRed4, ivRed5, ivRed6;
    private TextView wLevel, wTemp, hTemp;

    private int V1status = 0, V2status = 0, V3status = 0, V4status = 0,
            V5status = 0, V6status = 0;
    private double watLevel = 0.98, hPerc = 0.00;
    private int watTemp = 21;

    private SimServer sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivGreen1 = (ImageView) findViewById(R.id.ivGreen1);
        ivGreen2 = (ImageView) findViewById(R.id.ivGreen2);
        ivGreen3 = (ImageView) findViewById(R.id.ivGreen3);
        ivGreen4 = (ImageView) findViewById(R.id.ivGreen4);
        ivGreen5 = (ImageView) findViewById(R.id.ivGreen5);
        ivGreen6 = (ImageView) findViewById(R.id.ivGreen6);
        ivRed1 = (ImageView) findViewById(R.id.ivRed1);
        ivRed2 = (ImageView) findViewById(R.id.ivRed2);
        ivRed3 = (ImageView) findViewById(R.id.ivRed3);
        ivRed4 = (ImageView) findViewById(R.id.ivRed4);
        ivRed5 = (ImageView) findViewById(R.id.ivRed5);
        ivRed6 = (ImageView) findViewById(R.id.ivRed6);
        wLevel = (TextView) findViewById(R.id.wLevel);
        wTemp = (TextView) findViewById(R.id.wTemp);
        hTemp = (TextView) findViewById(R.id.hTemp);

        int port = 8080; // 843 flash policy port



        try {
            sim = new SimServer(port);
            System.out.println("Trying");
        } catch ( Exception ex ) {
            System.out.println(ex);
        }

        sim.start();

        TimerTask taskNew = new TimerTask() {
            @Override
            public void run() {


                String str = sim.getString();
                String cmd = "", command = "";

                if(!str.equals("")) {
                    cmd = str;
                    command = cmd.substring(0, 8);
                }

                    switch(command)
                    {
                        case "CONTROL:":
                            String A = cmd.substring(10,11);
                            String B1 = cmd.substring(15,16);
                            String B2 = cmd.substring(17,18);
                            String B3 = cmd.substring(19,20);
                            String B4 = cmd.substring(21,22);
                            String C = cmd.substring(26,30);
                            String F = cmd.substring(33);

                            V1status = Integer.parseInt(A);
                            V2status = Integer.parseInt(B1);
                            V3status = Integer.parseInt(B2);
                            V4status = Integer.parseInt(B3);
                            V5status = Integer.parseInt(B4);
                            V6status = Integer.parseInt(F);
                            hPerc = Double.parseDouble(C);

                            break;
                        case "MALFUNCT":
                            //MALFUNCTION:BURNER=0,LEAK=0,NO_INCOMING_PRODUCT=1,NO_OUTFLOW=1
                            String M1 = cmd.substring(19,20);
                            String M2 = cmd.substring(26,27);
                            String M3 = cmd.substring(48,49);
                            String M4 = cmd.substring(61,62);

                            break;

                    }
                //taken from http://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (V1status == 1) {
                            ivGreen1.setVisibility(View.VISIBLE);
                            ivRed1.setVisibility(View.INVISIBLE);
                            watLevel += .05;
                        } else {
                            ivGreen1.setVisibility(View.INVISIBLE);
                            ivRed1.setVisibility(View.VISIBLE);
                        }

                        //Valve B1
                        if (V2status == 1) {
                            ivGreen2.setVisibility(View.VISIBLE);
                            ivRed2.setVisibility(View.INVISIBLE);
                            watLevel -= .005;
                        } else {
                            ivGreen2.setVisibility(View.INVISIBLE);
                            ivRed2.setVisibility(View.VISIBLE);
                        }

                        //Valve B2
                        if (V3status == 1) {
                            ivGreen3.setVisibility(View.VISIBLE);
                            ivRed3.setVisibility(View.INVISIBLE);
                            watLevel -= .005;
                        } else {
                            ivGreen3.setVisibility(View.INVISIBLE);
                            ivRed3.setVisibility(View.VISIBLE);
                        }

                        //Valve B3
                        if (V4status == 1) {
                            ivGreen4.setVisibility(View.VISIBLE);
                            ivRed4.setVisibility(View.INVISIBLE);
                            watLevel -= .005;
                        } else {
                            ivGreen4.setVisibility(View.INVISIBLE);
                            ivRed4.setVisibility(View.VISIBLE);
                        }

                        //Valve B4
                        if (V5status == 1) {
                            ivGreen5.setVisibility(View.VISIBLE);
                            ivRed5.setVisibility(View.INVISIBLE);
                            watLevel -= .005;
                        } else {
                            ivGreen5.setVisibility(View.INVISIBLE);
                            ivRed5.setVisibility(View.VISIBLE);
                        }

                        //Valve F
                        if (V6status == 1) {
                            ivGreen6.setVisibility(View.VISIBLE);
                            ivRed6.setVisibility(View.INVISIBLE);
                            if (watLevel <= 0)
                                watLevel = 0;
                            else
                                watLevel -= .2;
                        } else {
                            ivGreen6.setVisibility(View.INVISIBLE);
                            ivRed6.setVisibility(View.VISIBLE);
                        }

                        //
                        if (watLevel <= .05 && V6status == 0) {
                            ivRed1.setVisibility(View.INVISIBLE);
                            ivGreen1.setVisibility(View.VISIBLE);
                            V1status = 1;
                        }

                        if (watLevel >= 0.98) {
                            ivGreen1.setVisibility(View.INVISIBLE);
                            ivRed1.setVisibility(View.VISIBLE);
                            V1status = 0;
                            watLevel = 0.98;
                        }

                        //add temperature logic

                        //add logic for each exception


                        double wLevelPerc = watLevel * 100;

                        wLevel.setText(wLevelPerc + "%");
                        wTemp.setText(watTemp + " C");
                        hTemp.setText(hPerc + "%");

                    }
                });



                    //String str = String.format("STATUS:A=%d,B=[%d,%d,%d,%d],C=",);

            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(taskNew,500,1000);
    }
}
