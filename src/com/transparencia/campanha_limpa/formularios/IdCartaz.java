package com.transparencia.campanha_limpa.formularios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.transparencia.campanha_limpa.R;
import com.transparencia.campanha_limpa.CampanhaLimpa;
import com.transparencia.campanha_limpa.BD2.DBAdapter;
import com.transparencia.campanha_limpa.enviarFoto.UploadFotoCartaz;

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
import android.widget.Spinner;
import android.widget.Toast;



public class IdCartaz extends Activity {

	private Spinner spinner_camaras2, spinner_distritos2, spinner_listas2, spinner8;
	private Button btnSubmit;
	private String[] distritos;
	private String[] camaras;
	private String[] listas;
	DBAdapter myDb = new DBAdapter(IdCartaz.this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_candidaturas);
		openDB();
		addItemsOnSpinner2();
		addItemsOnSpinner5();
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

	public void addItemsOnSpinner2() {

		spinner_distritos2 = (Spinner) findViewById(R.id.spinner_distrito2);
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
		spinner_distritos2.setAdapter(dataAdapter);
		spinner_distritos2.setPrompt("Distritos");
		spinner_distritos2.setOnItemSelectedListener(new OnItemSelectedListener() {

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

		spinner_camaras2 = (Spinner) findViewById(R.id.spinner_camaras2);
		List<String> list = new ArrayList<String>();
		list.add("Municípios");
		DBAdapter myDB1 = new DBAdapter(getBaseContext()); 
		myDB1.openDataBase();
		Cursor cursor = myDB1.getConcelhosByDistrito(spinner_distritos2.getSelectedItemId());
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor.getString(2)); 
			} while (cursor.moveToNext());
			}

			myDB1.close();
			camaras = (String []) list.toArray (new String [list.size ()]);
		
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,camaras);
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_camaras2.setAdapter(dataAdapter1);
		spinner_camaras2.setPrompt("Municipios");
		spinner_camaras2.setOnItemSelectedListener(new OnItemSelectedListener() {

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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			Intent home = new Intent(this, CampanhaLimpa.class);
			home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void addItemsOnSpinner4() {

		spinner_listas2 = (Spinner) findViewById(R.id.spinner_listas2);
		List<String> list = new ArrayList<String>();
		list.add("Listas");
		DBAdapter myDB2 = new DBAdapter(getBaseContext()); 
		myDB2.openDataBase();
		Cursor cursor = myDB2.getListasByCamara(spinner_camaras2.getSelectedItemId());
		if (cursor.moveToFirst()) { 
			do {
				list.add(cursor.getString(2)); 
			} while (cursor.moveToNext());
			}

			myDB2.close();
			listas = (String []) list.toArray (new String [list.size ()]);
		
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,listas);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_listas2.setAdapter(dataAdapter2);
		spinner_listas2.setPrompt("Listas");

	}
	
	public void addItemsOnSpinner5() {

		spinner8 = (Spinner) findViewById(R.id.spinner8);		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.tamanhos_arrays, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner8.setAdapter(adapter);
		spinner8.setPrompt("Tamanhos dos Cartazes");

	}
	
	
	
	/*public void addListenerOnSpinnerItemSelection(){
		
		spinner5 = (Spinner) findViewById(R.id.spinner5);
		spinner5.setOnItemSelectedListener(new CustomOnItemSelectedListener());
	}*/
	
	//get the selected dropdown list value
	
	public void addListenerOnButton() {

		spinner_camaras2 = (Spinner) findViewById(R.id.spinner_camaras2);
		spinner_distritos2 = (Spinner) findViewById(R.id.spinner_distrito2);
		spinner_listas2 = (Spinner) findViewById(R.id.spinner_listas2);
		spinner8 = (Spinner) findViewById(R.id.spinner8);

		
		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String ID_dtr= String.valueOf(spinner_distritos2.getSelectedItemId());
				String ID_Conc= String.valueOf(spinner_camaras2.getSelectedItemId()-1);
				String ID_Lis= String.valueOf(spinner_listas2.getSelectedItem());
				String ID_Cartaz= String.valueOf(spinner8.getSelectedItemId());

							
				Log.d("Distrito", ID_dtr); 
				Log.d("Conselho", ID_Conc);
				Log.d("Lista", ID_Lis);
				Log.d("Cartaz", ID_Cartaz);
				UploadFotoCartaz ID_D= new UploadFotoCartaz();
				 ID_D.Distritos(ID_dtr);
				 UploadFotoCartaz ID_C= new UploadFotoCartaz();
				 ID_C.Concelhos(ID_Conc);
				 UploadFotoCartaz ID_L= new UploadFotoCartaz();
				 ID_L.Listas(ID_Lis);
				 UploadFotoCartaz ID_Cart= new UploadFotoCartaz();
				 ID_Cart.Cartaz(ID_Cartaz);
				 
				 if(Integer.parseInt(ID_dtr) == 0){
						Toast.makeText(IdCartaz.this, "Selecione um distrito",Toast.LENGTH_SHORT).show();
					}else if(Integer.parseInt(ID_Conc) == 0){
						Toast.makeText(IdCartaz.this, "Selecione um município",Toast.LENGTH_SHORT).show();
					}else if(ID_Lis == "Listas"){
						Toast.makeText(IdCartaz.this, "Selecione uma lista",Toast.LENGTH_SHORT).show();
					}else if(Integer.parseInt(ID_Cartaz) == 0){
						Toast.makeText(IdCartaz.this, "Selecione uma dimensão de cartaz",Toast.LENGTH_SHORT).show();
					}else{
						startActivity(new Intent(IdCartaz.this,UploadFotoCartaz.class));
					}	 
			}
			
		});
	}

	     
	    
}