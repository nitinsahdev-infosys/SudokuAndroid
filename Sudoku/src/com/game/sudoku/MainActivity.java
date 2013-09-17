package com.game.sudoku;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.game.sudoku.R.id;

public class MainActivity extends Activity {
	
	private boolean isHighlighted = false;
	private TextView previousSelected = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onInputPanelClicked(View v) {
		String msg = "";
		if (v.equals(findViewById(R.id.InputPanelValue01))) {
			msg = "1 Clicked...";
		} else if (v.equals(findViewById(R.id.InputPanelValue02))) {
			//SetValueInCell(2);
			msg = "2 Clicked...";
		} else if (v.equals(findViewById(R.id.InputPanelValue03))) {
			msg = "3 Clicked...";
		} else if (v.equals(findViewById(R.id.InputPanelValue04))) {
			msg = "4 Clicked...";
		} else if (v.equals(findViewById(R.id.InputPanelValue05))) {
			msg = "5 Clicked...";
		} else if (v.equals(findViewById(R.id.InputPanelValue06))) {
			msg = "6 Clicked...";
		} else if (v.equals(findViewById(R.id.InputPanelValue07))) {
			msg = "7 Clicked...";
		} else if (v.equals(findViewById(R.id.InputPanelValue08))) {
			msg = "8 Clicked...";
		} else if (v.equals(findViewById(R.id.InputPanelValue09))) {
			msg = "9 Clicked...";
		}
		SetValueInCell( ((TextView)v).getText() );
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private void CheckClicked(View v) {		
		TableLayout sudukoTablelayout = (TableLayout) findViewById(id.sudukoTablelayout);

		for (int i = 0; i < sudukoTablelayout.getChildCount(); i++) {

			TableRow tableRow = (TableRow) sudukoTablelayout.getChildAt(i);
			int rowCount =  tableRow.getChildCount();
			
			for (int j = 0; j < rowCount; j++) {
				
				TableLayout tableLayout = (TableLayout) tableRow.getChildAt(j);
				int rowCountSubTable = tableLayout.getChildCount();
				
				for (int k = 0; k < rowCountSubTable; k++) {
					
					TableRow innerMostTableRow = (TableRow) tableLayout.getChildAt(k);
					int rowCountInnerMostRow = innerMostTableRow.getChildCount();
					
					for (int n = 0; n < rowCountInnerMostRow; n++) {
						TextView textView = (TextView) innerMostTableRow
														.getChildAt(n);
						
						if ( v.equals(textView)){
							CheckHighlighted(textView);
						}	
					}
				}
			}
		}
	}
	
	public void CheckHighlighted(View v) {
		if ( isHighlighted ) {
			if( !(v.getBackground().equals(Color.WHITE)) ){
				if ( previousSelected != null) {
					previousSelected.setBackgroundColor(Color.rgb(220, 220, 220));
					previousSelected = (TextView)v;
					v.setBackgroundColor(Color.WHITE);
				}
			} else {
				return;
			}
		} else {
			previousSelected = (TextView)v;
			v.setBackgroundColor(Color.WHITE);
			isHighlighted = true;
		}
	}
	
	private void SetValueInCell(CharSequence val) {
		if( isHighlighted ) {
			if( previousSelected != null ){
				previousSelected.setText(val);
			}
		}
	}
	
	public void OnCellClicked(View v) {
		CheckClicked(v);
	}
}
