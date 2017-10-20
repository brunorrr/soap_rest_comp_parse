package br.fatec.edu.comp_rest_soap.pass_json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class GerenciadorJson {
	
	private Connection conexao;
	
	public GerenciadorJson() throws ClassNotFoundException, SQLException{
//		conexao = ConnectionManager.getInstance().getConnection();
	}
	
	public void cadastrarCidade( JsonObject objCidade ) throws SQLException{
		
		objCidade = objCidade.getAsJsonObject("localidade");
		
		StringBuffer sb = new StringBuffer("INSERT INTO tbl_cidade(nome,etiqueta,uf,latitude,longitude) VALUES('");
		sb.append( objCidade.get("cidade").getAsString() );
		sb.append("','");
		sb.append( objCidade.get("etiqueta").getAsString() );
		sb.append("','");
		sb.append( objCidade.get("estado").getAsString().toUpperCase() );
		sb.append("','");
		sb.append( objCidade.get("latitude").getAsString() );
		sb.append("','");
		sb.append( objCidade.get("longitude").getAsString() );
		sb.append("');");
		
		System.out.println(sb.toString());
		
//		Statement statement = conexao.createStatement();
//		statement.executeUpdate( sb.toString() );
		
	}
	
	public void cadastrarDiaLog( JsonObject objDia ) throws SQLException, ParseException{
		StringBuffer sb = new StringBuffer("INSERT INTO tbl_dia_log(dia,cidade,uf,tempo,tipo) VALUES(");
		
		sb.append("TO_DATE('");
		sb.append( objDia.get("data").getAsString() );
		sb.append("','yyyyMMdd'),'");
		
		//Objeto  localidade
		JsonObject objLocal = objDia.getAsJsonObject("localidade");
		sb.append( objLocal.get("cidade").getAsString() );
		sb.append("','");
		sb.append( objLocal.get("estado").getAsString().toUpperCase() );
		sb.append("','");
		
		//Objeto Tipo
		JsonObject objTipo = objDia.getAsJsonObject("tipo");
		sb.append( objTipo.get("tempo").getAsString() );
		sb.append("','");
		sb.append( objTipo.get("tipo").getAsString() );
		
		sb.append("');");
		
		System.out.println(sb.toString());
		
//		Statement statement = conexao.createStatement();
//		statement.executeUpdate( sb.toString() );
	}
	
	public void cadastrarResumo( JsonObject obj ) throws ParseException{
		
		JsonArray arrayResumo = obj.getAsJsonArray("resumo");
		Iterator<JsonElement> iterator = arrayResumo.iterator();
		while( iterator.hasNext() ){
			JsonObject objetoJson = (JsonObject) iterator.next();
			
			StringBuffer sb = new StringBuffer("INSERT INTO tbl_resumo(dia,cidade,uf,mecanismo,"
					+ "qtde_leituras_pressao,media_pressao,"
					+ "qtde_leituras_minimas,media_temperatura_minima,"
					+ "qtde_leituras_maxima,media_temperatura_maxima,"
					+ "qtde_leituras_temperatura,media_temperatura,"
					+ "qtde_leituras_vento,media_vento,"
					+ "qtde_leituras_umidade,media_umidade) VALUES(");

			sb.append("TO_DATE('");
			sb.append( obj.get("data").getAsString() );
			sb.append("','yyyyMMdd'),'");

			//Objeto  localidade
			JsonObject objLocal = obj.getAsJsonObject("localidade");
			sb.append( objLocal.get("cidade").getAsString() );
			sb.append("','");
			sb.append( objLocal.get("estado").getAsString().toUpperCase() );
			sb.append("',");

			//Objeto Resumo
			if( !objetoJson.get("mecanismo").isJsonNull() ){
				sb.append( obterValorJson(objetoJson.get("mecanismo")) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("leituras_pressao") ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("media_pressao"), 1 ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("leituras_temperatura_minima") ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("media_temperatura_minima"), 1 ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("leituras_temperatura_maxima") ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("media_temperatura_maxima"), 1 ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("leituras_temperatura") ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("media_temperatura"), 1 ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("leituras_vento_vm") ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("media_vento_vm"), 1 ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("leituras_umidade") ) );
				sb.append(",");
				sb.append( obterValorJson( objetoJson.get("media_umidade"), 1 ) );
				sb.append(");");
			}
			
			System.out.println(sb.toString());
			
//			Statement statement = conexao.createStatement();
//			statement.executeUpdate( sb.toString() );
		}
	}
	
	public void cadastrarHorarios( JsonObject obj ){
		JsonArray arrayHorario = obj.getAsJsonArray("horarios");
		
		Iterator<JsonElement> iterator = arrayHorario.iterator();
		while( iterator.hasNext() ){
			JsonElement elementoMedicao = iterator.next();
			if(!elementoMedicao.isJsonNull()){
				JsonArray arrayMedicao = (JsonArray) elementoMedicao;
				Iterator<JsonElement> iteratorMedicao = arrayMedicao.iterator();
				while( iteratorMedicao.hasNext() ){
					JsonObject objetoJson = (JsonObject) iteratorMedicao.next();
					
					if( !objetoJson.isJsonNull() ){
						StringBuffer sb = new StringBuffer("INSERT INTO tbl_horario(dia,cidade,uf,"
								+ "mecanismo,hora,temperatura,velocidade_vento,"
								+ "situacao,url,"
								+ "pressao,por_sol,umidade,nascer_sol,"
								+ "temperatura_maxima,temperatura_minima) VALUES(");

						sb.append("TO_DATE('");
						sb.append( obj.get("data").getAsString() );
						sb.append("','yyyyMMdd'),'");

						//Objeto  localidade
						JsonObject objLocal = obj.getAsJsonObject("localidade");
						sb.append( objLocal.get("cidade").getAsString() );
						sb.append("','");
						sb.append( objLocal.get("estado").getAsString().toUpperCase() );
						sb.append("',");
						
						//Objeto Horarios
						sb.append( obterValorJson( objetoJson.get("mecanismo") ) );
						sb.append(",");
						sb.append( "'" + objetoJson.get("chave").getAsString().substring( 
								objetoJson.get("chave").getAsString().length() - 2 ) + "'" );
						sb.append(",");
						sb.append( obterValorJson( objetoJson.get("temperatura"), 1 ) );
						sb.append(",");
						sb.append( obterValorJson( objetoJson.get("vento_vm"), 1 ) );
						sb.append(",");
						sb.append( obterValorJson( objetoJson.get("situacao") ) );
						sb.append(",");
						sb.append( obterValorJson( objetoJson.get("url") ) );
						sb.append(",");
						sb.append( obterValorJson( objetoJson.get("pressao"), 1 ) );
						sb.append(",");
						sb.append( obterValorJson( objetoJson.get("por_sol") ) );
						sb.append(",");
						sb.append( obterValorJson( objetoJson.get("umidade"), 1 ) );
						sb.append(",");
						sb.append( obterValorJson( objetoJson.get("nascar_sol") ) );
						sb.append(",");
						sb.append( obterValorJson( objetoJson.get("temperatura_maxima"), 1 ) );
						sb.append(",");
						sb.append( obterValorJson( objetoJson.get("temperatura_minima"), 1 ) );
						sb.append(");");
						
						System.out.println( sb.toString() );
						
//						Statement statement = conexao.createStatement();
//						statement.executeUpdate( sb.toString() );				
					}
				}
			}
		}
		
	}
	
	private Object obterValorJson(JsonElement jsonElement) {
		return jsonElement.isJsonNull() ? "NULL" : "'" + jsonElement.getAsString() + "'";
	}
	
	public Object obterValorJson(JsonElement jsonElement, int casasDecimais){
		if( jsonElement.isJsonNull() )
			return "NULL";
		return "'" + new BigDecimal( jsonElement.getAsString() ).setScale(casasDecimais, RoundingMode.HALF_UP).toString().replace('.', ',') + "'";
	}

	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException, ClassNotFoundException, SQLException, ParseException {
		FileSearcher searcher = new FileSearcher();
		
		JsonParser parser = new JsonParser();
		
		GerenciadorJson gerenciador = new GerenciadorJson();
		
		List<File> registros = searcher.obterListaMedicoes(new File("C:\\Users\\bruno\\Desktop\\teste_json"));
		
		for( File registro : registros ){
			JsonObject obj = (JsonObject) parser.parse( new FileReader( registro ) );
			
			
			obj.getAsJsonObject("localidade").addProperty("etiqueta", registro.getName());
			
			gerenciador.cadastrarCidade( obj );
			gerenciador.cadastrarDiaLog( obj );
			gerenciador.cadastrarResumo( obj );
			gerenciador.cadastrarHorarios( obj );
		}
	}

}
