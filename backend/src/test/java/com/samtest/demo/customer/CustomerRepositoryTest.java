package com.samtest.demo.customer;

import com.samtest.demo.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


// Basically, we want to use independent database instead of
// using the actual one, just like CustomerJDBCDataAccessServiceTest,
// where the container -> database -> table are created for us.


// With @DataJpaTest and
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// This will help us to connect to one built using AbstractTestcontainers
@DataJpaTest
//Disable embedded server using statement below
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        //If we want to start with nothing, because main will instantiate an instance (check code).
        underTest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }
    @Test
    void existsPersonWithEmail() {

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = faker.name().fullName();
        Customer customer = new Customer(
                name,
                email,
                random.nextInt(16,99)
        );

        underTest.save(customer);

        //When
        var actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsPersonWithEmailFailsWhenEmailNotPresent() {

        String email = faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //When
        var actual = underTest.existsCustomerByEmail(email);

        //Then
        assertThat(actual).isFalse();

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

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c-> c.getId())
                .findFirst()
                .orElseThrow();

        //When
        var actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsPersonWithIdFailsWhenIdNotPresent() {

        int id = -1;
        //When
        var actual = underTest.existsCustomerById(id);

        //Then
        assertThat(actual).isFalse();

    }

}