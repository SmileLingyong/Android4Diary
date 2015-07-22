package com.org.android.diary.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.org.android.diary.R;
import com.org.android.diary.db.DiaryDao;
import com.org.android.diary.model.Diary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddDiaryActivity extends Activity {
	private TextView timeTextView = null;
	private TextView weekTextView = null;
	private Spinner weatherSpinner = null;
	private Calendar cal = Calendar.getInstance();
	private Date date = null;
	private SimpleDateFormat simpleDateFormat = null;
	public static final int WEEKDAYS = 7;
	private EditText diaryInfo = null;
	private EditText diaryTitle = null;
	public static String[] WEEK = { "������", "����һ", "���ڶ�", "������", "������", "������",
			"������" };
	private ImageView back = null;
	private SharedPreferences preferences = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setBackground();
	}
	@SuppressLint("SimpleDateFormat")
	private void init() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_diary);
		preferences = getSharedPreferences("image", MODE_PRIVATE);
		date = cal.getTime();
		simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		timeTextView = (TextView) this.findViewById(R.id.time);
		timeTextView.setText(simpleDateFormat.format(date));
		weekTextView = (TextView) this.findViewById(R.id.week);
		weekTextView.setText(DateToWeek(date));
		weatherSpinner = (Spinner) this.findViewById(R.id.weather);
		diaryInfo = (EditText)this.findViewById(R.id.edit_diary_info);
		diaryTitle = (EditText)this.findViewById(R.id.edit_title);
		back = (ImageView)this.findViewById(R.id.back_add_diary);
		back.setOnClickListener(new BackListener());
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, 
				R.array.weather, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		weatherSpinner.setAdapter(adapter);
		weatherSpinner.setPrompt(getString(R.string.weather));
		setBackground();
	}
	
	private void setBackground() {
		// �õ���ǰ����
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.add_diary_layout);
		// �õ�id,�˴�id�������ñ�����������ģ��˴��ݲ�����
		int id = preferences.getInt("id", 0);
		if (id == 0) {// id=0˵���ǳ�ʼ��ʱ�ı���
			// ���ñ�������
			layout.setBackgroundResource(R.drawable.diary_view_bg);
		} else if (id == 1) {// id=1˵���û�ѡ���˵�һ��ͼƬ
			layout.setBackgroundResource(R.drawable.diary_view_bg);
		} else if (id == 2) {// id=2˵���û�ѡ���˵ڶ���ͼƬ
			layout.setBackgroundResource(R.drawable.spring);
		} else if (id == 3) {// id=3˵���û�ѡ���˵�����ͼƬ
			layout.setBackgroundResource(R.drawable.summer);
		} else if (id == 4) {// id=4˵���û�ѡ���˵��ķ�ͼƬ
			layout.setBackgroundResource(R.drawable.autumn);
		} else if (id == 5) {// id=4˵���û�ѡ���˵��ķ�ͼƬ
			layout.setBackgroundResource(R.drawable.winter);
		}
	}
	public static String DateToWeek(Date date){
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayIndex < 1 || dayIndex > WEEKDAYS) {  
	        return null;  
	    }  
	    return WEEK[dayIndex - 1];  
	}
	
	class BackListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			back();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			back();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void back(){
		if ((!diaryTitle.getText().toString().trim().equals("")) && 
				(!diaryInfo.getText().toString().trim().equals(""))) {
			DiaryDao diaryDao = new DiaryDao(AddDiaryActivity.this);
			Diary diary = new Diary();
			diary.setDate(timeTextView.getText().toString());
			diary.setWeek(weekTextView.getText().toString());
			diary.setWeather(weatherSpinner.getSelectedItem().toString());
			diary.setDiaryTitle(diaryTitle.getText().toString());
			diary.setDiaryInfo(diaryInfo.getText().toString());
			diaryDao.insert(diary);
			Intent intent = new Intent();
			intent.setClass(AddDiaryActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
			Toast.makeText(AddDiaryActivity.this, R.string.save_success, 0).show();
		}else {
			Toast.makeText(AddDiaryActivity.this, R.string.empty_info, 0).show();
			AddDiaryActivity.this.finish();
			overridePendingTransition(R.anim.push_below_in, R.anim.push_below_out);
		}
	}
}
