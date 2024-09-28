package com.samtest.demo.customer;

import com.samtest.demo.AbstractTestcontainers;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Given
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                random.nextInt(16,99)
        );
        underTest.addCustomer(customer);

        // When
        List<Customer> customerList = underTest.selectAllCustomers();
        // Then
        assertThat(customerList).isNotNull();
    }

    @Test
    void selectCustomerById() {

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();

        Customer customer = new Customer(
                name,
                email,
                20
        );
        underTest.addCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c-> c.getId())
                .findFirst()
                .orElseThrow();

        //When
        Optional<Customer> actual  = underTest.selectCustomerById(id);

        //Then
        AssertionsForClassTypes.assertThat(actual).isPresent().hasValueSatisfying(c-> {

            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerbById() {

        //Given
        int id =-1;

        //When
        var actual = underTest.selectCustomerById(id);

        //Then
        assertThat(actual).isEmpty();

    }

    @Test
    void addCustomer() {

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        // Given
        Customer customer = new Customer(
                name,
                email,
                random.nextInt(16,99)
        );

       underTest.addCustomer(customer);

       int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c-> c.getId())
                .findFirst()
                .orElseThrow();

       //When
       Optional<Customer> actual  = underTest.selectCustomerById(id);

       //Then
       AssertionsForClassTypes.assertThat(actual).isPresent().hasValueSatisfying(c-> {

            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });

    }

    @Test
    void existsPersonWithEmail() {

        //Given
        Customer customer = new Customer(
                faker.name().fullName(),
                faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                random.nextInt(16,99)
        );

        underTest.addCustomer(customer);

        //When
        boolean actual = underTest.existsPersonWithEmail(customer.getEmail());

        //Then
        assertThat(actual).isTrue();
    }


    @Test
    void existsPersonWithId() {

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                random.nextInt(16,99)
        );

        underTest.addCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c-> c.getId())
                .findFirst()
                .orElseThrow();

        //When
        var actual = underTest.existsPersonWithId(id);

        //Then
        assertThat(actual).isTrue();

    }

    @Test
    void deleteCustomerById() {

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        // Given
        Customer customer = new Customer(
                name,
                email,
                random.nextInt(16,99)
        );

        underTest.addCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c-> c.getId())
                .findFirst()
                .orElseThrow();

        //When

        underTest.deleteCustomerById(id);

        boolean actualStillExist = underTest.existsPersonWithId(id);

        Optional<Customer> actualCustomer  = underTest.selectCustomerById(id);
        //Then
        AssertionsForClassTypes.assertThat(actualCustomer).isNotPresent();
        assertThat(actualStillExist).isFalse();


    }

    @Test
    void updateCustomerName() {

        // Given

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                random.nextInt(16,99)
        );

        underTest.addCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c-> c.getId())
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        //When

        Customer customerUpdate = new Customer();
        customerUpdate.setId(id);
        customerUpdate.setName(newName);

        underTest.updateCustomerInfo(customerUpdate);

        //Then
        Optional<Customer> actual  = underTest.selectCustomerById(id);
        AssertionsForClassTypes.assertThat(actual).isPresent().hasValueSatisfying(c-> {

            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);

        });

    }

    @Test
    void updateCustomerEmail() {

        // Given

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                random.nextInt(16,99)
        );

        underTest.addCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c-> c.getId())
                .findFirst()
                .orElseThrow();

        var newEmail = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When

        Customer customerUpdate = new Customer();
        customerUpdate.setId(id);
        customerUpdate.setEmail(newEmail);

        underTest.updateCustomerInfo(customerUpdate);

        //Then
        Optional<Customer> actual  = underTest.selectCustomerById(id);
        AssertionsForClassTypes.assertThat(actual).isPresent().hasValueSatisfying(c-> {

            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(newEmail);

        });

    }

    @Test
    void updateCustomerAge() {

        // Given

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                random.nextInt(16,99)
        );

        underTest.addCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c-> c.getId())
                .findFirst()
                .orElseThrow();

        var newAge =  random.nextInt(16,99);

        //When

        Customer customerUpdate = new Customer();
        customerUpdate.setId(id);
        customerUpdate.setAge(newAge);

        underTest.updateCustomerInfo(customerUpdate);

        //Then
        Optional<Customer> actual  = underTest.selectCustomerById(id);
        AssertionsForClassTypes.assertThat(actual).isPresent().hasValueSatisfying(c-> {

            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(newAge);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());

        });

    }

    @Test
    void willNotUpdateIfNoNewInfo() {

        // Given

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                random.nextInt(16,99)
        );

        underTest.addCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c-> c.getId())
                .findFirst()
                .orElseThrow();

        //When

        Customer customerUpdate = new Customer();
        customerUpdate.setId(id);

        underTest.updateCustomerInfo(customerUpdate);

        //Then
        Optional<Customer> actual  = underTest.selectCustomerById(id);
        AssertionsForClassTypes.assertThat(actual).isPresent().hasValueSatisfying(c-> {

            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());

        });

    }

}