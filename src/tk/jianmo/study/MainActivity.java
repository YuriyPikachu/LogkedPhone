package tk.jianmo.study;

import android.annotation.SuppressLint;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.view.View.*;
import android.text.*;
import android.view.InputQueue.*;

public class MainActivity extends Activity
{
	Intent intent;
	Context context;
	long usedTime=0,newTime=0;
	int keyTouthInt=0;
	TextView tv_time;
	SharedPreferences sp;
	Timer timer;
	TimerTask timertask;
	int theBeginTimeToFinish=1 * 60 * 60 * 24;//总的时间，以秒为单位
	int timetofinish=theBeginTimeToFinish;
    public void onCreate(Bundle savedInstanceState)   
	{
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//4.0以上锁定home键
		getWindow().setFlags(0x80000000, 0x80000000);
		//全屏不能在代码中设置，在AndroidManifest.xml中
        setContentView(R.layout.main);
		context = this;

		//获取控件
		tv_time = (TextView)super.findViewById(R.id.mainTextViewTime);
		
		//启动service
		intent = new Intent();
		intent.setClass(MainActivity.this, killpoccessserve.class);
		startService(intent);

		sp = getSharedPreferences("TimeSave", MODE_PRIVATE);
		
		//得到保存的时间
		timetofinish = sp.getInt("saveTime", timetofinish);
		//如果时间小于1s，则恢复原值
		if (timetofinish <= 1)
		{
			timetofinish = theBeginTimeToFinish;
		}
		timer = new Timer();
		
		timertask = new TimerTask(){
			@Override
			public void run()
			{
				runOnUiThread(new Runnable(){

						@Override
						public void run()
						{
							// TODO: Implement this method
							int hour,minute,second;
							hour = timetofinish / (1 * 60 * 60);//获取剩余的小时数
							minute = (timetofinish % (1 * 60 * 60)) / (1 * 60);//分钟数
							second = timetofinish % (1 * 60);//秒
							tv_time.setText(hour + "时" + minute + "分" + second + "秒后清除手机所有数据!");
							sp.edit().putInt("saveTime", timetofinish).commit();//保存，在程序退出或重启后能恢复
							if (timetofinish == 0)//判断剩余时间是否为0，为0退出程序
							{
								stopService(intent);
								System.exit(0);
							}
							timetofinish--;	//时间自减1S
						}
					});
			}
		};
		timer.schedule(timertask, 0, 1000);//一秒重复一次
	}

	@SuppressLint("NewApi")
	@Override
	//监听各种按键
	//keytouch和onkeydown中包含强制解锁代码
	//解锁方法:两次back→音量下→音量上→back→home→输入密码
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == event.KEYCODE_BACK)
		{
			if (keyTouthInt == 0)
			{
				usedTime = SystemClock.currentThreadTimeMillis();
				keyTouthInt = 1;
				usedTime = System.currentTimeMillis();
			}
			else if (keyTouthInt == 1)
			{
				keytouch(usedTime, keyTouthInt, 1);
			}
			else
			{
				keytouch(usedTime, keyTouthInt, 4);
			}
		}
		if (keyCode == event.KEYCODE_HOME)
		{
			keytouch(usedTime, keyTouthInt, 5);
			if (keyTouthInt == 6)
			{
				MyDialogFragment f = new MyDialogFragment();
				f.show(getFragmentManager(), "mydialog");
			}
		}
		if (keyCode == event.KEYCODE_MENU)
		{
			keytouch(usedTime, keyTouthInt, 100);
		}
		if (keyCode == event.KEYCODE_VOLUME_DOWN)
		{
			keytouch(usedTime, keyTouthInt, 2);
		}
		if (keyCode == event.KEYCODE_VOLUME_UP)
		{
			keytouch(usedTime, keyTouthInt, 3);
		}
		if (keyCode == event.KEYCODE_POWER)
		{
			Toast.makeText(MainActivity.this, "解锁加QQ2364800612！", Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	/*private Object getFragmentManager() {
		// TODO Auto-generated method stub
		return null;
	}*/

	//这个方法是锁定home键，4.0下可用，4.0上对部分机型可用.
    public void onAttachedToWindow()
  	{       
		//this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);   
		super.onAttachedToWindow(); 
	} 

	//对话框
	@SuppressLint({ "ValidFragment", "NewApi" })
	class MyDialogFragment extends DialogFragment
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			final EditText edit=new EditText(context);
			edit.setHint("兮颜火爆收徒!");
			builder.setView(edit);
			builder.setTitle("输入密码！！");
			builder.setMessage("解锁请加QQ2364800612!");
			builder.setPositiveButton("解锁", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						String getnumber=edit.getText().toString();
						//if (getnumber.equals("兮颜收徒50"))//判断密码是否正确
						{
							stopService(intent);
							System.exit(0);
						}	
					}
				});
			builder.setNegativeButton("我是傻逼", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface p1, int p2)
					{
					}
				});
			return builder.create();
		}
	}

	
	public void keytouch(long useTim, int keyTouthIn, int n)
	{
		newTime = System.currentTimeMillis();
		if ((newTime - useTim) <= 2000 && keyTouthIn == n)
		{
			usedTime = newTime;
			keyTouthInt = keyTouthIn + 1;
		}
		else
		{
			keyTouthInt = 0;
		}
	}
}
	

	





