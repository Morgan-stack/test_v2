package com.samtest.demo.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {

        var sql = """
                SELECT id, name, email, age
                FROM customer
                """;
        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper);
        return customers;
    }
    @Override
    public Optional<Customer> selectCustomerById(Integer id) {

        var sql = """
                SELECT id, name, email, age
                FROM customer
                WHERE id = ?
                """;

        return jdbcTemplate
                .query(sql, customerRowMapper,id)
                .stream()
                .findFirst();
    }

    //  ? ? ? will get from customer
    @Override
    public void addCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES(?, ?, ?)
                """;
        int update = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        System.out.println("jdbcTemplate.update: " + update);
    }

    @Override
    public boolean existsPersonWithEmail(String email) {
        // count how many ppl with this email
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,email);

        return count != null && count > 0;
    }

    @Override
    public boolean existsPersonWithId(Integer id) {

        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class,id);

        return count != null && count > 0;

    }

    @Override
    public void deleteCustomerById(Integer id) {

        var sql = """
                DELETE FROM customer
                WHERE id = ?
                """;
        int update = jdbcTemplate.update(sql,id);
        System.out.println("jdbcTemplate.update: " + update);

    }

    @Override
    public void updateCustomerInfo(Customer update) {

        if(update.getName() != null){
            String sql = "UPDATE customer SET name = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getName(),
                    update.getId()
            );
            System.out.println("jdbcTemplate.update: " + result);
        }
        if(update.getAge() != null){
            String sql = "UPDATE customer SET age = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getAge(),
                    update.getId()
            );
            System.out.println("jdbcTemplate.update: " + result);
        }
        if(update.getEmail() != null){
            String sql = "UPDATE customer SET email = ? WHERE id = ?";
            int result = jdbcTemplate.update(
                    sql,
                    update.getEmail(),
                    update.getId()
            );
            System.out.println("jdbcTemplate.update: " + result);
        }

    }
}
