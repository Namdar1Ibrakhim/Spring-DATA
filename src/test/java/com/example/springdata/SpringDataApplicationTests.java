package com.example.springdata;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
class SpringDataApplicationTests {

    //можно так создать обьекты заглушки и тестируемого приложения так выглядит код чище
//    @Mock
//    private AccountRepository accountRepository;
//    @InjectMocks
//    private TransferService transferService;




    //Модульные тесты — это методы, которые вызывают определенный сценарий использования, чтобы проверить его поведение в специфических условиях.

    //первый тест это сценарий успешного выполнения, где не возникает исключений или ошибок.
    @Test
    @DisplayName("Test the amount is transferred " +
            "from one account to another if no exception occurs.")
    public void moneyTransferHappyFlow() throws AccountNotFoundException {
        AccountRepository accountRepository = mock(AccountRepository.class);
        //Вместо настоящего экземпляра AccountRepository мы возьмем
        //объект-заглушку, которым сможем управлять. Такие заглушки создаются
        //с помощью метода mock(). Метод mock() становится доступен благодаря зависимости
        //Mockito (часто используемой для создания тестов с помощью JUnit)

            TransferService transferService =
                    new TransferService(accountRepository);

            Account sender = new Account();
            sender.setId(1);
            sender.setAmount(new BigDecimal(1000));


            Account destination = new Account();
            destination.setId(2);
            destination.setAmount(new BigDecimal(1000));


            given(accountRepository.findById(sender.getId()))
                    .willReturn(Optional.of(sender));
            given(accountRepository.findById(destination.getId()))
                    .willReturn(Optional.of(destination));
            //Теперь мы можем задать поведение объекта-заглушки, после чего вызвать
            //тестируемый метод и убедиться, что он правильно работает в созданных условиях.
            //для управления действиями заглушки используется метод given(). С помощью given() можно определить, как будет вести
            //себя заглушка при вызове одного из ее методов. В данном случае нам нужно,
            //чтобы метод findById() объекта AccountRepository возвращал определенный
            //экземпляр Account в зависимости от заданного значения параметра.

            transferService.transferMoney(
                    sender.getId(),
                    destination.getId(),
                    new BigDecimal(100)
            );
            //Вызываем метод, который
            //хотим протестировать,
            //передавая ему ID
            //отправителя, ID получателя
            //и сумму перевода


            verify(accountRepository)
                .changeAmount(1, new BigDecimal(900));
            verify(accountRepository)
                .changeAmount(2, new BigDecimal(1100));
            //вызывается ли метод объекта-заглушки используется метод verify()

    }


    //Создание тестов для выполнения с исключением
    @Test
    public void moneyTransferExceptionFlow(){
        AccountRepository accountRepository = mock(AccountRepository.class);
        //Вместо настоящего экземпляра AccountRepository мы возьмем
        //объект-заглушку, которым сможем управлять. Такие заглушки создаются
        //с помощью метода mock(). Метод mock() становится доступен благодаря зависимости
        //Mockito (часто используемой для создания тестов с помощью JUnit)

        TransferService transferService =
                new TransferService(accountRepository);

        Account sender = new Account();
        sender.setId(1);
        sender.setAmount(new BigDecimal(1000));

        given(accountRepository.findById(1L))
                .willReturn(Optional.of(sender));
        given(accountRepository.findById(2L))
                .willReturn(Optional.empty());
        //Управляя заглушкой AccountRepository,
        //мы делаем так, чтобы метод findById(),
        //вызванный для счета получателя,
        //возвращал пустой объект Optional

        assertThrows(
                AccountNotFoundException.class,
                () -> transferService.transferMoney(1, 2, new BigDecimal(100))
        );
        //Мы предполагаем, что для данного
        //варианта выполнения метод
        //должен выбрасывать исключение
        //AccountNotFoundException

        verify(accountRepository, never())
                .changeAmount(anyLong(), any());
        //Используем метод verify() с условием
        //never() для уверенности, что метод
        //changeAmount() не вызывается
    }

}
