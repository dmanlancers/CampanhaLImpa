package com.transparencia.campanha_limpa.formularios;

import java.io.IOException; 
import java.util.ArrayList;
import java.util.List;

import com.transparencia.campanha_limpa.R;
import com.transparencia.campanha_limpa.CampanhaLimpa;
import com.transparencia.campanha_limpa.BD2.DBAdapter;
import com.transparencia.campanha_limpa.enviarFoto.UploadFotoComicio;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



public class IdComicio extends Activity {

	private Spinner spinner_camaras1, spinner_distrito1,spinner_listas1;
	private Button btnSubmit;
	private EditText nome_comicio;
	private String[] distritos;
	private String[] camaras;
	private String[] listas;
	DBAdapter myDb = new DBAdapter(IdComicio.this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_comicios);
		openDB();
		addItemsOnSpinner2();
		addListenerOnButton();

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

	//add items into spinner dynamically
	public void addItemsOnSpinner2() {

		spinner_distrito1 = (Spinner) findViewById(R.id.spinner_distrito1);
		List<String> list = new ArrayList<String>();
		list.add("Distritos");
		DBAdapter myDB = new DBAdapter(getBaseContext());
		myDB.openDataBase();
		Cursor cursor = myDB.getAllDistritos();
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(1)); 
			} while (cursor.moveToNext());
		}

		myDB.close();
		distritos = (String []) list.toArray (new String [list.size ()]);

		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,distritos);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_distrito1.setAdapter(dataAdapter);
		spinner_distrito1.setPrompt("Distritos");
		spinner_distrito1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				addItemsOnSpinner3();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void addItemsOnSpinner3() {

		spinner_camaras1 = (Spinner) findViewById(R.id.spinner_camaras1);
		List<String> list = new ArrayList<String>();
		list.add("Municípios");
		DBAdapter myDB1 = new DBAdapter(getBaseContext()); 
		myDB1.openDataBase();
		Cursor cursor = myDB1.getConcelhosByDistrito(spinner_distrito1.getSelectedItemId());
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(2)); 
			} while (cursor.moveToNext());
		}

		myDB1.close();
		camaras = (String []) list.toArray (new String [list.size ()]);

		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,camaras);
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_camaras1.setAdapter(dataAdapter1);
		spinner_camaras1.setPrompt("Municípios");
		spinner_camaras1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				addItemsOnSpinner4();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void addItemsOnSpinner4() {

		spinner_listas1 = (Spinner) findViewById(R.id.spinner_listas1);
		List<String> list = new ArrayList<String>();
		list.add("Listas");
		DBAdapter myDB2 = new DBAdapter(getBaseContext()); 
		myDB2.openDataBase();
		Cursor cursor = myDB2.getListasByCamara(spinner_camaras1.getSelectedItemId());
		if (cursor.moveToFirst()) { 
			do {
				list.add(cursor.getString(2)); 
			} while (cursor.moveToNext());
		}

		myDB2.close();
		listas = (String []) list.toArray (new String [list.size ()]);

		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listas);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_listas1.setAdapter(dataAdapter2);
		spinner_listas1.setPrompt("Listas");
	}

	//get the selected dropdown list value

	public void addListenerOnButton() {

		spinner_camaras1	= (Spinner) findViewById(R.id.spinner_camaras1);
		spinner_distrito1	= (Spinner) findViewById(R.id.spinner_distrito1);
		spinner_listas1		= (Spinner) findViewById(R.id.spinner_listas1);
		nome_comicio		= (EditText) findViewById(R.id.nome_comicio);
		btnSubmit			= (Button) findViewById(R.id.btnSubmit);

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String ID_dtr= String.valueOf(spinner_distrito1.getSelectedItemId());
				String ID_Conc= String.valueOf(spinner_camaras1.getSelectedItemId());
				String ID_Lis= String.valueOf(spinner_listas1.getSelectedItem());
				String nome_comicios = nome_comicio.getText().toString();				

				Log.d("Distrito", ID_dtr); 
				Log.d("Concelho", ID_Conc);
				Log.d("Lista", ID_Lis);
				Log.d("Nome", nome_comicios);
				UploadFotoComicio ID_D= new UploadFotoComicio();
				ID_D.Distritos(ID_dtr);
				UploadFotoComicio ID_C= new UploadFotoComicio();
				ID_C.Concelhos(ID_Conc);
				UploadFotoComicio ID_L= new UploadFotoComicio();
				ID_L.Listas(ID_Lis);
				UploadFotoComicio Nome_Comicio= new UploadFotoComicio();
				Nome_Comicio.Comicios(nome_comicios);

				if(Integer.parseInt(ID_dtr) == 0){
					Toast.makeText(IdComicio.this, "Selecione um distrito",Toast.LENGTH_SHORT).show();
				}else if(Integer.parseInt(ID_Conc) == 0){
					Toast.makeText(IdComicio.this, "Selecione um município",Toast.LENGTH_SHORT).show();
				}else if(ID_Lis == "Listas"){
					Toast.makeText(IdComicio.this, "Selecione uma lista",Toast.LENGTH_SHORT).show();
				}else if(nome_comicios.isEmpty()){
					Toast.makeText(IdComicio.this, "Coloque um nome para o acão",Toast.LENGTH_SHORT).show();
				}else{
					startActivity(new Intent(IdComicio.this,UploadFotoComicio.class));

				}	
			}

		});
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent home = new Intent(this, CampanhaLimpa.class);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}   

}