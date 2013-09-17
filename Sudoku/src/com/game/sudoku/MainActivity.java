package com.game.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*TableLayout sudokuLayout = (TableLayout) findViewById(R.layout.activity_main);
		int count = sudokuLayout.getChildCount();*/	
//		System.out.println(count+"");
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onInputPanelClicked(View v) {
		
		if (v.equals(findViewById(R.id.InputPanelValue01))) {
			Toast.makeText(this, "1 Clicked...", Toast.LENGTH_SHORT).show();
		} else if (v.equals(findViewById(R.id.InputPanelValue02))) {
			Toast.makeText(this, "2 Clicked...", Toast.LENGTH_SHORT).show();
		} else if (v.equals(findViewById(R.id.InputPanelValue03))) {
			Toast.makeText(this, "3 Clicked...", Toast.LENGTH_SHORT).show();
		} else if (v.equals(findViewById(R.id.InputPanelValue04))) {
			Toast.makeText(this, "4 Clicked...", Toast.LENGTH_SHORT).show(); 
		} else if (v.equals(findViewById(R.id.InputPanelValue05))) {
			Toast.makeText(this, "5 Clicked...", Toast.LENGTH_SHORT).show();
		} else if (v.equals(findViewById(R.id.InputPanelValue06))) {
			Toast.makeText(this, "6 Clicked...", Toast.LENGTH_SHORT).show();
		} else if (v.equals(findViewById(R.id.InputPanelValue07))) {
			Toast.makeText(this, "7 Clicked...", Toast.LENGTH_SHORT).show();
		} else if (v.equals(findViewById(R.id.InputPanelValue08))) {
			Toast.makeText(this, "8 Clicked...", Toast.LENGTH_SHORT).show();
		} else if (v.equals(findViewById(R.id.InputPanelValue09))) {
			Toast.makeText(this, "9 Clicked...", Toast.LENGTH_SHORT).show();
		}
	}

}
