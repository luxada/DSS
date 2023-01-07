package data;
import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import business.SubCriacao.Circuito;
import business.SubCriacao.ParteCircuito;
import business.SubCriacao.Chicane;
import business.SubCriacao.Curva;
import business.SubCriacao.Reta;

public class CircuitoDAO implements Map<String, Circuito>
{

	private static CircuitoDAO singleton = null;

	private CircuitoDAO()
	{
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			String sql =	"CREATE TABLE IF NOT EXISTS Circuito (" +
								"`nomeC` varchar(50) NOT NULL,"+
								"`nrVoltas` int(11) NOT NULL,"+
								"`distancia` decimal(5,2) NOT NULL,"+
								"PRIMARY KEY (`nomeC`)"+
							") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
			stm.executeUpdate(sql);
			sql =	"CREATE TABLE IF NOT EXISTS ParteCircuito (" +
						"`idParte` int(11) NOT NULL,"+
						"`parte` varchar(10) NOT NULL,"+
						"`GDU` int(11) NOT NULL,"+
						"`nomeC_fk` VARCHAR(50) NOT NULL,"+
						"PRIMARY KEY (`idParte`),"+
						"KEY `nomeC_fk` (`nomeC_fk`),"+
						"CONSTRAINT `ParteCircuito_ibfk_1`"+
							"FOREIGN KEY (`nomeC_fk`) REFERENCES `Circuito` (`nomeC`),"+
						"CONSTRAINT `TipoParteCircuito` CHECK(`parte` IN ('chicane', 'curva', 'reta'))"+
						") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
			stm.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
	}

	public static CircuitoDAO getInstance()
	{
		if (CircuitoDAO.singleton == null)
			CircuitoDAO.singleton = new CircuitoDAO();

		return CircuitoDAO.singleton;
	}

	public int nrDePartesCircuito()
	{
		int size = 0;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT count(*) FROM ParteCircuito"))
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
	public int size()
	{
		int size = 0;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT count(*) FROM Circuito"))
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
	public boolean isEmpty()
	{
		return this.size()==0;
	}


	@Override 
	public boolean containsKey(Object key)
	{
		if (!(key instanceof String))
			return false;
		boolean contains;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nomeC FROM Circuito WHERE nomeC = '" + key + "'"))
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
		if (!(value instanceof Circuito))
			return false;
		return this.containsKey(((Circuito)value).getNomeCirc());
	}

	@Override
	public Circuito put(String nomeC, Circuito circuito)
	{
		Circuito r;
		if (!this.containsKey(nomeC))
			r = null;
		else
			r = this.get(nomeC);
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			String sql =	"INSERT INTO Circuito " + 
								"VALUES ('"+ circuito.getNomeCirc() + "', " +
											 circuito.getNrVoltas() + ", " +
											 circuito.getDistancia() + ")" +
								"ON DUPLICATE KEY UPDATE  nomeC=VALUES(nomeC), " +
														 "nrVoltas=VALUES(nrVoltas), " +
														 "distancia=VALUES(distancia)";
			stm.executeUpdate(sql);

			for (ParteCircuito pc : circuito.getPartesCircuito())
			{
				sql =	"INSERT INTO ParteCircuito "+
							"VALUES ("+ pc.getID() + ", '" +
										pc.toString() + "', "+
										pc.getGDU()+ ", '" +
										circuito.getNomeCirc() +"')" +
							"ON DUPLICATE KEY UPDATE	idParte=VALUES(idParte), " +
													   "parte=VALUES(parte), " +
													   "GDU=VALUES(GDU), " + 
													   "nomeC_fk=VALUES(nomeC_fk)";
				stm.executeUpdate(sql);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		
		return r;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Circuito> m)
	{
		for (Map.Entry<? extends String, ? extends Circuito> entry : m.entrySet())
			this.put(entry.getKey(), entry.getValue());
	}

	@Override
	public Circuito remove(Object key)
	{
		Circuito r;
		if (!this.containsKey(key))
			r = null;
		else
			r = this.get(key);
		
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			String sql = "DELETE FROM Corrida WHERE nomeC_fk = '" + key + "'";
			stm.executeUpdate(sql);
			sql = "DELETE FROM ParteCircuito WHERE nomeC_fk = '" + key + "'";
			stm.executeUpdate(sql);
			sql = "DELETE FROM Circuito WHERE nomeC = '" + key + "'";
			stm.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return r;
	}

	public List<ParteCircuito> getPartesCircuitoCorrida(String nomeC)
	{
		List<ParteCircuito> partesCircuito = new ArrayList<ParteCircuito>();

		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT * FROM ParteCircuito WHERE nomeC_fk = '" + nomeC + "'"))
		{
			while(rs.next())
			{
				ParteCircuito parte = null;
				switch (rs.getString("parte"))
				{
					case "chicane":
						parte = new Chicane(rs.getInt("idParte"), rs.getInt("GDU"));
						break;
					case "curva":
						parte = new Curva(rs.getInt("idParte"), rs.getInt("GDU"));
						break;
					case "reta":
						parte = new Reta(rs.getInt("idParte"), rs.getInt("GDU"));
						break;
				}
				partesCircuito.add(parte);

			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}

		return partesCircuito;
	}

	@Override
	public void clear()
	{
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			stm.executeUpdate("TRUNCATE Corrida");
			stm.executeUpdate("TRUNCATE ParteCircuito");
			stm.executeUpdate("TRUNCATE Circuito");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}

	}

	@Override
	public Circuito get(Object key)
	{
		if (!(key instanceof String))
			return null;
		
		Circuito c = null;
		String nomeC = (String)key;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT * FROM Circuito WHERE nomeC = '" + nomeC + "'"))
		{
			if (rs.next())
			{
				nomeC = rs.getString("nomeC");
				int nrVoltas = rs.getInt("nrVoltas");
				float distancia = rs.getFloat("distancia");
				
				List<ParteCircuito> pc = this.getPartesCircuitoCorrida(nomeC);

				c = new Circuito(nomeC, nrVoltas, distancia, pc);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}

		return c;
	}

	@Override
	public Set<String> keySet()
	{
		Set<String> values = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nomeC FROM Circuito"))
		{
			while(rs.next())
				values.add(rs.getString("nomeC"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return values;
	}

	@Override 
	public Collection<Circuito> values()
	{
		Collection<Circuito> circuitos = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nomeC FROM Circuito"))
		{
			while(rs.next())
			{
				String nomeC = rs.getString("nomeC");
				Circuito c = this.get(nomeC);
				circuitos.add(c);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return circuitos;
	}

	@Override 
	public Set<Entry<String, Circuito>> entrySet()
	{
		Set<Entry<String, Circuito>> entries = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nomeC FROM Circuito"))
		{
			while(rs.next())
			{
				String nomeC = rs.getString("nomeC");
				Circuito c = this.get(nomeC);
				entries.add(new AbstractMap.SimpleEntry<String,Circuito>(nomeC, c));
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
