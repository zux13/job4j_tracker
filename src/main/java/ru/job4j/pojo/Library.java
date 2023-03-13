package ru.job4j.pojo;

public class Library {

    public static void printArray(Book[] books) {
        for (Book book : books) {
            System.out.println("\"" + book.getName() + "\" страниц: " + book.getCount());
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Book cleanCode = new Book("Clean code", 464);
        Book eleazar = new Book("Елеазар", 40);
        Book algorithms = new Book("Грокаем алгоритмы", 290);
        Book headFirstJava = new Book("Head First Java", 1588);
        Book[] books = {cleanCode, eleazar, algorithms, headFirstJava};

        printArray(books);

        Book tempBook = books[3];
        books[3] = books[0];
        books[0] = tempBook;

        printArray(books);

        for (Book book : books) {
            if (book.getName().equals("Clean code")) {
                System.out.println("\"" + book.getName() + "\" страниц: " + book.getCount());
            }
        }
    }
}
