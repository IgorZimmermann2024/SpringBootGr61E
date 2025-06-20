package de.ait.javalessons.controller;


import de.ait.javalessons.model.BankAccount;
import de.ait.javalessons.repository.BankAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/accounts")
public class RestApiBankAccountController {

    private BankAccountRepository bankAccountRepository;

    public RestApiBankAccountController(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @GetMapping
    public List<BankAccount> getAllBankAccounts() {
        log.info("Getting all bank accounts");
        return bankAccountRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<BankAccount> createBankAccount(BankAccount bankAccount) {
        log.info("Creating bank account with id {}", bankAccount.getId());
        BankAccount savedBankAccount = bankAccountRepository.save(bankAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBankAccount);
    }

    @GetMapping("/{id}")
    ResponseEntity<BankAccount> getBankAccountById(@PathVariable String id) {
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(id);
        if (bankAccount.isPresent()) {
            log.info("Bank account with id: {} found", id);
            return ResponseEntity.status(HttpStatus.OK).body(bankAccount.get());
        }
        log.info("Bank account with id: {} not found", id);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    void deleteBankAccountById(@PathVariable String id) {
        bankAccountRepository.deleteById(id);
        log.info("Bank account with id: {} deleted", id);
    }


    //api/accounts/{id}/deposit?amount=500
    @PutMapping("/{id}/deposit")
    public ResponseEntity<BankAccount> deposit(@PathVariable String id, @RequestParam double amount) {
        log.info("Depositing {} to bank account with id {}", amount, id);
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow();
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
        return ResponseEntity.status(HttpStatus.OK).body(bankAccount);
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<BankAccount> withdraw(@PathVariable String id, @RequestParam double amount) {
        BankAccount bankAccount = bankAccountRepository.findById(id).orElseThrow();
        if (bankAccount.getBalance() < amount) {
            log.warn("Withdrawal of {} is not possible. Balance is {}", amount, bankAccount.getBalance());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(bankAccount);
        }
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
        return ResponseEntity.status(HttpStatus.OK).body(bankAccount);
    }

    @GetMapping("/search")
    public List<BankAccount> findByOwner(@RequestParam String name) {
        log.info("Searching for bank accounts with owner {}", name);
        return bankAccountRepository.findByOwnerName(name);
    }
}
