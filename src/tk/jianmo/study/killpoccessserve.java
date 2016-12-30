package tk.jianmo.study;

import android.app.*;
import android.content.*;
import android.os.*;
import java.util.*;

public class killpoccessserve extends Service
{
	Context context;
	@Override
	public IBinder onBind(Intent p1)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public void onCreate()
	{
		context = this;
		
		//Handler线程，接收timertask的message,用于重启应用。
		final Handler h = new Handler(new Handler.Callback(){
				public boolean handleMessage(Message msg)
				{
					ActivityManager am=(ActivityManager)context.getSystemService(context.ACTIVITY_SERVICE);
					List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1); 
					ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0); 
					ComponentName topActivity =runningTaskInfo.topActivity; 
					String packageName =topActivity.getPackageName(); 
				
					if(packageName.equals("tk.jianmo.study")){//判断是否是本应用
	}
					else{
						//不是本应用，启动应用，并kill掉之前的应用
						Intent intent=new Intent();
						intent.setClass(context,MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						am.killBackgroundProcesses(packageName);
					}
					return false;
				}
			});
		Timer timer;
		TimerTask timertask;
		timer = new Timer();
		timertask = new TimerTask(){
			@Override
			public void run()
			{
				h.obtainMessage().sendToTarget();

			}
		};

		timer.schedule(timertask, 0, 150);//0.15秒启动一次timertask,无延迟


	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		// TODO: Implement this method
		super.onStart(intent, startId);


	}
	@Override
	public void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
}
