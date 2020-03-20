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

public class NuevoPedido extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;	
		
	Label lblFechaPedido = new Label("Fecha:");
	Label lblCliente = new Label("Cliente;");
	
	TextField txtFechaPedido = new TextField(20);
	Choice choClientes = new Choice();
	
	Button btnSiguiente = new Button("Siguiente");
	Button btnCancelar = new Button("Cancelar");
	
	Dialog error;
	Button btnSalir;
	
	public NuevoPedido() 
	{
		setTitle("Nuevo Pedido");
		setLayout(new FlowLayout());
		
		choClientes.add("Seleccionar uno...");
		// Conectar a la base de datos
		Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
		// Sacar los datos de la tabla edificios
		// Rellenar el Choice
		String sqlSelect = "SELECT * FROM clientes";
		try {
			// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlSelect);
			while (rs.next()) 
			{
				choClientes.add(rs.getInt("idCliente")+
						"-"+rs.getString("nombreCliente")+
						", "+rs.getString("nifCliente"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("ERROR:al consultar");
			ex.printStackTrace();
			dialogoError("Error", "ERROR:al consultar Clientes","","Salir");
		}
		// Cerrar la conexión
		conexionBD.desconectar(con);
		
		add(lblFechaPedido);
		add(txtFechaPedido);
		add(lblCliente);
		add(choClientes);
		add(btnSiguiente);
		add(btnCancelar);
		
		btnSiguiente.addActionListener(this);
		btnCancelar.addActionListener(this);
		addWindowListener(this);
		
		setSize(280,150);
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
		
		if(a.equals(btnCancelar)) 
		{
			setVisible(false);
		}
		else if(a.equals(btnSiguiente)) 
		{
			// Validación de la fecha
			// DateFormat
			// Validación del choice
			if(choClientes.getSelectedIndex()!=0) 
			{
				// Conectar BD
				Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
				// Hacer el INSERT
				String[] Cliente=choClientes.getSelectedItem().split("-");
				
				String fechaEuropea = txtFechaPedido.getText();
				String[] tabla = fechaEuropea.split("/");
				
				
				String fechaAmericana = tabla[2]+tabla[1]+tabla[0];
								
				System.out.println(fechaAmericana);	
				
				int respuesta = insertar(con, "pedidos", fechaAmericana,Cliente[0]);
				
				// Mostramos resultado
				if(respuesta == 0)
				{
					System.out.println("ALTA de pedido correcta");					
					setVisible(false);	
					new DetallesPedido();
				}
				else
				{
					System.out.println("Error en ALTA de cliente");
					dialogoError("Error", "ERROR:al hacer el Insert,","","Volver");
				}
				// Desconectar de la base
				conexionBD.desconectar(con);			
			}
			else 
			{
				dialogoError("Error", "ERROR:debe elegir un cliente","","Salir");
			}
		}
		else 
		{
			error.setVisible(false);
		}
	}

	
	
	public int insertar(Connection con, String tabla, String fecha, String idCliente) 
	{
		int respuesta = 0;
		try 
		{
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			String cadenaSQL = "INSERT INTO " + tabla + " (fechaPedido,idClienteFK) "
					+ "VALUES ('" + fecha + "','" + idCliente + "')";
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
