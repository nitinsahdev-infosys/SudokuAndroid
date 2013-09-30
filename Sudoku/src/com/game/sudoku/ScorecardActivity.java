package com.game.sudoku;

import java.util.Date;

import com.game.sudoku.utils.Constants;
import com.game.sudoku.utils.SudokuSharedPreferences;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ScorecardActivity extends Activity {

	SudokuSharedPreferences sudokuSharedPrefs = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scorecard);

		sudokuSharedPrefs = SudokuSharedPreferences
				.getSudokuSharedPreferences(getApplicationContext());

		updateScorecard();
	}

	private void updateScorecard() {
		TextView totalTimeTV, avgTimeTV, numOfMovesTV, lastMoveTV = null;
		totalTimeTV = (TextView) findViewById(R.id.totalScore);
		avgTimeTV = (TextView) findViewById(R.id.averageTime);
		numOfMovesTV = (TextView) findViewById(R.id.numberOfMoves);
		lastMoveTV = (TextView) findViewById(R.id.lastMoveTime);

		//Total time of the puzzle
		String totalTimeStr = String.format("%02d", sudokuSharedPrefs.getInt(Constants.TIMER_MINUTES)) + ":"
				+ String.format("%02d", sudokuSharedPrefs.getInt(Constants.TIMER_SECONDS));
		totalTimeTV.setText(totalTimeStr);
		
		//Average time for a single move
		int avgTimeInSecs = (int) sudokuSharedPrefs.getFloat(Constants.AVERAGE_TIME_PER_MOVE);
		int minutes = avgTimeInSecs / 60;
		int seconds = avgTimeInSecs % 60;

		String avgTimeStr = String.format("%02d", minutes) + ":"
				+ String.format("%02d", seconds);
		avgTimeTV.setText(avgTimeStr);
		
		//Total number of moves user has tried.
		numOfMovesTV.setText(""+sudokuSharedPrefs.getInt(Constants.NUMBER_OF_MOVES));
		
		//Date and time of the last move
		Date dt = new Date(sudokuSharedPrefs.getLong(Constants.LAST_MOVE_TIME));
		String dateStr = dt.toLocaleString();
		lastMoveTV.setText(dateStr);

	}

}
