package es.studium.Practica;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class menuPrincipal extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	MenuBar barraMenu = new MenuBar();
	
	Menu mnuPedidos = new Menu("Pedidos");
	Menu mnuClientes = new Menu("Clientes");
	Menu mnuProductos = new Menu("Productos");
	
	
	MenuItem mniNuevoPedido = new MenuItem("Nuevo");
	MenuItem mniModificacionPedido = new MenuItem("Modificación");
	MenuItem mniConsultaPedido = new MenuItem("Consulta");
		
	MenuItem mniAltaClientes = new MenuItem("Alta");
	MenuItem mniBajaClientes = new MenuItem("Baja");	
	MenuItem mniModificaClientes = new MenuItem("Modificación");
	MenuItem mniConsultaClientes = new MenuItem("Consulta");
	
	MenuItem mniAltaProductos = new MenuItem("Alta");
	MenuItem mniBajaProductos = new MenuItem("Baja");
	MenuItem mniModificaProductos = new MenuItem("Modificación");
	MenuItem mniConsultaProductos = new MenuItem("Consulta");	
	
	menuPrincipal()
	{
		setTitle("Imprenta");
		setLayout(new FlowLayout());
		mnuPedidos.add(mniNuevoPedido);
		mnuPedidos.add(mniModificacionPedido);
		mnuPedidos.add(mniConsultaPedido);		
		
		mnuClientes.add(mniAltaClientes);
		mnuClientes.add(mniBajaClientes);
		mnuClientes.add(mniModificaClientes);
		mnuClientes.add(mniConsultaClientes);
		
		mnuProductos.add(mniAltaProductos);
		mnuProductos.add(mniBajaProductos);
		mnuProductos.add(mniModificaProductos);
		mnuProductos.add(mniConsultaProductos);
								
		barraMenu.add(mnuPedidos);
		barraMenu.add(mnuClientes);
		barraMenu.add(mnuProductos);
				
		addWindowListener(this);
		
		mniNuevoPedido.addActionListener(this);
		mniModificacionPedido.addActionListener(this);
		mniConsultaPedido.addActionListener(this);
		
		mniAltaClientes.addActionListener(this);
		mniBajaClientes.addActionListener(this);
		mniModificaClientes.addActionListener(this);
		mniConsultaClientes.addActionListener(this);
		
		mniAltaProductos.addActionListener(this);
		mniBajaProductos.addActionListener(this);
		mniModificaProductos.addActionListener(this);
		mniConsultaProductos.addActionListener(this);
		
		setMenuBar(barraMenu);
		setSize(400,150);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		setVisible(false);
		new Login();
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	
	public void actionPerformed(ActionEvent evento) 
	{
		Object a;
		a=evento.getSource();
		
		if(a.equals(mniNuevoPedido)) 
		{
			new NuevoPedido();
		}
		else if(a.equals(mniModificacionPedido)) 
		{
			
		}
		else if(a.equals(mniConsultaPedido)) 
		{
			new ConsultaPedido();
		}
		else if(a.equals(mniAltaClientes))
		{
			new AltaCliente();			
		}
		else if(a.equals(mniBajaClientes)) 
		{
			new BajaCliente();
		}
		else if(a.equals(mniModificaClientes)) 
		{
			new ModificarCliente();
		}
		else if(a.equals(mniConsultaClientes)) 
		{
			new ConsultaCliente();
		}
		else if(a.equals(mniAltaProductos))
		{
			new AltaProducto();
		}
		else if(a.equals(mniBajaProductos))
		{
			new BajaProducto();
		}
		else if(a.equals(mniModificaProductos))
		{
			new ModificarProducto();
		}
		else if(a.equals(mniConsultaProductos))
		{
			new ConsultaProducto();
		}
	}
	
	public void sinPermisos() 
	{			
			
		mniModificacionPedido.setEnabled(false);
		mniConsultaPedido.setEnabled(false);
		mniBajaClientes.setEnabled(false);
		mniModificaClientes.setEnabled(false);
		mniConsultaClientes.setEnabled(false);
		mniBajaProductos.setEnabled(false);
		mniModificaProductos.setEnabled(false);
		mniConsultaProductos.setEnabled(false);
						
	}
}