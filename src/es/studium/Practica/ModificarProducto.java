package es.studium.Practica;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ModificarProducto extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblProductoBorrar = new Label("Producto a borrar:");
	Choice choProducto = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Frame modificarProductos;
	TextField txtIdProducto;
	TextField txtNombreProducto;
	TextField txtDescripcionProducto;
	TextField txtPrecioVentaProducto;
	TextField txtPrecioCosteProducto;
	Button btnAceptarCambios;
	Button btnCancelarCambios;
	
	Dialog seguro;
	Button btnSi;
	Button btnNo;
	
	Dialog error = new Dialog(this,"ERROR", true);
	Button btnSalir;
	
	public ModificarProducto() 
	{
		setTitle("Modificar productos");
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
		else if(error.isActive()) 
		{
			error.setVisible(false);
		}
		else if(modificarProductos.isActive())
		{	
			modificarProductos.setVisible(false);
			setVisible(true);
		}
		else 
		{			
			seguro.setVisible(false);
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
				modificar();
				setVisible(false);
			}
			else 
			{
				dialogoError("Error", "Debe elegir un producto.","","Salir");
			}
		}
		else if(a.equals(btnCancelarCambios)) 
		{
			modificarProductos.setVisible(false);
			setVisible(true);
		}
		else if(a.equals(btnAceptarCambios)) 
		{
			dialogoSeguro();			
		}
		else if(a.equals(btnNo))
		{
			seguro.setVisible(false);
		}
		else if(a.equals(btnSi))
		{				
			//hacer los cambios
			int id = Integer.parseInt(txtIdProducto.getText());
			String nombre = txtNombreProducto.getText();
			String descripcion = txtDescripcionProducto.getText();
			Float precioVenta = Float.parseFloat(txtPrecioVentaProducto.getText());
			Float precioCoste = Float.parseFloat(txtPrecioCosteProducto.getText());
			// Conectar a BD
			Connection con = conexionBD.conectar("imprentaP","root","Studium2019;"); 
			// Ejecutar el UPDATE
			String sql ="UPDATE productos SET nombreProducto = '"+nombre+"', descripcionProducto = '"+descripcion+"', precioVentaProducto = '"+precioVenta+"', precioCosteProducto = '"+precioCoste+"' WHERE idProducto="+id;
			try {
				// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
				Statement stmt = con.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();				
			} catch (SQLException ex) {
				System.out.println("ERROR:al consultar");
				ex.printStackTrace();
				System.out.println("Error en Modificar producto");
				dialogoError("Error", "Error en Modificar producto","","Salir");
			}
			
			seguro.setVisible(false);
			System.out.println("Modificado de producto correcto");
			dialogoError("Modificado", "Modificado de producto correcto","","Salir");
			
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
		}
		else 
		{
			error.setVisible(false);
		}
	}
	
	public void mostrarDatos(Connection con, int idProducto, TextField id, TextField nombre, TextField descripcion, TextField precioVenta, TextField precioCoste)
	{
		String sql = "SELECT * FROM productos WHERE idProducto = "+idProducto;
		try 
		{
			id.setText(idProducto+"");
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery(sql);
			while(rs.next())
			{
				String n = rs.getString("nombreProducto");
				nombre.setText(n);
				String d = rs.getString("descripcionProducto");
				descripcion.setText(d);
				String pv = rs.getString("precioVentaProducto");
				precioVenta.setText(pv);
				String pc = rs.getString("precioCosteProducto");
				precioCoste.setText(pc);
			}
			sta.close();
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:al hacer el SELECT");
			ex.printStackTrace();
		}
	}
	
	public Frame modificar() 
	{
		modificarProductos = new Frame("Modificar Producto");
		modificarProductos.setLayout(new FlowLayout());
		Label lblIdProducto = new Label("idProducto");
		Label lblNombreProducto = new Label("Nombre:");
		Label lblDescripcionProducto = new Label("Descripción:");
		Label lblPrecioVentaProducto = new Label("Precio Venta:");
		Label lblPrecioCosteProducto = new Label("Precio Coste:");
		
		txtIdProducto = new TextField(20);
		txtNombreProducto = new TextField(20);
		txtDescripcionProducto = new TextField(20);
		txtPrecioVentaProducto = new TextField(20);
		txtPrecioCosteProducto = new TextField(20);
		
		btnAceptarCambios = new Button("Aceptar");
		btnCancelarCambios = new Button("Cancelar");
		
		modificarProductos.add(lblIdProducto);
		modificarProductos.add(txtIdProducto);
		txtIdProducto.setEnabled(false);
		modificarProductos.add(lblNombreProducto);
		modificarProductos.add(txtNombreProducto);
		modificarProductos.add(lblDescripcionProducto);
		modificarProductos.add(txtDescripcionProducto);
		modificarProductos.add(lblPrecioVentaProducto);
		modificarProductos.add(txtPrecioVentaProducto);
		modificarProductos.add(lblPrecioCosteProducto);
		modificarProductos.add(txtPrecioCosteProducto);
		modificarProductos.add(btnAceptarCambios);
		modificarProductos.add(btnCancelarCambios);
		
		modificarProductos.addWindowListener(this);
		btnAceptarCambios.addActionListener(this);
		btnCancelarCambios.addActionListener(this);
		
		// Sacar el id del elemento elegido
		int id = Integer.parseInt(choProducto.getSelectedItem().split("-")[0]);		
		// Pero relleno-->
		// Conectar a la base de datos
		Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
		// Seleccionar los datos del elemento
		mostrarDatos(con, id, txtIdProducto, txtNombreProducto, txtDescripcionProducto, txtPrecioVentaProducto, txtPrecioCosteProducto);
		//Desconectar la base de datos
		conexionBD.desconectar(con);
		
		modificarProductos.setSize(280,300);
		modificarProductos.setResizable(false);
		modificarProductos.setLocationRelativeTo(null);
		modificarProductos.setVisible(true);
		
		return modificarProductos;
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
		Label lblEtiqueta = new Label("¿Está seguro de modificar?");
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
}
