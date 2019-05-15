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
import com.transparencia.campanha_limpa.foto.fotoCartaz;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

public class Login_fotoCartaz extends Activity implements OnClickListener{
	
	private EditText user, pass;
	private Button mSubmit;
	private TextView mRegister;
	 // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 // Session Manager Class
 	
    //php login script location:
 	private static String IDUser;
    //localhost :  
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
   // private static final String LOGIN_URL = "http://xxx.xxx.x.x:1234/webservice/login.php";
 	    //private final Context myContext;
    //testing on Emulator:
    private static final String LOGIN_URL = "http://campanhalimpa.transparencia.pt/user-rest";
    
  //testing from a real server:
    //private static final String LOGIN_URL = "http://www.yourdomain.com/webservice/login.php";
    
    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "Status";
    private static final String TAG_MESSAGE = "MSG";
    private static final String TAG_MESSAGE_ERROR = "MSGR";
   private static final String TAG_User = "IdUser";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_registar_main);
		
		//setup input fields
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
		
		//setup buttons
		mSubmit = (Button)findViewById(R.id.login);
		mRegister = (TextView)findViewById(R.id.register);
		
		//register listeners
		
			mSubmit.setOnClickListener(this);
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
		case R.id.login:
			if(pass.getText().toString().length() != 0 || user.getText().toString().length() != 0 ){
				new AttemptLogin().execute();
			}
			else
			{
				Toast.makeText(Login_fotoCartaz.this, "Preencha os campos em falta!", Toast.LENGTH_LONG).show();
				
			}
			break;
		case R.id.register:
				Intent i = new Intent(this, RegistarCartaz.class);
				startActivity(i);
			break;

		default:
			break;
		}
	}
	
	class AttemptLogin extends AsyncTask<String, String, String> {
		
		 /**
         * Before starting background thread Show Progress Dialog
         * */
		boolean failure = false;
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login_fotoCartaz.this);
            pDialog.setMessage("A autenticar...");
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
            try {
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
 
                Log.d("request!", "starting");
                // getting product details by making HTTP request
              JSONObject json = jsonParser.makeHttpRequest(
                       LOGIN_URL, "POST", params);
 
                // check your log for json response
                Log.d("Login attempt", json.toString());
                
                //InsereUtilizador();
                /*if(json.toString()!=null){
            		
            		pDialog.setMessage("Login realizado com Sucesso");
            		
            		
            	}
            	
 */    
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success != 0) {
                	Log.d("Login Successful!", json.getString(TAG_SUCCESS));
                	//Toast.makeText(MainActivity.this, "Autenticado", Toast.LENGTH_LONG).show();
                	
                	Intent i = new Intent(Login_fotoCartaz.this, fotoCartaz.class);
                	finish();
                	 
                	//DBAdapter bd= new DBAdapter(getBaseContext());
        		    //bd.levaValor(json.getString(TAG_User));
                	 
                	IDUser =json.getString(TAG_User);
    				startActivity(i);
    				//devolveBD(IDUser);
    				
                	return json.getString(TAG_MESSAGE);
                	
                
                }else{
                	success = json.getInt(TAG_SUCCESS);
                	Log.d("Login Failure!", json.getString(TAG_MESSAGE_ERROR));
                	return json.getString(TAG_MESSAGE_ERROR);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
                
            
            
            
            return null;
            
		}
		//if (json.getString(TAG_User)!=0)
			
		private void devolveBD(String iDUser) {
			// TODO Auto-generated method stub
			
			
			SQLiteDatabase db;
     	    db = openOrCreateDatabase( "login.sqlite"        , SQLiteDatabase.CREATE_IF_NECESSARY        , null          );
     	    try {
     	    	
     	    	
     	        final String CREATE_TABLE_CONTAIN = "CREATE TABLE IF NOT EXISTS User("
     	                + "ID INTEGER);";
     	        db.execSQL(CREATE_TABLE_CONTAIN);
     	        //Toast.makeText(Login.this, "table created ", Toast.LENGTH_LONG).show();
     	     if(IDUser!=null){
     	        String sql =
     	            "INSERT INTO User (ID) VALUES("+ IDUser +")" ;       
     	                db.execSQL(sql);
     	     }
     	    }
     	    catch (Exception e) {
     	        Toast.makeText(Login_fotoCartaz.this, "ERROR "+e.toString(), Toast.LENGTH_LONG).show();  
     	}
			
			
			
			
		}

		/**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            devolveBD(IDUser);
            if (file_url != null){
            	
            	Toast.makeText(Login_fotoCartaz.this, file_url, Toast.LENGTH_LONG).show();
            }
            
        }
        
 
		

	}
}