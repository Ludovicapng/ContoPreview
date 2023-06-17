package it.creazionecontobancario.general;

import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.text.DecimalFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ContoCorrente extends Conto {

	private static final double TASSO_CC = 0.05;
	
	public ContoCorrente(String titolare, LocalDate dataAperturaConto, double saldo, ArrayList<String> historyMovimenti, double totVersamenti, double totPrelievi) {
		super(titolare, dataAperturaConto, saldo);
	}

	@Override
	public void generaInteressi() {
		double calcoloInteressi = getSaldo() * TASSO_CC;
		setSaldo(getSaldo() + calcoloInteressi); // Aggiorna il saldo con gli interessi
		setDataUltimaGenerazioneInteressi(LocalDate.now());
	}

	@Override
	public void generaInteressi(LocalDate data) { // Modifica 1
		double calcoloInteressi = (TASSO_CC / 365) * ChronoUnit.DAYS.between(getDataAperturaConto(), data); // differenza data apertura conto e data attuale
		setSaldo(getSaldo() + calcoloInteressi);
		setDataUltimaGenerazioneInteressi(data);
	}

	@Override
	public void generaInteressiNuovaRegola(LocalDate data) { // Modifica 2
		if (LocalDate.now().isAfter(getDataAperturaConto().plusYears(1))) {
			double calcoloInteressi = (TASSO_CC / 365) * ChronoUnit.DAYS.between(getDataUltimaGenerazioneInteressi(), data);
			setSaldo(getSaldo() + calcoloInteressi);
			setDataUltimaGenerazioneInteressi(data);
		}
	}

	@Override
	public String stampaInfoConto() {
		DecimalFormat df = new DecimalFormat("#.##");
		return "=*=*= Informazioni sul Conto =*=*=\nTitolare: " + getTitolare() + "\nData apertura conto: "
				+ getDataAperturaConto() + "\nTasso di interesse: " + TASSO_CC + "\nSaldo attuale: " + df.format(getSaldo())
				+ "\nData dell'ultima generazione di interessi: " + getDataUltimaGenerazioneInteressi() + "\n\nStorico movimenti:\n" + getHistoryMovimenti() + "\n\nTotale versamenti (+):" + getTotVersamenti() + "\nTotale prelievi (-):" + getTotPrelievi();
	}
	
	@Override
	public void stampaSuPdf() {
		Document contoCorrenteDOC = new Document();
		String path = "/Users/ludo/Desktop/ContoCorrenteDOC.pdf";
		
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

}
