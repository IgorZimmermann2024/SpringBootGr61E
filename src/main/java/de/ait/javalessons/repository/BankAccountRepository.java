package de.ait.javalessons.repository;

import de.ait.javalessons.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository  extends JpaRepository<BankAccount, String> {
    List<BankAccount> findByOwnerName(String ownerName);

}
