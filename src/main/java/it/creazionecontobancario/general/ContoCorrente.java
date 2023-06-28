package it.creazionecontobancario.general;

import java.io.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.text.DecimalFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import it.creazionecontobancario.database.ConnessioneDBbanca;
import it.creazionecontobancario.general.Correntista;

public class ContoCorrente extends Conto {

	double tasso = getTasso("CC");
	
	public ContoCorrente(int idConto, Correntista correntista, LocalDate dataAperturaConto, double saldo) {
		super(correntista, dataAperturaConto, saldo);
	}
	

	@Override
	public void generaInteressi() {
		double calcoloInteressi = getSaldo() * tasso;
		setTotInteressi(getTotInteressi() + calcoloInteressi);
		setDataUltimaGenerazioneInteressi(LocalDate.now());
	}

	@Override
	public void generaInteressi(LocalDate data) { // Modifica 1
		double calcoloInteressi = (tasso / 365) * ChronoUnit.DAYS.between(getDataAperturaConto(), data) * getSaldo(); // differenza data apertura conto e data attuale
		setTotInteressi(getTotInteressi() + calcoloInteressi);
		setDataUltimaGenerazioneInteressi(data);
	}

	@Override
	public void generaInteressiNuovaRegola(LocalDate data) { // Modifica 2
		if (LocalDate.now().isAfter(getDataAperturaConto().plusYears(1))) {
			double calcoloInteressi = (tasso / 365) * ChronoUnit.DAYS.between(getDataUltimaGenerazioneInteressi(), data) * getSaldo();
			setTotInteressi(getTotInteressi() + calcoloInteressi);
			setDataUltimaGenerazioneInteressi(data);
		}
	}

	@Override
	public String stampaInfoConto() {
		DecimalFormat df = new DecimalFormat("#.##");
		aggiornaSaldo();
		
		return "=*=*= Informazioni sul Conto =*=*=\nTitolare: " + getTitolare() + "\nData apertura conto: "
				+ getDataAperturaConto() + "\nTasso di interesse: " + tasso + "\nSaldo attuale: " + df.format(getSaldo())
				+ "\nData dell'ultima generazione di interessi: " + getDataUltimaGenerazioneInteressi() + "\n\nStorico movimenti:\n" + getHistoryMovimenti() + "\n\nTotale versamenti (+):" + getTotVersamenti() + "\nTotale prelievi (-):" + getTotPrelievi();
	}
	
	public void stampaSuPdf(int id_conto) {
		
		// Aggiungere il recupero dei dati dal DB per stampare su pdf
		
		ConnessioneDBbanca istanza = ConnessioneDBbanca.getInstance();
		
	    String codice = null;
	    String cognome = null;
	    String codice_tipo = null;
	    LocalDate data = LocalDate.now();
	    String dataFormatted = data.toString();
	    
	    try (Connection connection = istanza.getConnection()) {
	    	String query = "SELECT c.codice_tipo, co.cognome, cc.id_conto " +
	                "FROM conto c " +
	                "JOIN conto_correntista cc ON c.id_conto = cc.id_conto " +
	                "JOIN correntista co ON cc.id_correntista = co.id_correntista " +
	                "WHERE c.id_conto = ?";
	       
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setInt(1, id_conto);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    codice_tipo = resultSet.getString("tipo_conto");
	                    cognome = resultSet.getString("cognome");
	                    id_conto = resultSet.getInt("id_conto");
	                }
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		Document contoCorrenteDOC = new Document();

	    String fileName = "EC_" + codice_tipo + "_" + cognome + "_" + id_conto + "_" + dataFormatted + ".pdf";
	    String path = "/Users/ludo/Desktop/" + fileName;

		
		try {
			PdfWriter writer = PdfWriter.getInstance(contoCorrenteDOC, new FileOutputStream(path));
			contoCorrenteDOC.open();
			contoCorrenteDOC.add(new Paragraph(stampaInfoConto()));
			contoCorrenteDOC.close();
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}


	@Override
	public void stampaSuPdf() {
		
	}

}
