package it.creazionecontobancario.general;

import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import it.creazionecontobancario.database.ConnessioneDBbanca;
import it.creazionecontobancario.general.Correntista;

public class ContoInvestimento extends Conto {

	private double tassoInteresseInvestimento;

	public ContoInvestimento(Correntista correntista, LocalDate dataAperturaConto, double saldo) {
		super(correntista, dataAperturaConto, saldo);
	}

	@Override
	public void generaInteressi() {
		Random random = new Random();
		double tasso = random.nextDouble() * 201 - 100;

		setTassoInteresseInvestimento(tasso);
		double calcoloInteressi = getSaldo() * getTassoInteresseInvestimento();
		setTotInteressi(getTotInteressi() + calcoloInteressi);
		setDataUltimaGenerazioneInteressi(LocalDate.now());
	}

	@Override
	public void generaInteressi(LocalDate dataAperturaConto) {
		// METODO NON UTILIZZATO PER IL CONTO INVESTIMENTO!
	}

	@Override
	public String stampaInfoConto() {
		DecimalFormat df = new DecimalFormat("#.##");
		aggiornaSaldo();
		return "=*=*= Informazioni sul Conto =*=*=\nTitolare: " + getTitolare() + "\nData apertura conto: "
				+ getDataAperturaConto() + "\nTasso di interesse: " + df.format(getTassoInteresseInvestimento())
				+ "\nSaldo attuale: " + df.format(getSaldo()) + "\nData dell'ultima generazione di interessi: "
				+ getDataUltimaGenerazioneInteressi() + "\n\nStorico movimenti:\n" + getHistoryMovimenti()
				+ "\n\nTotale versamenti (+):" + getTotVersamenti() + "\nTotale prelievi (-):" + getTotPrelievi();
	}

	public double getTassoInteresseInvestimento() {
		return tassoInteresseInvestimento;
	}

	public void setTassoInteresseInvestimento(double tassoInteresseInvestimento) {
		this.tassoInteresseInvestimento = tassoInteresseInvestimento;
	}

	@Override
	public void stampaSuPdf(int id_conto, Correntista c) {
		String codice_tipo = "CI";
		LocalDate data = LocalDate.now();
		String dataFormatted = data.toString();

		Document contoInvestimentoDOC = new Document();

		String fileName = "EC_" + codice_tipo + "_" + c.getCognome() + "_" + id_conto + "_" + dataFormatted + ".pdf";
		String path = "/Users/ludo/Desktop/" + fileName;

		try {
			PdfWriter writer = PdfWriter.getInstance(contoInvestimentoDOC, new FileOutputStream(path));
			contoInvestimentoDOC.open();
			contoInvestimentoDOC.add(new Paragraph(stampaInfoConto()));
			contoInvestimentoDOC.close();
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void generaInteressiNuovaRegola(LocalDate data) {
		// TODO Auto-generated method stub

	}

}
