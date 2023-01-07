package data;

import java.sql.*;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import business.SubCriacao.C1;
import business.SubCriacao.C1H;
import business.SubCriacao.C2;
import business.SubCriacao.C2H;
import business.SubCriacao.GT;
import business.SubCriacao.GTH;
import business.SubCriacao.SC;
import business.SubCriacao.Carro;
import business.SubCriacao.MarcaModelo;

public class CarroDAO implements Map<MarcaModelo, Carro>
{

	private static CarroDAO singleton = null;

	private CarroDAO()
	{
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			String sql =	"CREATE TABLE IF NOT EXISTS Carro (" +
								"`marca` varchar(50) NOT NULL,"+
								"`modelo` varchar(50) NOT NULL,"+
								"`PAC` decimal(2,1) NOT NULL,"+
								"`potMotorCombs` int(11) NOT NULL,"+
								"`cilindrada` int(11) NOT NULL,"+
								"`potMotorEle` int(11) DEFAULT NULL,"+
								"`tipoCarro` varchar(5) NOT NULL," + 
								"CONSTRAINT `MarcaModelo` PRIMARY KEY (`marca`, `modelo`),"+
								"CONSTRAINT `CheckTipo` CHECK(`tipoCarro` IN ('C1', 'C1H', 'C2', 'C2H', 'GT', 'GTH', 'SC')),"+
								"CONSTRAINT `CheckHibrido` CHECK((`potMotorEle` IS NOT NULL AND `tipoCarro` LIKE '%H%') OR"+
								" (`potMotorEle` IS NULL AND NOT(`tipoCarro` LIKE '%H%')))"+
								") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";
			stm.executeUpdate(sql);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
	}

	public static CarroDAO getInstance()
	{
		if (CarroDAO.singleton == null)
			CarroDAO.singleton = new CarroDAO();

		return CarroDAO.singleton;
	}

	@Override
	public int size()
	{
		int size = 0;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT count(*) FROM Carro"))
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
		if (!(key instanceof MarcaModelo))
			return false;
		boolean contains;
		MarcaModelo chave = (MarcaModelo)key;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT marca, modelo FROM Carro WHERE marca='" + chave.getMarca() + "' AND modelo='" + chave.getModelo() + "'"))
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
		Carro c = (Carro)value;
		return this.containsKey(c.getMarcaModelo());
	}

