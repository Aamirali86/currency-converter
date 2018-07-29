package com.techytec.currencyconverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.techytec.currencyconverter.data.Currency_Data;

import com.google.gson.Gson;
import com.exchange.currency.converter.R;

public class C_C_List extends Activity{

	private ArrayList<Currency_Data> cArrayList;
	private SharedPreferences preferences;
	private Editor editor;
	private String str_gson_parse_cArrayList;
	private ListView lstc_c_list;
	private ImageButton imgbtnback;
	private Button btn_sort;
	private EditText edtserch;
	private ArrayList<Currency_Data> cc_list_adpt;
	private C_C__list_Adapter	class_list_Adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.c_c_list);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
		cArrayList= new ArrayList<Currency_Data>();
		cc_list_adpt= new ArrayList<Currency_Data>();

		str_gson_parse_cArrayList=preferences.getString("str_gson_parse_cArrayList","");

		if(str_gson_parse_cArrayList.equalsIgnoreCase(""))
		{

		}
		else
		{
			Gson strgson= new Gson();
			cArrayList= strgson.fromJson(str_gson_parse_cArrayList, new TypeToken<List<Currency_Data>>(){}.getType());

			Collections.sort(cArrayList, new Comparator<Currency_Data>(){
				public int compare(Currency_Data obj1, Currency_Data obj2)
				{
					// TODO Auto-generated method stub
					return obj1.getSymbol().compareToIgnoreCase(obj2.getSymbol());
				}
			});

		}

		for (int i = 0; i < cArrayList.size(); i++)
		{

			

		}
		lstc_c_list= (ListView)findViewById(R.id.lstc_c_list);
		imgbtnback= (ImageButton)findViewById(R.id.imgbtnback);

		btn_sort= (Button)findViewById(R.id.btn_sort);
		edtserch= (EditText)findViewById(R.id.edtserch);

		imgbtnback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				onBackPressed();
			}
		});

		btn_sort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String str_sort=btn_sort.getText().toString();

				if(str_sort.equalsIgnoreCase("Sort by- Code"))
				{
					Collections.sort(cArrayList, new Comparator<Currency_Data>(){
						public int compare(Currency_Data obj1, Currency_Data obj2)
						{
							// TODO Auto-generated method stub
							return obj1.getCurrency_full_name().compareToIgnoreCase(obj2.getCurrency_full_name());
						}
					});

					btn_sort.setText(getString(R.string.Sort_by_Name));
				}
				else if(str_sort.equalsIgnoreCase("Sort by- Name"))
				{


					Collections.sort(cArrayList, new Comparator<Currency_Data>(){
						public int compare(Currency_Data obj1, Currency_Data obj2)
						{
							// TODO Auto-generated method stub
							return obj1.getSymbol().compareToIgnoreCase(obj2.getSymbol());
						}
					});
					btn_sort.setText(getString(R.string.Sort_by_Code));
				}

				class_list_Adapter=new C_C__list_Adapter(getDialogContext(),cArrayList);
				lstc_c_list.setAdapter(class_list_Adapter);
			}
		});


		class_list_Adapter=new C_C__list_Adapter(getDialogContext(),cArrayList);
		lstc_c_list.setAdapter(class_list_Adapter);

		lstc_c_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				C_C_Main_Screen.resume_flag=false;
				if(C_C_Main_Screen.source_destination == 1)
				{
					editor.putInt("source", 1);

					editor.putString("src_Currency_full_name", cc_list_adpt.get(position).getCurrency_full_name());
					editor.putString("src_Name", cc_list_adpt.get(position).getName());
					editor.putString("src_Price", cc_list_adpt.get(position).getPrice());
					editor.putString("src_Symbol", cc_list_adpt.get(position).getSymbol());
					editor.putString("src_Ts", cc_list_adpt.get(position).getTs());
					editor.putString("src_Type", cc_list_adpt.get(position).getType());
					editor.putString("src_Utctime", cc_list_adpt.get(position).getUtctime());
					editor.putString("src_Volume", cc_list_adpt.get(position).getVolume());
					editor.putInt("src_icon", cc_list_adpt.get(position).getCurrency_icon());
					editor.commit();

				}
				else if(C_C_Main_Screen.source_destination == 2)
				{

					editor.putInt("destination", 1);

					editor.putString("dst_Currency_full_name", cc_list_adpt.get(position).getCurrency_full_name());
					editor.putString("dst_Name", cc_list_adpt.get(position).getName());
					editor.putString("dst_Price", cc_list_adpt.get(position).getPrice());
					editor.putString("dst_Symbol", cc_list_adpt.get(position).getSymbol());
					editor.putString("dst_Ts", cc_list_adpt.get(position).getTs());
					editor.putString("dst_Type", cc_list_adpt.get(position).getType());
					editor.putString("dst_Utctime", cc_list_adpt.get(position).getUtctime());
					editor.putString("dst_Volume", cc_list_adpt.get(position).getVolume());
					editor.putInt("dst_icon", cc_list_adpt.get(position).getCurrency_icon());
					editor.commit();

				}

				finish();

			}


		});

		edtserch.addTextChangedListener(new TextWatcher() 
		{
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) 
			{
				//					searchData(arg0);

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) 
			{

			}
			@Override
			public void afterTextChanged(Editable arg0) 
			{
				//				Toast.makeText(MapSearchActivity.this,"SearchText:- "+arg0,Toast.LENGTH_SHORT).show();

				//				prod_srch_list=new ArrayList<Currency_Data>();
				//				
				//				String str_data=arg0.toString();
				//				B_A_Constant_Data.sql.Read();
				//				prod_srch_list	= B_A_Constant_Data.sql.get_All_Products_Data(str_data);
				//				B_A_Constant_Data.db.close();
				//
				//				

				if(arg0.length()==0)
				{
					class_list_Adapter=new C_C__list_Adapter(getDialogContext(),cArrayList);
					lstc_c_list.setAdapter(class_list_Adapter);
				}
				else
				{

					class_list_Adapter.getFilter().filter(arg0);
				}




			}
		});
	}

	public class C_C__list_Adapter extends BaseAdapter implements Filterable
	{
		private Context activity;

		private LayoutInflater layoutInflater;

		public C_C__list_Adapter() 
		{
			layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		public C_C__list_Adapter(Context context,
				ArrayList<Currency_Data> prod_list) {
			// TODO Auto-generated constructor stub
			activity=context;
			cc_list_adpt=prod_list;

			layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return cc_list_adpt.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView==null)
			{
				holder = new ViewHolder();
				convertView=layoutInflater.inflate(R.layout.c_c_list_items, null);

				holder.txt_title=(TextView)convertView.findViewById(R.id.txt_title);
				holder.txt_desc=(TextView)convertView.findViewById(R.id.txt_desc);

				holder.imgitem=(ImageView)convertView.findViewById(R.id.imgitem);



				//				holder.imgView=(ImageView)convertView.findViewById(R.id.imgIcon);

				convertView.setTag(holder);
			}
			else
			{
				holder=(ViewHolder) convertView.getTag();
			}

			holder.txt_title.setText(cc_list_adpt.get(position).getSymbol());
			holder.txt_desc.setText(cc_list_adpt.get(position).getCurrency_full_name());
			//holder.imgitem.setImageResource(cc_list_adpt.get(position).getCurrency_icon());
			makeMaskImage(64,64, holder.imgitem, cc_list_adpt.get(position).getCurrency_icon(), R.drawable.mask_64_black, R.drawable.mask_64_white_);

			return convertView;
		}

		public class ViewHolder
		{

			TextView txt_title;
			TextView txt_desc;
			ImageView imgitem;

		}

		@Override
		public Filter getFilter() 
		{
			return new Filter()
			{
				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) 
				{

					Log.d("PUBLISHING RESULTS ","" + constraint);
					cc_list_adpt= (ArrayList<Currency_Data>) results.values;
					class_list_Adapter.notifyDataSetChanged();
					//B_A_SearchList.this.words_datasFinal=words_datasFinal;
					//B_A_SearchList.this.adapter.notifyDataSetChanged();
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) 
				{
					Log.d("PERFORM FILTERING for: ","" + constraint);
					if(constraint.length()>0)
					{
						ArrayList<Currency_Data> filteredResults = getFilteredResults(constraint);
						FilterResults results = new FilterResults();
						results.values = filteredResults;
						return results;	
					}
					else 
					{
						ArrayList<Currency_Data> filteredResults = new ArrayList<Currency_Data>();
						FilterResults results = new FilterResults();
						results.values = filteredResults;
						return results;
					}


				}
			};
		}

		protected ArrayList<Currency_Data> getFilteredResults(CharSequence constraint) 
		{
			ArrayList<Currency_Data> tempStoryList=new ArrayList<Currency_Data>();



			String name=null,symbol=null;
			int prod_id;
			try 
			{
				for(int i=0;i<cArrayList.size();i++)
				{
					Currency_Data prod_data= new Currency_Data();
					prod_data=cArrayList.get(i);

					name=prod_data.getCurrency_full_name();
					symbol=prod_data.getSymbol();


					if(name !=null && !name.equals(""))
					{
						//						if(stationData.getStationName().toLowerCase().startsWith(constraint.toString().toLowerCase()))
						if(name.toLowerCase().contains(constraint.toString().toLowerCase()))
						{

							tempStoryList.add(prod_data);							
						}
						else if(symbol !=null && !symbol.equals(""))
						{
							//						if(stationData.getStationName().toLowerCase().startsWith(constraint.toString().toLowerCase()))
							if(symbol.toLowerCase().contains(constraint.toString().toLowerCase()))
							{
								tempStoryList.add(prod_data);
							}

						}
					}
				}
			}
			catch (Exception e) 
			{
				Log.e("Story Name", ""+name);
				Log.e("get Filter Result()", e.toString());
			}


			return tempStoryList;
		}
	}

	private Context getDialogContext() {
		Context context;
		if (getParent() != null) context = getParent();
		else context = this;
		return context;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		finish();

	}
	//Method of creating mask runtime
			public void makeMaskImage(int img_height,int img_width,ImageView mImageView, int mContent,int mask1,int mask2)
			{
				Bitmap original = BitmapFactory.decodeResource(getResources(), mContent);
				Bitmap new_original= Bitmap.createScaledBitmap(original, img_width, img_height, true);

				Bitmap mask = BitmapFactory.decodeResource(getResources(),mask1);
				Bitmap new_mask= Bitmap.createScaledBitmap(mask, img_width, img_height, true);
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
}