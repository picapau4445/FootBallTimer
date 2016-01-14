package picapau.football.timer2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity  extends Activity {
	
	int requestSETTING = 1;
	
	int m_ItemCount = 0;
	ListView listview;
	ListAdapter adapter;
	
	List<String> NameList = new ArrayList<String>();
	Uri[] uri = new Uri[40];
	String[] title = new String[40];
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    // BackBtnアクション
	    if(keyCode==KeyEvent.KEYCODE_BACK)
	    {
	    	// 親intentへ値を返すために自分のintentへ設定を保管
	    	for( int i=0; i<40; i++)
	    	{
	    		try{
	    			getIntent().putExtra("uri"+String.valueOf(i), uri[i]);
	    		} catch( NullPointerException e){
	    			
	    		}
	    		
		    	getIntent().putExtra("title"+String.valueOf(i), title[i]);
	    	}
	    	
	    	setResult(RESULT_OK, getIntent());
	    	finish();
	    }
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
        listview = (ListView)findViewById( R.id.listView );
        listview.setOnItemClickListener( new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				// 音の選択
				Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				startActivityForResult(intent, arg2);
			}
		}); 
        
    	String str = new String();
    	Intent intent = getIntent();
    	
    	for( int i=0; i<40; i++)
    	{
    		try{
    			uri[i] = intent.getParcelableExtra("uri"+String.valueOf(i));
    		} catch (NullPointerException e) {
    		}
    		
			str = "";
    		try{
    			str = intent.getStringExtra("title"+String.valueOf(i));
    			if( !str.equals("") ){title[i] = str;}
    		} catch (NullPointerException e) {
    		}
    	}
    	
    	LoadData();
    	m_ItemCount = NameList.size();
    	SetList();
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if( resultCode == RESULT_OK )
		{
			Bundle bundle = data.getExtras();
		    Set<String> keys = bundle.keySet();
		    Iterator<String> itr = keys.iterator();
		    while( itr.hasNext() )
		    {
		        String key = (String) itr.next();

		        if(key.equals(RingtoneManager.EXTRA_RINGTONE_PICKED_URI))
		        {
		            Object obj = bundle.get(key);
		            uri[requestCode] = (Uri)obj;
		            
		            if( uri[requestCode] != null )
		            {
		            	// RingtoneManager
		            	Ringtone ringtone;
		            	ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri[requestCode]);
		            	title[requestCode] = ringtone.getTitle(getApplicationContext());
		            	
		            	// 音が鳴るURIか確認
		        		MediaPlayer ConfirmPlayer = MediaPlayer.create(getBaseContext(), uri[requestCode]);
		            	
		            	if( ConfirmPlayer == null )
		            	{
		            		Toast.makeText(this, "設定できません", Toast.LENGTH_SHORT).show();
		            		uri[requestCode] = null;
		            	}
		            	
		            	//再表示
		    			LoadData();
		    	        m_ItemCount = NameList.size();
		    	        SetList();
		    			adapter.notifyDataSetChanged();
		            	
		            }
		            
		            break;
		         }
		    }
		}
    }

    private void LoadData()
    {
    	NameList.clear();
    	
    	for( int i=0; i<40; i++ )
    	{
//    		if( ( i >= 0 && i<=10 ) || i == 15 || i == 20 )
//    		{
	    		try{
		    		NameList.add(title[i]);
	    		} catch (NullPointerException e) {
	    			NameList.add("なし");
	    		}
//    		}
    	}
    }
    
	private void SetList()
	{
        adapter = new ListAdapter(this, NameList);
        listview.setAdapter(adapter);
	}

	public class ListAdapter extends ArrayAdapter<String> 
	{	
        private LayoutInflater layoutInflater_;
        
        public ListAdapter(Context context, List<String> objects) 
        {
        	super(context, 0, objects);
        	layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        }
        
		@Override
        public View getView(final int position, View convertView, ViewGroup parent) 
        { 
			if ( convertView == null ) 
			{ 
				convertView = layoutInflater_.inflate(R.layout.setting_row, null);
			}
			
			final String data  = this.getItem(position);
			
			// 名前
			TextView textString; 
			textString = (TextView)convertView.findViewById(R.id.TextString); 
			textString.setText( String.valueOf(position) + "秒");
			
			// URI
			TextView textUri; 
			textUri = (TextView)convertView.findViewById(R.id.TextUri); 
			
			try{
				textUri.setText(data);
			} catch (NullPointerException e) {
				textUri.setText("");
			}

/*			
			// ID
			TextView textId;
			textId = (TextView)convertView.findViewById(R.id.Text_ID);
			textId.setText(String.valueOf(data._ID));
			
			// 編集ボタン
			ImageView imageEdit;
			imageEdit = (ImageView)convertView.findViewById(R.id.imageEdit); 
			imageEdit.setOnClickListener(new View.OnClickListener()
	        {
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent( MasterFormationViewActivity.this, picapau.football.scouting1.master.MasterFormationEditActivity.class );
					intent.putExtra(getString(R.string._id), data._ID);
					startActivityForResult( intent, 0 );
				}
			});
			
			// 削除ボタン
			ImageView imageDel;
			imageDel = (ImageView)convertView.findViewById(R.id.imageDel); 
			imageDel.setOnClickListener(new View.OnClickListener()
	        {
				@Override
				public void onClick(View v)
				{
					// 削除
					if( data._ID != 0 )
					{
						ShowDeleteDialog(data._ID);
					}
				}
			});
			
			
*/			
			
			return convertView;
        }
		
        @Override
        public int getCount() {
            return m_ItemCount;
        }
        
        @Override
        public String getItem(int position) {
            return NameList.get(position);
        }
        
        @Override
        public long getItemId(int position) {
            return position;
        }
        
	}


}