	@Override
	public Carro get(Object key)
	{
		if (!(key instanceof MarcaModelo))
			return null;
		MarcaModelo chave = (MarcaModelo)key;
		Carro u = null;
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT * FROM Carro WHERE marca='" + chave.getMarca() + "' AND modelo='" + chave.getModelo() + "'"))
		{
			if (rs.next())
			{
				String tipo = rs.getString("tipoCarro");
				switch(tipo)
				{
					case "C1": 
						u = new C1(rs.getString("marca"), rs.getString("modelo"), rs.getFloat("PAC"), rs.getInt("potMotorCombs"), rs.getInt("cilindrada"));
						break;
					case "C1H":
						u = new C1H(rs.getString("marca"), rs.getString("modelo"), rs.getFloat("PAC"), rs.getInt("potMotorCombs"), rs.getInt("cilindrada"), rs.getInt("potMotorEle"));
						break;
					case "C2":
						u = new C2(rs.getString("marca"), rs.getString("modelo"), rs.getFloat("PAC"), rs.getInt("potMotorCombs"), rs.getInt("cilindrada"));
						break;
					case "C2H":
						u = new C2H(rs.getString("marca"), rs.getString("modelo"), rs.getFloat("PAC"), rs.getInt("potMotorCombs"), rs.getInt("cilindrada"), rs.getInt("potMotorEle"));
						break;
					case "GT":
						u = new GT(rs.getString("marca"), rs.getString("modelo"), rs.getFloat("PAC"), rs.getInt("potMotorCombs"), rs.getInt("cilindrada"));
						break;
					case "GTH":
						u = new GTH(rs.getString("marca"), rs.getString("modelo"), rs.getFloat("PAC"), rs.getInt("potMotorCombs"), rs.getInt("cilindrada"), rs.getInt("potMotorEle"));
						break;
					case "SC": 
						u = new SC(rs.getString("marca"), rs.getString("modelo"), rs.getFloat("PAC"), rs.getInt("potMotorCombs"), rs.getInt("cilindrada"));
						break;
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return u;
	}


	private String valuesString (Carro carro)
	{
		String marca = carro.getMarca();
		String modelo = carro.getModelo();
		float PAC  = carro.getPac();
		int potMotorCombs = carro.getPotMotorCombs();
		int cilindrada = carro.getCilindrada();
		Integer potMotorEle;
		String tipo;
		if (carro instanceof C1)
		{
			potMotorEle = null;
			tipo = "C1";
		}
		else if (carro instanceof C1H)
		{
			potMotorEle = ((C1H)carro).getPotMotorEle();
			tipo = "C1H";
		}
		else if (carro instanceof C2)
		{
			potMotorEle = null;
			tipo = "C2";
		}
		else if (carro instanceof C2H)
		{
			potMotorEle = ((C2H)carro).getPotMotorEle();
			tipo = "C2H";
		}
		else if (carro instanceof GT)
		{
			potMotorEle = null;
			tipo = "GT";
		}
		else if (carro instanceof GTH)
		{
			potMotorEle = ((GTH)carro).getPotMotorEle();
			tipo = "GTH";
		}
		else
		{
			potMotorEle = null;
			tipo = "SC";
		}
		return "VALUES('" + marca + "', '" + modelo + "', " + PAC + ", " +
				potMotorCombs + ", " + cilindrada + ", " + potMotorEle + ", '" + tipo + "')";
	}

	@Override
	public Carro put(MarcaModelo key, Carro carro)
	{
		Carro u = this.get(key);
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{

			String sql =	"INSERT INTO Carro " + 
								this.valuesString(carro) +
							"ON DUPLICATE KEY UPDATE  marca=VALUES(marca), " +
													 "modelo=VALUES(modelo), " +
													 "PAC=VALUES(PAC)," +
													 "potMotorCombs=VALUES(potMotorCombs),"+
													 "cilindrada=VALUES(cilindrada),"+
													 "potMotorEle=VALUES(potMotorEle),"+
													 "tipoCarro=VALUES(tipoCarro)";
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
	public Carro remove(Object key)
	{
		if (!(key instanceof MarcaModelo))
			return null;
		MarcaModelo chave = (MarcaModelo) key;
		Carro u = this.get(key);
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			stm.executeUpdate("DELETE FROM Carro WHERE marca='" + chave.getMarca() + "' AND modelo='" + chave.getModelo() + "'");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return u;
	}

	@Override
	public void putAll(Map<? extends MarcaModelo, ? extends Carro> carros)
	{
		for (Map.Entry<? extends MarcaModelo, ? extends Carro> entry : carros.entrySet())
			this.put(entry.getKey(), entry.getValue());
	}

	@Override
	public void clear()
	{
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement())
		{
			stm.executeUpdate("TRUNCATE Carro");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
	}

	@Override
	public Set<MarcaModelo> keySet()
	{
		Set<MarcaModelo> values = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT marca, modelo FROM Carro"))
		{
			while(rs.next())
				values.add(new MarcaModelo(rs.getString("marca"), rs.getString("modelo")));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return values;
	}

	@Override 
	public Collection<Carro> values()
	{
		Collection<Carro> carros = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT marca, modelo FROM Carro"))
		{
			while(rs.next())
			{
				MarcaModelo mm = new MarcaModelo(rs.getString("marca"), rs.getString("modelo"));
				Carro c = this.get(mm);
				carros.add(c);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new NullPointerException(e.getMessage());
		}
		return carros;
	}

	@Override 
	public Set<Entry<MarcaModelo, Carro>> entrySet()
	{
		Set<Entry<MarcaModelo, Carro>> entries = new HashSet<>();
		try (	Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
				Statement stm = conn.createStatement();
				ResultSet rs = stm.executeQuery("SELECT marca,modelo FROM Carro"))
		{
			while(rs.next())
			{
				MarcaModelo mm = new MarcaModelo(rs.getString("marca"), rs.getString("modelo"));
				Carro c = this.get(mm);
				entries.add(new AbstractMap.SimpleEntry<MarcaModelo,Carro>(mm, c));
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
