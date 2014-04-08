package com.example.android.actionbarcompat.services.task;

import android.os.Handler;

import com.example.android.actionbarcompat.R;
import com.example.android.actionbarcompat.services.util.AbstractRunnable;

public class FactoryCommand {
	
	public static AbstractRunnable getCommand(Handler handler, int res_id){
		
		if(R.id.uno == res_id)
			return new Uno(handler);
		else if(R.id.due == res_id)
			return new Due(handler);
		else if(R.id.tre == res_id)
			return new Tre(handler);
//		else if(R.id.play == res_id)
//			return new Play(handler);
//		else if(R.id.clear == res_id)
//			return new ClearAll(handler);
		
		return null;
	}

}
