package com.game.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.game.sudoku.R.id;
import com.game.sudoku.utils.Constants;
import com.game.sudoku.utils.SudokuSharedPreferences;

public class SudokuPuzzleActivity extends Activity {

	private boolean isHighlighted = false;
	private TextView previousSelected = null;

	String easyUnsolvedPuzzle = "581792364672843591439651782438256179957184326"
			+ "2169738458459136272197684353675241##";
	// String easyUnsolvedPuzzle = "###7##36#67##4###14#9#5#7##43###6#7#9##1"
	// + "#4##6#1#9###45##5#1#6#72#9#6##35#67#24###";
	String mediumUnsolvedPuzzle = "#2#89##7#4#76#1#85###4#3#2#45#9##6#2##3#4"
			+ "####8####7#94##9#48####1#7##8#2#8##31###";
	String complexUnsolvedPuzzle = "###6###9#2##5#1#347##4###6##2##7##5##9#6"
			+ "8#7#3##7##5#28#1#2#8#6#3######1##74###2##";

	String easySolvedPuzzle = "581792364672843591439651782438256179957184326"
			+ "216973845845913627219768435367524198";
	String mediumSolvedPuzzle = "12689537443762198595847312645798361219324657"
			+ "8862517394269548731314769852785231649";
	String complexSolvedPuzzle = "4356821972695718347814935628263749511956827"
			+ "43347915628519248763326957418874136259";

	String puzzleArray = "";
	String savePuzzleArray = "";

	private long startTime = 0L;
	long timeInMilliseconds = 0L;
	int seconds = 0;
	int minutes = 0;

	float avgTimePerMove = 0.0f;
	int numberOfMoves = 0;
	long timeOfLastMove = 0;

	private TextView timerTextView = null;
	private TextView levelTextView = null;
	private Handler customHandler = new Handler();
	public static String timerVal = "";
	boolean isResuming = false;
	int selectPuzzleID;

	private SudokuSharedPreferences sudokuSharedPrefs = null;

