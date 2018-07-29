package com.techytec.currencyconverter;

import com.exchange.currency.converter.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class C_C_Setting extends Activity{

	private ImageButton imgbtnback;
	private TextView txtheadingname,txtNumber_of_decimals,txtUpdate_Frequency;
	private LinearLayout llNumber_of_decimals,llUpdate_Frequency,llhour12_clock;
	private LinearLayout llAutomatic_update,llOffline_Mode,llWLAN_Mode;
	private SharedPreferences preferences;
	private Editor editor;
	private int number_of_decimal,update_frequency,clock_12,automatic_update,offline_Mode;
	private CheckBox chkbx12_clock;
	private ImageView img12_clock,imgAutomatic_update,imgOffline_Mode,imgWLAN_Mode;
	private int wLAN_Mode;
	AdRequest adRequest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.c_c_settings);

		AdView adView = (AdView) this.findViewById(R.id.adView);
		adRequest = new AdRequest.Builder()
		.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		.addTestDevice("7AF8C338E6A4EA23E303067B6C1016ED")
		.build();
		adView.loadAd(adRequest);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();

		txtheadingname= (TextView)findViewById(R.id.txtheadingname);
		txtNumber_of_decimals= (TextView)findViewById(R.id.txtNumber_of_decimals);
		txtUpdate_Frequency= (TextView)findViewById(R.id.txtUpdate_Frequency);

		txtheadingname.setText(getResources().getString(R.string.menu_settings));
		imgbtnback= (ImageButton)findViewById(R.id.imgbtnback);
		img12_clock=(ImageView)findViewById(R.id.img12_clock);
		imgAutomatic_update=(ImageView)findViewById(R.id.imgAutomatic_update);
		imgOffline_Mode=(ImageView)findViewById(R.id.imgOffline_Mode);
		imgWLAN_Mode=(ImageView)findViewById(R.id.imgWLAN_Mode);

		llNumber_of_decimals= (LinearLayout)findViewById(R.id.llNumber_of_decimals);
		llUpdate_Frequency= (LinearLayout)findViewById(R.id.llUpdate_Frequency);
		llhour12_clock= (LinearLayout)findViewById(R.id.llhour12_clock);
		llAutomatic_update= (LinearLayout)findViewById(R.id.llAutomatic_update);
		llOffline_Mode= (LinearLayout)findViewById(R.id.llOffline_Mode);
		llWLAN_Mode= (LinearLayout)findViewById(R.id.llWLAN_Mode);


		number_of_decimal =preferences.getInt("Number_of_decimals", 2);
		update_frequency =preferences.getInt("Update_Frequency", 2);
		clock_12 =preferences.getInt("12_clock", 1);
		automatic_update =preferences.getInt("Automatic_update", 1);
		offline_Mode =preferences.getInt("Offline_Mode", 0);
		wLAN_Mode =preferences.getInt("WLAN_Mode", 0);

		if(offline_Mode==0)
		{
			imgOffline_Mode.setImageResource(R.drawable.tick_off);
			llUpdate_Frequency.setEnabled(true);
			llAutomatic_update.setEnabled(true);
			llWLAN_Mode.setEnabled(true);

			update_frequency =preferences.getInt("Update_Frequency", 2);

			if(update_frequency==0)
			{
				txtUpdate_Frequency.setText(getString(R.string.sec30));
			}
			else if(update_frequency==1)
			{
				txtUpdate_Frequency.setText(getString(R.string.min1));
			} 
			else if(update_frequency==2)
			{
				txtUpdate_Frequency.setText(getString(R.string.min2));
			}
			else if(update_frequency==3)
			{
				txtUpdate_Frequency.setText(getString(R.string.min5));
			}
			else if(update_frequency==4)
			{
				txtUpdate_Frequency.setText(getString(R.string.min10));
			}
			else if(update_frequency==5)
			{
				txtUpdate_Frequency.setText(getString(R.string.min20));
			}
			else if(update_frequency==6)
			{
				txtUpdate_Frequency.setText(getString(R.string.min30));
			}
			else if(update_frequency==7)
			{
				txtUpdate_Frequency.setText(getString(R.string.hour1));
			}
			else if(update_frequency==8)
			{
				txtUpdate_Frequency.setText(getString(R.string.Once_a_Day));
			}

			automatic_update =preferences.getInt("Automatic_update", 1);

			if(automatic_update==0)
			{
				imgAutomatic_update.setImageResource(R.drawable.tick_off);
			}
			else if(automatic_update==1)
			{
				imgAutomatic_update.setImageResource(R.drawable.tick_on);
			}

			wLAN_Mode =preferences.getInt("WLAN_Mode", 0);

			if(wLAN_Mode==0)
			{
				imgWLAN_Mode.setImageResource(R.drawable.tick_off);
			}
			else if(wLAN_Mode==1)
			{
				imgWLAN_Mode.setImageResource(R.drawable.tick_on);
			}

		}
		else if(offline_Mode==1)
		{
			imgOffline_Mode.setImageResource(R.drawable.tick_on);
			llUpdate_Frequency.setEnabled(false);
			llAutomatic_update.setEnabled(false);
			llWLAN_Mode.setEnabled(false);

			txtUpdate_Frequency.setText(getString(R.string.Automatic_Update_disabled));
			imgAutomatic_update.setImageResource(R.drawable.tick_off);
			imgWLAN_Mode.setImageResource(R.drawable.tick_off);
		}



		if(clock_12==0)
		{
			img12_clock.setImageResource(R.drawable.tick_off);
		}
		else if(clock_12==1)
		{
			img12_clock.setImageResource(R.drawable.tick_on);
		}



		if(number_of_decimal==0)
		{
			txtNumber_of_decimals.setText("0");
		}
		else if(number_of_decimal==1)
		{
			txtNumber_of_decimals.setText("0.1");
		}
		else if(number_of_decimal==2)
		{
			txtNumber_of_decimals.setText("0.12");
		}
		else if(number_of_decimal==3)
		{
			txtNumber_of_decimals.setText("0.123");
		}
		else if(number_of_decimal==4)
		{
			txtNumber_of_decimals.setText("0.1234");
		}
		else if(number_of_decimal==5)
		{
			txtNumber_of_decimals.setText("0.12345");

		}


		llWLAN_Mode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				wLAN_Mode =preferences.getInt("WLAN_Mode", 0);

				if(wLAN_Mode==0)
				{
					imgWLAN_Mode.setImageResource(R.drawable.tick_on);

					editor.putInt("WLAN_Mode", 1);
					editor.commit();
				}
				else if(wLAN_Mode==1)
				{
					imgWLAN_Mode.setImageResource(R.drawable.tick_off);

					editor.putInt("WLAN_Mode", 0);
					editor.commit();
				}


			}
		});


		llOffline_Mode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				offline_Mode =preferences.getInt("Offline_Mode", 0);


				if(offline_Mode==0)
				{
					imgOffline_Mode.setImageResource(R.drawable.tick_on);
					editor.putInt("Offline_Mode", 1);
					editor.commit();
					llUpdate_Frequency.setEnabled(false);
					llAutomatic_update.setEnabled(false);
					llWLAN_Mode.setEnabled(false);

					imgAutomatic_update.setImageResource(R.drawable.tick_off);
					txtUpdate_Frequency.setText(getString(R.string.Automatic_Update_disabled));
					imgWLAN_Mode.setImageResource(R.drawable.tick_off);
				}
				else if(offline_Mode==1)
				{
					imgOffline_Mode.setImageResource(R.drawable.tick_off);
					editor.putInt("Offline_Mode", 0);
					editor.commit();
					llUpdate_Frequency.setEnabled(true);
					llAutomatic_update.setEnabled(true);
					llWLAN_Mode.setEnabled(true);

					update_frequency =preferences.getInt("Update_Frequency", 2);

					if(update_frequency==0)
					{
						txtUpdate_Frequency.setText(getString(R.string.sec30));
					}
					else if(update_frequency==1)
					{
						txtUpdate_Frequency.setText(getString(R.string.min1));
					} 
					else if(update_frequency==2)
					{
						txtUpdate_Frequency.setText(getString(R.string.min2));
					}
					else if(update_frequency==3)
					{
						txtUpdate_Frequency.setText(getString(R.string.min5));
					}
					else if(update_frequency==4)
					{
						txtUpdate_Frequency.setText(getString(R.string.min10));
					}
					else if(update_frequency==5)
					{
						txtUpdate_Frequency.setText(getString(R.string.min20));
					}
					else if(update_frequency==6)
					{
						txtUpdate_Frequency.setText(getString(R.string.min30));
					}
					else if(update_frequency==7)
					{
						txtUpdate_Frequency.setText(getString(R.string.hour1));
					}
					else if(update_frequency==8)
					{
						txtUpdate_Frequency.setText(getString(R.string.Once_a_Day));
					}

					automatic_update =preferences.getInt("Automatic_update", 1);

					if(automatic_update==0)
					{
						imgAutomatic_update.setImageResource(R.drawable.tick_off);
					}
					else if(automatic_update==1)
					{
						imgAutomatic_update.setImageResource(R.drawable.tick_on);
					}

					

					wLAN_Mode =preferences.getInt("WLAN_Mode", 0);

					if(wLAN_Mode==0)
					{
						imgWLAN_Mode.setImageResource(R.drawable.tick_off);
					}
					else if(wLAN_Mode==1)
					{
						imgWLAN_Mode.setImageResource(R.drawable.tick_on);
					}

				}
			}
		});


		imgbtnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				onBackPressed();
			}
		});


		llAutomatic_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				automatic_update =preferences.getInt("Automatic_update", 1);


				if(automatic_update==0)
				{
					imgAutomatic_update.setImageResource(R.drawable.tick_on);
					editor.putInt("Automatic_update", 1);
					editor.commit();
				}
				else if(automatic_update==1)
				{
					imgAutomatic_update.setImageResource(R.drawable.tick_off);
					editor.putInt("Automatic_update", 0);
					editor.commit();
				}
			}
		});

		llhour12_clock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				clock_12 =preferences.getInt("12_clock", 0);


				if(clock_12==0)
				{
					img12_clock.setImageResource(R.drawable.tick_on);

					editor.putInt("12_clock", 1);
					editor.commit();
				}
				else if(clock_12==1)
				{
					img12_clock.setImageResource(R.drawable.tick_off);
					editor.putInt("12_clock", 0);
					editor.commit();
				}
			}
		});

		llNumber_of_decimals.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				openDialog_for_Number_Of_Decimals();

				//				Intent mainIntent = new Intent(C_C_Setting.this,C_C_Number_Of_Decimals.class);
				//				C_C_Setting.this.startActivity(mainIntent);
			}
		});


		llUpdate_Frequency.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openDialog_for_Update_Frequency();

			}
		});

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
	}

	protected void openDialog_for_Number_Of_Decimals()
	{
		try 
		{


			final CharSequence[] options= {


					"1:"+"  "+"0.1",
					"2:"+"  "+"0.12",
					"3:"+"  "+"0.123",
					"4:"+"  "+"0.1234",
					"5:"+"  "+"0.12345",

			};

			AlertDialog.Builder alt_bld = new AlertDialog.Builder(getDialogContext());
			alt_bld.setIcon(R.drawable.icon_48x48);
			alt_bld.setTitle(getString(R.string.Number_of_decimals));

			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						//Yes button clicked
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						break;
					}
				}
			};

			alt_bld.setNegativeButton(getResources().getString(R.string.Cancel), dialogClickListener);


			number_of_decimal =preferences.getInt("Number_of_decimals", 2);



			alt_bld.setSingleChoiceItems(options, number_of_decimal-1, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int item) 
				{
					dialog.dismiss();
					if(item==0)
					{
						editor.putInt("Number_of_decimals",1);
						editor.commit();
						txtNumber_of_decimals.setText("0.1");

					}
					else if(item==1)
					{
						editor.putInt("Number_of_decimals", 2);
						editor.commit();
						txtNumber_of_decimals.setText("0.12");

					}
					else if(item==2)
					{
						editor.putInt("Number_of_decimals", 3);
						editor.commit();
						txtNumber_of_decimals.setText("0.123");

					}
					else if(item==3)
					{
						editor.putInt("Number_of_decimals", 4);
						editor.commit();
						txtNumber_of_decimals.setText("0.1234");
					}
					else if(item==4)
					{
						editor.putInt("Number_of_decimals", 5);
						editor.commit();
						txtNumber_of_decimals.setText("0.12345");

					}

				}
			});
			AlertDialog alert = alt_bld.create();
			alert.show();
		}
		catch (Exception e) 
		{
			Log.e("Error playing file:- ",""+e.toString());
		}
	}

	protected void openDialog_for_Update_Frequency()
	{
		try 
		{


			final CharSequence[] options= {


					getString(R.string.sec30),
					getString(R.string.min1),
					getString(R.string.min2),
					getString(R.string.min5),
					getString(R.string.min10),
					getString(R.string.min20),	
					getString(R.string.min30),	
					getString(R.string.hour1),	
					getString(R.string.Once_a_Day),	



			};

			AlertDialog.Builder alt_bld = new AlertDialog.Builder(getDialogContext());
			alt_bld.setIcon(R.drawable.icon_48x48);
			alt_bld.setTitle(getString(R.string.Update_Frequency));

			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case DialogInterface.BUTTON_POSITIVE:
						//Yes button clicked
						break;

					case DialogInterface.BUTTON_NEGATIVE:
						//No button clicked
						break;
					}
				}
			};

			alt_bld.setNegativeButton(getResources().getString(R.string.Cancel), dialogClickListener);


			update_frequency =preferences.getInt("Update_Frequency", 2);



			alt_bld.setSingleChoiceItems(options, update_frequency, new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int item) 
				{
					dialog.dismiss();
					if(item==0)
					{
						editor.putInt("Update_Frequency",0);
						editor.commit();
						txtUpdate_Frequency.setText(getString(R.string.sec30));

					}
					else if(item==1)
					{
						editor.putInt("Update_Frequency", 1);
						editor.commit();
						txtUpdate_Frequency.setText(getString(R.string.min1));

					}
					else if(item==2)
					{
						editor.putInt("Update_Frequency", 2);
						editor.commit();
						txtUpdate_Frequency.setText(getString(R.string.min2));

					}
					else if(item==3)
					{
						editor.putInt("Update_Frequency", 3);
						editor.commit();
						txtUpdate_Frequency.setText(getString(R.string.min5));
					}
					else if(item==4)
					{
						editor.putInt("Update_Frequency", 4);
						editor.commit();
						txtUpdate_Frequency.setText(getString(R.string.min10));

					}
					else if(item==5)
					{
						editor.putInt("Update_Frequency", 5);
						editor.commit();
						txtUpdate_Frequency.setText(getString(R.string.min20));

					}
					else if(item==6)
					{
						editor.putInt("Update_Frequency", 6);
						editor.commit();
						txtUpdate_Frequency.setText(getString(R.string.min30));

					}
					else if(item==7)
					{
						editor.putInt("Update_Frequency", 7);
						editor.commit();
						txtUpdate_Frequency.setText(getString(R.string.hour1));
					}
					else if(item==8)
					{
						editor.putInt("Update_Frequency", 8);
						editor.commit();
						txtUpdate_Frequency.setText(getString(R.string.Once_a_Day));

					}

				}
			});
			AlertDialog alert = alt_bld.create();
			alert.show();
		}
		catch (Exception e) 
		{
			Log.e("Error playing file:- ",""+e.toString());
		}
	}
	private Context getDialogContext() {
		Context context;
		if (getParent() != null) context = getParent();
		else context = this;
		return context;
	}
}
