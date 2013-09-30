package com.game.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.game.sudoku.utils.Constants;
import com.game.sudoku.utils.SudokuSharedPreferences;

public class SettingsActivity extends Activity {

	Spinner timeBetweenMovesSpinner = null;
	SudokuSharedPreferences sudokuSharedPref = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.settings);

		sudokuSharedPref = SudokuSharedPreferences
				.getSudokuSharedPreferences(getApplicationContext());

		timeBetweenMovesSpinner = (Spinner) findViewById(R.id.timeBetweenMovesSpinner);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.max_time_allowed_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeBetweenMovesSpinner.setAdapter(adapter);

		// Setting default item as selected

		setDefaultHighlighted();

		timeBetweenMovesSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						handleSpinnerSelectedItem(id);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// Nothing to do here.
					}
				});
	}

	private void setDefaultHighlighted() {
		int val = 0;
		val = sudokuSharedPref.getInt(Constants.MAX_TIME_BETWEEN_MOVES);

		switch (val) {
		case 5:
			timeBetweenMovesSpinner.setSelection(0);
			break;
		case 10:
			timeBetweenMovesSpinner.setSelection(1);
			break;
		case 15:
			timeBetweenMovesSpinner.setSelection(2);
			break;
		case 20:
			timeBetweenMovesSpinner.setSelection(3);
			break;
		default:
			timeBetweenMovesSpinner.setSelection(2);
			break;
		}
	}

	private void handleSpinnerSelectedItem(long _id) {
		int id = (int) _id;
		switch (id) {
		case 0:
			sudokuSharedPref.putInt(Constants.MAX_TIME_BETWEEN_MOVES, 5);
			break;
		case 1:
			sudokuSharedPref.putInt(Constants.MAX_TIME_BETWEEN_MOVES, 10);
			break;
		case 2:
			sudokuSharedPref.putInt(Constants.MAX_TIME_BETWEEN_MOVES, 15);
			break;
		case 3:
			sudokuSharedPref.putInt(Constants.MAX_TIME_BETWEEN_MOVES, 20);
			break;

		}
	}

}
