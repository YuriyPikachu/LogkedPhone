package tk.jianmo.study;
import android.content.*;
import android.widget.*;


public class BootBroadcastReceiver extends BroadcastReceiver
{
	String action_boot="android.intent.action.BOOT_COMPLETED";
	
	@Override
	public void onReceive(Context context, Intent intent)
	{

		/*因为手机刷了MIUI，我不知道能不能自启，如果不能，删除注释符号。*/

//		if (intent.getAction().equals(action_boot)){
		Intent ootStartIntent=new Intent(context, MainActivity.class);
		ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(ootStartIntent);
//		}

	} }
