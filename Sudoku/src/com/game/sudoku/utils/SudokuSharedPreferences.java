package com.game.sudoku.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SudokuSharedPreferences{

	private Context ctx = null;
	private SharedPreferences sharedPreferences = null;
	private SharedPreferences.Editor spEditor = null;
	public static SudokuSharedPreferences sudokuSharedPreferences = null;
	
	
	private SudokuSharedPreferences(Context _ctx) {
		ctx = _ctx;
		sharedPreferences = ctx.getSharedPreferences("SudokuSharedPreferences", Context.MODE_PRIVATE);
		spEditor = sharedPreferences.edit();
	}
	
	
	public static SudokuSharedPreferences getSudokuSharedPreferences(Context ctx) {
		if( sudokuSharedPreferences != null) {
			return sudokuSharedPreferences;
		} else {
			sudokuSharedPreferences = new SudokuSharedPreferences(ctx); 
			return sudokuSharedPreferences;
		}
	}
	
	public Boolean putInt(String key, int val) {
		spEditor.putInt(key, val);
		return spEditor.commit();
	}
	
	public int getInt(String key) {
		return sharedPreferences.getInt(key, 0);
	}
	
	public Boolean putString(String key, String val) {
		spEditor.putString(key, val);
		return spEditor.commit();
	}
	
	public String getString(String key) {
		return sharedPreferences.getString(key, "");
	}
	
	public Boolean putBoolean(String key, Boolean val) {
		spEditor.putBoolean(key, val);
		return spEditor.commit();
	}
	
	public Boolean getBoolean(String key) {
		return sharedPreferences.getBoolean(key, false);
	}	
}