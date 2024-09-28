package com.samtest.demo.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO{

    private static List<Customer> customers;



    static {

        customers = new ArrayList<>();
        Customer sam = new Customer(
                1,
                "sam",
                "sam@gmail.com",
                20);

        customers.add(sam);

    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {

        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }


    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.stream()
                 .filter(c -> c.getId().equals(id))
                 .findFirst()
                 .ifPresent(customers :: remove);

    }

    @Override
    public boolean existsPersonWithId(Integer id) {
        return customers.stream()
                .anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void updateCustomerInfo(Customer update) {
        customers.add(update);
    }
}
