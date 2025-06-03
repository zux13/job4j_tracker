package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HbmTracker implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public Item add(Item item) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            session.persist(item);
            tx.commit();
            return item;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean replace(int id, Item item) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            int updateCount = session.createMutationQuery("UPDATE Item SET name=:name, created=:created WHERE id=:id")
                    .setParameter("name", item.getName())
                    .setParameter("created", item.getCreated())
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
            return updateCount > 0;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void delete(int id) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            session.createMutationQuery("DELETE Item WHERE id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Item> findAll() {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Item", Item.class)
                    .getResultList();
        }
    }

    @Override
    public List<Item> findByName(String key) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Item WHERE name=:name", Item.class)
                    .setParameter("name", key)
                    .getResultList();
        }
    }

    @Override
    public Item findById(int id) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Item WHERE id=:id", Item.class)
                    .setParameter("id", id)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
