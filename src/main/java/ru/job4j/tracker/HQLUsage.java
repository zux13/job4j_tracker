package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

public class HQLUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try (SessionFactory sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory()) {
            Session session = sf.openSession();
            Query query = session.createQuery("from Item");
            for (Object st : query.list()) {
                System.out.println(st);
            }
            findById(session, 3);
            update(session, 3);
            session.close();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void findById(Session session, int id) {
        Query<Item> query = session.createQuery(
                "from Item as i where i.id = :fId", Item.class);
        query.setParameter("fId", id);
        System.out.println(query.uniqueResult());
    }

    public static void update(Session session, int id) {
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE Item SET name = :fName WHERE id = :fId")
                    .setParameter("fName", "new name")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }
}
