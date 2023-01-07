package data;

import java.sql.*;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import business.SubUtilizadores.Jogador;
import business.SubUtilizadores.Administrador;
import business.SubUtilizadores.Utilizador;

public class UtilizadorDAO implements Map<String, Utilizador>
{

	private static UtilizadorDAO singleton = null;

	private UtilizadorDAO()
	{
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			String sql =	"CREATE TABLE IF NOT EXISTS Utilizador (" +
								"`nomeU` varchar(100) NOT NULL,"+
								"`password` varchar(20) NOT NULL,"+
								"`classificacao` int(11) NOT NULL,"+
								"`premium` tinyint(1) NOT NULL,"+
								"`admin` tinyint(1) NOT NULL,"+
								"PRIMARY KEY (`nomeU`)"+
							") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
			stm.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
	}

	public static UtilizadorDAO getInstance()
	{
		if (UtilizadorDAO.singleton == null)
			UtilizadorDAO.singleton = new UtilizadorDAO();

		return UtilizadorDAO.singleton;
	}

	@Override
	public int size()
	{
		int size = 0;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT count(*) FROM Utilizador"))
		{
			if (rs.next())
				size = rs.getInt(1);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return size;
	}

	@Override
	public boolean isEmpty() { return this.size()==0; }

	@Override
	public boolean containsKey(Object key)
	{
		boolean contains;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nomeU FROM Utilizador WHERE nomeU='" + key.toString() + "'"))
		{
			contains = rs.next();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return contains;
	}

	@Override
	public boolean containsValue(Object value)
	{
		Utilizador u = (Utilizador)value;
		return this.containsKey(u.getNome());
	}

	@Override
	public Utilizador get(Object key)
	{
		Utilizador u = null;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT * FROM Utilizador WHERE nomeU='" + key.toString() + "'"))
		{
			if (rs.next())
			{
				boolean administrador = rs.getInt("admin")==1;
				String nomeU = rs.getString("nomeU");
				String password = rs.getString("password");
				int classificacaoGlobal = rs.getInt("classificacao");
				int premium = rs.getInt("premium");
				if (administrador)
					u = new Administrador(nomeU, password, classificacaoGlobal);
				else
					u = new Jogador(nomeU, password, classificacaoGlobal, premium);

			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return u;
	}

	@Override
	public Utilizador put(String key, Utilizador utilizador)
	{
		Utilizador u = this.get(key);
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			int premium,admin;
			if (utilizador instanceof Administrador)
			{
				premium = 1;
				admin = 1;
			}
			else
			{
				premium = ((Jogador)utilizador).isPremium() ?1 :0;
				admin = 0;
			}
			String sql =	"INSERT INTO Utilizador " + 
								"VALUES ('"+utilizador.getNome() + "', '" +
											utilizador.getPassword() + "', " +
											utilizador.getClassificacaoGlobal() + ", " +
											premium + ", " +
											admin + ")" +
								"ON DUPLICATE KEY UPDATE nomeU=VALUES(nomeU), " +
														 "password=VALUES(password), " +
														 "classificacao=VALUES(classificacao), " +
														 "premium=VALUES(premium)," +
														 "admin=VALUES(admin)";
			stm.executeUpdate(sql);
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return u;
	}

	@Override
	public Utilizador remove(Object key)
	{
		Utilizador u = this.get(key);
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			stm.executeUpdate("DELETE FROM Utilizador WHERE nomeU='" + key.toString() + "'");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return u;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Utilizador> utilizadores)
	{
		for (Map.Entry<? extends String, ? extends Utilizador> entry : utilizadores.entrySet())
			this.put(entry.getKey(), entry.getValue());
	}

	@Override
	public void clear()
	{
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			stm.executeUpdate("TRUNCATE Utilizador");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
	}

	@Override
	public Set<String> keySet()
	{
		Set<String> values = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nomeU FROM Utilizador"))
		{
			while(rs.next())
				values.add(rs.getString("nomeU"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return values;
	}

	@Override 
	public Collection<Utilizador> values()
	{
		Collection<Utilizador> utilizadores = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nomeU FROM Utilizador"))
		{
			while(rs.next())
			{
				String nomeU = rs.getString("nomeU");
				Utilizador u = this.get(nomeU);
				utilizadores.add(u);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return utilizadores;
	}

	@Override 
	public Set<Entry<String, Utilizador>> entrySet()
	{
		Set<Entry<String, Utilizador>> entries = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nomeU FROM Utilizador"))
		{
			while(rs.next())
			{
				String nomeU = rs.getString("nomeU");
				Utilizador u = this.get(nomeU);
				entries.add(new AbstractMap.SimpleEntry<String,Utilizador>(nomeU, u));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return entries;
	}
}
