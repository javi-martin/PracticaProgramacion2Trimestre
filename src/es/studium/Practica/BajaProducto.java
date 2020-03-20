package es.studium.Practica;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BajaProducto extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblProductoBorrar = new Label("Producto a borrar:");
	Choice choProducto = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Dialog seguro;
	Button btnSi;
	Button btnNo;
	
	Dialog error;
	Button btnSalir;
	
	public BajaProducto() 
	{
		setTitle("Baja de producto");
		setLayout(new FlowLayout());
		
		choProducto.add("Seleccionar uno...");
		// Conectar a la base de datos
		Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
		// Sacar los datos de la tabla productos
		// Rellenar el Choice
		String sqlSelect = "SELECT * FROM productos";
		try {
			// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlSelect);
			while (rs.next()) 
			{
				choProducto.add(rs.getInt("idProducto")+
						"-"+rs.getString("nombreProducto")+
						", "+rs.getString("descripcionProducto"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("ERROR:al consultar");
			ex.printStackTrace();
			dialogoError("Error", "ERROR:al consultar Productos","","Salir");
		}
		// Cerrar la conexión
		conexionBD.desconectar(con);
		add(lblProductoBorrar);
		add(choProducto);
		add(btnAceptar);
		add(btnLimpiar);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		addWindowListener(this);
		setSize(500,200);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		if(this.isActive())
		{
			setVisible(false);
		}
		else if(seguro.isActive())
		{			
			seguro.setVisible(false);
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
		Object a;
		a=evento.getSource();
		if(a.equals(btnLimpiar))
		{
			choProducto.select(0);
		}
		else if(a.equals(btnAceptar)) 
		{
			if(choProducto.getSelectedIndex()!=0) 
			{
				dialogoSeguro();
			}
			else 
			{
				dialogoError("Error", "Debe elegir un producto.","","Salir");
			}
		}
		else if(a.equals(btnNo))
		{
			seguro.setVisible(false);
		}
		else if(a.equals(btnSi))
		{
			// Conectar a BD
			Connection con = conexionBD.conectar("imprentaP","root","Studium2019;"); 
			// Borrar
			String[] Producto=choProducto.getSelectedItem().split("-");
			int respuesta = borrar(con, "productos","idProducto",Integer.parseInt(Producto[0]));
			// Mostramos resultado
			if(respuesta == 0)
			{
				System.out.println("Borrado de producto correcto");
				dialogoError("Borrado", "Borrado de producto correcto","","Salir");
				
			}
			else
			{
				System.out.println("Error en Borrado de producto");
				dialogoError("Error", "Error en Borrado de producto","","Salir");
			}
			// Actualizar el Choice
			choProducto.removeAll();
			choProducto.add("Seleccionar uno...");
			String sqlSelect = "SELECT * FROM productos";
			try {
				// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sqlSelect);
				while (rs.next()) 
				{
					choProducto.add(rs.getInt("idProducto")+
							"-"+rs.getString("nombreProducto")+
							", "+rs.getString("descripcionProducto"));
				}
				rs.close();
				stmt.close();
			} catch (SQLException ex) {
				System.out.println("ERROR:al consultar");
				ex.printStackTrace();
				dialogoError("Error", "ERROR:al consultar Productos","","Salir");
			}
			// Cerrar la conexión
			conexionBD.desconectar(con);
			seguro.setVisible(false);
		}
		else 
		{
			error.setVisible(false);
		}
	}
	
	public Dialog dialogoError(String titulo, String mensaje1, String mensaje2, String tituloBoton) 
	{
		error = new Dialog(this,titulo, true);
		btnSalir = new Button(tituloBoton);		
		Label lblEtiqueta1 = new Label(mensaje1);
		Label lblEtiqueta2 = new Label(mensaje2);
		
		error.setLayout(new FlowLayout());
		error.setSize(200,150);
		btnSalir.addActionListener(this);
		
		error.add(lblEtiqueta1);
		error.add(lblEtiqueta2);
		error.add(btnSalir);
		
		error.addWindowListener(this);
		error.setResizable(false);
		error.setLocationRelativeTo(this);
		error.setVisible(true);
				
		return error;
	}
	
	public Dialog dialogoSeguro() 
	{
		seguro = new Dialog(this,"¿Seguro?", true);
		btnSi = new Button("Sí");
		btnNo = new Button("No");
		Label lblEtiqueta = new Label("¿Está seguro de eliminar?");
		seguro.setLayout(new FlowLayout());
		seguro.setSize(120,100);
		btnSi.addActionListener(this);
		btnNo.addActionListener(this);
		seguro.add(lblEtiqueta);
		seguro.add(btnSi);
		seguro.add(btnNo);
		seguro.addWindowListener(this);
		seguro.setResizable(false);
		seguro.setLocationRelativeTo(null);
		seguro.setVisible(true);
		
		return seguro;
	}
	
	public int borrar(Connection con,String tabla, String idCampo, int id)
	{
		int respuesta = 0;
		String sql = "DELETE FROM "+tabla+" WHERE "+idCampo+" = " + id;
		System.out.println(sql);
		try 
		{
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			sta.executeUpdate(sql);
			sta.close();
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:al hacer un Delete");
			ex.printStackTrace();
			respuesta = 1;
		}
		return respuesta;
	}
}
