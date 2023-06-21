package it.creazionecontobancario.main;
import java.time.LocalDate;
import java.util.ArrayList;

import com.itextpdf.text.log.SysoCounter;

import it.creazionecontobancario.general.ContoInvestimento;
import it.creazionecontobancario.general.ContoDeposito;
import it.creazionecontobancario.general.ContoCorrente;

public class Main {

	public static void main(String[] args) {
		
		LocalDate dataCC = LocalDate.of(2022, 01, 1);
		LocalDate dataCD = LocalDate.of(2022, 12, 31);
		LocalDate dataCI = LocalDate.of(2012, 6, 12);
		
		ArrayList<String> history = new ArrayList<>();
		double totVersamenti = 0;
		double totPrelievi = 0;
		double totInteressi = 0;

		ContoCorrente contoCorrente = new ContoCorrente("Mario Rossi", dataCC, 1000, history, totPrelievi, totVersamenti, totInteressi);
		ContoDeposito contoDeposito = new ContoDeposito("Giuseppe Mberetto", dataCD, 500.45, history, totPrelievi, totVersamenti, totInteressi);
		ContoInvestimento contoInvestimento = new ContoInvestimento("Loretta Goggi", dataCI, 150787, history, totPrelievi, totVersamenti, totInteressi);
		
//		contoCorrente.versa(500);
//		contoDeposito.versa(300);
//		contoInvestimento.versa(1000);
//		
//		System.out.println(contoCorrente.stampaInfoConto() + "\n\n");
//		System.out.println(contoDeposito.stampaInfoConto()+ "\n\n");
//		System.out.println(contoInvestimento.stampaInfoConto()+ "\n\n");
//
//		contoCorrente.preleva(600);
//		contoDeposito.preleva(568);
//		contoInvestimento.preleva(1000);
//		
//		System.out.println(contoCorrente.stampaInfoConto()+ "\n\n");
//		System.out.println(contoDeposito.stampaInfoConto()+ "\n\n");
//		System.out.println(contoInvestimento.stampaInfoConto()+ "\n\n");
//		
		LocalDate dataGenerazioneInteressiCC = LocalDate.of(2023, 07, 01);
		LocalDate dataGenIntCC = LocalDate.of(2021, 12, 31);

//		LocalDate dataGenerazioneInteressiCD = LocalDate.of(2022, 12, 31);
//		LocalDate dataGenerazioneInteressiCI = LocalDate.now();
//		
//		contoCorrente.generaInteressi(dataGenerazioneInteressiCC);
//		contoDeposito.generaInteressi(dataGenerazioneInteressiCD);
//		
//		System.out.println("---DOPO LA GENERAZIONE DI INTERESSI---\n");
//		System.out.println(contoCorrente.stampaInfoConto()+ "\n\n");
//		System.out.println(contoDeposito.stampaInfoConto()+ "\n\n");
//		System.out.println(contoInvestimento.stampaInfoConto()+ "\n\n");
//		
//		contoCorrente.versa(999);
//		System.out.println(contoCorrente.stampaInfoConto());
		
		contoCorrente.setDataUltimaGenerazioneInteressi(dataCC);
		contoCorrente.preleva(900, dataGenerazioneInteressiCC);
		System.out.println(contoCorrente.stampaInfoConto());
		contoCorrente.versa(400, dataGenIntCC);
		System.out.println("\n" +contoCorrente.stampaInfoConto());

		
		contoCorrente.stampaSuPdf();
		contoDeposito.stampaSuPdf();
		contoInvestimento.stampaSuPdf();
	
	}
}
