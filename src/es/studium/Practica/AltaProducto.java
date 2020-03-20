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

public class AltaProducto extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	Label lblNombreProducto = new Label("Nombre:");
	Label lblDescripcionProducto = new Label("Descripción:");
	Label lblPrecioVentaProducto = new Label("Precio Venta:");
	Label lblPrecioCosteProducto = new Label("Precio Coste:");
	
	TextField txtNombreProducto = new TextField(20);
	TextField txtDescripcionProducto = new TextField(20);
	TextField txtPrecioVentaProducto = new TextField(20);
	TextField txtPrecioCosteProducto = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnOtro = new Button("Aceptar y Otro");
	Button btnLimpiar = new Button("Limpiar");
	
	Dialog error;
	Button btnSalir;
	
	public AltaProducto() 
	{
		setTitle("ALTA de Cliente");
		setLayout(new FlowLayout());		
		
		add(lblNombreProducto);
		add(txtNombreProducto);
		add(lblDescripcionProducto);
		add(txtDescripcionProducto);
		add(lblPrecioVentaProducto);
		add(txtPrecioVentaProducto);
		add(lblPrecioCosteProducto);
		add(txtPrecioCosteProducto);
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
			txtNombreProducto.selectAll();
			txtNombreProducto.setText("");
			txtNombreProducto.requestFocus();
			txtDescripcionProducto.selectAll();
			txtDescripcionProducto.setText("");
			txtPrecioVentaProducto.selectAll();
			txtPrecioVentaProducto.setText("");
			txtPrecioCosteProducto.selectAll();
			txtPrecioCosteProducto.setText("");			
		}
		else if(a.equals(btnAceptar)) 
		{
			// Conectar BD
			Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
			// Hacer el INSERT
			int respuesta = insertar(con, "productos", txtNombreProducto.getText(),txtDescripcionProducto.getText(),txtPrecioVentaProducto.getText(),txtPrecioCosteProducto.getText());
			
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
			int respuesta = insertar(con, "productos", txtNombreProducto.getText(),txtDescripcionProducto.getText(),txtPrecioVentaProducto.getText(),txtPrecioCosteProducto.getText());
			
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
			txtNombreProducto.selectAll();
			txtNombreProducto.setText("");
			txtNombreProducto.requestFocus();
			txtDescripcionProducto.selectAll();
			txtDescripcionProducto.setText("");
			txtPrecioVentaProducto.selectAll();
			txtPrecioVentaProducto.setText("");
			txtPrecioCosteProducto.selectAll();
			txtPrecioCosteProducto.setText("");				
		}
		else 
		{
			error.setVisible(false);
		}
	}
	
	public int insertar(Connection con, String tabla, String nombre, String descripcion, String precioVenta, String precioCoste) 
	{
		int respuesta = 0;
		try 
		{
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			String cadenaSQL = "INSERT INTO " + tabla + " (nombreProducto,DescripcionProducto,precioVentaProducto,precioCosteProducto) "
					+ "VALUES ('" + nombre + "','" + descripcion + "','" + precioVenta + "','" + precioCoste + "')";
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
