

package com.transparencia.campanha_limpa.foto;

import java.io.File;
import java.io.FileOutputStream;

import com.transparencia.campanha_limpa.R;
import com.transparencia.campanha_limpa.CampanhaLimpa;
import com.transparencia.campanha_limpa.formularios.IdBrinde;
import com.transparencia.campanha_limpa.login.Login_fotoBrinde;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class fotoBrinde extends Activity {


	
	

	
	private Bitmap bmp;
	private File myImage;
	final static int cameraData = 0;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Warning();
		//StartGpsManager();
		TirarFoto();
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			
			bmp = (Bitmap) extras.get("data");

			File storagePath = new File(Environment.getExternalStorageDirectory() + "/Campanha_Limpa/Brindes/");
			storagePath.mkdirs();

			String nome = Long.toString(System.currentTimeMillis());

			myImage = new File(storagePath, nome+".jpg");
			/*String caminhofoto = ("/Campanha_Limpa/Cartazes/"+nome+".jpg");*/

			//-------data e hora atual----------------
		
			

			Bitmap b = Bitmap.createScaledBitmap(bmp, 2560, 1920, false);

			try {
				FileOutputStream out = new FileOutputStream(myImage);
				b.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
				startActivity(new Intent(fotoBrinde.this,Login_fotoBrinde.class));
				
				
				
				startActivity(new Intent(fotoBrinde.this,IdBrinde.class));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
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
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				
				Intent home = new Intent(this, CampanhaLimpa.class);
				home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(home);

				return true;
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

	public void showSettingsAlertInternet(){

		String titulo_gps = getString(R.string.titulo_internet);
		String mensagem_gps = getString(R.string.mensagem_internet);
		String definicoes_gps = getString(R.string.definicoes_internet);
		String cancelar_gps = getString(R.string.cancelar_internet);

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		alertDialog.setTitle(titulo_gps);

		alertDialog.setMessage(mensagem_gps);

		alertDialog.setPositiveButton(definicoes_gps, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
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



	//Verifica Internet

	public void  WarningInternet(){
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {


		}else{
			showSettingsAlertInternet();

		}

	}

	public void TirarFoto(){

		Intent i= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(i,cameraData);

	}

	
}
