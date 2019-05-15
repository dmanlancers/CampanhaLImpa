package com.transparencia.campanha_limpa.formularios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.transparencia.campanha_limpa.R;
import com.transparencia.campanha_limpa.CampanhaLimpa;
import com.transparencia.campanha_limpa.BD2.DBAdapter;
import com.transparencia.campanha_limpa.enviarFoto.UploadFotoBrinde;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class IdBrinde extends Activity {

	private Spinner spinner_camaras, spinner_distrito,spinner_listas;
	private Button btnSubmit;
	private EditText nome_brinde;
	private String[] distritos;
	private String[] camaras;
	private String[] listas;
	DBAdapter myDb = new DBAdapter(IdBrinde.this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_brindes);

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

		spinner_distrito = (Spinner) findViewById(R.id.spinner_distrito);
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
		spinner_distrito.setAdapter(dataAdapter);
		spinner_distrito.setPrompt("Distritos");
		spinner_distrito.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				addItemsOnSpinner3();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	public void addItemsOnSpinner3() {

		spinner_camaras = (Spinner) findViewById(R.id.spinner_camaras);
		List<String> list = new ArrayList<String>();
		list.add("Municípios");
		DBAdapter myDB1 = new DBAdapter(getBaseContext()); 
		myDB1.openDataBase();
		Cursor cursor = myDB1.getConcelhosByDistrito(spinner_distrito.getSelectedItemId());
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(2)); 
			} while (cursor.moveToNext());
		}

		myDB1.close();
		camaras = (String []) list.toArray (new String [list.size ()]);

		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,camaras);
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_camaras.setAdapter(dataAdapter1);
		spinner_camaras.setPrompt("Municípios");
		spinner_camaras.setOnItemSelectedListener(new OnItemSelectedListener() {

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

		spinner_listas = (Spinner) findViewById(R.id.spinner_listas);
		List<String> list = new ArrayList<String>();
		list.add("Listas");
		DBAdapter myDB2 = new DBAdapter(getBaseContext()); 
		myDB2.openDataBase();
		Cursor cursor = myDB2.getListasByCamara(spinner_camaras.getSelectedItemId());
		if (cursor.moveToFirst()) { 
			do {
				list.add(cursor.getString(2)); 
			} while (cursor.moveToNext());
		}

		myDB2.close();
		listas = (String []) list.toArray (new String [list.size ()]);

		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listas);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_listas.setAdapter(dataAdapter2);
		spinner_listas.setPrompt("Listas");
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//RemoveNotification();
			//StopGpsManager();
			Intent home = new Intent(this, CampanhaLimpa.class);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	//get the selected dropdown list value 
	public void addListenerOnButton() {

		spinner_camaras		= (Spinner) findViewById(R.id.spinner_camaras);
		spinner_distrito	= (Spinner) findViewById(R.id.spinner_distrito);
		spinner_listas		= (Spinner) findViewById(R.id.spinner_listas);
		nome_brinde			= (EditText) findViewById(R.id.nome_brinde);
		btnSubmit			= (Button) findViewById(R.id.btnSubmit);

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String ID_dtr= String.valueOf(spinner_distrito.getSelectedItemId());
				String ID_Conc= String.valueOf(spinner_camaras.getSelectedItemId()-1);
				String ID_Lis= String.valueOf(spinner_listas.getSelectedItem());
				String nome_brindes = nome_brinde.getText().toString();			

				Log.d("Distrito", ID_dtr); 
				Log.d("Concelho", ID_Conc);
				Log.d("Lista", ID_Lis);
				Log.d("Nome", nome_brindes);
				UploadFotoBrinde ID_D= new UploadFotoBrinde();
				ID_D.Distritos(ID_dtr);
				UploadFotoBrinde ID_C= new UploadFotoBrinde();
				ID_C.Concelhos(ID_Conc);
				UploadFotoBrinde ID_L= new UploadFotoBrinde();
				ID_L.Listas(ID_Lis);
				UploadFotoBrinde Nome_Brinde= new UploadFotoBrinde();
				Nome_Brinde.Brindes(nome_brindes);

				if(Integer.parseInt(ID_dtr) == 0){
					Toast.makeText(IdBrinde.this, "Selecione um distrito",Toast.LENGTH_SHORT).show();
				}else if(Integer.parseInt(ID_Conc) == 0){
					Toast.makeText(IdBrinde.this, "Selecione um município",Toast.LENGTH_SHORT).show();
				}else if(ID_Lis == "Listas"){
					Toast.makeText(IdBrinde.this, "Selecione uma lista",Toast.LENGTH_SHORT).show();
				}else if(nome_brindes.isEmpty()){
					Toast.makeText(IdBrinde.this, "Coloque um nome para o brinde",Toast.LENGTH_SHORT).show();
				}else{
					startActivity(new Intent(IdBrinde.this,UploadFotoBrinde.class));
				}	 
			}

		});



	}

}