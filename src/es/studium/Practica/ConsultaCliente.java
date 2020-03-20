package es.studium.Practica;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConsultaCliente extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	TextArea consulta = new TextArea(10,60);
	Button btnAceptar = new Button("Aceptar");
	Button btnPdf = new Button("Exportar a PDF");
	
	public ConsultaCliente() 
	{
		setTitle("Consulta de Clientes");
		setLayout(new FlowLayout());
		// Conectar a la base de datos
		Connection con = conexionBD.conectar("imprentaP","root","Studium2019;");
		// Seleccionar de la tabla edificios
		// Sacar la información
		rellenarTextArea(con, consulta);
		// Cerrar la conexión
		conexionBD.desconectar(con);
		consulta.setEditable(false);
		add(consulta);
		add(btnAceptar);
		add(btnPdf);
		btnAceptar.addActionListener(this);
		btnPdf.addActionListener(this);
		addWindowListener(this);
		setSize(500,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		setVisible(false);
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	
	public void actionPerformed(ActionEvent evento) 
	{
		Object a;
		a=evento.getSource();
		if(a.equals(btnAceptar))
		{
			setVisible(false);
		}
		else
		{
			System.out.println("Exportando a PDF...");
		}
	}
	
	public void rellenarTextArea(Connection con, TextArea t)
	{
		String sqlSelect = "SELECT * FROM clientes";
		try {
			// CREAR UN STATEMENT PARA UNA CONSULTA SELECT
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(sqlSelect);
			while (rs.next()) 
			{
				if(t.getText().length()==0)
				{
					t.setText(rs.getInt("idCliente")+
							"-"+rs.getString("nombreCliente")+
							", "+rs.getString("nifCliente")+
							", "+rs.getString("direccionCliente")+
							", "+rs.getString("localidadCliente")+
							", "+rs.getString("provinciaCliente")+
							", "+rs.getString("cpCliente")+
							", "+rs.getString("telefonoCliente")+
							", "+rs.getString("emailCliente"));
				}
				else
				{
					t.setText(t.getText() + "\n" +
							rs.getInt("idCliente")+
							"-"+rs.getString("nombreCliente")+
							", "+rs.getString("nifCliente")+
							", "+rs.getString("direccionCliente")+
							", "+rs.getString("localidadCliente")+
							", "+rs.getString("provinciaCliente")+
							", "+rs.getString("cpCliente")+
							", "+rs.getString("telefonoCliente")+
							", "+rs.getString("emailCliente"));
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			System.out.println("ERROR:al consultar");
			ex.printStackTrace();
		}
	}
}
