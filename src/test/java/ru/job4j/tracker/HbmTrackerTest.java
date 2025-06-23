package ru.job4j.tracker;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import ru.job4j.toone.Role;
import ru.job4j.toone.User;
import ru.job4j.toone.UserMessenger;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HbmTrackerTest {

    private StandardServiceRegistry registry;
    private SessionFactory sessionFactory;
    private HbmTracker tracker;

    @BeforeAll
    void init() {
        registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.driver_class", "org.h2.Driver")
                .applySetting("hibernate.connection.url", "jdbc:h2:./testdb;MODE=PostgreSQL;CASE_INSENSITIVE_IDENTIFIERS=TRUE;DB_CLOSE_DELAY=-1;")
                .applySetting("hibernate.connection.username", "")
                .applySetting("hibernate.connection.password", "")
                .applySetting("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .applySetting("hibernate.hbm2ddl.auto", "update")
                .applySetting("hibernate.show_sql", "true")
                .build();

        sessionFactory = new MetadataSources(registry)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Role.class)
                .addAnnotatedClass(UserMessenger.class)
                .buildMetadata()
                .buildSessionFactory();
    }

    @BeforeEach
    void setUp() {
        tracker = new HbmTracker(sessionFactory, registry);
    }

    @AfterEach
    void tearDown() {
        if (tracker != null) {
            tracker.findAll().forEach(i -> tracker.delete(i.getId()));
        }
    }

    @AfterAll
    void destroy() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Test
    public void whenAddNewItemThenTrackerHasSameItem() {
        Item item = new Item();
        item.setName("test1");
        tracker.add(item);
        Item result = tracker.findById(item.getId());
        assertThat(result.getName()).isEqualTo(item.getName());
    }

    @Test
    public void whenReplaceThenItemIsUpdated() {
        Item item = new Item();
        item.setName("old");
        item.setCreated(LocalDateTime.now());
        tracker.add(item);

        Item updated = new Item();
        updated.setName("new");
        updated.setCreated(LocalDateTime.now().plusDays(1));
        boolean result = tracker.replace(item.getId(), updated);

        Item fromDb = tracker.findById(item.getId());

        assertThat(result).isTrue();
        assertThat(fromDb.getName()).isEqualTo("new");
    }

    @Test
    public void whenDeleteThenItemIsGone() {
        Item item = new Item();
        item.setName("toDelete");
        tracker.add(item);
        tracker.delete(item.getId());
        Item fromDb = tracker.findById(item.getId());
        assertThat(fromDb).isNull();
    }

    @Test
    public void whenFindAllThenReturnListOfItems() {
        tracker.add(new Item("item1"));
        tracker.add(new Item("item2"));
        List<Item> result = tracker.findAll();
        assertThat(result.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void whenFindByNameThenReturnMatchingItems() {
        Item item = new Item("searchable");
        tracker.add(item);
        List<Item> result = tracker.findByName("searchable");
        assertThat(result).contains(item);
    }

    @Test
    public void whenFindByIdThenReturnCorrectItem() {
        Item item = new Item("byId");
        tracker.add(item);
        Item found = tracker.findById(item.getId());
        assertThat(found).isEqualTo(item);
    }
}
