package br.fatec.edu.comp_rest_soap.pass_json;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

	private static ConnectionManager instancia;
	private Connection con;
	
	private static final String INFO_CONEXAO = "jdbc:oracle:thin:@127.0.0.1:1521/XE";
	private static final String USUARIO_CONEXAO = "system";
	private static final String SENHA_CONEXAO = "root";

	private ConnectionManager(){
	}

	public static ConnectionManager getInstance(){
		if( instancia == null )
			instancia = new ConnectionManager();
		return instancia;
	}

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		if (con == null) { 
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}
		con = DriverManager.getConnection(
				INFO_CONEXAO, 
				USUARIO_CONEXAO, 
				SENHA_CONEXAO);
		return con;
	}

}
