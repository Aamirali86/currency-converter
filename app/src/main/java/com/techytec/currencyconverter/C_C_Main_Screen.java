package com.techytec.currencyconverter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.exchange.currency.converter.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.techytec.currencyconverter.data.C_C_Data;
import com.techytec.currencyconverter.data.Currency_Data;
import com.techytec.currencyconverter.data.Image_BitmapManager;

public class C_C_Main_Screen extends Activity implements AnimationListener {

    private ConnectivityManager connMgr_CC_main;
    private String responString, str_list, str_res;
    private WebserviceCallRegistrationTask_C_C_Main task_C_C_Main;
    private ArrayList<Currency_Data> cArrayList;
    private SharedPreferences preferences;
    private Editor editor;
    private String str_gson_parse_cArrayList;
    private TextView txt_source, txt_dest, txt_update_time;
    private ImageButton imgbtn_swap, imgbtn_refresh;
    private Animation animMove_left_right, animMove_right_left, animMove_rotate;
    private Animation animmove_left_right_apha, animmove_right_left_alpha;
    private LinearLayout ll_source, ll_dest;
    private LinearLayout llsettings, llupdate_data, llinfo;
    private ImageView img_graph, img_source, img_dest;
    private EditText etd_source;
    private EditText etd_dest;
    public static int source_destination;

    private String str_src, str_dest;
    private String lastVal = "1";
    private double total_amount;
    private TextWatcher txtwt_source, txtwt_desc;

    private TextView txt_d1, txt_d5, txt_m3, txt_y1, txt_y2, txt_y5;
    private String str_Currency_full_name, str_Name, str_Price, str_Symbol;
    private String str_Ts, str_Type, str_Utctime, str_Volume;
    private String dst_Currency_full_name, dst_Name, dst_Price, dst_Symbol;
    private String dst_Ts, dst_Type, dst_Utctime, dst_Volume;
    private boolean swap_flag;
    private boolean refresh_flag = false;
    public static boolean resume_flag = false;
    private int icon_drawable, dst_icon;
    public int currency_icon;
    private String time_duration = "3m";
    private String convert_from, convert_to;
    private TextView txt_No_Internet_Connection;
    private Button btn_retry;
    private int number_of_decimal;
    private Handler handler;
    public static Timer timer;
    public TimerTask timerTask;
    private int update_frequency, clock_12;
    private long frequency;
    private int devicesize_flag;
    protected int wLAN_Mode;
    private int offline_Mode;
    private int automatic_update;

    AdRequest adRequest;
    InterstitialAd interstitial;


    private Activity activity;
    /**
     * The log tag.
     */
    private static final String LOG_TAG = "InterstitialSample";
    private int usd_icon_id, pkr_icon_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        usd_icon_id = R.drawable.usd;
        pkr_icon_id = R.drawable.pkr;

        activity = this;
        connMgr_CC_main = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        devicesize_flag = preferences.getInt("devicesize_flag", 2);

        if (devicesize_flag >= 3) {
            setContentView(R.layout.c_c_main_activity_800_1200);
        } else {
            setContentView(R.layout.c_c_main_activity);
        }

        AdView adView = (AdView) this.findViewById(R.id.adView);
        adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("7AF8C338E6A4EA23E303067B6C1016ED")
                .build();
        adView.loadAd(adRequest);

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-5011464080435742/5557954116");
        interstitial.loadAd(adRequest);

        handler = new Handler();

        if (C_C_Data.infocounter <= 1) {

            if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {
                task_C_C_Main = new WebserviceCallRegistrationTask_C_C_Main();
                task_C_C_Main.execute(new String[]{"Webservice Calling for C_C_Main"});
            }

        }
        etd_source = (EditText) findViewById(R.id.etd_source);
        etd_dest = (EditText) findViewById(R.id.etd_dest);


        ll_source = (LinearLayout) findViewById(R.id.ll_source);
        ll_dest = (LinearLayout) findViewById(R.id.ll_dest);
        llsettings = (LinearLayout) findViewById(R.id.llsettings);
        llupdate_data = (LinearLayout) findViewById(R.id.llupdate_data);
        llinfo = (LinearLayout) findViewById(R.id.llinfo);

        imgbtn_swap = (ImageButton) findViewById(R.id.imgbtn_swap);
        imgbtn_refresh = (ImageButton) findViewById(R.id.imgbtn_refresh);

        img_graph = (ImageView) findViewById(R.id.img_graph);
        img_source = (ImageView) findViewById(R.id.img_source);
        img_dest = (ImageView) findViewById(R.id.img_dest);

        txt_update_time = (TextView) findViewById(R.id.txt_update_time);
        txt_source = (TextView) findViewById(R.id.txt_source);
        txt_dest = (TextView) findViewById(R.id.txt_dest);
        txt_d1 = (TextView) findViewById(R.id.txt_d1);
        txt_d5 = (TextView) findViewById(R.id.txt_d5);
        txt_m3 = (TextView) findViewById(R.id.txt_m3);
        txt_y1 = (TextView) findViewById(R.id.txt_y1);
        txt_y2 = (TextView) findViewById(R.id.txt_y2);
        txt_y5 = (TextView) findViewById(R.id.txt_y5);
        txt_No_Internet_Connection = (TextView) findViewById(R.id.txt_No_Internet_Connection);
        btn_retry = (Button) findViewById(R.id.btn_retry);

