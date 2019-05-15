package com.transparencia.campanha_limpa.BD2;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class GereBD {
	private SQLiteDatabase database;
	private DbHelper dbHelper;
	private final Context myContext;
	private static String BDNAME="tiac.sqlite";

	public GereBD(Context context) {
		dbHelper = new DbHelper(context);
		this.myContext=context;
		this.database=dbHelper.Bdprojeto();
		
	}
	
	@SuppressWarnings("static-access")
	public void open() throws SQLException {
		
		dbHelper.copiaBanco(myContext, BDNAME);
		
	}

	public void close() {
		
	}
	
	//Pesquisa pelo Distrito o id de distrito correspondente
		 public Distrito PesqDistrito(String nome){
			 
			 Distrito distrito = new Distrito();
		     
			 Cursor cursor = this.database.query("distrito", new String[]{"ID_distrito", "nome"}, null, null, null, null, "");
			 if(cursor.moveToFirst()){
		     
				 do{
					 if(nome.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("nome")))){
						 distrito.setIddistrito(cursor.getInt(cursor.getColumnIndex("ID_distrito")));
						 distrito.setnome(cursor.getString(cursor.getColumnIndex("nome")));
					 }
				 } while (cursor.moveToNext());
			 }
			 if(cursor != null && !cursor.isClosed()){
				 cursor.close();
			 }
			 return distrito;
		 }
		 		 

	/*
	//login de utilizador da aplica?‹o......
	public boolean inserirutilizador(Integer id_blog, String login, String pass){
		try{
			String query="INSERT INTO utilizadoraplicacao (id_blog,login,password) VALUES ('"+id_blog+"','"+login+"','"+pass+"')";
			database.execSQL(query);
			return true;
	        
		}catch(SQLException ex){
			return false;  
		}    
	}
	
	//Cria o blog para o respetivo utilizador......
	public boolean inserirblog(Integer id_blog, String titulo){
		try{
			String query="INSERT INTO blog (id_blog,titulo) VALUES ('"+id_blog+"','"+titulo+"')";
			database.execSQL(query);
			return true;
	        
		}catch(SQLException ex){
			return false;  
		}    
	}
	
	//Associa um ponto a uam entrada no blog (ficando desta forma este marcado como visitado)
	public boolean inserirvisita(Integer id_ponto, Integer id_entradablog){
		try{
			String query="INSERT INTO ponto_entradablog (id_ponto,id_entradablog) VALUES ('"+id_ponto+"','"+id_entradablog+"')";
			database.execSQL(query);
			return true;
	        
		}catch(SQLException ex){
			return false;  
		}    
	}
	
	//Inserir Coment‡rio feito pelo utilizador ao ponto visitado
	public boolean inserircomentario(Integer id_comentario, Integer id_entradablog, String texto_comentario){
		try{
			String query="INSERT INTO comentario (id_comentario,id_entradablog,texto_comentario) VALUES ('"+id_comentario+"'" +
					",'"+id_entradablog+"','"+texto_comentario+"')";
			database.execSQL(query);
			return true;
	        
		}catch(SQLException ex){
			return false;  
		}    	
	}
	
	//Inserir Foto tirada pelo utilizador ao ponto visitado
	public boolean inserirfotoutilizador(Integer id_foto_util, Integer id_entradablog, String caminho_foto_util){
		try{
			String query="INSERT INTO fotoutilizador (id_foto_util,id_entradablog,caminho_foto_util) VALUES ('"+id_foto_util+"'" +
					",'"+id_entradablog+"','"+caminho_foto_util+"')";
			database.execSQL(query);
			return true;
	        
		}catch(SQLException ex){
			return false;  
		}    	
	}
	
	//Inserir entrada no blog
	public boolean inserirentradablog(Integer id_entradablog, Integer id_blog, String titulo, String datahora){
		try{
			String query="INSERT INTO entradablog (id_entradablog,id_blog,titulo,datahora) VALUES ('"+id_entradablog+"'" +
					",'"+id_blog+"','"+titulo+"','"+datahora+"')";
			database.execSQL(query);
			return true;
	        
		}catch(SQLException ex){
			return false;  
		}    	
	}
	
	//Pesquisa pelo idPonto a imagem correspondente para geral o alerta de proximidade
	 public Imagem PesqImagem(Integer id_ponto){
		 
		 Imagem imagem = new Imagem();
	     
		 Cursor cursor = this.database.query("imagem", new String[]{"id_imagem", "id_ponto", "imagem_caminho", "label"}, null, null, null, null, "");
		 if(cursor.moveToFirst()){
	     
			 do{
				 if(id_ponto == cursor.getInt(cursor.getColumnIndex("id_ponto"))){
					 imagem.setIdimagem(cursor.getInt(cursor.getColumnIndex("id_imagem")));
					 imagem.setIdponto(cursor.getInt(cursor.getColumnIndex("id_ponto")));
					 imagem.setImagemcaminho(cursor.getString(cursor.getColumnIndex("imagem_caminho")));
					 imagem.setLabel(cursor.getString(cursor.getColumnIndex("label")));
				 }
			 } while (cursor.moveToNext());
		 }
		 if(cursor != null && !cursor.isClosed()){
			 cursor.close();
		 }
		 return imagem;
	 }
	 
	//Pesquisa pelo idPonto o video correspondente para geral o alerta de proximidade
	 public Video PesqVideo(Integer id_ponto){
		 
		 Video video = new Video();
	     
		 Cursor cursor = this.database.query("video", new String[]{"id_video", "id_ponto", "video_caminho"}, null, null, null, null, "");
		 if(cursor.moveToFirst()){
	     
			 do{
				 if(id_ponto == cursor.getInt(cursor.getColumnIndex("id_ponto"))){
					 video.setIdvideo(cursor.getInt(cursor.getColumnIndex("id_video")));
					 video.setIdponto(cursor.getInt(cursor.getColumnIndex("id_ponto")));
					 video.setVideocaminho(cursor.getString(cursor.getColumnIndex("video_caminho")));
				 }
			 } while (cursor.moveToNext());
		 }
		 if(cursor != null && !cursor.isClosed()){
			 cursor.close();
		 }
		 return video;
	 }
	 
	//Pesquisa pelo idPonto o som correspondente para geral o alerta de proximidade
	 public Som PesqSom(Integer id_ponto){
		 
		 Som som = new Som();
	     
		 Cursor cursor = this.database.query("som", new String[]{"id_som", "id_ponto", "som_caminho"}, null, null, null, null, "");
		 if(cursor.moveToFirst()){
	     
			 do{
				 if(id_ponto == cursor.getInt(cursor.getColumnIndex("id_ponto"))){
					 som.setIdsom(cursor.getInt(cursor.getColumnIndex("id_som")));
					 som.setIdponto(cursor.getInt(cursor.getColumnIndex("id_ponto")));
					 som.setSomcaminho(cursor.getString(cursor.getColumnIndex("som_caminho")));
				 }
			 } while (cursor.moveToNext());
		 }
		 if(cursor != null && !cursor.isClosed()){
			 cursor.close();
		 }
		 return som;
	 }
	 
	//Pesquisa pelo idPonto o Audio correspondente para geral o alerta de proximidade
	 public Audio PesqAudio(Integer id_ponto){
		 
		 Audio audio = new Audio();
	     
		 Cursor cursor = this.database.query("audio", new String[]{"id_audio", "id_ponto", "audio_caminho"}, null, null, null, null, "");
		 if(cursor.moveToFirst()){
	     
			 do{
				 if(id_ponto == cursor.getInt(cursor.getColumnIndex("id_ponto"))){
					 audio.setIdaudio(cursor.getInt(cursor.getColumnIndex("id_audio")));
					 audio.setIdponto(cursor.getInt(cursor.getColumnIndex("id_ponto")));
					 audio.setAudiocaminho(cursor.getString(cursor.getColumnIndex("audio_caminho")));
				 }
			 } while (cursor.moveToNext());
		 }
		 if(cursor != null && !cursor.isClosed()){
			 cursor.close();
		 }
		 return audio;
	 }
	 
	//Pesquisa pelo idPonto o Servico correspondente para geral o alerta de proximidade
	 public Servico PesqServico(Integer id_ponto){
		 
		 Servico servico = new Servico();
	     
		 Cursor cursor = this.database.query("servico", new String[]{"id_servico", "id_ponto", "servico"}, null, null, null, null, "");
		 if(cursor.moveToFirst()){
	     
			 do{
				 if(id_ponto == cursor.getInt(cursor.getColumnIndex("id_ponto"))){
					 servico.setIdservico(cursor.getInt(cursor.getColumnIndex("id_servico")));
					 servico.setIdponto(cursor.getInt(cursor.getColumnIndex("id_ponto")));
					 servico.setServico(cursor.getString(cursor.getColumnIndex("servico")));
				 }
			 } while (cursor.moveToNext());
		 }
		 if(cursor != null && !cursor.isClosed()){
			 cursor.close();
		 }
		 return servico;
	 }
	 
	 //Pesquisa todos os pontos "id_ponto", pelo id_enquadramento
	 public Integer[] PesqEnquadramento_ponto(Integer id_enquadramento){
		 
		 Cursor cursor = this.database.query("enquadramento_ponto", new String[]{"id_enquadramento", "id_ponto"}, null, null, null, null, "");
		 Integer[] aux = new Integer[cursor.getCount()];
		 int i=0;
		 if(cursor.moveToFirst()){
	     
			 do{
				 if(id_enquadramento == cursor.getInt(cursor.getColumnIndex("id_enquadramento"))){
					 EnquadramentoPonto enquadramentoponto = new EnquadramentoPonto();
					 enquadramentoponto.setIdenquadramento(cursor.getInt(cursor.getColumnIndex("id_enquadramento")));
					 enquadramentoponto.setIdponto(cursor.getInt(cursor.getColumnIndex("id_ponto")));
					 aux[i]=enquadramentoponto.getIdponto();
					 i++;
				 }
			 } while (cursor.moveToNext());
		 }
		 if(cursor != null && !cursor.isClosed()){
			 cursor.close();
		 }
		 return aux;
	 }
	 
	 public Ponto Pesq_coordenadas(Integer id_ponto){
		 
		 Ponto ponto = new Ponto();
		 
		 Cursor cursor = this.database.query("ponto", new String[]{"id_ponto", "designacao", "desc_det", "desc_brev", "coordenadaGPS_N", 
				 "coordenadaGPS_W", "raio"}, null, null, null, null, "");
		 
		 if(cursor.moveToFirst()){
		     
			 do{
				 if(id_ponto == cursor.getInt(cursor.getColumnIndex("id_ponto"))){
					 ponto.setIdponto(cursor.getInt(cursor.getColumnIndex("id_ponto")));
					 ponto.setDesignacao(cursor.getString(cursor.getColumnIndex("designacao")));
					 ponto.setDescdet(cursor.getString(cursor.getColumnIndex("desc_det")));
					 ponto.setDescbre(cursor.getString(cursor.getColumnIndex("desc_brev")));
					 ponto.setCoordenadagpsN(cursor.getString(cursor.getColumnIndex("coordenadaGPS_N")));
					 ponto.setCootdenadagpsW(cursor.getString(cursor.getColumnIndex("coordenadaGPS_W")));
					 ponto.setRaio(cursor.getInt(cursor.getColumnIndex("raio")));
				 }
			 } while (cursor.moveToNext());
		 }
		 if(cursor != null && !cursor.isClosed()){
			 cursor.close();
		 }
		 return ponto;
	 }
	 
	 public Integer Pesq_IdPonto_coord(String coordenadaGPS_N, String coordenadaGPS_W){
Ponto ponto = new Ponto();
		 
		 Cursor cursor = this.database.query("ponto", new String[]{"id_ponto", "designacao", "desc_det", "desc_brev", "coordenadaGPS_N", 
				 "coordenadaGPS_W", "raio"}, null, null, null, null, "");
		 
		 if(cursor.moveToFirst()){
		     
			 do{
				 if(coordenadaGPS_N.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("coordenadaGPS_N"))) && coordenadaGPS_W.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("coordenadaGPS_W")))){
					 ponto.setIdponto(cursor.getInt(cursor.getColumnIndex("id_ponto")));
					 ponto.setDesignacao(cursor.getString(cursor.getColumnIndex("designacao")));
					 ponto.setDescdet(cursor.getString(cursor.getColumnIndex("desc_det")));
					 ponto.setDescbre(cursor.getString(cursor.getColumnIndex("desc_brev")));
					 ponto.setCoordenadagpsN(cursor.getString(cursor.getColumnIndex("coordenadaGPS_N")));
					 ponto.setCootdenadagpsW(cursor.getString(cursor.getColumnIndex("coordenadaGPS_W")));
					 ponto.setRaio(cursor.getInt(cursor.getColumnIndex("raio")));
				 }
			 } while (cursor.moveToNext());
		 }
		 if(cursor != null && !cursor.isClosed()){
			 cursor.close();
		 }
		 return ponto.getIdponto();
	 }
	 
	 public Integer countfoto(){
		 String query ="select * from fotoutilizador";
		 Cursor dataCountfoto = this.database.rawQuery(query, null);
		 return dataCountfoto.getCount(); 
	 }
	 
	 public Integer countentradasblog(){
		 String query ="select * from entradablog";
		 Cursor dataCountentrada = this.database.rawQuery(query, null);
		 return dataCountentrada.getCount(); 
	 }
	 
	 public Integer countComentarios(){
		 String query = "select * from comentario";
		 Cursor dataCountcoment = this.database.rawQuery(query, null);
		 return dataCountcoment.getCount();
	 }
	 
	 public Integer countEnqPonto(){
		 String query = "select * from enquadramento_ponto";
		 Cursor dataCountcoment = this.database.rawQuery(query, null);
		 return dataCountcoment.getCount();
	 }
	 */
}
