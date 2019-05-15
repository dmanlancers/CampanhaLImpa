package com.transparencia.campanha_limpa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.transparencia.campanha_limpa.R;
import com.transparencia.campanha_limpa.formularios.IdBrinde;
import com.transparencia.campanha_limpa.formularios.IdCartaz;
import com.transparencia.campanha_limpa.formularios.IdComicio;
import com.transparencia.campanha_limpa.login.Login_fotoBrinde;
import com.transparencia.campanha_limpa.login.Login_fotoCartaz;
import com.transparencia.campanha_limpa.login.Login_fotoComicio;


public class OptionsCampanhaLimpa extends Activity {
	final static int cameraData = 0;
	public static String AdicionaID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submenu_upload);

		Warning();

		final Vibrator vibe = (Vibrator) OptionsCampanhaLimpa.this.getSystemService(Context.VIBRATOR_SERVICE);


		ImageButton  cartazes = (ImageButton) findViewById(R.id.UpCartazes);
		cartazes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);


				VerificaLoginCartaz();
			}
		});



		ImageButton  brinde = (ImageButton) findViewById(R.id.UpBrindes);
		brinde.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);


				VerificaLoginBrinde();

			}
		});

		ImageButton  comicio = (ImageButton) findViewById(R.id.UpComicios);
		comicio.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				vibe.vibrate(100);
				VerificaLoginComicio();



			}
		});
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

				startActivity(new Intent(OptionsCampanhaLimpa.this,Login_fotoCartaz.class));
			}else
			{
				startActivity(new Intent(OptionsCampanhaLimpa.this,IdCartaz.class));
			}
			// return count
			return cursor.getCount();



			//db = this.getReadableDatabase();

		}
		catch (Exception e) {
			Toast.makeText(OptionsCampanhaLimpa.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();  
		}
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

				startActivity(new Intent(OptionsCampanhaLimpa.this,Login_fotoBrinde.class));
			}else
			{
				startActivity(new Intent(OptionsCampanhaLimpa.this,IdBrinde.class));
			}
			// return count
			return cursor.getCount();


			//db = this.getReadableDatabase();

		}
		catch (Exception e) {
			Toast.makeText(OptionsCampanhaLimpa.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();  
		}
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

				startActivity(new Intent(OptionsCampanhaLimpa.this,Login_fotoComicio.class));
			}else
			{
				startActivity(new Intent(OptionsCampanhaLimpa.this,IdComicio.class));
			}
			// return count
			return cursor.getCount();



			//db = this.getReadableDatabase();

		}
		catch (Exception e) {
			Toast.makeText(OptionsCampanhaLimpa.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();  
		}
		return 0;


	}
}