	/*
	 * totalTime average time between entering values
	 */
	boolean isActivityInFocus = false;
	boolean isTimeOverDialogShown = false;
	boolean shouldTimerThreadRun = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoku_puzzle);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		if (bundle.size() > 0)
			selectPuzzleID = bundle.getInt("ID");

		isTimeOverDialogShown = false;
		shouldTimerThreadRun = true;
		sudokuSharedPrefs = SudokuSharedPreferences
				.getSudokuSharedPreferences(this);

		// to set timer
		timerTextView = (TextView) findViewById(R.id.TextViewTimer);
		startTime = SystemClock.uptimeMillis();
		if (selectPuzzleID >= 0 && selectPuzzleID < 3) {
			String[] array = getResources().getStringArray(R.array.strArray);
			sudokuSharedPrefs.putString(Constants.SAVED_PUZZLE_LEVEL,
					array[selectPuzzleID]);
			setPuzzleLevel();
		}

		setPuzzle(selectPuzzleID);
		setPuzzleTimer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(getApplicationContext(),
					SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_scorecard:

			saveScoreCardValues();

			Intent intent1 = new Intent(getApplicationContext(),
					ScorecardActivity.class);
			startActivity(intent1);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void saveScoreCardValues() {
		sudokuSharedPrefs.putInt(Constants.TIMER_MINUTES, minutes);
		sudokuSharedPrefs.putInt(Constants.TIMER_SECONDS, seconds);
		sudokuSharedPrefs.putFloat(Constants.AVERAGE_TIME_PER_MOVE,
				avgTimePerMove);
		sudokuSharedPrefs.putInt(Constants.NUMBER_OF_MOVES, numberOfMoves);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		setPuzzleTimer();
		super.onResume();
	}

	/**
	 * start the thread
	 */
	private void setPuzzleTimer() {
		runOnUiThread(updateTimerThread);
		// customHandler.postDelayed(updateTimerThread, 0);
		sudokuSharedPrefs.putLong(Constants.LAST_MOVE_TIME,
				System.currentTimeMillis());
	}

	/**
	 * This method handles the input from the inputpanel at the bottom of the
	 * puzzle screen. In addition this method also update the time of the last
	 * move and stores it in the sudoku shared preferences.
	 * 
	 * @param v
	 *            : view who's click events is to be handled.
	 */
	public void onInputPanelClicked(View v) {
		if (v.equals(findViewById(R.id.InputPanelClear))) {
			setValueInCell("");
		} else {
			setValueInCell(((Button) v).getText());
			numberOfMoves++;
		}

		timeOfLastMove = System.currentTimeMillis();
		sudokuSharedPrefs.putLong(Constants.LAST_MOVE_TIME, timeOfLastMove);
	}

	private void setPuzzleLevel() {
		// to set level of complexity on screen
		levelTextView = (TextView) findViewById(R.id.TextViewLevel);
		levelTextView.setText(getString(R.string.level)
				+ sudokuSharedPrefs.getString(Constants.SAVED_PUZZLE_LEVEL));
	}

	/**
	 * This method loads the required puzzle into the puzzle screen. This is the
	 * same method which will be called when the puzzle will be resumed.
	 * 
	 * @param selectedComplexity
	 */
	public void setPuzzle(int selectedComplexity) {
		StringBuilder strBuilder = new StringBuilder();
		switch (selectedComplexity) {
		case 0:
			for (int i = 0; i < easyUnsolvedPuzzle.length(); i++) {
				strBuilder.append(easyUnsolvedPuzzle.charAt(i));
			}
			sudokuSharedPrefs.putString(Constants.LOADED_UNSOLVED_PUZZLE,
					easyUnsolvedPuzzle);
			sudokuSharedPrefs.putString(Constants.LOADED_PUZZLE_SOLUTION,
					easySolvedPuzzle);
			sudokuSharedPrefs.putString(Constants.PUZZLE_ARRAY,
					strBuilder.toString());
			sudokuSharedPrefs.putBoolean(Constants.IS_PUZZLE_SAVED, false);
			fillPuzzle(sudokuSharedPrefs.getString(Constants.PUZZLE_ARRAY),
					false);

			break;
		case 1:
			for (int i = 0; i < mediumUnsolvedPuzzle.length(); i++) {
				strBuilder.append(mediumUnsolvedPuzzle.charAt(i));
			}
			sudokuSharedPrefs.putString(Constants.LOADED_UNSOLVED_PUZZLE,
					mediumUnsolvedPuzzle);
			sudokuSharedPrefs.putString(Constants.LOADED_PUZZLE_SOLUTION,
					mediumSolvedPuzzle);
			sudokuSharedPrefs.putString(Constants.PUZZLE_ARRAY,
					strBuilder.toString());
			sudokuSharedPrefs.putBoolean(Constants.IS_PUZZLE_SAVED, false);
			fillPuzzle(sudokuSharedPrefs.getString(Constants.PUZZLE_ARRAY),
					false);

			break;
		case 2:
			for (int i = 0; i < complexUnsolvedPuzzle.length(); i++) {
				strBuilder.append(complexUnsolvedPuzzle.charAt(i));
			}
			sudokuSharedPrefs.putString(Constants.LOADED_UNSOLVED_PUZZLE,
					complexUnsolvedPuzzle);
			sudokuSharedPrefs.putString(Constants.LOADED_PUZZLE_SOLUTION,
					complexSolvedPuzzle);
			sudokuSharedPrefs.putString(Constants.PUZZLE_ARRAY,
					strBuilder.toString());
			sudokuSharedPrefs.putBoolean(Constants.IS_PUZZLE_SAVED, false);
			fillPuzzle(sudokuSharedPrefs.getString(Constants.PUZZLE_ARRAY),
					false);

			break;

		case 3:
			resumePuzzle();
			break;
		}

	}

	/**
	 * 
	 * to fill puzzle
	 */
	private void fillPuzzle(String puzzleArray, Boolean isResuming) {
		TableLayout sudukoTablelayout = (TableLayout) findViewById(id.sudukoTablelayout);

		int x = 0;

		for (int i = 0; i < sudukoTablelayout.getChildCount(); i++) {

			TableRow tableRow = (TableRow) sudukoTablelayout.getChildAt(i);
			int rowCount = tableRow.getChildCount();

			for (int j = 0; j < rowCount; j++) {

				TableLayout tableLayout = (TableLayout) tableRow.getChildAt(j);
				int rowCountSubTable = tableLayout.getChildCount();

				for (int k = 0; k < rowCountSubTable; k++) {

					TableRow innerMostTableRow = (TableRow) tableLayout
							.getChildAt(k);
					int rowCountInnerMostRow = innerMostTableRow
							.getChildCount();

					for (int n = 0; n < rowCountInnerMostRow; n++) {
						TextView textView = (TextView) innerMostTableRow
								.getChildAt(n);

						char charAtx = puzzleArray.charAt(x);
						if (charAtx == '#') {
							charAtx = '\0';
							textView.setText("" + charAtx);
						} else if (charAtx == '!') {
							// Ignore this char and continue.
						} else {
							if (!isResuming) {
								textView.setBackgroundColor(Color.rgb(171, 169,
										170));
								textView.setEnabled(false);
							}
							textView.setText("" + charAtx);
						}
						x++;
					}
				}
			}
		}
	}

	/**
	 * 
	 * to get the filled puzzle
	 */
	private void getPuzzle() {
		TableLayout sudukoTablelayout = (TableLayout) findViewById(id.sudukoTablelayout);

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < sudukoTablelayout.getChildCount(); i++) {

			TableRow tableRow = (TableRow) sudukoTablelayout.getChildAt(i);
			int rowCount = tableRow.getChildCount();

			for (int j = 0; j < rowCount; j++) {

				TableLayout tableLayout = (TableLayout) tableRow.getChildAt(j);
				int rowCountSubTable = tableLayout.getChildCount();

				for (int k = 0; k < rowCountSubTable; k++) {

					TableRow innerMostTableRow = (TableRow) tableLayout
							.getChildAt(k);
					int rowCountInnerMostRow = innerMostTableRow
							.getChildCount();

					for (int n = 0; n < rowCountInnerMostRow; n++) {
						TextView textView = (TextView) innerMostTableRow
								.getChildAt(n);

						if (textView.getText().equals('\0')) {
							Toast.makeText(this, R.string.incompletePuzzle,
									Toast.LENGTH_SHORT).show();
						} else {
							sb.append(textView.getText());
						}
					}
				}
			}
		}

		sudokuSharedPrefs.putString(Constants.SUBMITTED_PUZZLE_NAME,
				sb.toString());
	}

	void computeResult() {
		if (sudokuSharedPrefs.getString(Constants.SUBMITTED_PUZZLE_NAME)
				.equals(sudokuSharedPrefs
						.getString(Constants.LOADED_PUZZLE_SOLUTION))) {

			calculateScore();
			clearOldGameData();
		} else {
			Toast.makeText(this, getString(R.string.unsuccessful_attempt),
					Toast.LENGTH_SHORT).show();
		}
	}

	private void clearOldGameData() {
		sudokuSharedPrefs.putInt(Constants.TIMER_MINUTES, 0);
		sudokuSharedPrefs.putInt(Constants.TIMER_SECONDS, 0);
		sudokuSharedPrefs.putFloat(Constants.AVERAGE_TIME_PER_MOVE, 0.0f);
		sudokuSharedPrefs.putInt(Constants.NUMBER_OF_MOVES, 0);
		sudokuSharedPrefs.putLong(Constants.LAST_MOVE_TIME, 0l);
		sudokuSharedPrefs.putInt(Constants.TOTAL_PUZZLE_TIME, 0);
		sudokuSharedPrefs.putString(Constants.LOADED_UNSOLVED_PUZZLE, "");
		sudokuSharedPrefs.putString(Constants.LOADED_PUZZLE_SOLUTION, "");
		sudokuSharedPrefs.putString(Constants.PUZZLE_ARRAY, "");
		sudokuSharedPrefs.putString(Constants.SAVED_PUZZLE, "");
		sudokuSharedPrefs.putString(Constants.SUBMITTED_PUZZLE_NAME, "");
		sudokuSharedPrefs.putBoolean(Constants.IS_PUZZLE_SAVED, false);
	}

	/**
	 * To handle click events to be shown on puzzle table
	 * 
	 * @param view
	 */
	private void getCellClicked(View selectedView) {
		TableLayout sudukoTablelayout = (TableLayout) findViewById(id.sudukoTablelayout);

		for (int i = 0; i < sudukoTablelayout.getChildCount(); i++) {
			// Rows of main table like mainTableRow1, mainTableRow2,
			// mainTableRow3
			TableRow tableRow = (TableRow) sudukoTablelayout.getChildAt(i);
			int rowCount = tableRow.getChildCount();

			for (int j = 0; j < rowCount; j++) {
				// inside Table layouts for one 3*3 grid like R1C1, R1C2, R1C3
				TableLayout tableLayout = (TableLayout) tableRow.getChildAt(j);
				int rowCountSubTable = tableLayout.getChildCount();

				for (int k = 0; k < rowCountSubTable; k++) {
					// individuals rows of each table row inside inner table
					// layout like R1C1.
					TableRow innerMostTableRow = (TableRow) tableLayout
							.getChildAt(k);
					int rowCountInnerMostRow = innerMostTableRow
							.getChildCount();

					for (int n = 0; n < rowCountInnerMostRow; n++) {

						TextView textView = (TextView) innerMostTableRow
								.getChildAt(n);

						if (selectedView.equals(textView)) {
							highlightSelected(textView);
							return;
						}
					}
				}
			}
		}
	}

	private void savePuzzle() {
		TableLayout sudukoTablelayout = (TableLayout) findViewById(id.sudukoTablelayout);

		for (int i = 0; i < sudukoTablelayout.getChildCount(); i++) {

			TableRow tableRow = (TableRow) sudukoTablelayout.getChildAt(i);
			int rowCount = tableRow.getChildCount();

			for (int j = 0; j < rowCount; j++) {

				TableLayout tableLayout = (TableLayout) tableRow.getChildAt(j);
				int rowCountSubTable = tableLayout.getChildCount();

				for (int k = 0; k < rowCountSubTable; k++) {

					TableRow innerMostTableRow = (TableRow) tableLayout
							.getChildAt(k);
					int rowCountInnerMostRow = innerMostTableRow
							.getChildCount();

					for (int n = 0; n < rowCountInnerMostRow; n++) {
						TextView textView = (TextView) innerMostTableRow
								.getChildAt(n);

						// If the field was empty, store '#' instead.
						if (textView.getText().length() > 0) {
							savePuzzleArray += textView.getText();
						} else {
							savePuzzleArray += '#';
						}

						// if (ch == '\0') {
						//
						// } else {
						// savePuzzleArray += textView.getText();
						// }
					}
				}
			}
		}

		sudokuSharedPrefs.putString(Constants.SAVED_PUZZLE, savePuzzleArray);
		sudokuSharedPrefs.putBoolean(Constants.IS_PUZZLE_SAVED, true);

		// Save the timer values.
		sudokuSharedPrefs.putInt(Constants.TIMER_MINUTES, minutes);
		sudokuSharedPrefs.putInt(Constants.TIMER_SECONDS, seconds);
		sudokuSharedPrefs.putFloat(Constants.AVERAGE_TIME_PER_MOVE,
				avgTimePerMove);
		sudokuSharedPrefs.putInt(Constants.NUMBER_OF_MOVES, numberOfMoves);
	}

	public void resumePuzzle() {
		if (sudokuSharedPrefs.getBoolean(Constants.IS_PUZZLE_SAVED)) {

			String unsolvedPuzzle = sudokuSharedPrefs
					.getString(Constants.LOADED_UNSOLVED_PUZZLE);
			String savedPuzzle = sudokuSharedPrefs
					.getString(Constants.SAVED_PUZZLE);

			String resumedPuzzle = "";
			StringBuilder sb = new StringBuilder();

			// Load the unsolved puzzle, to get the fixed values in place.
			fillPuzzle(unsolvedPuzzle, false);

			// Load the saved puzzle but keeping the prefixed values unchanged.

			for (int i = 0; i < savedPuzzle.length(); i++) {

				if (unsolvedPuzzle.charAt(i) != savedPuzzle.charAt(i)) {
					sb.append(savedPuzzle.charAt(i));
				} else if ((unsolvedPuzzle.charAt(i) == savedPuzzle.charAt(i))
						&& unsolvedPuzzle.charAt(i) != '#') {
					sb.append('!');
				} else {
					sb.append('#');
				}
			}
			resumedPuzzle = sb.toString();
			fillPuzzle(resumedPuzzle, true);
		}
		setPuzzleLevel();
		resumeTimerValue();
	}

	private void resumeTimerValue() {
		minutes = sudokuSharedPrefs.getInt(Constants.TIMER_MINUTES);
		seconds = sudokuSharedPrefs.getInt(Constants.TIMER_SECONDS);

		timeInMilliseconds = ((minutes * 60) + seconds) * 1000;
		startTime -= timeInMilliseconds;

		avgTimePerMove = sudokuSharedPrefs
				.getFloat(Constants.AVERAGE_TIME_PER_MOVE);
		numberOfMoves = sudokuSharedPrefs.getInt(Constants.NUMBER_OF_MOVES);
	}

	public void highlightSelected(View v) {
		if (isHighlighted) {
			if (!(v.getBackground().equals(Color.WHITE))) {
				if (previousSelected != null) {
					previousSelected
							.setBackgroundResource(R.drawable.rounded_edges);
					previousSelected = (TextView) v;
					v.setBackgroundResource(R.drawable.selected_rounded_edges);
				}
			} else {
				return;
			}
		} else {
			previousSelected = (TextView) v;
			v.setBackgroundResource(R.drawable.selected_rounded_edges);
			isHighlighted = true;
		}
	}

	private void setValueInCell(CharSequence val) {
		if (isHighlighted) {
			if (previousSelected != null) {
				previousSelected.setText(val);
			}
		}
	}

	public void onCellClicked(View v) {
		getCellClicked(v);
	}

	public void onSubmitPuzzle(View v) {
		// TODO: Stop the timer.

		getPuzzle();
		computeResult();
	}

	public void calculateScore() {

		saveScoreCardValues();
		shouldTimerThreadRun = false;

		String totalTimeStr = String.format("%02d",
				sudokuSharedPrefs.getInt(Constants.TIMER_MINUTES))
				+ ":"
				+ String.format("%02d",
						sudokuSharedPrefs.getInt(Constants.TIMER_SECONDS));
		int score = Constants.MAX_SCORE - minutes;

		String successMsg = getString(R.string.puzzleCompletedSuccess)
				+ totalTimeStr + " minutes";
		successMsg += "\n\n" + "Your score is : " + score;

		showDialogMessage(getString(R.string.Tekedge), successMsg, "Ok");
	}

	private void showDialogMessage(String title, String msg, String buttonText) {

		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				SudokuPuzzleActivity.this);
		builder1.setMessage(msg);
		builder1.setTitle(title);

		// Add the neutral button
		builder1.setNeutralButton(buttonText,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						SudokuPuzzleActivity.this.finish();
						return;
					}
				});

		// Create the AlertDialog
		AlertDialog dialog = builder1.create();
		dialog.show();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder2 = new AlertDialog.Builder(
				SudokuPuzzleActivity.this);
		builder2.setMessage(R.string.alertmsg);

		// Add the buttons
		builder2.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) { 
						// Add code for Yes
						savePuzzle();
						dialog.dismiss();
						SudokuPuzzleActivity.this.finish();
					}
				});
		builder2.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						SudokuPuzzleActivity.this.finish();
					}
				});

		// Create the AlertDialog 
		AlertDialog dialog = builder2.create();
		dialog.show();

	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {

			if (shouldTimerThreadRun) {
				timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

				long secsFromMillis = (int) (timeInMilliseconds / 1000);
				minutes = (int) secsFromMillis / 60;
				seconds = (int) secsFromMillis % 60;

				timerVal = String.format("%02d", minutes) + ":"
						+ String.format("%02d", seconds);
				timerTextView.setText(timerVal);
				customHandler.postDelayed(this, 0);

				if (numberOfMoves > 0)
					avgTimePerMove = secsFromMillis / numberOfMoves;

				Long maxInactiveTime = Long.valueOf(sudokuSharedPrefs
						.getInt(Constants.MAX_TIME_BETWEEN_MOVES) * 60 * 1000);

				Long lastMoveTime = sudokuSharedPrefs
						.getLong(Constants.LAST_MOVE_TIME);

				Long systemTime = System.currentTimeMillis();

				// Enter this condition only once in an attempt where the user
				// has not done any inputs for more than the time set in the
				// settings.
				if (isTimeOverDialogShown == false
						&& systemTime.compareTo(lastMoveTime + maxInactiveTime) > 0) {

					isTimeOverDialogShown = true;

					AlertDialog.Builder builder3 = new AlertDialog.Builder(
							SudokuPuzzleActivity.this);
					builder3.setMessage(getString(R.string.timeIsOver));
					builder3.setTitle(getString(R.string.Tekedge));
					builder3.setNeutralButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialogInterface,
										int arg1) {
									dialogInterface.dismiss();
									SudokuPuzzleActivity.this.finish();
								}
							});

					// Create the AlertDialog
					AlertDialog dialog = builder3.create();
					dialog.show();
				}
			}
		}
	};
}
