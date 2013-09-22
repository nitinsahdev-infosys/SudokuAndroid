package com.game.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
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

	String EasyUnsolvedPuzzle = "3###1#78#5#1####9#####56###17#82#5#####"
			+ "##########1492#4#####98925####8376######45";

	String easyUnsolvedPuzzle1 = "3##914785581237694479856231179823546462"
			+ "759813583614927431657298925148376768392145";
	String mediumUnsolvedPuzzle = "#6###########7694##9856####79823######627"
			+ "#98#########274316##29##25##8##676###214#";
	String complexUnsolvedPuzzle = "####14#####12#7########6231##98####646##"
			+ "5########14#2##31#####89#####37##683##1#5";

	String easySolvedPuzzle = "3629147855812376944798562311798235464627"
			+ "59813583614927431657298925148376768392145";
	String mediumSolvedPuzzle = "3629147855812376944798562311798235464627"
			+ "59813583614927431657298925148376768392145";
	String complexSolvedPuzzle = "3629147855812376944798562311798235464627"
			+ "59813583614927431657298925148376768392145";

	String puzzleArray = "";

	String savePuzzleArray = "";

	private long startTime = 0L;
	long timeInMilliseconds = 0L;
	int seconds = 0;
	int minutes = 0;
	
	double avgTimePerMove = 0.0f;
	int numberOfMoves = 0;
	
	private TextView timerTextView = null;
	private Handler customHandler = new Handler();
	public static String timerVal = "";
	boolean isResuming = false;

	private SudokuSharedPreferences sudokuSharedPreferences = null;

	/*
	 * totalTime average time between entering values
	 */
	boolean isActivityInFocus = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoku_puzzle);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		int selectPuzzleID = bundle.getInt("ID");
		sudokuSharedPreferences = SudokuSharedPreferences
				.getSudokuSharedPreferences(this);

		// to set timer
		timerTextView = (TextView) findViewById(R.id.TextViewTimer);
		startTime = SystemClock.uptimeMillis();
		
		
		setPuzzle(selectPuzzleID);
		setPuzzleTimer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
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
		customHandler.postDelayed(updateTimerThread, 0);
	}

	/**
	 * 
	 * @param v
	 *            : view
	 */
	public void onInputPanelClicked(View v) {
		if (v.equals(findViewById(R.id.InputPanelClear))) {
			setValueInCell("");
		} else {
			setValueInCell(((Button) v).getText());
		}
	}

	/**
	 * Set the selected puzzle
	 * 
	 * @param selectedComplexity
	 */
	public void setPuzzle(int selectedComplexity) {
		StringBuilder strBuilder = new StringBuilder();
		switch (selectedComplexity) {
		case 0:
			for (int i = 0; i < easyUnsolvedPuzzle1.length(); i++) {
				strBuilder.append(easyUnsolvedPuzzle1.charAt(i));
			}
			sudokuSharedPreferences.putString(Constants.LOADED_UNSOLVED_PUZZLE,
					easyUnsolvedPuzzle1);
			sudokuSharedPreferences.putString(Constants.LOADED_PUZZLE_SOLUTION,
					easySolvedPuzzle);
			sudokuSharedPreferences.putString(Constants.PUZZLE_ARRAY,
					strBuilder.toString());
			sudokuSharedPreferences.putBoolean(Constants.IS_PUZZLE_SAVED, false);
			fillPuzzle(
					sudokuSharedPreferences.getString(Constants.PUZZLE_ARRAY),
					false);

			break;
		case 1:
			for (int i = 0; i < mediumUnsolvedPuzzle.length(); i++) {
				strBuilder.append(mediumUnsolvedPuzzle.charAt(i));
			}
			sudokuSharedPreferences.putString(Constants.LOADED_UNSOLVED_PUZZLE,
					mediumUnsolvedPuzzle);
			sudokuSharedPreferences.putString(Constants.LOADED_PUZZLE_SOLUTION,
					mediumSolvedPuzzle);
			sudokuSharedPreferences.putString(Constants.PUZZLE_ARRAY,
					strBuilder.toString());
			sudokuSharedPreferences.putBoolean(Constants.IS_PUZZLE_SAVED, false);
			fillPuzzle(
					sudokuSharedPreferences.getString(Constants.PUZZLE_ARRAY),
					false);

			break;
		case 2:
			for (int i = 0; i < complexUnsolvedPuzzle.length(); i++) {
				strBuilder.append(complexUnsolvedPuzzle.charAt(i));
			}
			sudokuSharedPreferences.putString(Constants.LOADED_UNSOLVED_PUZZLE,
					complexUnsolvedPuzzle);
			sudokuSharedPreferences.putString(Constants.LOADED_PUZZLE_SOLUTION,
					complexSolvedPuzzle);
			sudokuSharedPreferences.putString(Constants.PUZZLE_ARRAY,
					strBuilder.toString());
			sudokuSharedPreferences.putBoolean(Constants.IS_PUZZLE_SAVED, false);
			fillPuzzle(
					sudokuSharedPreferences.getString(Constants.PUZZLE_ARRAY),
					false);

			break;

		case 3:
			resumePuzzle();
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

		sudokuSharedPreferences.putString(Constants.SUBMITTED_PUZZLE_NAME,
				sb.toString());
		computeResult();
	}

	void computeResult() {
		if (sudokuSharedPreferences.getString(Constants.SUBMITTED_PUZZLE_NAME)
				.equals(sudokuSharedPreferences
						.getString(Constants.LOADED_PUZZLE_SOLUTION))) {
			Toast.makeText(this,
					getString(R.string.puzzleCompletedSuccess) + timerVal,
					Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, getString(R.string.unsuccessful_attempt),
					Toast.LENGTH_SHORT).show();
		}
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
						char ch = textView.getText().charAt(0);

						if (ch == '\0') {
							savePuzzleArray += '#';
						} else {
							savePuzzleArray += textView.getText();
						}
					}
				}
			}
		}

		sudokuSharedPreferences.putString(Constants.SAVED_PUZZLE,
				savePuzzleArray);
		sudokuSharedPreferences.putBoolean(Constants.IS_PUZZLE_SAVED, true);
		
		//Save the timer values.
		sudokuSharedPreferences.putInt(Constants.TIMER_MINUTES, minutes);
		sudokuSharedPreferences.putInt(Constants.TIMER_SECONDS, seconds);
	}

	public void resumePuzzle() {
		if (sudokuSharedPreferences.getBoolean(Constants.IS_PUZZLE_SAVED)) {

			String unsolvedPuzzle = sudokuSharedPreferences
					.getString(Constants.LOADED_UNSOLVED_PUZZLE);
			String savedPuzzle = sudokuSharedPreferences
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

		resumeTimerValue();
	}

	private void resumeTimerValue() {
		minutes = sudokuSharedPreferences.getInt(Constants.TIMER_MINUTES);
		seconds = sudokuSharedPreferences.getInt(Constants.TIMER_SECONDS);
		
		timeInMilliseconds = ((minutes * 60) + seconds ) * 1000;
		startTime -= timeInMilliseconds;
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
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.alertmsg);

		// Add the buttons
		builder.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Add code for Yes
						savePuzzle();
						SudokuPuzzleActivity.this.finish();
					}
				});
		builder.setNegativeButton(R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						SudokuPuzzleActivity.this.finish();
					}
				});

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();

	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {
			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
			
			seconds = (int) (timeInMilliseconds / 1000);
			minutes = seconds / 60;
			seconds = seconds % 60;

			timerVal = String.format("%02d", minutes) + ":"
					+ String.format("%02d", seconds);
			timerTextView.setText(timerVal);
			customHandler.postDelayed(this, 0);
		}
	};

}
