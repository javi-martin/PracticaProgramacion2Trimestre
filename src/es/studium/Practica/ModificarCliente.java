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

public class ModificarCliente extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblClienteBorrar = new Label("Producto a borrar:");
	Choice choCliente = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Frame modificarCliente;
	TextField txtIdCliente;
	TextField txtNombreCliente;
	TextField txtnifCliente;
	TextField txtDireccionCliente;
	TextField txtLocalidadCliente;
	TextField txtProvinciaCliente;
	TextField txtCpCliente;
	TextField txtTelefonoCliente;
	TextField txtEmailCliente;
	Button btnAceptarCambios;
	Button btnCancelarCambios;
	
	Dialog seguro;
	Button btnSi;
	Button btnNo;
	
	Dialog error = new Dialog(this,"ERROR", true);
	Button btnSalir;
	
	public ModificarCliente() 
	{
		setTitle("Modificar clientes");
		setLayout(new FlowLayout());
		
		choCliente.add("Seleccionar uno...");
		// Conectar a la base de datos
		Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
		// Sacar los datos de la tabla productos
		// Rellenar el Choice
		String sqlSelect = "SELECT * FROM clientes";
		try {
			// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlSelect);
			while (rs.next()) 
			{
				choCliente.add(rs.getInt("idCliente")+
						"-"+rs.getString("nombreCliente")+
						", "+rs.getString("nifCliente"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("ERROR:al consultar");
			ex.printStackTrace();
			dialogoError("Error", "ERROR:al consultar clientes","","Salir");
		}
		// Cerrar la conexión
		conexionBD.desconectar(con);
		
		add(choCliente);
		add(btnAceptar);
		add(btnLimpiar);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		addWindowListener(this);
		setSize(250,200);
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
		else if(modificarCliente.isActive())
		{	
			modificarCliente.setVisible(false);
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
			choCliente.select(0);
		}
		else if(a.equals(btnAceptar)) 
		{
			if(choCliente.getSelectedIndex()!=0) 
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
			modificarCliente.setVisible(false);
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
			int id = Integer.parseInt(txtIdCliente.getText());
			String nombre = txtNombreCliente.getText();
			String nif = txtnifCliente.getText();
			String direccion = txtDireccionCliente.getText();
			String localidad = txtLocalidadCliente.getText();
			String provincia = txtProvinciaCliente.getText();
			int cp = Integer.parseInt(txtCpCliente.getText());
			int telefono = Integer.parseInt(txtTelefonoCliente.getText());
			String email = txtEmailCliente.getText();
			// Conectar a BD
			Connection con = conexionBD.conectar("imprentaP","root","Studium2019;"); 
			// Ejecutar el UPDATE
			String sql ="UPDATE clientes SET nombreCliente = '"+nombre+"', nifCliente = '"+nif+"', direccionCliente = '"+direccion+"', localidadCliente = '"+localidad+"', provinciaCliente = '"+provincia+"', cpCliente = '"+cp+"', telefonoCliente = '"+telefono+"', emailCliente = '"+email+"' WHERE idCliente="+id;
			try {
				// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
				Statement stmt = con.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();				
			} catch (SQLException ex) {
				System.out.println("ERROR:al consultar");
				ex.printStackTrace();
				System.out.println("Error en Modificar cliente");
				dialogoError("Error", "Error en Modificar cliente","","Salir");
			}
			
			seguro.setVisible(false);
			System.out.println("Modificado de cliente correcto");
			dialogoError("Modificado", "Modificado de cliente correcto","","Salir");
			
			// Actualizar el Choice
			choCliente.removeAll();
			choCliente.add("Seleccionar uno...");
			String sqlSelect = "SELECT * FROM clientes";
			try {
				// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sqlSelect);
				while (rs.next()) 
				{
					choCliente.add(rs.getInt("idCliente")+
							"-"+rs.getString("nombreCliente")+
							", "+rs.getString("nifCliente"));
				}
				rs.close();
				stmt.close();
			} catch (SQLException ex) {
				System.out.println("ERROR:al consultar");
				ex.printStackTrace();
				dialogoError("Error", "ERROR:al consultar clientes","","Salir");
			}
			// Cerrar la conexión
			conexionBD.desconectar(con);			
		}
		else 
		{
			error.setVisible(false);
		}
	}
	
	public void mostrarDatos(Connection con, int idCliente, TextField id, TextField nombre, TextField nifCliente, TextField direccionCliente, TextField localidadCliente, TextField provinciaCliente, TextField cpCliente, TextField telefonoCliente, TextField emailCliente)
	{
		String sql = "SELECT * FROM clientes WHERE idCliente = "+idCliente;
		try 
		{
			id.setText(idCliente+"");
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery(sql);
			while(rs.next())
			{
				String n = rs.getString("nombreCliente");
				nombre.setText(n);
				String nif = rs.getString("nifCliente");
				nifCliente.setText(nif);
				String direccion = rs.getString("direccionCliente");
				direccionCliente.setText(direccion);
				String localidad = rs.getString("localidadCliente");
				localidadCliente.setText(localidad);
				String provincia = rs.getString("provinciaCliente");
				provinciaCliente.setText(provincia);
				String cp = rs.getString("cpCliente");
				cpCliente.setText(cp);
				String telefono = rs.getString("telefonoCliente");
				telefonoCliente.setText(telefono);
				String email = rs.getString("emailCliente");
				emailCliente.setText(email);
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
		modificarCliente = new Frame("Modificar Cliente");
		modificarCliente.setLayout(new FlowLayout());
		Label lblIdCliente = new Label("idCliente");
		Label lblNombreCliente = new Label("Nombre:   ");
		Label lblNifCliente = new Label("NIF/DNI:   ");
		Label lblDireccionCliente = new Label("Dirección:");
		Label lblLocalidadCliente = new Label("Localidad:");
		Label lblProvinciaCliente = new Label("Provincia:");
		Label lblCpCliente = new Label("CP:         ");
		Label lblTelefonoCliente = new Label("Teléfono:");
		Label lblEmailCliente = new Label("Email:    ");
		
		txtIdCliente = new TextField(20);
		txtNombreCliente = new TextField(20);
		txtnifCliente = new TextField(20);
		txtDireccionCliente = new TextField(20);
		txtLocalidadCliente = new TextField(20);
		txtProvinciaCliente = new TextField(20);
		txtCpCliente = new TextField(20);
		txtTelefonoCliente = new TextField(20);
		txtEmailCliente = new TextField(20);
		
		btnAceptarCambios = new Button("Aceptar");
		btnCancelarCambios = new Button("Cancelar");
		
		modificarCliente.add(lblIdCliente);
		modificarCliente.add(txtIdCliente);
		txtIdCliente.setEnabled(false);
		modificarCliente.add(lblNombreCliente);
		modificarCliente.add(txtNombreCliente);
		modificarCliente.add(lblNifCliente);
		modificarCliente.add(txtnifCliente);
		modificarCliente.add(lblDireccionCliente);
		modificarCliente.add(txtDireccionCliente);
		modificarCliente.add(lblLocalidadCliente);
		modificarCliente.add(txtLocalidadCliente);
		modificarCliente.add(lblProvinciaCliente);
		modificarCliente.add(txtProvinciaCliente);
		modificarCliente.add(lblCpCliente);
		modificarCliente.add(txtCpCliente);
		modificarCliente.add(lblTelefonoCliente);
		modificarCliente.add(txtTelefonoCliente);
		modificarCliente.add(lblEmailCliente);
		modificarCliente.add(txtEmailCliente);
		
		modificarCliente.add(btnAceptarCambios);
		modificarCliente.add(btnCancelarCambios);
		
		modificarCliente.addWindowListener(this);
		btnAceptarCambios.addActionListener(this);
		btnCancelarCambios.addActionListener(this);
		
		// Sacar el id del elemento elegido
		int id = Integer.parseInt(choCliente.getSelectedItem().split("-")[0]);		
		// Pero relleno-->
		// Conectar a la base de datos
		Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
		// Seleccionar los datos del elemento
		mostrarDatos(con, id, txtIdCliente, txtNombreCliente, txtnifCliente, txtDireccionCliente, txtLocalidadCliente, txtProvinciaCliente, txtCpCliente, txtTelefonoCliente, txtEmailCliente);
		//Desconectar la base de datos
		conexionBD.desconectar(con);
		
		modificarCliente.setSize(280,350);
		modificarCliente.setResizable(false);
		modificarCliente.setLocationRelativeTo(null);
		modificarCliente.setVisible(true);
		
		return modificarCliente;
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
