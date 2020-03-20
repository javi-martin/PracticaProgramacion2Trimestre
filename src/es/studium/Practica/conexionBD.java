package es.studium.Practica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionBD 
{	
	
	public static Connection conectar(String baseDatos, String usuario, String contraseña)
	{
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/"+baseDatos+"?autoReconnect=true&useSSL=false";
		String user = usuario;
		String password = contraseña;
		Connection con = null;
		try {
			// Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			// Establecer la conexión con la BD empresa
			con = DriverManager.getConnection(url, user, password);
			if (con != null) {
				System.out.println("Conectado a la base de datos");				
			}
		} 
		catch (SQLException ex) {
			System.out.println("ERROR:La dirección no es válida o el usuario y clave");
			ex.printStackTrace();				
		}
		
		catch (ClassNotFoundException cnfe) {
			System.out.println("Error 1-" + cnfe.getMessage());				
		}
		return con;
	}
	
	public static void desconectar(Connection con)
	{
		try
		{
			con.close();
			System.out.println("Desconectado de la base de datos");	
		}
		catch(Exception e) {}
	}
}