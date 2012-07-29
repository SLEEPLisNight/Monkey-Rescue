package com.junyangwang.monkeyrescue;

import com.junyangwang.monkeyrescue.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Game extends Activity implements OnClickListener{
	
	public int highestLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		try {
			Database entry = new Database(this);
			entry.open();
			//entry.createEntry(20, 1);
			highestLevel = entry.getHighestLevel();
			entry.close();
		} catch (Exception e){
			
		}
		
		//set up all the ImageButtons and TextViews in TableRow
		for(int i = 0; i <= highestLevel + 1; i++){
			
			ImageButton ib = new ImageButton(this);
			ib.setId(i);
			ib.setImageResource(R.drawable.monkey3);
			ib.setOnClickListener(this);
			
			TextView tv = new TextView(this);
			tv.setGravity(Gravity.CENTER);
			tv.setText("lvl:" + i);
			
			LinearLayout ll = new LinearLayout(this);
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setPadding(10, 10, 0, 0);
			ll.addView(ib);
			ll.addView(tv);
			
			if (i < 8){
				TableRow tr = (TableRow)findViewById(R.id.tableRow1);
				tr.addView(ll);
			} else if (i < 16){
				TableRow tr2 = (TableRow)findViewById(R.id.tableRow2);
				tr2.addView(ll);
			} else if (i < 24){
				TableRow tr3 = (TableRow)findViewById(R.id.tableRow3);
				tr3.addView(ll);
			} else if (i < 32){
				TableRow tr4 = (TableRow)findViewById(R.id.tableRow4);
				tr4.addView(ll);
			}
		}
		
		//set up the deleteAll Button
		Button bDeleteAllLevels = (Button)findViewById(R.id.bDeleteAllLevels);
		bDeleteAllLevels.setOnClickListener(this);
	
	}

	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		for(int i = 0; i <= highestLevel + 1; i++){
			if (arg0.getId() == i){
				setContentView(new GameView(this, i));
			}
		}
		
		//Button bDeleteAllLevels
		if (arg0.getId() == R.id.bDeleteAllLevels){
			try {
				Database entry = new Database(this);
				entry.open();
				entry.deleteAllLevels();
				entry.close();
				this.finish();
			} catch (Exception e){
				
			}
		}
	}
	

}
