package ru.job4j.tracker;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.job4j.toone.Role;
import ru.job4j.toone.User;
import ru.job4j.toone.UserMessenger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HbmTrackerIT {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("tracker")
            .withUsername("postgres")
            .withPassword("postgres");

    private HbmTracker tracker;
    private StandardServiceRegistry registry;
    private SessionFactory sessionFactory;

    @BeforeAll
    void beforeAll() {
        POSTGRES.start();

        registry = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.driver_class", "org.postgresql.Driver")
                .applySetting("hibernate.connection.url", POSTGRES.getJdbcUrl())
                .applySetting("hibernate.connection.username", POSTGRES.getUsername())
                .applySetting("hibernate.connection.password", POSTGRES.getPassword())
                .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                .applySetting("hibernate.show_sql", "true")
                .applySetting("hibernate.hbm2ddl.auto", "create-drop")
                .build();

        sessionFactory = new MetadataSources(registry)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Role.class)
                .addAnnotatedClass(UserMessenger.class)
                .buildMetadata()
                .buildSessionFactory();
    }

    @AfterAll
    void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
        POSTGRES.stop();
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

    @Test
    void whenAddItemThenCanFindById() {
        Item item = new Item("Postgres Test");
        tracker.add(item);
        Item result = tracker.findById(item.getId());
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Postgres Test");
    }

    @Test
    void whenReplaceItemThenUpdated() {
        Item original = new Item(0, "Old name", LocalDateTime.now());
        tracker.add(original);

        Item updated = new Item(0, "New name", LocalDateTime.now().plusDays(1));
        boolean result = tracker.replace(original.getId(), updated);
        Item fromDb = tracker.findById(original.getId());

        assertThat(result).isTrue();
        assertThat(fromDb.getName()).isEqualTo("New name");
        assertThat(fromDb.getCreated().truncatedTo(ChronoUnit.SECONDS))
                .isEqualTo(updated.getCreated().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void whenDeleteItemThenNotFound() {
        Item item = new Item("To delete");
        tracker.add(item);
        tracker.delete(item.getId());
        Item fromDb = tracker.findById(item.getId());
        assertThat(fromDb).isNull();
    }

    @Test
    void whenFindAllThenReturnAllItems() {
        tracker.add(new Item("item1"));
        tracker.add(new Item("item2"));
        List<Item> all = tracker.findAll();
        assertThat(all).hasSize(2);
    }

    @Test
    void whenFindByNameThenReturnMatchingItems() {
        Item item = new Item("match");
        tracker.add(item);
        List<Item> result = tracker.findByName("match");
        assertThat(result).contains(item);
    }

    @Test
    void whenFindByIdThenReturnCorrectItem() {
        Item item = new Item("search by id");
        tracker.add(item);
        Item found = tracker.findById(item.getId());
        assertThat(found).isEqualTo(item);
    }
}
