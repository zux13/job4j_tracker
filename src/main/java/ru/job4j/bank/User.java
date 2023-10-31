package ru.job4j.bank;

import java.util.Objects;

/**
 * Описывает пользователя сервиса {@link BankService} со свойствами <b>passport</b> и <b>username</b>.
 *
 * @author Rustem Galiullin
 * @version 1.0
 */
public class User {

    /**
     * Хранит паспорт пользователя в строковом виде.
     */
    private String passport;

    /**
     * Хранит имя пользователя в строковом виде.
     */
    private String username;

    /**
     * Канонический конструктор класса.
     *
     * @param passport паспорт пользователя в строковом виде
     * @param username имя пользователя в строковом виде
     */
    public User(String passport, String username) {
        this.passport = passport;
        this.username = username;
    }

    /**
     * Возвращает поле {@link #passport}.
     *
     * @return <code>String</code> - паспорт пользователя в виде строки
     */
    public String getPassport() {
        return passport;
    }

    /**
     * Устанавливает значение поля {@link #passport}.
     *
     * @param passport паспорт пользователя в виде строки
     */
    public void setPassport(String passport) {
        this.passport = passport;
    }

    /**
     * Возвращает поле {@link #username}.
     *
     * @return <code>String</code> - имя пользователя в виде строки
     */
    public String getUsername() {
        return username;
    }

    /**
     * Устанавливает значение поля {@link #username}.
     *
     * @param username имя пользователя в виде строки
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Сравнивает переданный объект с текущим.
     *
     * @param o Объект типа {@code Object} с которым проводится сравнение
     * @return  <code>true</code> если в параметре передан текущий объект
     *          или значение полей {@link #passport} сравниваемых объектов равны.
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
        User user = (User) o;
        return Objects.equals(passport, user.passport);
    }

    /**
     * Возвращает значение хэша поля {@link #passport}.
     *
     * @return <code>int</code> - результат вызова метода {@link Objects#hash(Object...)}
     *          с параметром {@link #passport}
     */
    @Override
    public int hashCode() {
        return Objects.hash(passport);
    }
}