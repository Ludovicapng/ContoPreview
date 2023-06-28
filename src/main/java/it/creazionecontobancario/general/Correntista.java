package it.creazionecontobancario.general;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import it.creazionecontobancario.database.ConnessioneDBbanca;

public class Correntista {

	protected ConnessioneDBbanca istanza;
	protected int id_correntista;
	protected String nome;
	protected String cognome;
	protected String citta;
	protected String nazione;
	protected String telefono;

	public Correntista(int id_correntista, String nome, String cognome, String citta, String nazione, String telefono) {
		this.id_correntista = id_correntista;
		this.nome = nome;
		this.cognome = cognome;
		this.citta = citta;
		this.nazione = nazione;
		this.telefono = telefono;

		istanza = ConnessioneDBbanca.getInstance();
	}

	public int getId_correntista() {
		return id_correntista;
	}

	public String getNome() {
        try (Connection connection = istanza.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT nome FROM correntista WHERE id_correntista = ?")) {
               statement.setInt(1, getId_correntista()); 
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                   nome = resultSet.getString("nome");
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }

           return nome; 
           }

	public String getCognome() {
	    try (Connection connection = istanza.getConnection();
	            PreparedStatement statement = connection.prepareStatement("SELECT cognome FROM correntista WHERE id_correntista = ?")) {
	           statement.setInt(1, getId_correntista()); 
	           ResultSet resultSet = statement.executeQuery();
	           if (resultSet.next()) {
	               cognome = resultSet.getString("cognome");
	           }
	       } catch (SQLException e) {
	           e.printStackTrace();
	       }

	       return cognome; 
	}

	public String getCitta() {
	    try (Connection connection = istanza.getConnection();
	            PreparedStatement statement = connection.prepareStatement("SELECT citta FROM correntista WHERE id_correntista = ?")) {
	           statement.setInt(1, getId_correntista()); 
	           ResultSet resultSet = statement.executeQuery();
	           if (resultSet.next()) {
	               citta = resultSet.getString("citta");
	           }
	       } catch (SQLException e) {
	           e.printStackTrace();
	       }

	       return citta; 
	}

	public String getNazione() {
	    try (Connection connection = istanza.getConnection();
	            PreparedStatement statement = connection.prepareStatement("SELECT nazione FROM correntista WHERE id_correntista = ?")) {
	           statement.setInt(1, getId_correntista()); 
	           ResultSet resultSet = statement.executeQuery();
	           if (resultSet.next()) {
	               nazione = resultSet.getString("nazione");
	           }
	       } catch (SQLException e) {
	           e.printStackTrace();
	       }

	       return nazione; 
	}

	public String getTelefono() {
	    try (Connection connection = istanza.getConnection();
	            PreparedStatement statement = connection.prepareStatement("SELECT telefono FROM correntista WHERE id_correntista = ?")) {
	           statement.setInt(1, getId_correntista()); 
	           ResultSet resultSet = statement.executeQuery();
	           if (resultSet.next()) {
	               telefono = resultSet.getString("telefono");
	           }
	       } catch (SQLException e) {
	           e.printStackTrace();
	       }

	       return telefono; 
	}


	public void setId_correntista(int id_correntista) {
		this.id_correntista = id_correntista;
	}

	public void setNome(String nome) {
		String query = "UPDATE correntista SET nome = ? WHERE id_correntista = ?";
		try (Connection connection = istanza.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, nome);
			statement.setInt(2, getId_correntista());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.nome = nome;
	}

	public void setCognome(String cognome) {
		String query = "UPDATE correntista SET cognome = ? WHERE id_correntista = ?";
		try (Connection connection = istanza.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, cognome);
			statement.setInt(2, getId_correntista());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.cognome = cognome;
	}

	public void setCitta(String citta) {
		String query = "UPDATE citta SET nome = ? WHERE id_correntista = ?";
		try (Connection connection = istanza.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, citta);
			statement.setInt(2, getId_correntista());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.citta = citta;
	}

	public void setNazione(String nazione) {
		String query = "UPDATE correntista SET nazione = ? WHERE id_correntista = ?";
		try (Connection connection = istanza.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, nazione);
			statement.setInt(2, getId_correntista());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.nazione = nazione;
	}

	public void setTelefono(String telefono) {
		String query = "UPDATE correntista SET nome = ? WHERE id_correntista = ?";
		try (Connection connection = istanza.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, telefono);
			statement.setInt(2, getId_correntista());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.telefono = telefono;
	}

}
