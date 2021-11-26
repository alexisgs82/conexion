package conexion;

import java.sql.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * 
 * @author James F. Gomez V. 2021-11-20 Simulador-Transaccion-SubGrupo-30
 */
public class Conexion {

	public static void main(String[] args) {
		Conexion con = new Conexion();
		con.conectar();
	}

	Scanner seleccionTransaccion = new Scanner(System.in);

	public Connection conectar() {
		Connection con = null;
		PreparedStatement pstm = null;
		// Agregar libreria al path de Mysql, version que maneja actual es 8.0.27 y SO
		// Ubuntu 20
		// Conexion driver para Mysql
		String driver = "com.mysql.cj.jdbc.Driver";
		String user = "root";
		String pwd = "Liberty1";
		String url = "jdbc:mysql://localhost:3306/banco_db";

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, pwd);
			System.out.println("Conexión establecida con éxito");
			System.out.println(
					"Seleccione la transacción que desee de acuerdo al número: \n1. Crear cliente \n" + "2. Salir \n");
			int transaccion = seleccionTransaccion.nextInt();
			switch (transaccion) {
			case 1:
				crearCliente(con, pstm);
				break;
			default:
				System.out.println("Salio sin realizar algun tipo de transacción");
				break;
			}
		} catch (Exception e) {
			System.out.println("Error. No se conecto " + e);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return con;
	}

	// DAO - Create
	public void crearCliente(Connection con, PreparedStatement pstm) throws SQLException {
		System.out.println("Iniciando creación de cliente");
		String sql = "INSERT INTO `banco_db`.`cliente` (`id_cliente`, `nombres`, `apellidos`, "
				+ "`numero_identificacion`, `fecha_nacimiento`, `telefono`, `email`, `direccion`)";

		sql += "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		System.out.println("id_cliente: ");
		int id_cliente = seleccionTransaccion.nextInt();
		// int id_cliente = JOptionPane.showInputDialog("id_cliente: ");
		// String name = JOptionPane.showInputDialog("Nombre: ");
		String name = JOptionPane.showInputDialog("Nombre: ");
		// JOptionPane.showMessageDialog(null, "Hello " + name);
		pstm = con.prepareStatement(sql);
		pstm.setInt(1, id_cliente);
		pstm.setString(2, name);
		pstm.setString(3, "Goo g");
		pstm.setString(4, "400879");
		pstm.setString(5, "1991-01-25");
		pstm.setLong(6, 6059999);
		pstm.setString(7, "siri@mail.com");
		pstm.setString(8, "Diag 1 # 99-99");

		int aplique = pstm.executeUpdate();
		if (aplique == 1) {
			System.out.println(aplique + " fila insertada con éxito");
			// Nota : esto confirmará implícitamente la transacción activa y creará una
			// nueva
			con.setAutoCommit(false);
			con.commit();
		} else {
			con.rollback();
			throw new RuntimeException("Error insertando la fila");
		}
	}

}
