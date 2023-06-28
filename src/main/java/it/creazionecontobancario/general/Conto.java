package it.creazionecontobancario.general;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.log.SysoCounter;

import it.creazionecontobancario.database.ConnessioneDBbanca;
import it.creazionecontobancario.general.Correntista;

public abstract class Conto {
	
	protected ConnessioneDBbanca istanza;
	protected int idConto;
	protected Correntista correntista;
	protected LocalDate dataAperturaConto;
	protected double saldo;
	protected LocalDate dataUltimaGenerazioneInteressi;
	protected ArrayList<String> historyMovimenti = new ArrayList<>();
	protected double totVersamenti;
	protected double totPrelievi;
	protected double totInteressi;

	public Conto(int idConto, LocalDate dataAperturaConto, double saldo, LocalDate dataUltimaGenerazioneInteressi,
			ArrayList<String> histoArrayList, double totVersamenti, double totPrelievi, double totInteressi, Correntista correntista) {
		istanza = ConnessioneDBbanca.getInstance();
		this.idConto = idConto;
		this.correntista = correntista;
		this.dataAperturaConto = dataAperturaConto;
		this.saldo = saldo;
		this.dataUltimaGenerazioneInteressi = dataUltimaGenerazioneInteressi;
		this.historyMovimenti = historyMovimenti;
		this.totVersamenti = totVersamenti;
		this.totPrelievi = totPrelievi;
		this.totInteressi = totInteressi;
	}

	public Conto(Correntista correntista, LocalDate dataAperturaConto, double saldo) {
		istanza = ConnessioneDBbanca.getInstance();
		this.correntista = correntista;
		this.dataAperturaConto = dataAperturaConto;
		this.saldo = saldo;
	}

	public Conto() {
		istanza = ConnessioneDBbanca.getInstance();
	}


	public void versa(double importo, LocalDate data) {
		generaInteressiNuovaRegola(data);
		setSaldo(getSaldo() + importo);
		historyMovimenti.add("[" + LocalDate.now() + "] Versamento:\t+\t" + importo);
		setTotVersamenti(getTotVersamenti() + importo);
		istanza.insertOperazione(getIdConto(),LocalDate.now(),"VER",importo);
		
	}

	public void preleva(double importo, LocalDate data) {
		if (importo < getSaldo()) {
			generaInteressiNuovaRegola(data);
			double saldoAggiornato = getSaldo() - importo;
			setSaldo(saldoAggiornato);
			historyMovimenti.add("[" + LocalDate.now() + "] Prelievo:\t-\t" + importo + "\n");
			setTotPrelievi(getTotPrelievi() + importo);
			istanza.insertOperazione(getIdConto(),LocalDate.now(),"PRE",importo);
		} else {
			System.out.println("Cifra superiore al saldo! Impossibile prelevare.");
		}
	}

	// Metodo che aggiorna il saldo aggiungendo gli interessi sia su Java sia nel DB
	public void aggiornaSaldo() {
		setSaldo(getSaldo() + totInteressi);
		
	    String query = "UPDATE conto SET saldo = ? WHERE id_conto = ?";
	    try (Connection connection = istanza.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setDouble(1, getSaldo());
	        statement.setInt(2, getIdConto());
	        statement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		
	}

	// Metodo generale per recuperare il tasso di CC e CD in base al parametro
	public double getTasso(String stringa) {
		double tasso = 0.0;
        try (Connection connection = istanza.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT tasso FROM tipo_conto WHERE codice = ?")) {
               statement.setString(1, stringa); 
               ResultSet resultSet = statement.executeQuery();
               if (resultSet.next()) {
                   tasso = resultSet.getDouble("tasso");
               }
           } catch (SQLException e) {
               e.printStackTrace();
           }

           return tasso; 
	}

	public abstract void generaInteressi();

	public abstract void generaInteressi(LocalDate dataAperturaConto);

	public abstract String stampaInfoConto();

	public abstract void stampaSuPdf();

	public abstract void generaInteressiNuovaRegola(LocalDate data);

	public String getHistoryMovimenti() {
		StringBuilder sb = new StringBuilder();
		for (String movimento : historyMovimenti) {
			sb.append("- ").append(movimento).append("");
		}
		return sb.toString();
	}


	public int getIdConto() {
		return idConto;
	}

	public void setIdConto(int idConto) {
		this.idConto = idConto;
	}

	public double getTotInteressi() {
		return totInteressi;
	}

	public void setTotInteressi(double totInteressi) {
		this.totInteressi = totInteressi;
	}

	public double getTotVersamenti() {
		return totVersamenti;
	}

	public double getTotPrelievi() {
		return totPrelievi;
	}

	public void setHistoryMovimenti(ArrayList<String> historyMovimenti) {
		this.historyMovimenti = historyMovimenti;
	}

	public void setTotVersamenti(double totVersamenti) {
		this.totVersamenti = totVersamenti;
	}

	public void setTotPrelievi(double totPrelievi) {
		this.totPrelievi = totPrelievi;
	}

	public Correntista getTitolare() {
		return correntista;
	}

	public LocalDate getDataAperturaConto() {
		return dataAperturaConto;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setCorrentista(Correntista titolare) {
		this.correntista = correntista;
	}

	public void setDataAperturaConto(LocalDate dataAperturaConto) {
		this.dataAperturaConto = dataAperturaConto;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
		
	    String query = "UPDATE conto SET saldo = ? WHERE id_correntista = ?";
	    try (Connection connection = istanza.getConnection();
	         PreparedStatement statement = connection.prepareStatement(query)) {
	        statement.setDouble(1, saldo);
	        statement.setInt(2, getIdConto());
	        statement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public LocalDate getDataUltimaGenerazioneInteressi() {
		return dataUltimaGenerazioneInteressi;
	}

	public void setDataUltimaGenerazioneInteressi(LocalDate dataUltimaGenerazioneInteressi) {
		this.dataUltimaGenerazioneInteressi = dataUltimaGenerazioneInteressi;
	}

}
