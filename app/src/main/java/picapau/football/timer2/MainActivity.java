package picapau.football.timer2;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private TextView textGameClock;
	private MyCountDownTimer countdowntimer;
	private Button buttonStart;
	private Button buttonStop;
	private Button buttonReset;	
	private long nGameClock;

	private MyCountDownTimer40 countdowntimer40;
	private Button button40Start;
	private Button button40Reset;	
	private long n40;
	RadioGroup radioGroup;
	RadioButton radio40;
	RadioButton radio25;
	
	ImageView img01;
	ImageView img02;
	ImageView img0;
	ImageView img1;
	ImageView img2;
	ImageView img3;
	ImageView img4;
	ImageView img5;
	ImageView img6;
	ImageView img7;
	ImageView img8;
	ImageView img9;
	
	boolean bTimer = false;
	
	int requestSETTING = 1;
	
	// 音を鳴らすオブジェクト
	MediaPlayer[] player = new MediaPlayer[40];
	Uri[] uri = new Uri[40];
	String[] title = new String[40];

	public void StopMusic()
	{
		for( int i=0; i<40; i++)
		{
			if( player[i] == null )
			{
				continue;
			}
			
			if( player[i].isPlaying() )
			{
				player[i].pause();
				player[i].stop();
				player[i].reset();
				player[i].release();
				if( uri[i] != null ){ player[i] = MediaPlayer.create(getBaseContext(), uri[i]); }
				else{ player[i] = GetDefaultMusic(i); }
				
				try {
					player[i].setLooping(false);
				} catch (NullPointerException e) {
					
				}
				
				try {
					//player[i].prepare();
					player[i].prepareAsync();
				} catch (IllegalStateException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO 自動生成された catch ブロック
//					e.printStackTrace();
				}
				
				
			}
		}
	}
	
	public MediaPlayer GetDefaultMusic(int i)
	{
		switch(i)
		{
		case 0:
			return MediaPlayer.create(getBaseContext(), R.raw.buzzer);
		default:
			return null;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // BackBtnアクション
	    if(keyCode==KeyEvent.KEYCODE_BACK)
	    {
	    	countdowntimer40.cancel();
	    	
	    	StopMusic();
			
	    	finish();
	    }
		return true;
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			
			StopMusic();
			
			if( bTimer == false)
			{
				countdowntimer40 = new MyCountDownTimer40(n40+1000, 1);
				countdowntimer40.start();
				bTimer = true;
			}
			else
			{
				countdowntimer40.cancel();
				if( radio40.isChecked() )
				{
					n40 = 40000;
					img01.setImageResource(R.drawable.d4);
					img02.setImageResource(R.drawable.d0);
				}
				else
				{
					n40 = 25000;
					img01.setImageResource(R.drawable.d2);
					img02.setImageResource(R.drawable.d5);
				}
				Get4025Format(n40);
				
				bTimer = false;
			}
			
			break;
		}
		
        return true;
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// GameClock Initialize
		textGameClock = (TextView)findViewById(R.id.textGameClock);
		buttonStart = (Button)findViewById(R.id.buttonStart);
		buttonStop = (Button)findViewById(R.id.buttonStop);
		buttonReset = (Button)findViewById(R.id.buttonReset);		
		nGameClock = 60000;
		textGameClock.setText(GetTimerFormat(nGameClock));
		countdowntimer = new MyCountDownTimer(nGameClock, 1);//dummy
		
		textGameClock.setVisibility(View.GONE);
		buttonStart.setVisibility(View.GONE);
		buttonStop.setVisibility(View.GONE);
		buttonReset.setVisibility(View.GONE);
		
		buttonStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				countdowntimer = new MyCountDownTimer(nGameClock+1000, 1);
				countdowntimer.start();
			};
		});
		
		buttonStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				countdowntimer.cancel();
				
			};
		});
		
		buttonReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				countdowntimer.cancel();
				nGameClock = 60000;
				textGameClock.setText(GetTimerFormat(nGameClock));
			};
		});
		
		
		// 4025 Initializ
		
		for( int i=0; i<40; i++)
		{
			uri[i] = null;
		}
		
		// プリファレンスから設定を呼び出し
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		String strUri = new String();
		String strTitle = new String();
		
		for( int i=39; i>=0; i-- )
		{
			strUri = "";
			strUri = pref.getString("uri" + String.valueOf(i), "");
			if( !strUri.equals("") ){uri[i] = Uri.parse(strUri);}
			
			if( uri[i] != null )
			{ 
				player[i] = MediaPlayer.create(getBaseContext(), uri[i]);
				try{
					player[i].setLooping(false);
				} catch(NullPointerException e){
					
				}
			}
			else
			{ 
				player[i] = GetDefaultMusic(i);
			}
			
			strTitle = "";
			strTitle = pref.getString("title" + String.valueOf(i), "");
			if( strTitle.equals("") )
			{
				if( i == 0 )
				{
					title[i] = "デフォルトのブザー音";
				}
				else
				{
					title[i] = "なし";
				}
			}
			else
			{
				title[i] = strTitle;
			}
		}
		
		img01 = (ImageView)findViewById(R.id.imageView1);
		img02 = (ImageView)findViewById(R.id.imageView2);
		img01.setScaleType(ImageView.ScaleType.FIT_CENTER);
		img02.setScaleType(ImageView.ScaleType.FIT_CENTER);
		img01.setImageResource(R.drawable.d4);
		img02.setImageResource(R.drawable.d0);
		
		button40Start = (Button)findViewById(R.id.button40Start);
		button40Reset = (Button)findViewById(R.id.button40Reset);
		button40Start.setVisibility(View.GONE);
		button40Reset.setVisibility(View.GONE);
		n40 = 40000;
		Get4025Format(n40);
		countdowntimer40 = new MyCountDownTimer40(n40, 1);//dummy
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radio40 = (RadioButton) findViewById(R.id.radio40);
		radio25 = (RadioButton) findViewById(R.id.radio25);
		
		button40Start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				countdowntimer40 = new MyCountDownTimer40(n40+1000, 1);
				countdowntimer40.start();
			};
		});
		
		button40Reset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				countdowntimer40.cancel();
				if( radio40.isChecked() )
				{
					n40 = 40000;
					img01.setImageResource(R.drawable.d4);
					img02.setImageResource(R.drawable.d0);
				}
				else
				{
					n40 = 25000;
					img01.setImageResource(R.drawable.d2);
					img02.setImageResource(R.drawable.d5);
				}
				Get4025Format(n40);
			};
		});
		
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton radioButton = (RadioButton) findViewById(checkedId);
				
				countdowntimer40.cancel();
				
				StopMusic();
				
				bTimer = false;
				
				n40 = Integer.parseInt(radioButton.getText().toString()) * 1000;
				Get4025Format(n40);
			};
		});
	}

	// オプションメニュー
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch ( item.getItemId() )
		{
			case R.id.menu_settings:
			{
				Intent intent = new Intent( MainActivity.this, SettingActivity.class );
				
				intent.putExtra("parent", this.getIntent());
				
				for( int i=0; i<40; i++ )
				{
					intent.putExtra("uri" + String.valueOf(i), uri[i]);
					intent.putExtra("title" + String.valueOf(i), title[i]);
				}
				
				startActivityForResult( intent , requestSETTING );
				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		// 設定画面から戻ってきた時
		if( requestCode == requestSETTING )
		{
    		SharedPreferences pref = getPreferences(MODE_PRIVATE);
            Editor editor = pref.edit();
            
			String str = new String();
			
	    	for( int i=0; i<40; i++)
	    	{
				// 設定値を反映させます
	    		try{
	    			uri[i] = data.getParcelableExtra("uri"+String.valueOf(i));
	    			if( uri[i] != null )
	    			{
	    	            editor.putString("uri"+String.valueOf(i), uri[i].toString() );
	    	            editor.commit();
	    			}
	    		} catch (NullPointerException e) {
	    		}
	    		
				str = "";
	    		try{
	    			str = data.getStringExtra("title"+String.valueOf(i));
	    			if( !str.equals("") )
	    			{
	    				title[i] = str;
	    	            editor.putString("title"+String.valueOf(i), str );
	    	            editor.commit();
	    			}
	    		} catch (NullPointerException e) {
	    		}
	    		
	    		// プレイヤーも準備
				if( uri[i] != null )
				{
					player[i] = MediaPlayer.create(getBaseContext(), uri[i]);
					
					try {
						player[i].setLooping(false);
					} catch (NullPointerException e) {
						
					}
				}
				else
				{
					player[i] = GetDefaultMusic(i);
				}
	    	}
	    	
	    }
    }

	public class MyCountDownTimer extends CountDownTimer{
		  
	    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
	        super(millisInFuture, countDownInterval);
	     }
	  
	    @Override
	    public void onFinish() {
	        // カウントダウン完了後に呼ばれる
	    	textGameClock.setText("00:00");
	    }
	  
	    @Override
	    public void onTick(long millisUntilFinished) {
	    	nGameClock = millisUntilFinished - 1000;
	    	textGameClock.setText(GetTimerFormat(millisUntilFinished));
	    }
	}
	
	public class MyCountDownTimer40 extends CountDownTimer{
		  
	    public MyCountDownTimer40(long millisInFuture, long countDownInterval) {
	        super(millisInFuture, countDownInterval);
	     }
	  
	    @Override
	    public void onFinish() {
	        // カウントダウン完了後に呼ばれる
	    	img01.setImageResource(R.drawable.d0);
	    	img02.setImageResource(R.drawable.d0);
	    	
	    }
	  
	    @Override
	    public void onTick(long millisUntilFinished) {
	    	n40 = millisUntilFinished - 1000;
	    	Get4025Format(millisUntilFinished);
	    }
	}
	
	public String GetTimerFormat(long millisec)
	{
    	Long nMin = (millisec)/1000/60;
    	Long nSec = (millisec)/1000%60;
    	String strTimer = "";
    	strTimer = String.format("%1$02d:%2$02d", nMin, nSec);
		return strTimer;
	}
	
	public void Get4025Format(long millisec)
	{
    	int nSec = (int) ((millisec)/1000%60);

    	// 音を鳴らす処理
		try{
			if( nSec == 0 ||
				nSec == 38 ||
				nSec == 35
				)
			{
				StopMusic();
				player[nSec].seekTo(0);
				player[nSec].start();
			}
			
		} catch (NullPointerException e){
			
		}
    	
    	String strTimer = "";
    	strTimer = String.format("%1$02d", nSec);
    	
		String str10;
		String str1;
		
		str10 = strTimer.substring(0, 1);
		str1 = strTimer.substring(1, 2);
		
		if( str10.equals("0") ){img01.setImageResource(R.drawable.d0);}
		if( str10.equals("1") ){img01.setImageResource(R.drawable.d1);}
		if( str10.equals("2") ){img01.setImageResource(R.drawable.d2);}
		if( str10.equals("3") ){img01.setImageResource(R.drawable.d3);}
		if( str10.equals("4") ){img01.setImageResource(R.drawable.d4);}
		if( str10.equals("5") ){img01.setImageResource(R.drawable.d5);}
		if( str10.equals("6") ){img01.setImageResource(R.drawable.d6);}
		if( str10.equals("7") ){img01.setImageResource(R.drawable.d7);}
		if( str10.equals("8") ){img01.setImageResource(R.drawable.d8);}
		if( str10.equals("9") ){img01.setImageResource(R.drawable.d9);}
		
		if( str1.equals("0") ){img02.setImageResource(R.drawable.d0);}
		if( str1.equals("1") ){img02.setImageResource(R.drawable.d1);}
		if( str1.equals("2") ){img02.setImageResource(R.drawable.d2);}
		if( str1.equals("3") ){img02.setImageResource(R.drawable.d3);}
		if( str1.equals("4") ){img02.setImageResource(R.drawable.d4);}
		if( str1.equals("5") ){img02.setImageResource(R.drawable.d5);}
		if( str1.equals("6") ){img02.setImageResource(R.drawable.d6);}
		if( str1.equals("7") ){img02.setImageResource(R.drawable.d7);}
		if( str1.equals("8") ){img02.setImageResource(R.drawable.d8);}
		if( str1.equals("9") ){img02.setImageResource(R.drawable.d9);}
		    	
	}
}
