package it.creazionecontobancario.general;

import java.time.LocalDate;
import java.util.ArrayList;

import com.itextpdf.text.Document;
import com.itextpdf.text.log.SysoCounter;

public abstract class Conto {

	protected String titolare;
	protected LocalDate dataAperturaConto;
	protected double saldo;
	protected LocalDate dataUltimaGenerazioneInteressi;
	protected ArrayList<String> historyMovimenti = new ArrayList<>();
	protected double totVersamenti;
	protected double totPrelievi;

	public Conto(String titolare, LocalDate dataAperturaConto, double saldo, LocalDate dataUltimaGenerazioneInteressi, ArrayList<String> histoArrayList, double totVersamenti, double totPrelievi) {
		this.titolare = titolare;
		this.dataAperturaConto = dataAperturaConto;
		this.saldo = saldo;
		this.dataUltimaGenerazioneInteressi = dataUltimaGenerazioneInteressi;
		this.historyMovimenti = historyMovimenti;
		this.totVersamenti = totVersamenti;
		this.totPrelievi = totPrelievi;
	}

	public Conto(String titolare, LocalDate dataAperturaConto, double saldo) {
		this.titolare = titolare;
		this.dataAperturaConto = dataAperturaConto;
		this.saldo = saldo;
	}


	public void versa(double importo, LocalDate data) {
		generaInteressiNuovaRegola(data);
		setSaldo(getSaldo() + importo);
		historyMovimenti.add("(" + LocalDate.now() + ") Versamento: (+)" + importo);	
		setTotVersamenti(getTotVersamenti() + importo);
	}

	public void preleva(double importo, LocalDate data) {
		if (importo < getSaldo()) {
			generaInteressiNuovaRegola(data);
			double saldoAggiornato = getSaldo() - importo;
			setSaldo(saldoAggiornato);
			historyMovimenti.add("[" + LocalDate.now() + "] Prelievo: (-)" + importo + "\n");	
			setTotPrelievi(getTotPrelievi() + importo);
		}
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

	public String getTitolare() {
		return titolare;
	}

	public LocalDate getDataAperturaConto() {
		return dataAperturaConto;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setTitolare(String titolare) {
		this.titolare = titolare;
	}

	public void setDataAperturaConto(LocalDate dataAperturaConto) {
		this.dataAperturaConto = dataAperturaConto;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public LocalDate getDataUltimaGenerazioneInteressi() {
		return dataUltimaGenerazioneInteressi;
	}

	public void setDataUltimaGenerazioneInteressi(LocalDate dataUltimaGenerazioneInteressi) {
		this.dataUltimaGenerazioneInteressi = dataUltimaGenerazioneInteressi;
	}
	
	

}
