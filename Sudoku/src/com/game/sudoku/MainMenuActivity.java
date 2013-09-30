package com.game.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.game.sudoku.utils.Constants;
import com.game.sudoku.utils.SudokuSharedPreferences;

public class MainMenuActivity extends Activity {
	public SudokuSharedPreferences sudokuSharedPreferences = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);

		sudokuSharedPreferences = SudokuSharedPreferences
				.getSudokuSharedPreferences(this);
		sudokuSharedPreferences.putInt(Constants.MAX_TIME_BETWEEN_MOVES, 15);
	}

	@Override
	protected void onResume() {
		if (!sudokuSharedPreferences.getBoolean(Constants.IS_PUZZLE_SAVED)) {
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
			builder.setSingleChoiceItems(R.array.strArray, -1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							Intent intent = new Intent(MainMenuActivity.this,
									SudokuPuzzleActivity.class);
							Bundle bundle = new Bundle();
							bundle.putInt("ID", id);
							intent.putExtras(bundle);
							startActivity(intent);
							dialog.cancel();
						}
					});
			// Create the AlertDialog
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		if (v.equals(findViewById(R.id.resumePuzzle))) {
			// Id for resume puzzle...
			int id = 3;

			Intent resumeIntent = new Intent(this, SudokuPuzzleActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("ID", id);
			resumeIntent.putExtras(bundle);
			startActivity(resumeIntent);
		}
		if (v.equals(findViewById(R.id.about))) {
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
		}
		if (v.equals(findViewById(R.id.settings))) {
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
		}

	}
}
