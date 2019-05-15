package com.transparencia.campanha_limpa.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.transparencia.campanha_limpa.R;
import com.transparencia.campanha_limpa.CampanhaLimpa;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistarComicio extends Activity implements OnClickListener{

	private EditText user, pass, Email,conf_pass;
	private Button  mRegister;
	@SuppressWarnings("unused")
	private TextView frmlogin;
	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser = new JSONParser();

	//php login script

	//localhost :  
	//testing on your device
	//put your local ip instead,  on windows, run CMD > ipconfig
	//or in mac's terminal type ifconfig and look for the ip under en0 or en1
	// private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/register.php";

	//testing on Emulator:
	private static final String LOGIN_URL = "http://campanhalimpa.transparencia.pt/user-rest";

	//testing from a real server:
	//private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/register.php";

	//ids
	private static final String TAG_SUCCESS = "Status";
	private static final String TAG_MESSAGE = "MSGR";
	@SuppressWarnings("unused")
	private static final String TAG_User = "IdUser";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		user = (EditText)findViewById(R.id.username);
		user.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
				if( user.getText().toString().length() < 5)
					user.setError(Html.fromHtml("<font color='black'>O nome de utilizador tem de conter minimo 5 caracteres!</font>"));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if( user.getText().toString().trim().length() >= 5)
					user.setError(null);

			}
		});


		Email = (EditText)findViewById(R.id.email);
		final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		Email.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(Email.getText().toString().trim().matches(emailPattern) && Email.getText().toString().trim().length() > 0){
					Email.setError(null);
				}else{
					Email.setError(Html.fromHtml("<font color='black'>Email invalido</font>"));

				}
			}
		});

		pass = (EditText)findViewById(R.id.password);
		pass.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if( pass.getText().toString().length() < 5 )
					pass.setError(Html.fromHtml("<font color='black'>A palavra passe tem de conter minimo 5 caracteres!</font>"));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if( pass.getText().toString().trim().length() > 5)
					pass.setError(null);
			}
		});

		conf_pass = (EditText)findViewById(R.id.confirmar_password);
		conf_pass.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}  

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(pass.getText().toString().equals(conf_pass.getText().toString())){
					conf_pass.setError(null);
				}else{
					conf_pass.setError(Html.fromHtml("<font color='black'>As palavras passe nao coincidem!</font>"));
				}
			}
		});

		frmlogin = (TextView)findViewById(R.id.login1);
		mRegister = (Button)findViewById(R.id.register);
		mRegister.setOnClickListener(this);

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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.register:
			new CreateUser().execute();
			break;
		case R.id.login1:
			Intent i = new Intent(this, Login_fotoComicio.class);
			startActivity(i);
			break;

		default:
			break;
		}

	}



	class CreateUser extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegistarComicio.this);
			pDialog.setMessage("A registar utilizador...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			// Check for success tag
			int success;
			String username = user.getText().toString();
			String password = pass.getText().toString();
			String email = Email.getText().toString();
			try {

				// convers‹o MD5 password
				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("MD5");
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				md.update(password.getBytes());

				byte byteData[] = md.digest();

				//convert the byte to hex format method 1
				final  StringBuffer sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				}
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", sb.toString()));
				params.add(new BasicNameValuePair("email", email));

				Log.d("request!", "starting");

				//Posting user data to script 
				JSONObject json = jsonParser.makeHttpRequest(
						LOGIN_URL, "POST", params);

				// full json response
				Log.d("Login attempt", json.toString());

				// json success element
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("User Created!", json.toString());              	

					Intent i = new Intent(RegistarComicio.this, Login_fotoCartaz.class);
					finish();

					//DBAdapter bd= new DBAdapter(getBaseContext());
					//bd.levaValor(json.getString(TAG_User));


					startActivity(i);
					return json.getString(TAG_MESSAGE);
				}else{
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url != null){
				Toast.makeText(RegistarComicio.this, file_url, Toast.LENGTH_LONG).show();
			}

		}

	}


}
