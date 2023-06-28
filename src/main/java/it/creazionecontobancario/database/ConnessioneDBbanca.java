package it.creazionecontobancario.database;

import java.sql.*;
import java.sql.Date;
import java.io.*;
import java.util.*;

import javax.lang.model.element.QualifiedNameable;

import it.creazionecontobancario.general.Conto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class ConnessioneDBbanca extends Conto {
	
	private String dbConnection;
	private String dbName;
	private String dbUser;
	private String dbPassword;
	private Connection connection;
	
	private ConnessioneDBbanca() {
		super();
		Properties prop = new Properties();
		try (InputStream inputStream = ConnessioneDBbanca.class.getClassLoader().getResourceAsStream("db.properties")) {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			prop.load(inputStream);
			dbConnection = prop.getProperty("url");
			dbName = prop.getProperty("name");
			dbUser = prop.getProperty("user");
			dbPassword = prop.getProperty("password");
			
			connection = DriverManager.getConnection(dbConnection + dbName, dbUser, dbPassword);

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ConnessioneDBbanca getInstance() {
		return Holder.INSTANCE;
	}
	
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbConnection + dbName, dbUser, dbPassword);
    }
    
    public void closeConnection() throws SQLException {
        if ( getConnection() != null) {
            try {
                getConnection().close();
                System.out.println("Connessione chiusa.");
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
	
	private static class Holder {
		private static final ConnessioneDBbanca INSTANCE = new ConnessioneDBbanca();
	}
	
    public void executeQuery(String query) {
        if (connection == null)
            return;
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } 
        catch (SQLException e) {
        	System.out.println("ERRORE " + e.getMessage());
        }
    }
    
    
    // Insert nella tabella Correntista
    public void insertCorrentista(String nome, String cognome, String citta, String nazione, String telefono) {
        String query = "INSERT INTO correntista (nome, cognome, citta, nazione, telefono) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome);
            statement.setString(2, cognome);
            statement.setString(3, citta);
            statement.setString(4, nazione);
            statement.setString(5, telefono);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    // Insert nella tabella Conto
    public void insertConto(String codice_tipo, int id_correntista, String data_apertura, String data_chiusura) {
        // Formato stringa per la data: Yyyy-mm-dd
        java.sql.Date date1 = null;
        java.sql.Date date2 = null;

        try {
            date1 = java.sql.Date.valueOf(data_apertura);
            date2 = java.sql.Date.valueOf(data_chiusura);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        String query = "INSERT INTO conto (codice_tipo, id_correntista, data_apertura, data_chiusura) VALUES (?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, codice_tipo);
            statement.setInt(2, id_correntista);
            statement.setDate(3, date1);
            statement.setDate(4, date2);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    // Metodo per effettuare l'insert nella tabella tipo_conto
    public void insertTipoConto(String codice, double tasso, String descrizione) {
        String query = "INSERT INTO tipo_conto (codice, tasso, descrizione) VALUES (?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, codice);
            statement.setDouble(2, tasso);
            statement.setString(3, descrizione);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Metood per aggiungere i movimenti nella tabella Operazioni
    public void insertOperazione(int id_conto, LocalDate dataOperazione, String codice_operazione, double quantita) {
        // Formato stringa per la data: Yyyy-mm-dd
        java.sql.Date data_operazione = null;
        try {
            data_operazione = java.sql.Date.valueOf(dataOperazione.toString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        String query = "INSERT INTO operazione (id_conto, data_operazione, codice_operazione, quantita, saldo) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id_conto);
            statement.setDate(2, data_operazione);
            statement.setString(3, codice_operazione);
            statement.setDouble(4, quantita);
            if (codice_operazione.equals("VER")) {
                setSaldo(getSaldo() + quantita);
            } else if (codice_operazione.equals("PRE")) {
                double saldo = getSaldo() - quantita;
                if (getSaldo() > quantita) {
                    setSaldo(getSaldo() - quantita);
                }
            }
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void generaInteressi() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generaInteressi(LocalDate dataAperturaConto) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String stampaInfoConto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stampaSuPdf() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void generaInteressiNuovaRegola(LocalDate data) {
		// TODO Auto-generated method stub
		
	}
	
}
