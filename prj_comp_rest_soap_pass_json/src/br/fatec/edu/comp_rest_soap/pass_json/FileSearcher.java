package br.fatec.edu.comp_rest_soap.pass_json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class FileSearcher {
	
	public List<File> obterListaMedicoes(File diretorio) throws JsonIOException, JsonSyntaxException, IOException{
		return Arrays.asList( diretorio.listFiles() );
	}
	
}
