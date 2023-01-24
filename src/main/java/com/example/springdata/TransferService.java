package com.example.springdata;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional //все методы в транзации тогда обеспеиваем согласованность данных
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void transferMoney(long idSender, long idReceiver, BigDecimal amount) throws AccountNotFoundException {
        Account sender =
                accountRepository.findById(idSender)
                        .orElseThrow(() -> new AccountNotFoundException());
        Account receiver =
                accountRepository.findById(idReceiver)
                        .orElseThrow(() -> new AccountNotFoundException());
        //извлекаем из датабейс акаунты отправителя и получателя

        BigDecimal senderNewAmount = sender.getAmount().subtract(amount);
        BigDecimal receiverNewAmount = receiver.getAmount().add(amount);
        //высчитываем новое значение

        accountRepository.changeAmount(idSender, senderNewAmount);
        accountRepository.changeAmount(idReceiver, receiverNewAmount);
        //устанавливаем новое значение

    }
    public Iterable<Account> getAllAccounts() {
        return accountRepository.findAll();
        //AccountRepository наследует этот
        //метод от интерфейса CrudRepository,
        //принадлежащего Spring Data
    }
    //select * from account;


    public List<Account> findAccountsByName(String name) {
        return accountRepository.findAccountsByName(name);
    }
    //select * from account where name = :name;

}
