package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication


public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(
			ClientRepository clientRepository,
			AccountRepository accountRepository,
			TransactionRepository transactionRepository,
			LoanRepository loanRepository,
			ClientLoanRepository clientLoanRepository,
			CardRepository cardRepository) {
		return (args) -> {
			Client client1=  new Client("Melba","Lorenzo","melba@mindhub.com", "asdf1");
				Account account1= new Account("VIN-001", LocalDateTime.now(),5000,AccountType.CURRENT);
				Account account2= new Account("VIN-002",LocalDateTime.now().plusDays(1),7500,AccountType.SAVINGS);
				client1.setPassword(passwordEncoder.encode(client1.getPassword()));
					client1.addAccount(account1);client1.addAccount(account2);

			Client client2= new Client("Franco", "Sanna","franco@mindhub.com", "asdf2");
				Account account3= new Account("VIN-003",LocalDateTime.now(),300,AccountType.CURRENT);
					client2.addAccount(account3);
					client2.setPassword(passwordEncoder.encode(client2.getPassword()));
					Transaction transaction1a= new Transaction(CREDIT,LocalDateTime.now(),300,"cobro de salario");
					Transaction transaction1b= new Transaction(DEBIT,LocalDateTime.now(),500,"pago de impuestos");
					transaction1a.debitOrCredit();
					transaction1b.debitOrCredit();
						account1.addTransaction(transaction1a); account1.addTransaction(transaction1b);
					Transaction transaction2a= new Transaction(DEBIT,LocalDateTime.now(),200,"pagaste luz");
					transaction2a.debitOrCredit();
						account2.addTransaction(transaction2a);


			Loan loan1=new Loan("Hipotecario", 500000, Arrays.asList(12,24,36,48,60));
			Loan loan2=new Loan("Personal",100000,Arrays.asList( 6,12,24));
			Loan loan3=new Loan("Automotriz",300000, Arrays.asList(6,12,24,36));


			ClientLoan clientLoan1= new ClientLoan(400000,60,client1,loan1);
			ClientLoan clientLoan2= new ClientLoan(50000,12,client1,loan2);
			ClientLoan clientLoan3= new ClientLoan(100000,24,client2,loan2);
			ClientLoan clientLoan4= new ClientLoan(200000,36,client2,loan3);


			Card MelvaCard1= new Card(
					"Melva Lorenzo",
					"4540_7300_3288_2211",
					"137",
					LocalDateTime.now(),
					LocalDateTime.now().minusYears(2),
					CardType.DEBIT,
					CardColor.GOLD);
			Card MelvaCard2= new Card(
					"Melva Lorenzo",
					"4540_7815_3315_3222",
					"240",
					LocalDateTime.now(),
					LocalDateTime.now().plusYears(5),
					CardType.CREDIT,
					CardColor.TITANIUM);
			Card MelvaCard3= new Card(
					"Melva Lorenzo",
					"4540_7300_3288_2221",
					"137",
					LocalDateTime.now(),
					LocalDateTime.now().plusYears(5),
					CardType.DEBIT,
					CardColor.SILVER);
			Card FrancoCard1= new Card(
					"Franco Sanna",
					"4540_7231_4687_4789",
					"573",
					LocalDateTime.now(),
					LocalDateTime.now().plusYears(5),
					CardType.CREDIT,
					CardColor.SILVER);
			client1.addCard(MelvaCard1); client1.addCard(MelvaCard2); client2.addCard(FrancoCard1); client1.addCard(MelvaCard3);
			clientRepository.save(client1); accountRepository.save(account1); accountRepository.save(account2);
			clientRepository.save(client2); accountRepository.save(account3);
			transactionRepository.save(transaction1a); transactionRepository.save(transaction1b); transactionRepository.save(transaction2a);
			loanRepository.save(loan1);loanRepository.save(loan2);loanRepository.save(loan3);
			clientLoanRepository.save(clientLoan1);clientLoanRepository.save(clientLoan2);clientLoanRepository.save(clientLoan3);clientLoanRepository.save(clientLoan4);
			cardRepository.save(MelvaCard1); cardRepository.save(MelvaCard2); cardRepository.save(MelvaCard3); cardRepository.save(FrancoCard1);
		};
	}

}