        if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {
            convert_from = txt_source.getText().toString().toLowerCase();
            convert_to = txt_dest.getText().toString().toLowerCase();
            //Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/z?s=usdinr%3dX&t=3m&q=l&l=on&z=l&a=v&p=s", img_graph, 400,400);

            Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
            txt_No_Internet_Connection.setVisibility(TextView.GONE);
            btn_retry.setVisibility(Button.GONE);
        } else {
            btn_retry.setVisibility(Button.VISIBLE);
            img_graph.setImageResource(R.drawable.no_chart);
            txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);

        }


        llupdate_data.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                wLAN_Mode = preferences.getInt("WLAN_Mode", 0);

                interstitial.show();

                NetworkInfo mWifi = connMgr_CC_main.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (wLAN_Mode == 1) {
                    if (mWifi.isConnected()) {


                        task_C_C_Main = new WebserviceCallRegistrationTask_C_C_Main();
                        task_C_C_Main.execute(new String[]{"Webservice Calling for C_C_Main"});

                    } else {

                    }
                } else {

                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        task_C_C_Main = new WebserviceCallRegistrationTask_C_C_Main();
                        task_C_C_Main.execute(new String[]{"Webservice Calling for C_C_Main"});
                    } else {

                    }

                }

            }
        });
        llinfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent mainIntent = new Intent(C_C_Main_Screen.this, C_C_AboutUS.class);
                C_C_Main_Screen.this.startActivity(mainIntent);
            }
        });
        llsettings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent mainIntent = new Intent(C_C_Main_Screen.this, C_C_Setting.class);
                C_C_Main_Screen.this.startActivity(mainIntent);
            }
        });
        btn_retry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                time_duration = "3m";

                txt_d1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_d5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_m3.setTextColor(activity.getResources().getColor(R.color.bg_setting_color));
                txt_y1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y2.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));

                if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {
                    convert_from = txt_source.getText().toString().toLowerCase();
                    convert_to = txt_dest.getText().toString().toLowerCase();
                    //Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/z?s=usdinr%3dX&t=3m&q=l&l=on&z=l&a=v&p=s", img_graph, 400,400);

                    Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
                    txt_No_Internet_Connection.setText(getString(R.string.No_Internet_Connection));
                    txt_No_Internet_Connection.setVisibility(TextView.GONE);
                    btn_retry.setVisibility(Button.GONE);
                } else {
                    img_graph.setImageResource(R.drawable.no_chart);
                    btn_retry.setVisibility(Button.VISIBLE);
                    txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                }
            }
        });


        txt_d1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //				time_duration="1d";

                convert_from = txt_source.getText().toString().toLowerCase();
                convert_to = txt_dest.getText().toString().toLowerCase();


                txt_d1.setTextColor(activity.getResources().getColor(R.color.bg_setting_color));
                txt_d5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_m3.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y2.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));

                //	http://chart.finance.yahoo.com/b?s=USDINR%3dX
                if (swap_flag) {

                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {
                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/b?s=" + convert_to + convert_from + "%3dX", img_graph, 400, 400);

                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                } else {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {
                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/b?s=" + convert_from + convert_to + "%3dX", img_graph, 400, 400);

                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                }

            }
        });
        txt_d5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                //				time_duration="5d";

                txt_d1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_d5.setTextColor(activity.getResources().getColor(R.color.bg_setting_color));
                txt_m3.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y2.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));


                convert_from = txt_source.getText().toString().toLowerCase();
                convert_to = txt_dest.getText().toString().toLowerCase();
                //http://chart.finance.yahoo.com/w?s=USDCHF%3dX
                if (swap_flag) {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/w?s=" + convert_to + convert_from + "%3dX", img_graph, 400, 400);
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                } else {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/w?s=" + convert_from + convert_to + "%3dX", img_graph, 400, 400);
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                }
            }
        });
        txt_m3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                time_duration = "3m";

                txt_d1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_d5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_m3.setTextColor(activity.getResources().getColor(R.color.bg_setting_color));
                txt_y1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y2.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));


                convert_from = txt_source.getText().toString().toLowerCase();
                convert_to = txt_dest.getText().toString().toLowerCase();

                if (swap_flag) {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_to + convert_from + "=x", img_graph, 400, 400);
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                } else {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                }
            }
        });

        txt_y1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                time_duration = "1y";

                txt_d1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_d5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_m3.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y1.setTextColor(activity.getResources().getColor(R.color.bg_setting_color));
                txt_y2.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));

                convert_from = txt_source.getText().toString().toLowerCase();
                convert_to = txt_dest.getText().toString().toLowerCase();

                if (swap_flag) {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_to + convert_from + "=x", img_graph, 400, 400);
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                } else {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                }
            }
        });
        txt_y2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                time_duration = "2y";

                convert_from = txt_source.getText().toString().toLowerCase();
                convert_to = txt_dest.getText().toString().toLowerCase();

                txt_d1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_d5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_m3.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y2.setTextColor(activity.getResources().getColor(R.color.bg_setting_color));
                txt_y5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));

                if (swap_flag) {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_to + convert_from + "=x", img_graph, 400, 400);
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                } else {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                }
            }
        });
        txt_y5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                time_duration = "5y";

                txt_d1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_d5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_m3.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y2.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y5.setTextColor(activity.getResources().getColor(R.color.bg_setting_color));

                convert_from = txt_source.getText().toString().toLowerCase();
                convert_to = txt_dest.getText().toString().toLowerCase();

                if (swap_flag) {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_to + convert_from + "=x", img_graph, 400, 400);
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                } else {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                }
            }
        });


        // load the animMove_left_right
        animMove_left_right = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move_left_right);
        animMove_left_right.setAnimationListener(this);
        // load the animMove_right_left
        animMove_right_left = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move_right_left);
        animMove_right_left.setAnimationListener(this);
        // load the move_left_right_apha
        animmove_left_right_apha = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move_left_right_apha);
        animmove_left_right_apha.setAnimationListener(this);
        // load the move_right_left_alpha
        animmove_right_left_alpha = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.move_right_left_alpha);
        animmove_right_left_alpha.setAnimationListener(this);
        // load the animMove_rotate
        animMove_rotate = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);
        animMove_rotate.setAnimationListener(this);

        ll_source.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String str_dest_code = txt_source.getText().toString();

                if (swap_flag) {
                    C_C_Main_Screen.source_destination = 2;
                } else {
                    C_C_Main_Screen.source_destination = 1;
                }

                resume_flag = true;


                Intent mainIntent = new Intent(C_C_Main_Screen.this, C_C_List.class);
                C_C_Main_Screen.this.startActivity(mainIntent);
            }
        });

        ll_dest.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                String str_dest_code = txt_dest.getText().toString();
                if (swap_flag) {
                    C_C_Main_Screen.source_destination = 1;
                } else {
                    C_C_Main_Screen.source_destination = 2;
                }

                resume_flag = true;


                Intent mainIntent = new Intent(C_C_Main_Screen.this, C_C_List.class);
                C_C_Main_Screen.this.startActivity(mainIntent);
            }
        });

        imgbtn_swap.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                if (swap_flag) {
                    ll_source.startAnimation(animmove_left_right_apha);
                    ll_dest.startAnimation(animmove_right_left_alpha);
                    swap_flag = false;

                    convert_from = txt_source.getText().toString().toLowerCase();
                    convert_to = txt_dest.getText().toString().toLowerCase();
                    //Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/z?s=usdinr%3dX&t=3m&q=l&l=on&z=l&a=v&p=s", img_graph, 400,400);

                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {
                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                } else {

                    if (devicesize_flag >= 3) {
                        // load the animMove_left_right
                        animMove_left_right = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.move_left_right_3);
                        animMove_left_right.setAnimationListener(C_C_Main_Screen.this);
                        // load the animMove_right_left
                        animMove_right_left = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.move_right_left_3);
                        animMove_right_left.setAnimationListener(C_C_Main_Screen.this);
                        ll_source.startAnimation(animMove_left_right);
                        ll_dest.startAnimation(animMove_right_left);
                    } else {
                        ll_source.startAnimation(animMove_left_right);
                        ll_dest.startAnimation(animMove_right_left);
                    }

                    swap_flag = true;

                    convert_from = txt_source.getText().toString().toLowerCase();
                    convert_to = txt_dest.getText().toString().toLowerCase();
                    //Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/z?s=usdinr%3dX&t=3m&q=l&l=on&z=l&a=v&p=s", img_graph, 400,400);
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {
                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_to + convert_from + "=x", img_graph, 400, 400);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                }


                imgbtn_swap.startAnimation(animMove_rotate);


                str_src = etd_source.getText().toString();
                str_dest = etd_dest.getText().toString();
                etd_source.setText(str_dest);
                etd_dest.setText(str_src);


            }
        });

        imgbtn_refresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                refresh_flag = true;


                if (txt_source.getText().toString().equalsIgnoreCase("USD")) {
                    String str_calculate_rate = calculate_rate_src("1", txt_source.getText().toString(), txt_dest.getText().toString());
                    double refresh_total_amount = Double.parseDouble(str_calculate_rate);
                    if (swap_flag) {
                        etd_dest.setText("1");
                        etd_source.setText("" + refresh_total_amount);
                        int pos = etd_dest.getText().length();
                        etd_dest.setSelection(pos);
                    } else {
                        etd_source.setText("1");
                        etd_dest.setText("" + refresh_total_amount);
                        int pos = etd_source.getText().length();
                        etd_source.setSelection(pos);
                    }

                } else if (txt_dest.getText().toString().equalsIgnoreCase("USD")) {
                    String str_calculate_rate = calculate_rate_dest("1", txt_source.getText().toString(), txt_dest.getText().toString());
                    double refresh_total_amount = Double.parseDouble(str_calculate_rate);

                    if (swap_flag) {
                        etd_source.setText("1");
                        etd_dest.setText("" + refresh_total_amount);
                        int pos = etd_source.getText().length();
                        etd_source.setSelection(pos);
                    } else {

                        etd_dest.setText("1");
                        etd_source.setText("" + refresh_total_amount);
                        int pos = etd_dest.getText().length();
                        etd_dest.setSelection(pos);
                    }

                } else {
                    String str_calculate_rate = calculate_rate_src("1", txt_source.getText().toString(), txt_dest.getText().toString());
                    double refresh_total_amount = Double.parseDouble(str_calculate_rate);

                    if (swap_flag) {
                        etd_dest.setText("1");
                        etd_source.setText("" + refresh_total_amount);
                        int pos = etd_dest.getText().length();
                        etd_dest.setSelection(pos);
                    } else {
                        etd_source.setText("1");
                        etd_dest.setText("" + refresh_total_amount);
                        int pos = etd_source.getText().length();
                        etd_source.setSelection(pos);
                    }

                }
            }
        });

        cArrayList = new ArrayList<Currency_Data>();

        str_gson_parse_cArrayList = preferences.getString("str_gson_parse_cArrayList", "");

        if (str_gson_parse_cArrayList.equalsIgnoreCase("")) {

        } else {
            Gson strgson = new Gson();
            cArrayList = strgson.fromJson(str_gson_parse_cArrayList, new TypeToken<List<Currency_Data>>() {
            }.getType());
        }
        for (int i = 0; i < cArrayList.size(); i++) {

            Log.e("cArrayList getSymbol", cArrayList.get(i).getSymbol() + "");
            Log.e("cArrayList getPrice", cArrayList.get(i).getPrice() + "");

        }

        txtwt_desc = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                Log.i("REACHES AFTER", "YES");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                Log.i("REACHES BEFORE", "YES");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

                if (refresh_flag) {
                    refresh_flag = false;
                } else {
                    if (s.length() != 0) {

                        String str_dest = s.toString();
                        int count_dot = countOccurrences(s.toString(), '.');

                        if (count_dot <= 1) {
                            if (swap_flag) {
                                String str_calculate_rate = calculate_rate_src(s.toString(), txt_source.getText().toString(), txt_dest.getText().toString());
                                total_amount = Double.parseDouble(str_calculate_rate);
                            } else {
                                String str_calculate_rate = calculate_rate_dest(s.toString(), txt_source.getText().toString(), txt_dest.getText().toString());
                                total_amount = Double.parseDouble(str_calculate_rate);
                            }
                        } else {
                            str_dest = str_dest.substring(0, str_dest.length() - 1);
                            etd_dest.setText("" + str_dest);
                            int pos = etd_dest.getText().length();
                            etd_dest.setSelection(pos);

                            if (swap_flag) {
                                String str_calculate_rate = calculate_rate_src(str_dest, txt_source.getText().toString(), txt_dest.getText().toString());
                                total_amount = Double.parseDouble(str_calculate_rate);
                            } else {
                                String str_calculate_rate = calculate_rate_dest(str_dest, txt_source.getText().toString(), txt_dest.getText().toString());
                                total_amount = Double.parseDouble(str_calculate_rate);
                            }
                        }


                        //						Log.e("total_amount", total_amount+"");
                        //						total_amount=total_amount/10;

                    } else {

                        String str_calculate_rate = calculate_rate_dest("1", txt_source.getText().toString(), txt_dest.getText().toString());
                        total_amount = Double.parseDouble(str_calculate_rate);
                    }

                    etd_dest.removeTextChangedListener(txtwt_desc);//after this line you do the editing code
                    if (s.length() != 0) {

                        if (txt_source.getText().toString().equalsIgnoreCase("USD")) {
                            if (swap_flag) {
                                etd_source.setText("" + total_amount);
                                int pos = etd_dest.getText().length();
                                etd_dest.setSelection(pos);
                            } else {
                                etd_source.setText("" + total_amount);
                                int pos = etd_dest.getText().length();
                                etd_dest.setSelection(pos);
                            }
                        } else if (txt_dest.getText().toString().equalsIgnoreCase("USD")) {
                            if (swap_flag) {

                                etd_source.setText("" + total_amount);
                                int pos = etd_dest.getText().length();
                                etd_dest.setSelection(pos);
                            } else {

                                etd_source.setText("" + total_amount);
                                int pos = etd_dest.getText().length();
                                etd_dest.setSelection(pos);
                            }
                        } else {
                            etd_source.setText("" + total_amount);
                            int pos = etd_dest.getText().length();
                            etd_dest.setSelection(pos);
                        }

                    } else {
                        etd_source.setText("0");
                        int pos = etd_dest.getText().length();
                        etd_dest.setSelection(pos);

                    }
                    etd_dest.addTextChangedListener(txtwt_desc); // you register again for listener callbacks


                }


            }
        };

        etd_dest.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                if (!hasFocus) {
                    // Update your textview depending on which edittext lost focus
                } else {
                    etd_source.removeTextChangedListener(txtwt_source);
                    etd_dest.addTextChangedListener(txtwt_desc);
                }


            }
        });


        txtwt_source = new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                Log.i("REACHES AFTER", "YES");

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                Log.i("REACHES BEFORE", "YES");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {


                if (refresh_flag) {
                    refresh_flag = false;
                } else {
                    if (s.length() != 0) {

                        String str_src = s.toString();
                        int count_dot = countOccurrences(s.toString(), '.');

                        if (count_dot <= 1) {
                            if (swap_flag) {
                                String str_calculate_rate = calculate_rate_dest(s.toString(), txt_source.getText().toString(), txt_dest.getText().toString());
                                total_amount = Double.parseDouble(str_calculate_rate);
                            } else {
                                String str_calculate_rate = calculate_rate_src(s.toString(), txt_source.getText().toString(), txt_dest.getText().toString());
                                total_amount = Double.parseDouble(str_calculate_rate);
                            }
                        } else {
                            str_src = str_src.substring(0, str_src.length() - 1);
                            etd_source.setText("" + str_src);
                            int pos = etd_source.getText().length();
                            etd_source.setSelection(pos);
                            if (swap_flag) {
                                String str_calculate_rate = calculate_rate_dest(str_src, txt_source.getText().toString(), txt_dest.getText().toString());
                                total_amount = Double.parseDouble(str_calculate_rate);
                            } else {
                                String str_calculate_rate = calculate_rate_src(str_src, txt_source.getText().toString(), txt_dest.getText().toString());
                                total_amount = Double.parseDouble(str_calculate_rate);
                            }
                        }


                        //					Log.e("total_amount", total_amount+"");
                        //					total_amount=total_amount/10;

                    } else {

                        String str_calculate_rate = calculate_rate_src("1", txt_source.getText().toString(), txt_dest.getText().toString());
                        total_amount = Double.parseDouble(str_calculate_rate);
                    }

                    etd_source.removeTextChangedListener(txtwt_source);//after this line you do the editing code
                    if (s.length() != 0) {

                        if (txt_source.getText().toString().equalsIgnoreCase("USD")) {
                            if (swap_flag) {
                                etd_dest.setText("" + total_amount);
                                int pos = etd_source.getText().length();
                                etd_source.setSelection(pos);
                            } else {
                                etd_dest.setText("" + total_amount);
                                int pos = etd_source.getText().length();
                                etd_source.setSelection(pos);
                            }
                        } else if (txt_dest.getText().toString().equalsIgnoreCase("USD")) {
                            if (swap_flag) {

                                etd_dest.setText("" + total_amount);
                                int pos = etd_source.getText().length();
                                etd_source.setSelection(pos);
                            } else {

                                etd_dest.setText("" + total_amount);
                                int pos = etd_source.getText().length();
                                etd_source.setSelection(pos);
                            }
                        } else {
                            etd_dest.setText("" + total_amount);
                            int pos = etd_source.getText().length();
                            etd_source.setSelection(pos);
                        }

                    } else {
                        etd_dest.setText("0");

                        int pos = etd_source.getText().length();
                        etd_source.setSelection(pos);

                    }
                    etd_source.addTextChangedListener(txtwt_source); // you register again for listener callbacks

                }


            }
        };

        etd_source.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                if (!hasFocus) {
                    // Update your textview depending on which edittext lost focus
                } else {
                    etd_dest.removeTextChangedListener(txtwt_desc);
                    etd_source.addTextChangedListener(txtwt_source);
                }

            }
        });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


        if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

            Log.e("Constant_Data", C_C_Data.infocounter + "");

        }

        update_frequency = preferences.getInt("Update_Frequency", 2);
        offline_Mode = preferences.getInt("Offline_Mode", 0);
        automatic_update = preferences.getInt("Automatic_update", 1);
        clock_12 = preferences.getInt("12_clock", 1);
        wLAN_Mode = preferences.getInt("WLAN_Mode", 0);

        NetworkInfo mWifi = connMgr_CC_main.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (clock_12 == 0) {
            if (C_C_Data.update_time != null) {
                txt_update_time.setText(getString(R.string.Last_Update) + ": " + C_C_Data.update_time);
            }
        } else if (clock_12 == 1) {
            if (C_C_Data.update_time != null) {
                txt_update_time.setText(getString(R.string.Last_Update) + ": " + C_C_Data.update_time);
            }
        }

        if (timer != null) {

            timer.cancel();
            timer = null;
        }


        timerTask = new TimerTask() {
            int i = 0;

            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {


                        if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {

                            task_C_C_Main = new WebserviceCallRegistrationTask_C_C_Main();
                            task_C_C_Main.execute(new String[]{"Webservice Calling for C_C_Main"});
                        }

                    }
                });
            }
        };
        timer = new Timer();


        if (offline_Mode == 0) {
            if (automatic_update == 1) {

                if (update_frequency == 0) {
                    frequency = 1000 * 30;
                    timer.schedule(timerTask, frequency, frequency);
                } else if (update_frequency == 1) {
                    frequency = 1000 * 60 * 1;
                    timer.schedule(timerTask, frequency, frequency);
                } else if (update_frequency == 2) {
                    frequency = 1000 * 60 * 2;
                    timer.schedule(timerTask, frequency, frequency);
                } else if (update_frequency == 3) {
                    frequency = 1000 * 60 * 5;
                    timer.schedule(timerTask, frequency, frequency);
                } else if (update_frequency == 4) {
                    frequency = 1000 * 60 * 10;
                    timer.schedule(timerTask, frequency, frequency);
                } else if (update_frequency == 5) {
                    frequency = 1000 * 60 * 20;
                    timer.schedule(timerTask, frequency, frequency);
                } else if (update_frequency == 6) {
                    frequency = 1000 * 60 * 30;
                    timer.schedule(timerTask, frequency, frequency);
                } else if (update_frequency == 7) {
                    frequency = 1000 * 60 * 60;
                    timer.schedule(timerTask, frequency, frequency);
                } else {

                }

            }

        }

        if (!resume_flag) {
            if (cArrayList.size() > 0) {

                String str_calculate_rate;
                int source_flag = preferences.getInt("source", 0);
                int destination_flag = preferences.getInt("destination", 0);
                Log.e("source_flag", source_flag + "");
                Log.e("destination_flag", destination_flag + "");
                if (source_flag == 0 && destination_flag == 0) {

                    if (swap_flag) {
                        txt_source.setText("USD");
                        img_source.setImageResource(R.drawable.usd_circle);
                        txt_dest.setText("PKR");
                        img_dest.setImageResource(R.drawable.pkr);

                        //makeMaskImage(48,48, img_source, usd_icon_id, R.drawable.mask_black_48, R.drawable.mask_48_);
                        //makeMaskImage(48,48, img_dest, ind_icon_id, R.drawable.mask_black_48, R.drawable.mask_48_);

                        str_calculate_rate = calculate_rate_dest("1", txt_source.getText().toString(), txt_dest.getText().toString());
                        total_amount = Double.parseDouble(str_calculate_rate);
                        etd_source.setText("" + total_amount);
                        etd_dest.setText("1");

                    } else {

                        str_calculate_rate = calculate_rate_src("1", txt_source.getText().toString(), txt_dest.getText().toString());
                        total_amount = Double.parseDouble(str_calculate_rate);
                        etd_dest.setText("" + total_amount);
                        etd_source.setText("1");
                    }


                }


                if (source_flag == 1) {

                    str_Currency_full_name = preferences.getString("src_Currency_full_name", "");
                    str_Name = preferences.getString("src_Name", "");
                    str_Price = preferences.getString("src_Price", "");
                    str_Symbol = preferences.getString("src_Symbol", "");
                    Log.e("str_Symbol", str_Symbol);
                    str_Ts = preferences.getString("src_Ts", "");
                    str_Type = preferences.getString("src_Type", "");
                    str_Utctime = preferences.getString("src_Utctime", "");
                    str_Volume = preferences.getString("src_Volume", "");
                    icon_drawable = preferences.getInt("src_icon", R.drawable.no_image);


                    //				if(swap_flag)
                    //				{
                    //					Log.e("str_Symbol swap_flag", swap_flag+"");
                    //					txt_dest.setText(str_Symbol);
                    //					//img_dest.setImageResource(icon_drawable);
                    //					makeMaskImage(48,48, img_dest, icon_drawable, R.drawable.mask_black_48, R.drawable.mask_48_);
                    //
                    //
                    //				}
                    //				else
                    //				{
                    Log.e("str_Symbol swap_flag", swap_flag + "");
                    txt_source.setText(str_Symbol);
                    //img_source.setImageResource(icon_drawable);

                    makeMaskImage(48, 48, img_source, icon_drawable, R.drawable.mask_black_48, R.drawable.mask_48_);
                    //	}


                }
                if (destination_flag == 1) {
                    dst_Currency_full_name = preferences.getString("dst_Currency_full_name", "");
                    dst_Name = preferences.getString("dst_Name", "");
                    dst_Price = preferences.getString("dst_Price", "");
                    dst_Symbol = preferences.getString("dst_Symbol", "");
                    Log.e("dst_Symbol", dst_Symbol);
                    dst_Ts = preferences.getString("dst_Ts", "");
                    dst_Type = preferences.getString("dst_Type", "");
                    dst_Utctime = preferences.getString("dst_Utctime", "");
                    dst_Volume = preferences.getString("dst_Volume", "");
                    dst_icon = preferences.getInt("dst_icon", R.drawable.no_image);


                    //				if(swap_flag)
                    //				{
                    //					Log.e("dst_Symbol swap_flag", swap_flag+"");
                    //					txt_source.setText(dst_Symbol);
                    //					//img_source.setImageResource(dst_icon);
                    //
                    //					makeMaskImage(48,48, img_source, dst_icon, R.drawable.mask_black_48, R.drawable.mask_48_);
                    //				}
                    //				else
                    //				{
                    Log.e("dst_Symbol swap_flag", swap_flag + "");
                    txt_dest.setText(dst_Symbol);
                    //	img_dest.setImageResource(dst_icon);

                    makeMaskImage(48, 48, img_dest, dst_icon, R.drawable.mask_black_48, R.drawable.mask_48_);
                    //	}


                }

                str_calculate_rate = calculate_rate_src("1", txt_source.getText().toString(), txt_dest.getText().toString());
                total_amount = Double.parseDouble(str_calculate_rate);
                etd_dest.setText("" + total_amount);
                etd_source.setText("1");

                convert_from = txt_source.getText().toString().toLowerCase();
                convert_to = txt_dest.getText().toString().toLowerCase();
                //	Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/z?s=usdinr%3dX&t=3m&q=l&l=on&z=l&a=v&p=s", img_graph, 400,400);

                if (offline_Mode == 1) {
                    img_graph.setImageResource(R.drawable.no_chart);
                    btn_retry.setVisibility(Button.VISIBLE);
                    txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);
                    txt_No_Internet_Connection.setText(getString(R.string.No_Internet_Connection) + ". " + getString(R.string.Uncheck_Offline_Mode));
                } else {
                    if (wLAN_Mode == 1) {

                        if (mWifi.isConnected()) {
                            Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
                            txt_No_Internet_Connection.setVisibility(TextView.GONE);
                            btn_retry.setVisibility(Button.GONE);


                        } else {
                            img_graph.setImageResource(R.drawable.no_chart);
                            btn_retry.setVisibility(Button.VISIBLE);
                            txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);
                            txt_No_Internet_Connection.setText(getString(R.string.No_Internet_Connection) + ". " + getString(R.string.Uncheck_WLAN_Mode));
                        }

                    } else {
                        if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {
                            Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
                            txt_No_Internet_Connection.setVisibility(TextView.GONE);
                            btn_retry.setVisibility(Button.GONE);
                        } else {
                            img_graph.setImageResource(R.drawable.no_chart);
                            btn_retry.setVisibility(Button.VISIBLE);
                            txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);
                            txt_No_Internet_Connection.setText(getString(R.string.No_Internet_Connection));
                        }
                    }

                }

            } else {

            }

        }


        if (offline_Mode == 1) {
            img_graph.setImageResource(R.drawable.no_chart);
            btn_retry.setVisibility(Button.VISIBLE);
            txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);
            txt_No_Internet_Connection.setText(getString(R.string.No_Internet_Connection) + ". " + getString(R.string.Uncheck_Offline_Mode));
        } else {

            if (wLAN_Mode == 1) {
                if (mWifi.isConnected()) {
                    Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
                    txt_No_Internet_Connection.setVisibility(TextView.GONE);
                    btn_retry.setVisibility(Button.GONE);


                } else {
                    img_graph.setImageResource(R.drawable.no_chart);
                    btn_retry.setVisibility(Button.VISIBLE);
                    txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);
                    txt_No_Internet_Connection.setText(getString(R.string.No_Internet_Connection) + ". " + getString(R.string.Uncheck_WLAN_Mode));
                }
            }
        }

        btn_retry.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                time_duration = "3m";

                txt_d1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_d5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_m3.setTextColor(activity.getResources().getColor(R.color.bg_setting_color));
                txt_y1.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y2.setTextColor(activity.getResources().getColor(R.color.textblackcolor));
                txt_y5.setTextColor(activity.getResources().getColor(R.color.textblackcolor));

                if (offline_Mode == 1) {
                    img_graph.setImageResource(R.drawable.no_chart);
                    btn_retry.setVisibility(Button.VISIBLE);
                    txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);
                    txt_No_Internet_Connection.setText(getString(R.string.No_Internet_Connection) + ". " + getString(R.string.Uncheck_Offline_Mode));
                } else {
                    if (connMgr_CC_main.getActiveNetworkInfo() != null && connMgr_CC_main.getActiveNetworkInfo().isAvailable() && connMgr_CC_main.getActiveNetworkInfo().isConnected()) {
                        convert_from = txt_source.getText().toString().toLowerCase();
                        convert_to = txt_dest.getText().toString().toLowerCase();
                        //Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/z?s=usdinr%3dX&t=3m&q=l&l=on&z=l&a=v&p=s", img_graph, 400,400);

                        Image_BitmapManager.INSTANCE.loadBitmap("http://chart.finance.yahoo.com/" + time_duration + "?" + convert_from + convert_to + "=x", img_graph, 400, 400);
                        txt_No_Internet_Connection.setText(getString(R.string.No_Internet_Connection));
                        txt_No_Internet_Connection.setVisibility(TextView.GONE);
                        btn_retry.setVisibility(Button.GONE);
                    } else {
                        img_graph.setImageResource(R.drawable.no_chart);
                        btn_retry.setVisibility(Button.VISIBLE);
                        txt_No_Internet_Connection.setVisibility(TextView.VISIBLE);


                    }
                }

            }
        });


    }

    private class WebserviceCallRegistrationTask_C_C_Main extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {


        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {

                HttpClient httpclient = new DefaultHttpClient();
                //
                HttpGet httpget = new HttpGet("http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json");
                //HttpGet httpget = new HttpGet("http://chart.finance.yahoo.com/1y?usdjpy=x");

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String response = httpclient.execute(httpget, responseHandler);

                // HttpResponse response = null;
                //
                //			            response = httpclient.execute(httpget);
                //
                //			        BasicResponseHandler responseHandler = new BasicResponseHandler();
                //			        String strResponse = null;
                //			        if (response != null)
                //			        {
                //
                //			                strResponse = responseHandler.handleResponse(response);
                //
                //			        }

                //HttpResponse res=httpclient.execute(httppost);

                responString = response;
                //	Log.e("responce", responString);


            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                Log.e("UnsupportedEncoding", e.toString());
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
                Log.e("ClientProtocolException", e.toString());
            } catch (IOException e) {

                // TODO Auto-generated catch block

                e.printStackTrace();
                Log.e("IOException", e.toString());
            }

            return responString;
        }

        @Override
        protected void onPostExecute(String result) {


            task_C_C_Main.cancel(true);

            if (responString != null) {

                //	longInfo(responString);
                JSONObject list_data = null;
                JSONObject obj = null;
                try {


                    obj = new JSONObject(responString);


                    if (obj.has("list")) {
                        str_list = obj.getString("list");
                        //Log.e("list",""+str_list);

                        list_data = new JSONObject(str_list);

                        if (list_data.has("resources")) {
                            JSONArray res_arr_list = list_data.getJSONArray("resources");

                            //Log.e("res_arr_list",""+res_arr_list.toString());

                            if (res_arr_list.length() > 0) {

                                cArrayList = new ArrayList<Currency_Data>();

                                for (int i = 0; i < res_arr_list.length(); i++) {
                                    JSONObject res_arr_item = res_arr_list.getJSONObject(i);

                                    if (res_arr_item.has("resource")) {
                                        String str_resource = res_arr_item.getString("resource");

                                        JSONObject jsob_resource = new JSONObject(str_resource);

                                        if (jsob_resource.has("fields")) {


                                            String str_fields = jsob_resource.getString("fields");

                                            JSONObject jsob_fields = new JSONObject(str_fields);

                                            if (jsob_fields.has("name")) {
                                                Currency_Data cur_data = new Currency_Data();

                                                String str_name = jsob_fields.getString("name");
                                                String str_symbol = jsob_fields.getString("symbol");
                                                String str_price = jsob_fields.getString("price");
                                                String str_utctime = jsob_fields.getString("utctime");
                                                String str_type = jsob_fields.getString("type");
                                                String str_ts = jsob_fields.getString("ts");
                                                String str_volume = jsob_fields.getString("volume");

                                                String str_country_name = "";
                                                String str_code_symbol = str_symbol.substring(0, str_symbol.indexOf("="));
                                                //	Log.e("str_symbol",str_code_symbol);

                                                for (int j = 0; j < C_C_Data.country_name.length; j++) {

                                                    CharSequence char_seq = str_code_symbol;


                                                    if (C_C_Data.country_name[j].contains(char_seq)) {

                                                        currency_icon = C_C_Data.country_icon[j];
                                                        str_country_name = C_C_Data.country_name[j].substring(C_C_Data.country_name[j].indexOf("-") + 2, C_C_Data.country_name[j].length());
                                                        //Log.e("str_country_name ", str_country_name);


                                                        break;
                                                    }
                                                }

                                                cur_data.setName(str_name);
                                                cur_data.setPrice(str_price);
                                                cur_data.setSymbol(str_code_symbol);
                                                cur_data.setTs(str_ts);
                                                cur_data.setType(str_type);
                                                cur_data.setUtctime(str_utctime);
                                                cur_data.setVolume(str_volume);
                                                cur_data.setCurrency_full_name(str_country_name);
                                                cur_data.setCurrency_icon(currency_icon);
                                                cArrayList.add(cur_data);
                                            }

                                        }

                                    }


                                }

                            }

                            if (cArrayList.size() > 0) {
                                Gson strgson = new Gson();
                                str_gson_parse_cArrayList = strgson.toJson(cArrayList);
                                editor.putString("str_gson_parse_cArrayList", str_gson_parse_cArrayList);
                                editor.commit();

                                //	C_C_Data.update_time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                                clock_12 = preferences.getInt("12_clock", 1);

                                if (clock_12 == 0) {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                    C_C_Data.update_time = dateFormat.format(new java.util.Date());
                                } else if (clock_12 == 1) {
                                    C_C_Data.update_time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                }


                                if (C_C_Data.update_time != null) {
                                    txt_update_time.setText(getString(R.string.Last_Update) + ": " + C_C_Data.update_time);
                                }
                            }
                        }

                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.e("Error", e.toString());
                }
            } else {
                //				Toast toast= Toast.makeText(getApplicationContext(), getResources().getString(R.string.No_Internet_Connection),Toast.LENGTH_SHORT);
                //				toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                //				toast.show();
            }
        }
    }

    public static void longInfo(String str) {
        if (str.length() > 4000) {
            Log.e("", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.e("", str);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for zoom in animation
        if (animation == animMove_left_right) {

        }

        if (animation == animMove_right_left) {

        }


    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    private String calculate_rate_src(String str_value, String str_src, String str_dest) {

        double float_value = 1;
        String str_answer = "";
        double answer_value = 1;
        String str_src_price = "1";
        String str_dest_price = "1";
        String str_usd_price = "1";

        if (str_value != "" && str_value.length() > 0) {
            float_value = Double.parseDouble(str_value);
        } else {
            float_value = 1;
        }

        if (str_src.equalsIgnoreCase(str_dest)) {
            answer_value = float_value;
        } else {


            if (str_src.equalsIgnoreCase("USD")) {

                for (int i = 0; i < cArrayList.size(); i++) {

                    if (cArrayList.get(i).getSymbol().equalsIgnoreCase(str_dest)) {
                        str_src_price = cArrayList.get(i).getPrice();
                        Log.e("str_price_src" + i, str_src_price + "");

                        break;
                    }

                }
                double price = Double.parseDouble(str_src_price);
                answer_value = float_value * price;

            } else if (str_dest.equalsIgnoreCase("USD")) {

                for (int i = 0; i < cArrayList.size(); i++) {
                    if (cArrayList.get(i).getSymbol().equalsIgnoreCase(str_src)) {

                        str_dest_price = cArrayList.get(i).getPrice();
                        Log.e("str_price_dest" + i, str_dest_price + "");
                        break;
                    }

                }
                double price = Double.parseDouble(str_dest_price);
                answer_value = float_value / price;
            } else {


                for (int i = 0; i < cArrayList.size(); i++) {
                    if (cArrayList.get(i).getSymbol().equalsIgnoreCase(str_src)) {
                        Log.e("cArrayList.getSymbol()" + i, cArrayList.get(i).getSymbol() + "");
                        Log.e("cArrayList.getPrice()" + i, cArrayList.get(i).getPrice() + "");
                        str_src_price = cArrayList.get(i).getPrice();

                    } else if (cArrayList.get(i).getSymbol().equalsIgnoreCase("USD")) {
                        Log.e("cArrayList.getSymbol()" + i, cArrayList.get(i).getSymbol() + "");
                        Log.e("cArrayList.getPrice()" + i, cArrayList.get(i).getPrice() + "");
                        str_usd_price = cArrayList.get(i).getPrice();

                    } else if (cArrayList.get(i).getSymbol().equalsIgnoreCase(str_dest)) {
                        Log.e("cArrayList.getSymbol()" + i, cArrayList.get(i).getSymbol() + "");
                        Log.e("cArrayList.getPrice()" + i, cArrayList.get(i).getPrice() + "");
                        str_dest_price = cArrayList.get(i).getPrice();

                    }

                }

                double src_price = Double.parseDouble(str_src_price);
                double usd_price = Double.parseDouble(str_usd_price);
                double dest_price = Double.parseDouble(str_dest_price);

                double src_usd_value = float_value / src_price;
                answer_value = src_usd_value * dest_price;
            }

        }
        number_of_decimal = preferences.getInt("Number_of_decimals", 2);


        if (number_of_decimal == 0) {
            str_answer = String.valueOf((int) answer_value);
        } else {
            str_answer = String.valueOf(formatFigureToTwoPlaces(answer_value));
        }

        return str_answer;
    }


    private String calculate_rate_dest(String str_value, String str_src, String str_dest) {

        double float_value = 1;
        String str_answer = "";
        double answer_value = 1;
        String str_src_price = "1";
        String str_dest_price = "1";
        String str_usd_price = "1";

        if (str_value != "" && str_value.length() > 0) {
            float_value = Double.parseDouble(str_value);
        } else {
            float_value = 1;
        }

        if (str_src.equalsIgnoreCase(str_dest)) {
            answer_value = float_value;
        } else {

            if (str_src.equalsIgnoreCase("USD")) {

                for (int i = 0; i < cArrayList.size(); i++) {

                    if (cArrayList.get(i).getSymbol().equalsIgnoreCase(str_dest)) {
                        str_src_price = cArrayList.get(i).getPrice();
                        Log.e("str_price_src" + i, str_src_price + "");

                        break;
                    }

                }
                double price = Double.parseDouble(str_src_price);
                answer_value = float_value / price;

            } else if (str_dest.equalsIgnoreCase("USD")) {

                for (int i = 0; i < cArrayList.size(); i++) {
                    if (cArrayList.get(i).getSymbol().equalsIgnoreCase(str_src)) {

                        str_dest_price = cArrayList.get(i).getPrice();
                        Log.e("str_price_dest" + i, str_dest_price + "");
                        break;
                    }

                }
                double price = Double.parseDouble(str_dest_price);
                answer_value = float_value * price;
            } else {


                for (int i = 0; i < cArrayList.size(); i++) {
                    if (cArrayList.get(i).getSymbol().equalsIgnoreCase(str_src)) {
                        Log.e("cArrayList.getSymbol()" + i, cArrayList.get(i).getSymbol() + "");
                        Log.e("cArrayList.getPrice()" + i, cArrayList.get(i).getPrice() + "");
                        str_src_price = cArrayList.get(i).getPrice();

                    } else if (cArrayList.get(i).getSymbol().equalsIgnoreCase("USD")) {
                        Log.e("cArrayList.getSymbol()" + i, cArrayList.get(i).getSymbol() + "");
                        Log.e("cArrayList.getPrice()" + i, cArrayList.get(i).getPrice() + "");
                        str_usd_price = cArrayList.get(i).getPrice();

                    } else if (cArrayList.get(i).getSymbol().equalsIgnoreCase(str_dest)) {
                        Log.e("cArrayList.getSymbol()" + i, cArrayList.get(i).getSymbol() + "");
                        Log.e("cArrayList.getPrice()" + i, cArrayList.get(i).getPrice() + "");
                        str_dest_price = cArrayList.get(i).getPrice();

                    }

                }

                double src_price = Double.parseDouble(str_src_price);
                double usd_price = Double.parseDouble(str_usd_price);
                double dest_price = Double.parseDouble(str_dest_price);

                double src_usd_value = float_value * src_price;
                answer_value = src_usd_value / dest_price;
            }

        }
        number_of_decimal = preferences.getInt("Number_of_decimals", 2);


        if (number_of_decimal == 0) {
            str_answer = String.valueOf((int) answer_value);
        } else {
            str_answer = String.valueOf(formatFigureToTwoPlaces(answer_value));
        }


        return str_answer;
    }


    public String formatFigureToTwoPlaces(double value) {
        number_of_decimal = preferences.getInt("Number_of_decimals", 2);

        DecimalFormat myFormatter = new DecimalFormat("00.00");

        if (number_of_decimal == 0) {
            myFormatter = new DecimalFormat("00");
        } else if (number_of_decimal == 1) {
            myFormatter = new DecimalFormat("00.0");
        } else if (number_of_decimal == 2) {
            myFormatter = new DecimalFormat("00.00");
        } else if (number_of_decimal == 3) {
            myFormatter = new DecimalFormat("00.000");
        } else if (number_of_decimal == 4) {
            myFormatter = new DecimalFormat("00.0000");
        } else if (number_of_decimal == 5) {
            myFormatter = new DecimalFormat("00.00000");

        }

        return myFormatter.format(value);
    }

    /**
     * Called when an ad is clicked and going to start a new Activity that will
     * leave the application (e.g. breaking out to the Browser or Maps
     * application).
     */


    //	      showButton.setText("Show Interstitial");
    //	      showButton.setEnabled(true);


    //Method of creating mask runtime
    public void makeMaskImage(int img_height, int img_width, ImageView mImageView, int mContent, int mask1, int mask2) {
        Bitmap original = BitmapFactory.decodeResource(getResources(), mContent);
        Bitmap new_original = Bitmap.createScaledBitmap(original, img_width, img_height, true);

        Bitmap mask = BitmapFactory.decodeResource(getResources(), mask1);
        Bitmap new_mask = Bitmap.createScaledBitmap(mask, img_width, img_height, true);
        Bitmap result = Bitmap.createBitmap(new_mask.getWidth(), new_mask.getHeight(), Config.ARGB_8888);

        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(new_original, 00, 00, null);
        mCanvas.drawBitmap(new_mask, 00, 00, paint);
        paint.setXfermode(null);
        mImageView.setImageBitmap(result);
        mImageView.setScaleType(ScaleType.FIT_XY);
        mImageView.setBackgroundResource(mask2);


    }

    public int countOccurrences(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        resume_flag = false;
        swap_flag = false;
    }
}
