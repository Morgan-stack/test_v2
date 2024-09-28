package com.samtest.demo.customer;

import com.samtest.demo.exception.DuplicateResourceException;
import com.samtest.demo.exception.RequestValidationException;
import com.samtest.demo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

// With this annotation ,
// no need to use auto - closable
// for tear down.
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDAO customerDAO;

    @BeforeEach
    void setUp() {
            underTest = new CustomerService(customerDAO);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllCustomers() {

        //When
        underTest.getAllCustomers();

        //Then
        verify(customerDAO).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {

        //Given
        int id = 10;
        Customer customer = new Customer(id, "sam","sam@gmail.com",11);
        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomer(id);

        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {

        //Given
        int id = 10;

        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.empty());

        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "customer with id [%s] not found.".formatted((id)));
    }

    @Test
    void addCustomer() {
        //Given
        String email = "sam@gmail.com";

        Mockito.when(customerDAO.existsPersonWithEmail(email)).thenReturn(false);
        CustomerRegistration request = new CustomerRegistration(
                "sam",email,2
        );

        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );
        verify(customerDAO).addCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void willThrowExceptionWhenEmailExistsWhileAddingCustomer() {
        //Given
        String email = "sam@gmail.com";

        Mockito.when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);
        CustomerRegistration request = new CustomerRegistration(
                "sam",email,2
        );

        //When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email taken.");
        //Then
        verify(customerDAO, never()).addCustomer(any());

    }

    @Test
    void deleteCustomerById() {
        //Given
        int id = 10;

        //When
        Mockito.when(customerDAO.existsPersonWithId(id)).thenReturn(true);
        underTest.deleteCustomerById(id);

        //Then
        verify(customerDAO).deleteCustomerById(id);

    }

    @Test
    void willThrowExceptionIfdeleteCustomerThatDoesNotExist() {
        //Given
        int id = 10;

        //When
        Mockito.when(customerDAO.existsPersonWithId(id)).thenReturn(false);

        assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "customer with id [%s] not found.".formatted((id)));
        //Then
        verify(customerDAO, never()).deleteCustomerById(any());

    }

    @Test
    void canUpdateAllCustomerDetails() {
        //Given
        int id = 10;
        Customer customer = new Customer(id, "sam","sam@gmail.com",11);

        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String updateName = "Alenjandro";
        String email = "Alenjandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(updateName,email,28);

        Mockito.when(customerDAO.existsPersonWithEmail(email)).thenReturn(false);

        //When
        underTest.updateCustomerInfo(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomerInfo(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
    }

    @Test
    void canUpdateCustomerName() {
        //Given
        int id = 10;
        String originalEmail = "sam@gmail.com";
        Customer customer = new Customer(id, "sam", originalEmail,11);

        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String updateName = "Alenjandro";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(updateName,null,null);

        //When
        underTest.updateCustomerInfo(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomerInfo(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
    }
    @Test
    void canUpdateCustomerAge() {
        //Given
        int id = 10;
        String originalEmail = "sam@gmail.com";
        Customer customer = new Customer(id, "sam", originalEmail,11);

        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        int newAge = 29;

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null,null,newAge);

        //When
        underTest.updateCustomerInfo(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomerInfo(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
    }

    @Test
    void canUpdateCustomerEmail() {
        //Given
        int id = 10;
        String originalEmail = "sam@gmail.com";
        Customer customer = new Customer(id, "sam", originalEmail,11);

        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String updateEmail = "Alenjandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null,updateEmail,null);

        Mockito.when(customerDAO.existsPersonWithEmail(updateEmail)).thenReturn(false);

        //When
        underTest.updateCustomerInfo(id,updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);
        verify(customerDAO).updateCustomerInfo(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(updateEmail);
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
    }

    @Test
    void willThrowWhenUpdateTakenEmail() {
        //Given
        int id = 10;
        Customer customer = new Customer(id, "sam","sam@gmail.com",11);

        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String updateName = "Alenjandro";
        String email = "Alenjandro@gmail.com";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(updateName,email,28);

        Mockito.when(customerDAO.existsPersonWithEmail(email)).thenReturn(true);

        //When
        assertThatThrownBy(() -> underTest.updateCustomerInfo(id,updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email taken.");
        //Then
        verify(customerDAO, never()).updateCustomerInfo(any());

    }

    @Test
    void willThrowWhenNoUpdateChanges() {
        //Given
        int id = 10;
        Customer customer = new Customer(id, "sam","sam@gmail.com",11);

        Mockito.when(customerDAO.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String updateName = "Alenjandro";
        String email = "Alenjandro@gmail.com";

        CustomerUpdateRequest updateRequest =
                new CustomerUpdateRequest(customer.getName(), customer.getEmail(),customer.getAge());


        //When
        assertThatThrownBy(()->underTest.updateCustomerInfo(id,updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        //Then
        verify(customerDAO, never()).updateCustomerInfo(any());
    }
}