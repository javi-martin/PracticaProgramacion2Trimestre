package es.studium.Practica;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DetallesPedido extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	Panel panel1 = new Panel();
	Panel panel2 = new Panel();
	Panel panel3 = new Panel();
	Panel panel4 = new Panel();
	Panel panel5 = new Panel();
	Panel lineaTitulos = new Panel();
	Panel linea1 = new Panel();
	Panel linea2 = new Panel();
	Panel linea3 = new Panel();
	
	Label lblPedido = new Label("PEDIDO Nº:");
	TextField txtIdPedido = new TextField(5);
	
	Label lblArticulo = new Label("Artículo:");
	Choice choProducto = new Choice();
	Label lblCantidad = new Label("Cantidad:");
	TextField txtCantidad = new TextField(5);
	Button btnAgregar = new Button("Agregar");	
	
	Label lblSubtotal = new Label("Subtotal:");
	TextField txtSubtotal = new TextField(10);
	Label lblIva = new Label("Iva 21%:");
	TextField txtIva = new TextField(5);
	Label lblTotal = new Label("Total:");
	TextField txtTotal = new TextField(10);
	
	Button btnFinalizar = new Button("Finalizar");
	Button btnAnular = new Button("Anular");
	
	Label lblId = new Label("id          ");
	Label lblNombre = new Label("Articulo                                 ");
	Label lblCantidadLinea = new Label("Cantidad");
	Label lblPrecio = new Label("Precio");
	Label lblSubtotalLinea = new Label("Subtotal");
	
	TextField idLinea1 = new TextField(5);
	TextField nombreLinea1 = new TextField(20);
	TextField cantidadLinea1 = new TextField(5);
	TextField precioLinea1 = new TextField(5);
	TextField subtotalLinea1 = new TextField(5);
	
	TextField idLinea2 = new TextField(5);
	TextField nombreLinea2 = new TextField(20);
	TextField cantidadLinea2 = new TextField(5);
	TextField precioLinea2 = new TextField(5);
	TextField subtotalLinea2 = new TextField(5);
	
	TextField idLinea3 = new TextField(5);
	TextField nombreLinea3 = new TextField(20);
	TextField cantidadLinea3 = new TextField(5);
	TextField precioLinea3 = new TextField(5);
	TextField subtotalLinea3 = new TextField(5);
	
	Dialog error;
	Button btnSalir;
	
	Dialog seguro;
	Button btnSi;
	Button btnNo;
	
	Float subtotal = (float) 0;
	
	
	public DetallesPedido()
	{
		setTitle("Detalles de Pedido");
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
		
		add(panel1);
		add(panel2);
		add(panel3);
		panel1.add(lblPedido);
		panel1.add(txtIdPedido);
		txtIdPedido.setEnabled(false);
		// Rellanar el TextField txtIdPedido 
		
		// Sacar el ultimo id de la tabla pedidos
		String  ultimoId = "SELECT * FROM pedidos";
				
		try {
			// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(ultimoId);
			rs.last();
			int id = rs.getInt("idPedido");
			txtIdPedido.setText(id+"");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Cerrar la conexión
		conexionBD.desconectar(con);
		
		panel2.add(lblArticulo);
		panel2.add(choProducto);
		panel3.add(lblCantidad);
		panel3.add(txtCantidad);
		txtCantidad.setText("0");
		panel3.add(btnAgregar);
		
		add(lineaTitulos);
		lineaTitulos.add(lblId);
		lineaTitulos.add(lblNombre);
		lineaTitulos.add(lblCantidadLinea);
		lineaTitulos.add(lblPrecio);
		lineaTitulos.add(lblSubtotalLinea);
		
		add(linea1);
		linea1.add(idLinea1);
		idLinea1.setText("0");
		linea1.add(nombreLinea1);
		linea1.add(cantidadLinea1);
		linea1.add(precioLinea1);
		linea1.add(subtotalLinea1);
		subtotalLinea1.setText("0");
		
		add(linea2);
		linea2.add(idLinea2);
		idLinea2.setText("0");
		linea2.add(nombreLinea2);
		linea2.add(cantidadLinea2);
		linea2.add(precioLinea2);
		linea2.add(subtotalLinea2);
		subtotalLinea2.setText("0");
		
		add(linea3);
		linea3.add(idLinea3);
		idLinea3.setText("0");
		linea3.add(nombreLinea3);
		linea3.add(cantidadLinea3);
		linea3.add(precioLinea3);
		linea3.add(subtotalLinea3);
		subtotalLinea3.setText("0");		
		
		add(panel4);
		add(panel5);
		panel4.add(lblSubtotal);
		panel4.add(txtSubtotal);
		panel4.add(lblIva);
		panel4.add(txtIva);
		panel4.add(lblTotal);
		panel4.add(txtTotal);
		panel5.add(btnFinalizar);
		panel5.add(btnAnular);
		
		addWindowListener(this);
		btnAgregar.addActionListener(this);
		btnFinalizar.addActionListener(this);
		btnAnular.addActionListener(this);
		
		setSize(550,400);		
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
		if(a.equals(btnAgregar)) 
		{
			String vacio = "0";
			int cantidadInicial = txtCantidad.getText().compareTo(vacio);
			
			if(choProducto.getSelectedIndex()!=0 && cantidadInicial!=0) 
			{				
				String idRepetido = choProducto.getSelectedItem().split("-")[0];
				
				// Conectar a la base de datos
				Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");			
				
				if(idLinea1.getText().equals(vacio)) 
				{
					mostrarDatos(con, idLinea1, nombreLinea1, cantidadLinea1, precioLinea1, subtotalLinea1);					
				}
				else if(idLinea1.getText().equals(idRepetido)) 
				{
					String idProducto = choProducto.getSelectedItem().split("-")[0];
					int cantidad = Integer.parseInt(cantidadLinea1.getText())+Integer.parseInt(txtCantidad.getText());
					Float subtotalLinea = cantidad*Float.parseFloat(precioLinea1.getText());
										
					cantidadLinea1.setText(cantidad+"");
					subtotalLinea1.setText(subtotalLinea+"");					
					txtCantidad.setText(cantidad+"");
					borrar(con, "aparece","idProductoFK", idProducto);
				}
				else if(idLinea2.getText().equals(vacio)) 
				{
					mostrarDatos(con, idLinea2, nombreLinea2, cantidadLinea2, precioLinea2, subtotalLinea2);
					
				}
				else if(idLinea2.getText().equals(idRepetido)) 
				{
					String idProducto = choProducto.getSelectedItem().split("-")[0];
					int cantidad = Integer.parseInt(cantidadLinea2.getText())+Integer.parseInt(txtCantidad.getText());
					Float subtotalLinea = cantidad*Float.parseFloat(precioLinea2.getText());					
					
					cantidadLinea2.setText(cantidad+"");
					subtotalLinea2.setText(subtotalLinea+"");					
					txtCantidad.setText(cantidad+"");
					borrar(con, "aparece","idProductoFK", idProducto);
				}
				else if(idLinea3.getText().equals(vacio)) 
				{
					mostrarDatos(con, idLinea3, nombreLinea3, cantidadLinea3, precioLinea3, subtotalLinea3);					
				}
				else if(idLinea3.getText().equals(idRepetido)) 
				{
					String idProducto = choProducto.getSelectedItem().split("-")[0];
					int cantidad = Integer.parseInt(cantidadLinea3.getText())+Integer.parseInt(txtCantidad.getText());
					Float subtotalLinea = cantidad*Float.parseFloat(precioLinea3.getText());
					
					cantidadLinea3.setText(cantidad+"");
					subtotalLinea3.setText(subtotalLinea+"");					
					txtCantidad.setText(cantidad+"");
					borrar(con, "aparece","idProductoFK", idProducto);
				}
				else 
				{
					dialogoError("Error", "Debe finalizar pedido","y hacer un pedido nuevo","Salir");
				}
				
				subtotal = Float.parseFloat(subtotalLinea1.getText())+Float.parseFloat(subtotalLinea2.getText())+Float.parseFloat(subtotalLinea3.getText());
				txtSubtotal.setText(subtotal+"");
				Float iva = (float) (subtotal*0.21);
				txtIva.setText(iva+"");
				Float total = subtotal + iva;
				txtTotal.setText(total+"");
				
				insertar(con);
				
				choProducto.select(0);
				txtCantidad.setText("0");
				// Cerrar la conexión
				conexionBD.desconectar(con);
			}
			else 
			{
				dialogoError("Error", "Debe elegir un","producto y/o cantidad","Salir");
			}
			
		}
		
		else if(a.equals(btnFinalizar)) 
		{
			// Conectar a la base de datos
			Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
			
			update(con);
			setVisible(false);
			
			// Cerrar la conexión
			conexionBD.desconectar(con);
		}
		else if(a.equals(btnAnular)) 
		{
			dialogoSeguro();
		}
		else if(a.equals(btnNo)) 
		{
			seguro.setVisible(false);
		}
		else if(a.equals(btnSi)) 
		{
			String id = txtIdPedido.getText();
			// Conectar a la base de datos
			Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
			
			borrar(con,"aparece", "idPedidoFK",id);
			
			borrar(con, "pedidos", "idPedido", id);
			
			setVisible(false);
			
			dialogoError("Pedido", "Pedido ANULADO","","Salir");
			
			// Cerrar la conexión
			conexionBD.desconectar(con);
		}
		else 
		{
			error.setVisible(false);
		}
	}
	
	public void insertar(Connection con) 
	{
		String idProducto = choProducto.getSelectedItem().split("-")[0];
		try 
		{
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			String cadenaSQL = "INSERT INTO aparece (idProductoFk,idPedidoFK, cantidadProducto) "
					+ "VALUES ('" + idProducto + "','" + txtIdPedido.getText() + "','" + txtCantidad.getText() + "')";
			System.out.println(cadenaSQL);
			sta.executeUpdate(cadenaSQL);
			sta.close();
		} 
		catch (Exception ex) 
		{
			System.out.println("ERROR:al hacer un Insert");
			ex.printStackTrace();
			
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
	
	public void mostrarDatos(Connection con, TextField id, TextField nombre, TextField cantidad, TextField precioVenta, TextField subtotalLinea)
	{
		// Sacar el id del elemento elegido
		int idProducto = Integer.parseInt(choProducto.getSelectedItem().split("-")[0]);
		String sql = "SELECT * FROM productos WHERE idProducto = "+idProducto;
		try 
		{			
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery(sql);
			while(rs.next())
			{
				String idlinea = rs.getString("idProducto");
				id.setText(idlinea);
				String n = rs.getString("nombreProducto");
				nombre.setText(n);
				String c = txtCantidad.getText();
				cantidad.setText(c);
				String pv = rs.getString("precioVentaProducto");
				precioVenta.setText(pv);
				String sub = (Float.parseFloat(txtCantidad.getText()))*(Float.parseFloat(rs.getString("precioVentaProducto")))+"";
				subtotalLinea.setText(sub);
				
				subtotal = (Float.parseFloat(txtCantidad.getText()))*(Float.parseFloat(rs.getString("precioVentaProducto")));
				
				
			}
			sta.close();
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:al hacer el SELECT");
			ex.printStackTrace();
		}
	}
	public Dialog update(Connection con) 
	{
		int id = Integer.parseInt(txtIdPedido.getText());
		
		String sql ="UPDATE pedidos SET subtotalPedido = '"+txtSubtotal.getText()+"', ivaPedido = '"+txtIva.getText()+"' WHERE idPedido="+id;
		
		try {
			// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
			Statement stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();				
		} catch (SQLException ex) {
			System.out.println("ERROR:en Modificar pedido");
			ex.printStackTrace();
			System.out.println("Error en Modificar pedido");
			dialogoError("Error", "Error en Modificar pedido","","Salir");
		}
		
		return dialogoError("Pedido", "Pedido finalizado correctamente.","","Salir"); 
	}
	public void borrar(Connection con, String tabla, String idFK, String id)
	{
				
		String sql = "DELETE FROM "+tabla+" WHERE "+idFK+"  = "+id;
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
			System.out.println("ERROR:al anular pedido");
			ex.printStackTrace();
			dialogoError("Error", "ERROR:al anular pedido","","Salir");
		}
		
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
}
