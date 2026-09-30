package com.code.HibernateProjrct1;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.code.HibernateProjrct1.entity.OrderDetails;
import com.code.HibernateProjrct1.entity.Orders;
import com.code.HibernateProjrct1.entity.Product;
import com.code.HibernateProjrct1.entity.Users;

public class App {

    public static void main(String[] args) {

        SessionFactory sessionFactory = null;
        Session session = null;
        Transaction transaction = null;

        try {

            sessionFactory = HibernateUtil.getSessionFactory();

            session = sessionFactory.openSession();

            transaction = session.beginTransaction();

            // Get existing User
            Users user = session.get(Users.class, 1L);

            // Get existing Product
            Product product = session.get(Product.class, 1L);

            if (user == null) {
                System.out.println("User with ID 1 not found.");
                return;
            }

            if (product == null) {
                System.out.println("Product with ID 1 not found.");
                return;
            }

            // Create Order
            Orders order = new Orders(
                    LocalDateTime.now(),
                    new BigDecimal("110000.00"),
                    user
            );

            // Create first OrderDetails
            OrderDetails detail1 = new OrderDetails(
                    1,
                    new BigDecimal("55000.00"),
                    order,
                    product
            );

            // Create second OrderDetails
            OrderDetails detail2 = new OrderDetails(
                    1,
                    new BigDecimal("55000.00"),
                    order,
                    product
            );

            // Add both details to Order
            order.getOrderDetails().add(detail1);
            order.getOrderDetails().add(detail2);

            // Save Order
            session.persist(order);

            transaction.commit();

            System.out.println("=================================");
            System.out.println("Order created successfully!");
            System.out.println("Order ID: " + order.getId());
            System.out.println("Total Amount: " + order.getTotalAmount());
            System.out.println("Order Details: "
                    + order.getOrderDetails().size());
            System.out.println("=================================");

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            e.printStackTrace();

        } finally {

            if (session != null) {
                session.close();
            }

            if (sessionFactory != null) {
                sessionFactory.close();
            }

            System.out.println("Hibernate session closed.");
        }
    }
}