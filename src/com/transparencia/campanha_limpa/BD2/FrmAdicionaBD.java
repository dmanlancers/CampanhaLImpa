package com.transparencia.campanha_limpa.BD2;


import java.io.IOException;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.transparencia.campanha_limpa.R;


/*
 * Steps to using the DB:
 * 1. [DONE] Instantiate the DB Adapter
 * 2. [DONE] Open the DB
 * 3. [DONE] use get, insert, delete, .. to change data.
 * 4. [DONE]Close the DB
 */

/**
 * Demo application to show how to use the 
 * built-in SQL lite database.
 */
public class FrmAdicionaBD extends Activity {
	
	/*DBAdapter myDb;*/
	DBAdapter myDb = new DBAdapter(FrmAdicionaBD.this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bd);
		openDB();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		closeDB();
	}


	private void openDB() {
		try {
			  
         	myDb.createDataBase();
  
  	} catch (IOException ioe) {
  
  		throw new Error("Não foi possível criar a base de dados");
  
  	}
  
  	try {
  
  		myDb.openDataBase();
  
  	}catch(SQLException sqle){
  
  		throw sqle;
  
  	}
	}
	private void closeDB() {
		myDb.close();
	}

	
	
	private void displayText(String message) {
        TextView textView = (TextView) findViewById(R.id.textDisplay);
        textView.setText(message);
	}
	
	

	public void onClick_AddRecord(View v) {
		displayText("Clicked add record!");
		
		/*long newId = myDb.insertRow("Jenny", 5559, "Green");*/
		
		// Query for the record we just added.
		// Use the ID:
		/*Cursor cursor = myDb.getRow(newId);
		displayRecordSet(cursor);*/
	}

	public void onClick_ClearAll(View v) {
		displayText("Clicked clear all!");
		/*myDb.deleteAll();*/
	}

	public void onClick_DisplayRecords(View v) {
		displayText("Clicked display record!");
		
		Cursor cursor = myDb.getListasByCamara(2);
		displayRecord(cursor);
	}
	
	private void displayRecord(Cursor cursor) {
		String message = "";
		// populate the message from the cursor
		 
		// Reset cursor to start, checking to see if there's data:
		if (cursor.moveToFirst()) {
			do {
				// Process the data: 
				int id = cursor.getInt(0);

				int id2 = cursor.getInt(1);
				String nome = cursor.getString(2);
				
				// Append data to the message:
				message += "id=" + id
						   +", id_camara=" + id2
						   +", nome=" + nome
						   +"\n";
			} while(cursor.moveToNext());
		}
		
		// Close the cursor to avoid a resource leak.
		cursor.close();
		
		displayText(message);
	}
}










