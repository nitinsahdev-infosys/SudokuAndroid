package com.game.sudoku;

import com.game.sudoku.utils.Constants;
import com.game.sudoku.utils.SudokuSharedPreferences;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainMenuActivity extends Activity {
	public final String ACTION_VAL = null;

	public SudokuSharedPreferences sudokuSharedPreferences = null;

	String[] strArray = { "Easy", "Medium", "Hard" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		sudokuSharedPreferences = SudokuSharedPreferences.getSudokuSharedPreferences(this);
	}
	
	@Override
	protected void onResume() {
		if ( !sudokuSharedPreferences.getBoolean(Constants.IS_PUZZLE_SAVED) ) {
			findViewById(R.id.resumePuzzle).setEnabled(false);
		} else {
			findViewById(R.id.resumePuzzle).setEnabled(true);
		}
		
		super.onResume();
	}

	/**
	 * On handle Buttons click
	 * 
	 * @param v
	 */
	public void OnButtonClicked(View v) {
		Intent intent = null;
		if (v.equals(findViewById(R.id.newPuzzle))) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			// Add the buttons
			builder.setSingleChoiceItems(strArray, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							Intent intent = new Intent(getApplicationContext(),
									SudokuPuzzleActivity.class);
							Bundle bundle = new Bundle();
							bundle.putInt("ID", id);
							intent.putExtras(bundle);
							startActivity(intent);
							Toast.makeText(getApplicationContext(),
									"message " + id, Toast.LENGTH_SHORT).show();
							dialog.cancel();
						}
					});
			// Create the AlertDialog
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		if (v.equals(findViewById(R.id.resumePuzzle))) {
			Toast.makeText(getApplicationContext(),
					"Resume ", Toast.LENGTH_SHORT).show();
			
			// Id for resume puzzle...
			int id = 3;
			
			Intent resumeIntent = new Intent(getApplicationContext(),
					SudokuPuzzleActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("ID", id);
			resumeIntent.putExtras(bundle);
			startActivity(resumeIntent);
			
			Toast.makeText(getApplicationContext(),
					"message " + id, Toast.LENGTH_SHORT).show();
		
		}
		if (v.equals(findViewById(R.id.about))) {
			Toast.makeText(getApplicationContext(),
					"about ", Toast.LENGTH_SHORT).show();
			intent = new Intent(getApplicationContext(), AboutActivity.class);
			startActivity(intent);
		}
		if (v.equals(findViewById(R.id.help))) {
			Toast.makeText(getApplicationContext(),
					"help ", Toast.LENGTH_SHORT).show();
			
			intent = new Intent(getApplicationContext(), HelpActivity.class);
			startActivity(intent);
		}
		if (v.equals(findViewById(R.id.settings))) {
			Toast.makeText(getApplicationContext(),
					"settings ", Toast.LENGTH_SHORT).show();
			
			intent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(intent);
		}

	}
}
