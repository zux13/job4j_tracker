package ru.job4j.bank;

import java.util.Objects;

/**
 * Описывает аккаунт пользователя {@link User}.
 *
 * @author Rustem Galiullin
 * @version 1.0
 */
public class Account {

    /**
     * Хранит реквизиты аккаунта в строковом виде.
     */
    private String requisite;

    /**
     * Хранит баланс аккаунта в виде {@code double}.
     */
    private double balance;

    /**
     * Канонический конструктор класса.
     *
     * @param requisite реквизиты аккаунта в строковом виде
     * @param balance   баланс аккаунта типа {@code double}
     */
    public Account(String requisite, double balance) {
        this.requisite = requisite;
        this.balance = balance;
    }

    /**
     * Возвращает поле {@link #requisite}.
     *
     * @return <code>String</code> - реквизиты аккаунта в строковом виде
     */
    public String getRequisite() {
        return requisite;
    }

    /**
     * Устанавливает значение поля {@link #requisite}.
     *
     * @param requisite реквизиты аккаунта в строковом виде
     */
    public void setRequisite(String requisite) {
        this.requisite = requisite;
    }

    /**
     * Возвращает значение поля {@link #balance}.
     *
     * @return <code>double</code> - баланс аккаунта
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Устанавливает значение поля {@link #balance}.
     *
     * @param balance баланс аккаунта типа {@code double}
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Сравнивает переданный объект с текущим.
     *
     * @param o Объект типа {@code Object} с которым проводится сравнение
     * @return  <code>true</code> если в параметре передан текущий объект
     *          или значение полей {@link #requisite} сравниваемых объектов равны.
     *          <code>false</code> во всех остальных случаях
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return Objects.equals(requisite, account.requisite);
    }

    /**
     * Возвращает значение хэша поля {@link #requisite}.
     *
     * @return <code>int</code> - результат вызова метода {@link Objects#hash(Object...)}
     *         с параметром {@link #requisite}
     */
    @Override
    public int hashCode() {
        return Objects.hash(requisite);
    }
}