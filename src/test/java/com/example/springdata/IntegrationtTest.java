package com.example.springdata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class IntegrationtTest {

    @MockBean
    private AccountRepository accountRepository;
    //Создаем объект-заглушку,
    //который входит в состав
    //контекста Spring

    @Autowired
    private TransferService transferService;
    //Внедряем реальный объект
    //из контекста Spring, поведение
    //которого хотим протестировать

    @Test
    void transferServiceTransferAmountTest() throws AccountNotFoundException {
        Account sender = new Account();
        sender.setId(1);
        sender.setAmount(new BigDecimal(1000));

        Account receiver = new Account();
        receiver.setId(2);
        receiver.setAmount(new BigDecimal(1000));

        when(accountRepository.findById(1L))
                .thenReturn(Optional.of(sender));
        when(accountRepository.findById(2L))
                .thenReturn(Optional.of(receiver));

        //Определяем все
        //предпосылки для теста

        transferService
                .transferMoney(1, 2, new BigDecimal(100));
        verify(accountRepository)
                .changeAmount(1, new BigDecimal(900));
        verify(accountRepository)
                .changeAmount(2, new BigDecimal(1100));
    }

    //На практикемодульные тесты применяются для проверки поведения отдельных компонентов, а интеграционные тесты Spring — для необходимых сценариев интеграции.
    //Несмотря на то что такие тесты тоже позволяют изучить реакцию компонента (если
    //выполнить все тестовые сценарии для логики метода), использовать их таким образом — плохая идея. Интеграционные тесты занимают больше времени, поскольку
    //затрагивают конфигурацию контекста Spring. Каждый вызов метода активирует несколько механизмов Spring, необходимых для работы фреймворка, — в зависимости
    //от того, какие функции нужны данному методу. Нет смысла тратить время и ресурсы
    //на реализацию каждого сценария тестирования логики приложения. Для экономии
    //времени лучший вариант — проверять логику отдельных компонентов посредством
    //модульных тестов и использовать интеграционные тесты только для уверенности,
    //что метод взаимодействует с фреймворком правильно

}
