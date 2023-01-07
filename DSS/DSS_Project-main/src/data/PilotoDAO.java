package data;

import java.sql.*;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import business.SubCriacao.Piloto;

public class PilotoDAO implements Map<String, Piloto>
{

	private static PilotoDAO singleton = null;

	private PilotoDAO()
	{
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			String sql =	"CREATE TABLE IF NOT EXISTS Piloto (" +
								"`nome` varchar(100) NOT NULL,"+
								"`SVA` decimal(2,1) NOT NULL,"+
								"`CTS` decimal(2,1) NOT NULL,"+
								"PRIMARY KEY (`nome`)"+
							") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
			stm.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
	}

	public static PilotoDAO getInstance()
	{
		if (PilotoDAO.singleton == null)
			PilotoDAO.singleton = new PilotoDAO();

		return PilotoDAO.singleton;
	}

	@Override
	public int size()
	{
		int size = 0;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT count(*) FROM Piloto"))
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
				ResultSet rs = stm.executeQuery("SELECT nome FROM Piloto WHERE nome='" + key + "'"))
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
		Piloto p = (Piloto)value;
		return this.containsKey(p.getNome());
	}

	@Override
	public Piloto get(Object key)
	{
		Piloto u = null;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT * FROM Piloto WHERE nome='" + key.toString() + "'"))
		{
			if (rs.next())
			{
				u = new Piloto(	rs.getString("nome"),
								rs.getFloat("SVA"),
								rs.getFloat("CTS"));
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
	public Piloto put(String key, Piloto piloto)
	{
		Piloto p = this.get(key);
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			String sql =	"INSERT INTO Piloto " + 
								"VALUES ('"+piloto.getNome() + "', " +
											piloto.getSVA() + ", " +
											piloto.getCTS() + ")" +
								"ON DUPLICATE KEY UPDATE nome=VALUES(nome), " +
														 "SVA=VALUES(SVA), " +
														 "CTS=VALUES(CTS)";
			stm.executeUpdate(sql);
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return p;
	}

	@Override
	public Piloto remove(Object key)
	{
		Piloto u = this.get(key);
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			stm.executeUpdate("DELETE FROM Piloto WHERE nome='" + key + "'");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return u;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Piloto> pilotos)
	{
		for (Map.Entry<? extends String, ? extends Piloto> entry : pilotos.entrySet())
			this.put(entry.getKey(), entry.getValue());
	}

	@Override
	public void clear()
	{
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			stm.executeUpdate("TRUNCATE Piloto");
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
				ResultSet rs = stm.executeQuery("SELECT nome FROM Piloto"))
		{
			while(rs.next())
				values.add(rs.getString("nome"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return values;
	}

	@Override 
	public Collection<Piloto> values()
	{
		Collection<Piloto> pilotos = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nome FROM Piloto"))
		{
			while(rs.next())
			{
				String nome = rs.getString("nome");
				Piloto u = this.get(nome);
				pilotos.add(u);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return pilotos;
	}

	@Override 
	public Set<Entry<String, Piloto>> entrySet()
	{
		Set<Entry<String, Piloto>> entries = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nome FROM Piloto"))
		{
			while(rs.next())
			{
				String nome = rs.getString("nome");
				Piloto u = this.get(nome);
				entries.add(new AbstractMap.SimpleEntry<String,Piloto>(nome, u));
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
