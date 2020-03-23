package es.studium.Practica;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login extends Frame implements WindowListener, ActionListener
{

	private static final long serialVersionUID = 1L;
		
	Label lblUsuario = new Label("Usuario:");
	Label lblClave = new Label("Clave:   ");
	
	TextField txtUsuario = new TextField(20); 
	TextField txtClave = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Dialog error = new Dialog(this,"ERROR", true);
	Button btnVolver;	
	
	public Login() 
	{
		setLayout(new FlowLayout());
		setSize(270, 150);
		setResizable(false);
		setLocationRelativeTo(null);
		
		add(lblUsuario);
		add(txtUsuario);
		add(lblClave);
		//Para que cuando escriba la clave sangan *
		txtClave.setEchoChar('*');
		add(txtClave);
		add(btnAceptar);
		add(btnLimpiar);
		
		txtUsuario.setText("Administrador");
		txtClave.setText("Administrador");
		
		addWindowListener(this);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);

		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Login();
	}
	
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		if(this.isActive())
		{
			System.exit(0);
		}
		else
		{
			error.setVisible(false);
		}
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	
	public void actionPerformed(ActionEvent evento)
	{
		if(evento.getSource().equals(btnLimpiar))
		{
			// Tareas del botón Limpiar
			txtUsuario.selectAll();
			txtUsuario.setText("");
			txtClave.selectAll();
			txtClave.setText("");
			txtUsuario.requestFocus();
		}

		else if(evento.getSource().equals(btnAceptar)) 
		{
			// Tareas del botón Aceptar					
			String cadenaEncriptada = getSHA256(txtClave.getText());
			String usuarioPermiso = txtUsuario.getText();
			String tipoUsuario = tipoUsuario();
			
			String sentencia = "SELECT * FROM usuarios WHERE ";
			sentencia += "nombreUsuario = '"+usuarioPermiso+"'";
			sentencia += " AND claveUsuario = '"+cadenaEncriptada+"'";
			

			System.out.println(sentencia);
			
			// Conectar a la base de datos
			Connection con = conectar("imprentaP","root","Studium2019;");
			
			if(con!=null) 
			{
				try
				{
					//Crear una sentencia
					Statement stmt = con.createStatement();
					//Crear un objeto ResultSet para guardar lo obtenido
					//y ejecutar la sentencia SQL
					ResultSet rs = stmt.executeQuery(sentencia);
					
					if(rs.next())
					{
												
						if(tipoUsuario.equals("0")) 
						{
							new menuPrincipal();
							
							setVisible(false);
						}
						else 
						{
							new menuPrincipal().sinPermisos();
							setVisible(false);
						}
						
					}
					else
					{
						dialogoError("Erro", "Usuario y/o contraseña incorrectas");
					}
				}

				catch (SQLException sqle)
				{
					System.out.println("Error 2-"+sqle.getMessage());
				}
			}			
			
			conexionBD.desconectar(con);			
		}
		else 
		{
			// Tareas de Volver
			error.setVisible(false);
		}
	}

	public static String getSHA256(String data)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(data.getBytes());
			byte byteData[] = md.digest();
			
			for (int i = 0; i < byteData.length; i++) 
			{
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100,
						16).substring(1));
			}
		} 

		catch(Exception e)
		{
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	public Dialog dialogoError(String titulo, String mensaje) 
	{
		error = new Dialog(this,titulo, true);
		btnVolver = new Button("Volver");		
		Label lblEtiqueta = new Label(mensaje);
		
		error.setLayout(new FlowLayout());
		error.setSize(310,100);
		btnVolver.addActionListener(this);
		
		error.add(lblEtiqueta);
		error.add(btnVolver);
		
		error.addWindowListener(this);
		error.setResizable(false);
		error.setLocationRelativeTo(this);
		error.setVisible(true);
				
		return error;
	}
	
	public Connection conectar(String baseDatos, String usuario, String contraseña)
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
				
			dialogoError("ERROR: Conexión Base Datos","ERROR:La dirección no es válida o el usuario y clave");
		}
		
		catch (ClassNotFoundException cnfe) {
			System.out.println("Error 1-" + cnfe.getMessage());
			
			dialogoError("ERROR: Conexión Base Datos","Drive no cargado");
		}
		return con;
	}
	public String tipoUsuario() 
	{
		String tipo = null;
		String sql = "SELECT * FROM usuarios WHERE nombreUsuario = " +"'"+txtUsuario.getText()+"'";
		// Conectar a la base de datos
		Connection con = conectar("imprentaP","root","Studium2019;");
		try 
		{			
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery(sql);
			while(rs.next()) 
			{
				tipo = rs.getString("tipoUsuario");
			}			
		}
		catch (SQLException ex) 
		{
			System.out.println("ERROR:al hacer el SELECT");
			ex.printStackTrace();
		}
		return tipo;
	}
}