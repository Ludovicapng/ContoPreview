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

public class ContoDeposito extends Conto {
	
	private static final double TASSO_CD = 0.10;
	
	public ContoDeposito(String titolare, LocalDate dataAperturaConto, double saldo, ArrayList<String> historyMovimenti, double totVersamenti, double totPrelievi) {
		super(titolare, dataAperturaConto, saldo);
	}

	@Override
	public void generaInteressi() { // generaInteressi base
		double calcoloInteressi = getSaldo() * TASSO_CD;
		setSaldo(getSaldo() + calcoloInteressi); // Aggiorna il saldo con gli interessi
		setDataUltimaGenerazioneInteressi(LocalDate.now());
	}
	
	@Override
	public void generaInteressi(LocalDate data) { // Modifica 1
		double calcoloInteressi = (TASSO_CD / 365) * ChronoUnit.DAYS.between(getDataAperturaConto(), data); // differenza data apertura conto e data inserita
		setSaldo(getSaldo() + calcoloInteressi);
		setDataUltimaGenerazioneInteressi(data);
	}
	
	@Override
	public void generaInteressiNuovaRegola(LocalDate data) { // Modifica 2
		if (LocalDate.now().isAfter(getDataAperturaConto().plusYears(1))) {
			double calcoloInteressi = (TASSO_CD / 365) * ChronoUnit.DAYS.between(getDataUltimaGenerazioneInteressi(), data);
			setSaldo(getSaldo() + calcoloInteressi);
			setDataUltimaGenerazioneInteressi(LocalDate.now());
		}
	}
	
	public String stampaInfoConto() {
		DecimalFormat df = new DecimalFormat("#.##");
		return "=*=*= Informazioni sul Conto =*=*=\nTitolare: " + getTitolare() + "\nData apertura conto: "
				+ getDataAperturaConto() + "\nTasso di interesse: " + TASSO_CD + "\nSaldo attuale: " + df.format(getSaldo())
				+ "\nData dell'ultima generazione di interessi: " + getDataUltimaGenerazioneInteressi() + "\n\nStorico movimenti:\n" + getHistoryMovimenti() + "\n\nTotale versamenti (+):" + getTotVersamenti() + "\nTotale prelievi (-):" + getTotPrelievi();
	}

	
	@Override
	public void stampaSuPdf() {
		Document contoDepositoDOC = new Document();
		String path = "/Users/ludo/Desktop/ContoDepositoDOC.pdf";
		
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
