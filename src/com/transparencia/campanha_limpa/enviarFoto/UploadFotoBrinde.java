package com.transparencia.campanha_limpa.enviarFoto;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.transparencia.campanha_limpa.R;
import com.transparencia.campanha_limpa.CampanhaLimpa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class UploadFotoBrinde extends Activity {
	private static final int PICK_IMAGE = 1;
	private ImageView imgView;
	private Button upload;
	private Button choose;
	private EditText titulo_foto;
	private Bitmap bitmap;
	private ProgressDialog dialog;
	public static double latitude;
	public static double longitude;
	public Location location;
	public static double Lat;
	public static String IdDistritos;
	public static String IdConcelhos;
	public static String IdLista;
	public static String nomebrindes;
	public static double Lon;
	public static double TM;
	final static int cameraData = 0;
	GeneralLocationListener gpsLocationListener;
	GeneralLocationListener towerLocationListener;
	LocationManager gpsLocationManager;
	LocationManager towerLocationManager;
	private static String VarBd;

	boolean towerEnabled;
	boolean gpsEnabled;
	boolean isStarted;
	boolean isUsingGps;
	//int IdUser=IDUtilizador;
	//int IdLista=4;
	//int IdCartaz=4;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageupload);

		WarningInternet();
		VerificaLogin();
		//latitude = location.getLatitude();
		//longitude = location.getLongitude();
		RestartGpsManagers();
		imgView = (ImageView) findViewById(R.id.ImageView);
		upload = (Button) findViewById(R.id.Upload);
		choose= (Button) findViewById(R.id.choose);
		titulo_foto = (EditText) findViewById(R.id.Caption);
		upload.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (bitmap == null) {
					Toast.makeText(getApplicationContext(),
							"Escolha a foto primeiro", Toast.LENGTH_SHORT).show();
				} else {
					dialog = ProgressDialog.show(UploadFotoBrinde.this, "A enviar foto...",
							"Aguarde por favor...", true);
					new ImageUploadTask().execute();

				}

			}
		});
		choose.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				try {
					sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
					Intent intent = new Intent();
					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
					startActivityForResult(
							Intent.createChooser(intent, "Selecione foto"),
							PICK_IMAGE);
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							e.getMessage(),
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
				return ;
			}
		});

	}
	public class GeneralLocationListener implements LocationListener,
	GpsStatus.Listener {

		public void onLocationChanged(Location loc) {

			try {
				if (loc != null) {



					Latitude = loc.getLatitude();
					Longitude = loc.getLongitude();
					ImageUploadTask lat=new ImageUploadTask();
					ImageUploadTask lon=new ImageUploadTask();
					lat.setLat(Latitude);
					lon.setLong(Longitude);
					String message = String.format(

							"Current Location \n Longitude: %1$s \n Latitude: %2$s",
							loc.getLongitude(), loc.getLatitude()

							);

					Log.i("GPS", "Scanned " + message+ ":");

				}

				return;




			} catch (Exception ex) {

			}

		}

		public boolean GotLocation;
		public double Latitude;
		public void onProviderDisabled(String provider) {

			RestartGpsManagers();

		}

		public void onProviderEnabled(String provider) {

			RestartGpsManagers();
		}

		public double Longitude;


		public void onStatusChanged(String provider, int status, Bundle extras) {

		}


		public void onGpsStatusChanged(int event) {

			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX:


				break;

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:

				break;

			case GpsStatus.GPS_EVENT_STARTED:

				break;

			case GpsStatus.GPS_EVENT_STOPPED:

				break;

			}


		}


	}
	public void StopGpsManager() {

		if (towerLocationListener != null) {
			towerLocationManager.removeUpdates(towerLocationListener);

		}

		if (gpsLocationListener != null) {
			gpsLocationManager.removeUpdates(gpsLocationListener);
			gpsLocationManager.removeGpsStatusListener(gpsLocationListener);
		}
	}

	public void StartGpsManager() {



		gpsLocationListener = new GeneralLocationListener();
		towerLocationListener = new GeneralLocationListener();

		gpsLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		towerLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


		CheckTowerAndGpsStatus();

		if (gpsEnabled) {

			gpsLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,0,0,gpsLocationListener);

			gpsLocationManager.addGpsStatusListener(gpsLocationListener);

			isUsingGps = true;

		} else if (towerEnabled) {
			isUsingGps = false;

			towerLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0,
					0, towerLocationListener);

		} else {
			isUsingGps = false;

			return;
		}


	}

	private void CheckTowerAndGpsStatus() {
		towerEnabled = towerLocationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		gpsEnabled = gpsLocationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

	}

	public void RestartGpsManagers() {

		StopGpsManager();
		StartGpsManager();
	}





	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_IMAGE:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImageUri = data.getData();
				String filePath = null;

				try {
					// OI FILE Manager
					String filemanagerstring = selectedImageUri.getPath();

					// MEDIA GALLERY
					String selectedImagePath = getPath(selectedImageUri);

					if (selectedImagePath != null) {
						filePath = selectedImagePath;
					} else if (filemanagerstring != null) {
						filePath = filemanagerstring;
					} else {
						Toast.makeText(getApplicationContext(), "Unknown path",
								Toast.LENGTH_LONG).show();
						Log.e("Bitmap", "Unknown path");
					}

					if (filePath != null) {
						decodeFile(filePath);
					} else {
						bitmap = null;
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Internal error",
							Toast.LENGTH_LONG).show();
					Log.e(e.getClass().getName(), e.getMessage(), e);
				}
			}
			break;
		default:
		}
	}
	public void onLocationChanged(Location loc) {

		try {
			if (loc != null) {



				latitude = loc.getLatitude();
				longitude = loc.getLongitude();



				String message = String.format(

						"Current Location \n Longitude: %1$s \n Latitude: %2$s",
						loc.getLongitude(), loc.getLatitude()

						);

				Log.i("GPS_inicial", "Scanned " + message+ ":");

			}

			return;




		} catch (Exception ex) {

		}

	}

	class ImageUploadTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... unsued) {
			try {


				//MultipartEntity entity = new MultipartEntity(
				//HttpMultipartMode.BROWSER_COMPATIBLE);
				//ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();

				/* Fill byteArrayOutStream with data */

				StringBuilder stringBuilder = new StringBuilder();



				//stringBuilder.append("data:image/jpeg;base64,");
				//stringBuilder.append(encodedData);

				// Log the result


				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, bos);
				//byte[] data = bos.toByteArray();
				//String img_full = Base64.encodeToString(data, Base64.DEFAULT);
				String img_full = Base64.encodeToString(bos.toByteArray(),
						Base64.NO_WRAP);
				stringBuilder.append("data:image/jpeg;base64,");
				stringBuilder.append(img_full);
				android.util.Log.d("Base64Test", stringBuilder.toString());
				android.util.Log.d("imagem", img_full);
				String Latitude = Double.toString(latitude);
				String Longitude = Double.toString(longitude);
				String idUser    = VarBd;
				String distrito= IdDistritos;
				String partido    = IdLista;
				String concelho   = IdConcelhos;
				String titulofoto = titulo_foto.getText().toString();
				String nome    = nomebrindes;
				//android.util.Log.d("latitude", Latitude);
				String message = String.format(

						"Current Location \n Longitude: %1$s \n Latitude: %2$s",
						latitude, longitude

						);

				Log.i("GPS", "Scanned " + message+ ":");
				//Log.i("base", Base64.encodeToString(data, Base64.DEFAULT));
				//entity.addPart("returnformat", new StringBody("json"));
				//entity.addPart("uploaded", new ByteArrayBody(data,
				//"myImage.jpg"));
				//entity.addPart("photoCaption", new StringBody(caption.getText()
				//.toString()));
				HttpClient client = new DefaultHttpClient();  
				String postURL = "http://campanhalimpa.transparencia.pt/brinde-rest";
				HttpPost post = new HttpPost(postURL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();

				img_full="data:image/jpeg;base64,"+img_full;
				params.add(new BasicNameValuePair("img_full", img_full));
				params.add(new BasicNameValuePair("longitude", Longitude));
				params.add(new BasicNameValuePair("latitude", Latitude));
				params.add(new BasicNameValuePair("idUser", idUser));
				params.add(new BasicNameValuePair("partido", partido));
				params.add(new BasicNameValuePair("concelho", concelho));
				params.add(new BasicNameValuePair("distrito", distrito));
				params.add(new BasicNameValuePair("desc", titulofoto));
				params.add(new BasicNameValuePair("nome", nome));


				//params.add(new BasicNameValuePair("pass", "xyz"));
				UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
				post.setEntity(ent);
				HttpResponse responsePOST = client.execute(post);  
				HttpEntity resEntity = responsePOST.getEntity();  
				if (resEntity != null) {    
					Log.i("RESPONSE",EntityUtils.toString(resEntity));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;


			// (null);
		}

		public void setLong(double longitude) {
			// TODO Auto-generated method stub
			Lon = longitude;
			UploadFotoBrinde.longitude = longitude;
		}

		public void setLat(double latitude) {
			// TODO Auto-generated method stub
			Lat = latitude;
			UploadFotoBrinde.latitude = latitude;
		}

		@Override
		protected void onProgressUpdate(Void... unsued) {

		}

		@Override
		protected void onPostExecute(String sResponse) {
			try {
				if (dialog.isShowing())
					dialog.dismiss();
				concluido();

				if (sResponse != null) {
					JSONObject JResponse = new JSONObject(sResponse);
					String success = JResponse.getString("SUCCESS");
					//String message = JResponse.getString("MESSAGE");

					if (success == null) {
						//Toast.makeText(getApplicationContext(), message,
						//Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"Photo uploaded successfully",

								Toast.LENGTH_SHORT).show();
						titulo_foto.setText("");
					}
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						e.getMessage(),
						Toast.LENGTH_LONG).show();
				Log.e(e.getClass().getName(), e.getMessage(), e);
			}
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			StopGpsManager();
			super.onStop();


			System.exit(0);
			finish();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			// HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
			// THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else
			return null;
	}

	public void decodeFile(String filePath) {
		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);

		imgView.setImageBitmap(bitmap);

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
				Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
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

	public void Distritos(String iD_dtr) {
		IdDistritos=iD_dtr;
		UploadFotoBrinde.IdDistritos=iD_dtr;

	}


	public void Concelhos(String iD_Conc) {
		IdConcelhos = iD_Conc;
		UploadFotoBrinde.IdConcelhos=iD_Conc;

	}

	public void Listas(String iD_Lis) {
		IdLista = iD_Lis;
		UploadFotoBrinde.IdLista=iD_Lis;

	}

	public void Brindes(String Nome_Brindes) {
		// TODO Auto-generated method stub
		nomebrindes = Nome_Brindes;
		UploadFotoBrinde.nomebrindes =Nome_Brindes;

	}





	private int VerificaLogin() {
		// TODO Auto-generated method stub


		SQLiteDatabase db;
		db = openOrCreateDatabase( "login.sqlite"        , SQLiteDatabase.CREATE_IF_NECESSARY        , null          );
		try {


			final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS User("
					+ "ID INTEGER);";
			db.execSQL(CREATE_TABLE_CONTAIN);
			//Toast.makeText(CampanhaLimpa.this, "Fa�a a autentica�‹o ", Toast.LENGTH_LONG).show();

			String sql =
					"SELECT ID FROM User";       
			//db.rawQuery(sql, null);
			Cursor cursor = db.rawQuery(sql, null);
			//cursor.getInt(0);


			if (cursor.moveToFirst()){
				do{
					VarBd = cursor.getString( cursor.getColumnIndex("ID") );	
					/*Toast.makeText(UploadFotoBrinde.this, VarBd, Toast.LENGTH_LONG).show();*/

				}
				while (cursor.moveToNext());



			}
			cursor.close();
			// return count
			return cursor.getCount();



			//db = this.getReadableDatabase();

		}
		catch (Exception e) {
			Toast.makeText(UploadFotoBrinde.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();  
		}
		return 0;


	}

	public void concluido(){



		dialog = ProgressDialog.show(UploadFotoBrinde.this, "Foto enviada!",
				"Obrigado por participar!", true);

		Intent home = new Intent(this, CampanhaLimpa.class);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(home);


	}
}