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
import java.sql.Connection;
import java.sql.Statement;

public class AltaCliente extends Frame implements WindowListener, ActionListener
{

	private static final long serialVersionUID = 1L;
	
	Label lblNombreCliente = new Label("Nombre:   ");
	Label lblNifCliente = new Label("NIF/DNI:   ");
	Label lblDireccionCliente = new Label("Dirección:");
	Label lblLocalidadCliente = new Label("Localidad:");
	Label lblProvinciaCliente = new Label("Provincia:");
	Label lblCpCliente = new Label("CP:         ");
	Label lblTelefonoCliente = new Label("Teléfono:");
	Label lblEmailCliente = new Label("Email:    ");
	
	TextField txtNombreCliente = new TextField(20);
	TextField txtNifCliente = new TextField(20);
	TextField txtDireccionCliente = new TextField(20);
	TextField txtLocalidadCliente = new TextField(20);
	TextField txtProvinciaCliente = new TextField(20);
	TextField txtCpCliente = new TextField(20);
	TextField txtTelefonoCliente = new TextField(20);
	TextField txtEmailCliente = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnOtro = new Button("Aceptar y Otro");
	Button btnLimpiar = new Button("Limpiar");
	
	Dialog error;
	Button btnSalir;
	
	public AltaCliente() 
	{
		setTitle("ALTA de Cliente");
		setLayout(new FlowLayout());		
		
		add(lblNombreCliente);
		add(txtNombreCliente);
		add(lblNifCliente);
		add(txtNifCliente);
		add(lblDireccionCliente);
		add(txtDireccionCliente);
		add(lblLocalidadCliente);
		add(txtLocalidadCliente);
		add(lblProvinciaCliente);
		add(txtProvinciaCliente);
		add(lblCpCliente);
		add(txtCpCliente);
		add(lblTelefonoCliente);
		add(txtTelefonoCliente);
		add(lblEmailCliente);
		add(txtEmailCliente);
		add(btnAceptar);
		add(btnOtro);
		add(btnLimpiar);
		
		btnAceptar.addActionListener(this);
		btnOtro.addActionListener(this);
		btnLimpiar.addActionListener(this);		
		addWindowListener(this);
		
		setSize(280,300);
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
			txtNombreCliente.selectAll();
			txtNombreCliente.setText("");
			txtNombreCliente.requestFocus();
			txtNifCliente.selectAll();
			txtNifCliente.setText("");
			txtDireccionCliente.selectAll();
			txtDireccionCliente.setText("");
			txtLocalidadCliente.selectAll();
			txtLocalidadCliente.setText("");
			txtProvinciaCliente.selectAll();
			txtProvinciaCliente.setText("");
			txtCpCliente.selectAll();
			txtCpCliente.setText("");
			txtTelefonoCliente.selectAll();
			txtTelefonoCliente.setText("");
			txtEmailCliente.selectAll();
			txtEmailCliente.setText("");
		}
		else if(a.equals(btnAceptar)) 
		{
			// Conectar BD
			Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
			// Hacer el INSERT
			int respuesta = insertar(con, "clientes", txtNombreCliente.getText(),txtNifCliente.getText(),txtDireccionCliente.getText(),txtLocalidadCliente.getText(),txtProvinciaCliente.getText(),txtCpCliente.getText(),txtTelefonoCliente.getText(),txtEmailCliente.getText());
			
			// Mostramos resultado
			if(respuesta == 0)
			{
				System.out.println("ALTA de cliente correcta");
				dialogoError("Alta Cliente", "ALTA de cliente correcta","","Salir");
				setVisible(false);				
			}
			else
			{
				System.out.println("Error en ALTA de cliente");
				dialogoError("Error", "ERROR:al hacer un Insert","Datos introducidos incorrectos.","Volver");
			}
			// Desconectar de la base
			conexionBD.desconectar(con);
			
		}
		else if(a.equals(btnOtro))
		{
			// Conectar BD
			Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
			// Hacer el INSERT
			int respuesta = insertar(con, "clientes", txtNombreCliente.getText(),txtNifCliente.getText(),txtDireccionCliente.getText(),txtLocalidadCliente.getText(),
								txtProvinciaCliente.getText(),txtCpCliente.getText(),txtTelefonoCliente.getText(),txtEmailCliente.getText());
			
			// Mostramos resultado
			if(respuesta == 0)
			{
				System.out.println("ALTA de cliente correcta");
				dialogoError("Alta Cliente", "ALTA de cliente correcta","","Volver");
			}
			else
			{
				System.out.println("Error en ALTA de cliente");
				dialogoError("Error", "ERROR:al hacer un Insert","Datos introducidos incorrectos.","Volver");
			}
			// Desconectar de la base
			conexionBD.desconectar(con);
			
			txtNombreCliente.selectAll();
			txtNombreCliente.setText("");
			txtNombreCliente.requestFocus();
			txtNifCliente.selectAll();
			txtNifCliente.setText("");
			txtDireccionCliente.selectAll();
			txtDireccionCliente.setText("");
			txtLocalidadCliente.selectAll();
			txtLocalidadCliente.setText("");
			txtProvinciaCliente.selectAll();
			txtProvinciaCliente.setText("");
			txtCpCliente.selectAll();
			txtCpCliente.setText("");
			txtTelefonoCliente.selectAll();
			txtTelefonoCliente.setText("");
			txtEmailCliente.selectAll();
			txtEmailCliente.setText("");
		}
		else 
		{
			error.setVisible(false);
		}
	}	
	

	public int insertar(Connection con, String tabla, String nombre, String nif, String direccion, String localidad, String provincia, String cp, String telefono, String email) 
	{
		int respuesta = 0;
		try 
		{
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			String cadenaSQL = "INSERT INTO " + tabla + " (nombreCliente,nifCliente,direccionCliente,localidadCliente,provinciaCliente, cpCliente, telefonoCliente, emailCliente) "
					+ "VALUES ('" + nombre + "','" + nif + "','" + direccion + "','" + localidad + "','" + provincia + "'," + cp + "," + telefono + ",'" + email + "')";
			System.out.println(cadenaSQL);
			sta.executeUpdate(cadenaSQL);
			sta.close();
		} 
		catch (Exception ex) 
		{
			System.out.println("ERROR:al hacer un Insert");
			ex.printStackTrace();
			respuesta = 1;
		}		
		return respuesta;
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
}