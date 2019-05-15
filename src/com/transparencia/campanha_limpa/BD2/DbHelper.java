package com.transparencia.campanha_limpa.BD2;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbHelper{
 
    private SQLiteDatabase myDataBase;
    private static String BDNAME="tiac.sqlite";
    private static boolean COPIAEFECTUADA=false;
 
    private final Context myContext;

    public DbHelper(Context context) {
 
    	super();
        this.myContext = context;
    }
    
    @SuppressWarnings("deprecation")
	public void open(){
    	
    	if(COPIAEFECTUADA==false){
    		copiaBanco(myContext, BDNAME);
    		myDataBase = myContext.openOrCreateDatabase(BDNAME, Context.MODE_WORLD_WRITEABLE, null);
    	}else{
    		myDataBase = myContext.openOrCreateDatabase(BDNAME, Context.MODE_WORLD_WRITEABLE, null);
    	}
    }
    
    public void close(){
        myDataBase.close();
    }
    
    public static void copiaBanco(Context ctx, String nomeDB){
    		  
        // Cria o banco vazio  
    	@SuppressWarnings("deprecation")
		SQLiteDatabase db = ctx.openOrCreateDatabase(nomeDB, Context.MODE_WORLD_WRITEABLE, null);  
   	    db.close();  
    		        
  	    try {
  	    	
  		    // Abre o arquivo que deve estar na pasta assets  
  		    InputStream is = ctx.getAssets().open(nomeDB);  
    		// Abre o arquivo sql vazio ele fica em:  
  		    // /data/data/nome.do.pacote.da.app/databases  
  		    @SuppressWarnings("resource")
			FileOutputStream fos = new FileOutputStream(  		
  		    ctx.getDatabasePath(nomeDB));  
  		    // Copia byte a byte o arquivo do assets para       
  		    // o aparelho/emulador  
    		     
  		    byte[] buffer = new byte[1024];  
  		    int read;  
  		    while ((read = is.read(buffer)) > 0){  
  		    	fos.write(buffer, 0, read);    
  		    } 
  		    COPIAEFECTUADA=true;
    		
  	    } catch (IOException e) {  
  	    	e.printStackTrace(); 		
  	    	COPIAEFECTUADA=false;
  	    }   	
    }
    
    @SuppressWarnings("deprecation")
	public SQLiteDatabase Bdprojeto(){
    	return myContext.openOrCreateDatabase(BDNAME, Context.MODE_WORLD_WRITEABLE, null);
    }
    
    

}
