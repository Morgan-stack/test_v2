package com.samtest.demo.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;

    // We use mock because we want our test to be fast,
    // due to lesser dependencies needed.
    @Mock
    private CustomerRepository customerRepository;


    @BeforeEach
    void setUp() {
        //creates an autoclosable mock (i'm guessing it's an instance)
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {

        //When
        underTest.selectAllCustomers();

        //Then
        // Verify whether the correct function is called
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        int id = 1;
        //When
        underTest.selectCustomerById(id);
        //Then
        verify(customerRepository).findById(id);
    }

    @Test
    void addCustomer() {
        //Given
        Customer customer = new Customer("sam","sam@test.com",33);

        //When
        underTest.addCustomer(customer);

        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existsPersonWithEmail() {
        //Given
        String email = "sdasd@gmail.com";

        //When
        underTest.existsPersonWithEmail(email);

        //Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        //Given
        int id = 1;

        //When
        underTest.existsPersonWithId(id);

        //Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        //Given
        int id = 1;
        //When
        underTest.deleteCustomerById(id);
        //Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomerInfo() {
        //Given
        Customer update = new Customer("sam","sam@test.com",33);

        //When
        underTest.updateCustomerInfo(update);

        //Then
        verify(customerRepository).save(update);
    }


}