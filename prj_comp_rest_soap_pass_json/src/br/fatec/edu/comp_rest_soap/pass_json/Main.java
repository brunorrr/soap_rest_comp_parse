package br.fatec.edu.comp_rest_soap.pass_json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class Main {

	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException, ClassNotFoundException, SQLException, ParseException {
		final String diretorioOrigem = "C:\\Users\\bruno\\Desktop\\20161207";
		final String arquivoDestino = "C:\\Users\\bruno\\Desktop\\teste json\\inserts.sql";
		
		
		FileSearcher searcher = new FileSearcher();
		JsonParser parser = new JsonParser();
		GerenciadorJson gerenciador = new GerenciadorJson();
		List<File> registros = searcher.obterListaMedicoes(new File(diretorioOrigem));
		File destino = new File(arquivoDestino);
		
		if( !destino.exists() )
			System.out.println("Arquivo sql criado com o nome " + destino.getName() );
		else
			destino.delete();
		
		destino.createNewFile();
		System.out.println("Usando o arquivo " + destino.getAbsolutePath());
		
		BufferedWriter bufferLeitor = new BufferedWriter( new FileWriter( destino ) );
		
		int cidadesProcessadas = 0;
		
		for( File registro : registros ){
			System.out.println( "Processando cidade " + (++cidadesProcessadas) + " de " + registros.size() );
			
			JsonObject obj = (JsonObject) parser.parse( new FileReader( registro ) );
			
			obj.getAsJsonObject("localidade").addProperty("etiqueta", registro.getName());
			
			String insertCidade = gerenciador.cadastrarCidade( obj );
			bufferLeitor.append( insertCidade + "\n" );
			
			String insertDiaLog = gerenciador.cadastrarDiaLog( obj );
			bufferLeitor.append( insertDiaLog + "\n" );
			
			List<String> insertsResumo = gerenciador.cadastrarResumo( obj );
			
			for( String insResumo : insertsResumo )
				bufferLeitor.append( insResumo + "\n" );
			
			List<String> insertsHorario = gerenciador.cadastrarHorarios( obj );
			for( String insHorario : insertsHorario )
				bufferLeitor.append( insHorario + "\n" );
			
		}
		
		System.out.println("Todos os registros processados.");
	}
	
}
