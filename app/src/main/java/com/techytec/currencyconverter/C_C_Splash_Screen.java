package com.techytec.currencyconverter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
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
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.exchange.currency.converter.R;
import com.techytec.currencyconverter.data.Currency_Data;
import com.techytec.currencyconverter.data.C_C_Data;

import com.google.gson.Gson;

public class C_C_Splash_Screen extends Activity {

	private Timer timer;
	private TimerTask timerTask;
	private int width ;
	private int height;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private int devicesize_flag;

	private TextView txttop,txtbottom;
	private ConnectivityManager connMgr_CC_splash;
	public WebserviceCallRegistrationTask_C_C_Splash task_C_C_Splash;
	public String responString;
	public String str_list;
	public ArrayList<Currency_Data> cArrayList;
	public int currency_icon,clock_12;
	public String str_gson_parse_cArrayList;
	private boolean parse_cArrayList_flag=false;
	private ImageView imgsplashicon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_c__c__splash__screen);

		Runtime.getRuntime().gc();

		C_C_Data.infocounter=0;
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();


		connMgr_CC_splash = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connMgr_CC_splash.getActiveNetworkInfo() != null && connMgr_CC_splash.getActiveNetworkInfo().isAvailable() && connMgr_CC_splash.getActiveNetworkInfo().isConnected()) 
		{

			task_C_C_Splash= new WebserviceCallRegistrationTask_C_C_Splash();
			task_C_C_Splash.execute(new String[] { "Webservice Calling for C_C_Splash_Screen" });
		}

		Display display = getWindowManager().getDefaultDisplay(); 
		width = display.getWidth();
		height= display.getHeight();
		C_C_Data.screenwidth=width;
		C_C_Data.screenheight=height;

		Log.e("width", ""+width);
		Log.e("height", ""+height);

		txttop=(TextView)findViewById(R.id.txttop);
		txtbottom=(TextView)findViewById(R.id.txtbottom);
		imgsplashicon=(ImageView)findViewById(R.id.imgsplashicon);

		devicesize_flag=isTablet(this);

		editor.putInt("devicesize_flag", devicesize_flag);
		editor.commit();

		if(devicesize_flag==4)
		{

			imgsplashicon.setImageResource(R.drawable.icon_500x500);
			txtbottom.setTextSize(this.getResources().getDimension(R.dimen.texttripleextralargesize));
			txttop.setTextSize(this.getResources().getDimension(R.dimen.texttripleextralargesize));

		}
		else
		{
			if(C_C_Data.screenwidth<=320)
			{
				txtbottom.setTextSize(this.getResources().getDimension(R.dimen.textlargesize));
				txttop.setTextSize(this.getResources().getDimension(R.dimen.textlargesize));

			}


		}
		Log.e("tblatflag", ""+devicesize_flag);

		try
		{
			String language="en";

			Locale locale=Locale.getDefault();

			Log.e("Language code:- ",locale.getLanguage());

			if(locale.getLanguage().equals("ar"))
			{
				language="ar";
			}
			else if(locale.getLanguage().equals("de"))
			{
				language="de";
			}
			else if(locale.getLanguage().equals("en"))
			{
				language="en";
			}
			else if(locale.getLanguage().equals("es"))
			{
				language="es";
			}
			else if(locale.getLanguage().equals("fr"))
			{
				language="fr";
			}

			else if(locale.getLanguage().equals("he"))
			{
				language="he";
			}
			else if(locale.getLanguage().equals("it"))
			{
				language="it";
			}
			else if(locale.getLanguage().equals("iw"))
			{
				language="iw";
			}
			else if(locale.getLanguage().equals("ja"))
			{
				language="ja";
			}
			else if(locale.getLanguage().equals("ko"))
			{
				language="ko";
			}
			else if(locale.getLanguage().equals("ln"))
			{
				language="ln";
			}
			else if(locale.getLanguage().equals("pt"))
			{
				language="pt";
			}
			else if(locale.getLanguage().equals("ru"))
			{
				language="ru";
			}
			else if(locale.getLanguage().equals("th"))
			{
				language="th";
			}
			else if(locale.getLanguage().equals("zh_CN") || locale.getLanguage().equals("zh"))
			{
				language="zh_CN";
			}
			else if(locale.getLanguage().equals("zh_TW"))
			{
				language="zh_TW";
			}

			Log.i("Default Locale: ",language);
			Locale locale2 = new Locale(language);
			Locale.setDefault(locale2);
			Configuration config2 = new Configuration();
			config2.locale = locale2;
			getBaseContext().getResources().updateConfiguration(config2,getBaseContext().getResources().getDisplayMetrics());

		}
		catch (Exception e) 
		{
			Log.e("Getting Device ID Error", e.toString());
		}


		if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
		{


			if(timer!=null)
				timer.cancel();

			timerTask=new TimerTask() 
			{	
				@Override
				public void run() 
				{
					if(timer!=null)
						timer.cancel();

					Intent mainIntent = new Intent(C_C_Splash_Screen.this,C_C_Main_Screen.class);
					C_C_Splash_Screen.this.startActivity(mainIntent);
					C_C_Splash_Screen.this.finish();

				}
			};
			timer=new Timer();
			timer.schedule(timerTask,3000,100);
		}


	}

	public int isTablet(Context context) {

		int devicesize=2;
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
		Log.e("xlarge", ""+xlarge);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);

		Log.e("large", ""+large);
		boolean nrml = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL);
		Log.e("nrml", ""+nrml);
		boolean small = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL);
		Log.e("small", ""+small);

		if(small)
		{
			devicesize=1;
		}
		if(nrml)
		{
			devicesize=2;
		}
		if(large)
		{
			devicesize=3;
		}
		if(xlarge)
		{
			devicesize=4;
		}

		return devicesize;
	}

	@Override
	protected void onDestroy() 
	{

		System.gc();
		Runtime.getRuntime().gc();
		super.onDestroy();
		try
		{
			unbindDrawables(findViewById(R.id.llspashscreen)); 
		}
		catch (Exception e) 
		{
			Log.e("splash scren Error", e.toString());
		}

	}

	private void unbindDrawables(View view) 
	{
		if (view.getBackground() != null) 
		{
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) 
		{
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) 
			{
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}
	}
	private class WebserviceCallRegistrationTask_C_C_Splash extends AsyncTask<String, Void, String> {


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

				responString=response;
				Log.e("", responString);


			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				Log.e("UnsuportEncodeException", e.toString());
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


			task_C_C_Splash.cancel(true);

			if(responString!=null)
			{

				//longInfo(responString);
				JSONObject list_data=null;
				JSONObject obj=null;
				try {


					obj=new JSONObject(responString);


					if (obj.has("list"))
					{
						str_list = obj.getString("list");
						//Log.e("list",""+str_list);

						list_data=new JSONObject(str_list);

						if (list_data.has("resources"))
						{
							JSONArray res_arr_list= list_data.getJSONArray("resources");

						//	Log.e("res_arr_list",""+res_arr_list.toString());
							longInfo(res_arr_list.toString());

							if(res_arr_list.length()>0)
							{

								cArrayList= new ArrayList<Currency_Data>();

								for (int i = 0; i < res_arr_list.length(); i++) 
								{
									JSONObject res_arr_item=res_arr_list.getJSONObject(i);

									if (res_arr_item.has("resource"))
									{
										String str_resource =res_arr_item.getString("resource");

										JSONObject	jsob_resource=new JSONObject(str_resource);

										if (jsob_resource.has("fields"))
										{


											String str_fields =jsob_resource.getString("fields");

											JSONObject	jsob_fields=new JSONObject(str_fields);

											if (jsob_fields.has("name"))
											{
												Currency_Data cur_data = new Currency_Data();

												String str_name =jsob_fields.getString("name");
												String str_symbol =jsob_fields.getString("symbol");
												String str_price =jsob_fields.getString("price");
												String str_utctime =jsob_fields.getString("utctime");
												String str_type =jsob_fields.getString("type");
												String str_ts =jsob_fields.getString("ts");
												String str_volume=jsob_fields.getString("volume");

												String str_country_name="";
												String str_code_symbol =str_symbol.substring(0, str_symbol.indexOf("="));
												//Log.e("str_symbol",str_code_symbol);

												for (int j = 0; j < C_C_Data.country_name.length; j++) 
												{

													CharSequence char_seq=	str_code_symbol;


													if(C_C_Data.country_name[j].contains(char_seq))
													{

														currency_icon=C_C_Data.country_icon[j];
														str_country_name=C_C_Data.country_name[j].substring(C_C_Data.country_name[j].indexOf("-")+2, C_C_Data.country_name[j].length());
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

							if(cArrayList.size()>0)
							{
								Gson strgson= new Gson();
								str_gson_parse_cArrayList = strgson.toJson(cArrayList);
								editor.putString("str_gson_parse_cArrayList", str_gson_parse_cArrayList);
								editor.commit();

								parse_cArrayList_flag=true;
								
								clock_12 =preferences.getInt("12_clock", 1);
								
								if(clock_12==0)
								{
									SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
									C_C_Data.update_time = dateFormat.format(new java.util.Date());
								}
								
								else if(clock_12==1)
								{
									C_C_Data.update_time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
								}
								

							}
						}

					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("Error",e.toString());
				}
			}
			else
			{
				Toast toast= Toast.makeText(getApplicationContext(), getResources().getString(R.string.No_Internet_Connection),Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
				toast.show();
			}
		}
	}
	public static void longInfo(String str) {
		if(str.length() > 4000) {
			Log.e("",str.substring(0, 4000));
			longInfo(str.substring(4000));
		} else
			Log.e("",str);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		if(timer!=null)
			timer.cancel();

		if(task_C_C_Splash!=null)
		{
			task_C_C_Splash.cancel(true);

		}
		if(parse_cArrayList_flag)
		{

		}
		else
		{
			editor.putString("str_gson_parse_cArrayList", "");
			editor.commit();
		}

	}
}