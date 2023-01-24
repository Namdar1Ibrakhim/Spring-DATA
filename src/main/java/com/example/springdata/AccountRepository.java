package com.example.springdata;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;
//Spring Data позволяет не писать методы и не отправлять запросы через JDBC
//во всех случаех коннекта у него одинаковый синтаксис который наследуется от интерфейса
//и позволяет ускорять писать запросы
public interface AccountRepository extends CrudRepository<Account, Long> {
    //Первое значение <>, принадлежащее
    //к параметризованному типу, — это тип
    //класса модели, представляющего таблицу.
    //Второй — тип поля первичного ключа


    //Запросы в базы данных, когда вызываем нам вытаскивает данные из базы данных
    @Query("SELECT * FROM account WHERE name = :name")
    List<Account> findAccountsByName(String name);
    //

    @Modifying
    @Query("UPDATE account SET amount = :amount WHERE id = :id")
    void changeAmount(long id, BigDecimal amount);

    //Любая операция, изменяющая данные (с выполнением запроса INSERT, UPDATE
    //или DELETE) должна сопровождаться аннотацией @Modifying, которая показывает Spring Data, что данная операция модифицирует записи, хранящиеся
    //в базе данных.


}
