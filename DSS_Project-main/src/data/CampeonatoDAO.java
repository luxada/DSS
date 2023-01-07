package data;

import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import business.SubCampeonatos.Campeonato;
import business.SubCampeonatos.Corrida;

public class CampeonatoDAO implements Map<String, Campeonato>
{

	private static CampeonatoDAO singleton = null;

	private CampeonatoDAO()
	{
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			String sql =	"CREATE TABLE IF NOT EXISTS Campeonato (" +
								"`nomeCamp` varchar(100) NOT NULL,"+
								"PRIMARY KEY (`nomeCamp`)"+
							") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
			stm.executeUpdate(sql);
			sql =	"CREATE TABLE IF NOT EXISTS Circuito (" +
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
			sql =	"CREATE TABLE IF NOT EXISTS Corrida (" +
						"`idCorrida` int(11) NOT NULL,"+
						"`nomeC_fk` VARCHAR(50) NOT NULL,"+
						"`nomeCamp_fk` varchar(100) NOT NULL,"+
						"PRIMARY KEY (`idCorrida`),"+
						"KEY `nomeC_fk` (`nomeC_fk`),"+
						"KEY `nomeCamp_fk` (`nomeCamp_fk`),"+
						"CONSTRAINT `Corrida_ibfk_1`"+
							"FOREIGN KEY (`nomeC_fk`) REFERENCES `Circuito`(`nomeC`),"+
						"CONSTRAINT `Cirruda_ibfk_2`"+
							"FOREIGN KEY (`nomeCamp_fk`) REFERENCES `Campeonato`(`nomeCamp`)"+
					") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
			stm.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
	}

	public static CampeonatoDAO getInstance()
	{
		if (CampeonatoDAO.singleton == null)
			CampeonatoDAO.singleton = new CampeonatoDAO();

		return CampeonatoDAO.singleton;
	}

	public int nrOfCorridas()
	{
		int size = 0;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT count(*) FROM Corrida"))
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
				ResultSet rs = stm.executeQuery("SELECT count(*) FROM Campeonato"))
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
				ResultSet rs = stm.executeQuery("SELECT nomeCamp FROM Campeonato WHERE nomeCamp = '" + key + "'"))
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
		if (!(value instanceof Campeonato))
			return false;
		return this.containsKey(((Campeonato)value).getNome());
	}

	@Override
	public Campeonato put(String key, Campeonato campeonato)
	{
		Campeonato r = this.get(key);
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			String sql =	"INSERT INTO Campeonato " + 
								"VALUES ('"+	campeonato.getNome() + "')"+
								"ON DUPLICATE KEY UPDATE  nomeCamp=VALUES(nomeCamp)";
			stm.executeUpdate(sql);

			for (Corrida c : campeonato.getCorridas())
			{
				sql =	"INSERT INTO Corrida "+
							"VALUES ("+ c.getID() + ", '" +
										c.getCircuito().getNomeCirc()+ "', '" +
										campeonato.getNome() +"')" +
							"ON DUPLICATE KEY UPDATE	idCorrida=VALUES(idCorrida), " +
													   "nomeC_fk=VALUES(nomeC_fk), " + 
													   "nomeCamp_fk=VALUES(nomeCamp_fk)";
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
	public void putAll(Map<? extends String, ? extends Campeonato> m)
	{
		for (Map.Entry<? extends String, ? extends Campeonato> entry : m.entrySet())
			this.put(entry.getKey(), entry.getValue());
	}

	@Override
	public Campeonato remove(Object key)
	{
		Campeonato c = this.get(key);
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			String sql = "DELETE FROM Corrida WHERE nomeCamp_fk = '" + key + "'";
			stm.executeUpdate(sql);
			sql = "DELETE FROM Campeonato WHERE nomeCamp = '" + key + "'";
			stm.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return c;
	}

	public List<Corrida> getCorridasCampeonato(String nomeCamp)
	{
		List<Corrida> partesCampeonato = new ArrayList<Corrida>();

		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT idCorrida, nomeC_fk FROM Corrida WHERE nomeCamp_fk = '" + nomeCamp + "'"))
		{
			while(rs.next())
				partesCampeonato.add(new Corrida(	rs.getInt("idCorrida"),
													CircuitoDAO.getInstance().get(rs.getString("nomeC_fk"))));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}

		return partesCampeonato;
	}

	@Override
	public void clear()
	{
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			stm.executeUpdate("TRUNCATE Corrida");
			stm.executeUpdate("TRUNCATE Campeonato");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}

	}

	@Override
	public Campeonato get(Object key)
	{
		if (!(key instanceof String))
			return null;
		
		Campeonato c = null;
		String nomeCamp = (String)key;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT * FROM Campeonato WHERE nomeCamp = '" + nomeCamp + "'"))
		{
			if (rs.next())
			{
				List<Corrida> corridas = this.getCorridasCampeonato(nomeCamp);
				c = new Campeonato(nomeCamp, corridas);
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
				ResultSet rs = stm.executeQuery("SELECT nomeCamp FROM Campeonato"))
		{
			while(rs.next())
				values.add(rs.getString("nomeCamp"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return values;
	}

	@Override 
	public Collection<Campeonato> values()
	{
		Collection<Campeonato> campeonatos = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nomeCamp FROM Campeonato"))
		{
			while(rs.next())
			{
				String nomeCamp = rs.getString("nomeCamp");
				Campeonato c = this.get(nomeCamp);
				campeonatos.add(c);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return campeonatos;
	}

	@Override 
	public Set<Entry<String, Campeonato>> entrySet()
	{
		Set<Entry<String, Campeonato>> entries = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT nomeCamp FROM Campeonato"))
		{
			while(rs.next())
			{
				String nomeCamp = rs.getString("nomeCamp");
				Campeonato c = this.get(nomeCamp);
				entries.add(new AbstractMap.SimpleEntry<String,Campeonato>(nomeCamp, c));
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
