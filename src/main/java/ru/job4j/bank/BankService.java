package ru.job4j.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Описывает банковский сервис.
 * <p>
 *
 *
 * @author Rustem Galiullin
 * @version 1.0
 */
public class BankService {

    /**
     * Хранит список аккаунтов пользователей в коллекции типа HashMap, где ключ - {@link User},
     * значение - List с типами {@link Account}.
     */
    private final Map<User, List<Account>> users = new HashMap<>();

    /**
     * Добавляет новый элемент в коллекцию {@link #users} в случае когда в ней отсутствует переданный ключ.
     *
     * @param user пользователь типа {@link User}
     */
    public void addUser(User user) {
        users.putIfAbsent(user, new ArrayList<Account>());
    }

    /**
     * Удаляет из коллекции {@link #users} элемент по паспорту.
     *
     * @param passport паспорт пользователя в строковом виде
     */
    public void deleteUser(String passport) {
        users.remove(new User(passport, ""));
    }

    /**
     * Добавляет аккаунт пользователя в коллекцию, в случае когда его там нет.
     * <p>
     * Осуществляет поиск пользователя по паспорту, вызывая метод {@link #findByPassport(String)}. Если пользователь
     * найден, получается список его аккаунтов и, в случае отсутствия в списке, переданный аккаунт добавляется.
     *
     * @param passport паспорт пользователя в строковом виде
     * @param account  аккаунт пользователя {@link Account}
     */
    public void addAccount(String passport, Account account) {
        User user = findByPassport(passport);
        if (user != null) {
            List<Account> accounts = users.get(user);
            if (!accounts.contains(account)) {
                accounts.add(account);
            }
        }
    }

    /**
     * Осуществляет поиск пользователя {@link User} в коллекции {@link #users} по паспорту.
     *
     * @param passport паспорт пользователя в строковом виде
     * @return <code>User</code> - если пользователь найден в коллекции.
     *         <code>null</code> - если пользователь не найден в коллекции.
     */
    public User findByPassport(String passport) {
        for (User usr : users.keySet()) {
            if (usr.getPassport().equals(passport)) {
                return usr;
            }
        }
        return null;
    }

    /**
     * Осуществляет поиск аккаунта {@link Account} в коллекции {@link #users} по паспорту и реквизитам.
     * <p>
     * Вызывается метод {@link #findByPassport(String)} и, в случае если пользователь найден, получает список
     * его аккаунтов, среди которых ищется тот, чьи реквизиты совпадают с переданными в метод.
     *
     * @param passport  паспорт пользователя в строковом виде
     * @param requisite реквизиты аккаунта пользователя в строковом виде
     * @return          <code>Account</code> - если найден пользователь и у него есть аккаунт с переданными реквизитами.
     *                  <code>null</code> - если не найден пользователь или не найден аккаунт.
     */
    public Account findByRequisite(String passport, String requisite) {
        User user = findByPassport(passport);
        if (user != null) {
            List<Account> accounts = users.get(user);
            for (Account acc : accounts) {
                if (acc.getRequisite().equals(requisite)) {
                    return acc;
                }
            }
        }
        return null;
    }

    /**
     * Осуществляет перевод средств от одного пользователя другому.
     * <p>
     * Для отправителя и получателя вызывает метод {@link #findByRequisite(String, String)}. В случае, если
     * оба аккаунта найдены и на балансе аккаунта отправителя достаточно средств для перевода, осуществляет перевод
     * на сумму <b>amount</b> и возвращает <code>true</code>.
     *
     * @param srcPassport   паспорт пользователя осуществляющего перевод
     * @param srcRequisite  реквизиты аккаунта пользователя осуществляющего перевод
     * @param destPassport  паспорт пользователя получающего перевод
     * @param destRequisite реквизиты аккаунта пользователя получающего перевод
     * @param amount        Сумма средств для перевода
     * @return              <code>true</code> - если перевод успешен
     *                      <code>false</code> - если на аккаунте пользователя недостаточно средств, если один или оба
     *                      пользователя не найдены по паспорту, если один или оба аккаунта пользователей не найдены по
     *                      реквизитам
     */
    public boolean transferMoney(String srcPassport, String srcRequisite,
                                 String destPassport, String destRequisite, double amount) {
        boolean rsl = false;
        Account srcAccount = findByRequisite(srcPassport, srcRequisite);
        Account destAccount = findByRequisite(destPassport, destRequisite);

        if (srcAccount != null && destAccount != null && srcAccount.getBalance() >= amount) {
            srcAccount.setBalance(srcAccount.getBalance() - amount);
            destAccount.setBalance(destAccount.getBalance() + amount);
            rsl = true;
        }
        return rsl;
    }

    /**
     * Получает список аккаунтов {@link Account} пользователя {@link User}, хранящихся в коллекции {@link #users}.
     *
     * @param user  пользователь <code>User</code>
     * @return      <code>List<Account></code> - если переданный пользователь найден в коллекции
     *              <code>null</code> - если переданный пользователь отсутствует в колекции
     */
    public List<Account> getAccounts(User user) {
        return users.get(user);
    }
}