package ru.job4j.pojo;

public class ShopDrop {
    public static Product[] delete(Product[] products, int index) {
        for (int i = index; i < products.length; i++) {
            products[i] = (i == products.length - 1) ? null : products[i + 1];
        }
        return products;
    }
}
