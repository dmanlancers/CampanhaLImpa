package com.transparencia.campanha_limpa;


import com.transparencia.campanha_limpa.R;
import com.transparencia.campanha_limpa.Estatisticas.Estatisticas;
import com.transparencia.campanha_limpa.foto.fotoBrinde;
import com.transparencia.campanha_limpa.foto.fotoCartaz;
import com.transparencia.campanha_limpa.foto.fotoComicio;
import com.transparencia.campanha_limpa.login.Login_fotoBrinde;
import com.transparencia.campanha_limpa.login.Login_fotoCartaz;
import com.transparencia.campanha_limpa.login.Login_fotoComicio;

import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


public class CampanhaLimpa extends Activity {
	final static int cameraData = 0;
	public static String AdicionaID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.campanha_limpa);

		Warning();

		final Vibrator vibe = (Vibrator) CampanhaLimpa.this.getSystemService(Context.VIBRATOR_SERVICE);


		ImageButton  cartaz = (ImageButton) findViewById(R.id.cartazes);
		cartaz.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);

				//tartActivity(new Intent(CampanhaLimpa.this,IdCartaz.class));
				VerificaLoginCartaz();
			} 
		});



		ImageButton  brindes = (ImageButton) findViewById(R.id.brindes);
		brindes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);

				//startActivity(new Intent(CampanhaLimpa.this,IdBrinde.class));
				VerificaLoginBrinde();

			}
		});
		ImageButton  estatisticas = (ImageButton) findViewById(R.id.estatisticas);
		estatisticas.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);
				startActivity(new Intent(CampanhaLimpa.this, Estatisticas.class));
			}
		});
		ImageButton  comicios = (ImageButton) findViewById(R.id.comicios);
		comicios.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);
				VerificaLoginComicio();
				//startActivity(new Intent(CampanhaLimpa.this,IdComicio.class));


			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campanha_limpa, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.upload:
			try {
				startActivity(new Intent(CampanhaLimpa.this,OptionsCampanhaLimpa.class));
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);

		case R.id.sobre:
			try {
				startActivity(new Intent(CampanhaLimpa.this,Sobre.class));
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
		return false;
	}




	/*função que força o utilizador a ligar o GPS*/
	public void showSettingsAlert(){

		String titulo_gps = getString(R.string.titulo_gps);
		String mensagem_gps = getString(R.string.mensagem_gps);
		String definicoes_gps = getString(R.string.definicoes_gps);
		String cancelar_gps = getString(R.string.cancelar_gps);

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setTitle(titulo_gps);

		alertDialog.setMessage(mensagem_gps);

		alertDialog.setPositiveButton(definicoes_gps, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);



			}
		});

		// on pressing cancel button
		alertDialog.setNegativeButton(cancelar_gps, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();




	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {			


		}
		return super.onKeyDown(keyCode, event);
	}
	public void  Warning(){
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

		}else{
			showSettingsAlert();

		}

	}
	public void TirarFoto(){

		Intent i= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(i,cameraData);


	}
	public void adicionaId(String tagId) {
		// TODO Auto-generated method stub
		AdicionaID =tagId;
		CampanhaLimpa.AdicionaID= tagId;
	}
	private int VerificaLoginCartaz() {
		// TODO Auto-generated method stub


		SQLiteDatabase db; 
		db = openOrCreateDatabase( "login.sqlite"        , SQLiteDatabase.CREATE_IF_NECESSARY        , null          );
		try {


			final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS User("
					+ "ID INTEGER);";
			db.execSQL(CREATE_TABLE_CONTAIN);

			String sql =
					"SELECT ID FROM User";       
			//db.rawQuery(sql, null);
			Cursor cursor = db.rawQuery(sql, null);
			//cursor.close();
			if (cursor.getCount()==0){

				startActivity(new Intent(CampanhaLimpa.this,Login_fotoCartaz.class));
			}else
			{
				startActivity(new Intent(CampanhaLimpa.this,fotoCartaz.class));
			}
			cursor.close();
			// return count
			return cursor.getCount();



			//db = this.getReadableDatabase();

		}
		catch (Exception e) {
			Toast.makeText(CampanhaLimpa.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();  
		}
		db.close();
		return 0;


	}
	private int VerificaLoginBrinde() {
		// TODO Auto-generated method stub


		SQLiteDatabase db;
		db = openOrCreateDatabase( "login.sqlite"        , SQLiteDatabase.CREATE_IF_NECESSARY        , null          );
		try {


			final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS User("
					+ "ID INTEGER);";
			db.execSQL(CREATE_TABLE_CONTAIN);

			String sql =
					"SELECT ID FROM User";       
			//db.rawQuery(sql, null);
			Cursor cursor = db.rawQuery(sql, null);
			//cursor.close();
			if (cursor.getCount()==0){

				startActivity(new Intent(CampanhaLimpa.this,Login_fotoBrinde.class));
			}else
			{
				startActivity(new Intent(CampanhaLimpa.this,fotoBrinde.class));
			}
			cursor.close();
			// return count
			return cursor.getCount();


			//db = this.getReadableDatabase();

		}
		catch (Exception e) {
			Toast.makeText(CampanhaLimpa.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();  
		}
		db.close();
		return 0;


	}
	private int VerificaLoginComicio() {
		// TODO Auto-generated method stub


		SQLiteDatabase db;
		db = openOrCreateDatabase( "login.sqlite"        , SQLiteDatabase.CREATE_IF_NECESSARY        , null          );
		try {


			final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS User("
					+ "ID INTEGER);";
			db.execSQL(CREATE_TABLE_CONTAIN);

			String sql =
					"SELECT ID FROM User";       
			//db.rawQuery(sql, null);
			Cursor cursor = db.rawQuery(sql, null);
			//cursor.close();
			if (cursor.getCount()==0){

				startActivity(new Intent(CampanhaLimpa.this,Login_fotoComicio.class));
			}else
			{
				startActivity(new Intent(CampanhaLimpa.this,fotoComicio.class));
			}
			cursor.close();
			// return count
			return cursor.getCount();

			//db = this.getReadableDatabase();

		}
		catch (Exception e) {
			Toast.makeText(CampanhaLimpa.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();  
		}
		db.close();
		return 0;


	}
}