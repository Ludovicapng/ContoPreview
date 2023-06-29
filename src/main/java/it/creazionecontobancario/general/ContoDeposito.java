package it.creazionecontobancario.general;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import it.creazionecontobancario.general.Correntista;

public class ContoDeposito extends Conto {
	
	double tasso = getTasso("CD");

	public ContoDeposito(Correntista correntista, LocalDate dataAperturaConto, double saldo) {
		super(correntista, dataAperturaConto, saldo);
	}

	@Override
	public void generaInteressi() { // generaInteressi base
		double calcoloInteressi = getSaldo() * tasso;
		setTotInteressi(getTotInteressi() + calcoloInteressi);
		setDataUltimaGenerazioneInteressi(LocalDate.now());
	}
	
	@Override
	public void generaInteressi(LocalDate data) { // Modifica 1
		double calcoloInteressi = (tasso / 365) * ChronoUnit.DAYS.between(getDataAperturaConto(), data); // differenza data apertura conto e data inserita
		setTotInteressi(getTotInteressi() + calcoloInteressi);
		setDataUltimaGenerazioneInteressi(data);
	}
	
	@Override
	public void generaInteressiNuovaRegola(LocalDate data) { // Modifica 2
		if (LocalDate.now().isAfter(getDataAperturaConto().plusYears(1))) {
			double calcoloInteressi = (tasso / 365) * ChronoUnit.DAYS.between(getDataUltimaGenerazioneInteressi(), data);
			setTotInteressi(getTotInteressi() + calcoloInteressi);
			setDataUltimaGenerazioneInteressi(LocalDate.now());
		}
	}
	
	public String stampaInfoConto() {
		DecimalFormat df = new DecimalFormat("#.##");
		aggiornaSaldo();
		
		return "=*=*= Informazioni sul Conto =*=*=\nTitolare: " + getTitolare() + "\nData apertura conto: "
				+ getDataAperturaConto() + "\nTasso di interesse: " + tasso + "\nSaldo attuale: " + df.format(getSaldo())
				+ "\nData dell'ultima generazione di interessi: " + getDataUltimaGenerazioneInteressi() + "\n\nStorico movimenti:\n" + getHistoryMovimenti() + "\n\nTotale versamenti (+):" + getTotVersamenti() + "\nTotale prelievi (-):" + getTotPrelievi();
	}

	
	@Override
	public void stampaSuPdf(int id_conto, Correntista c) {
		String codice_tipo = "CD";
	    LocalDate data = LocalDate.now();
	    String dataFormatted = data.toString();
	    
		Document contoDepositoDOC = new Document();

	    String fileName = "EC_" + codice_tipo + "_" + c.getCognome() + "_" + id_conto + "_" + dataFormatted + ".pdf";
	    String path = "/Users/ludo/Desktop/" + fileName;

		
		try {
			PdfWriter writer = PdfWriter.getInstance(contoDepositoDOC, new FileOutputStream(path));
			contoDepositoDOC.open();
			contoDepositoDOC.add(new Paragraph(stampaInfoConto()));
			contoDepositoDOC.close();
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
}
