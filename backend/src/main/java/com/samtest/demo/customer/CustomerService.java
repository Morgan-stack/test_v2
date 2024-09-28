package com.samtest.demo.customer;

import com.samtest.demo.exception.DuplicateResourceException;
import com.samtest.demo.exception.RequestValidationException;
import com.samtest.demo.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

//@Component
@Service
public class CustomerService {

    private final CustomerDAO customerDAO;

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers(){
        return customerDAO.selectAllCustomers();
    }
    public Customer getCustomer(Integer id){
        return customerDAO.selectCustomerById(id)
                .orElseThrow(
                        //()-> new IllegalArgumentException("customer with id [%s] not found.".formatted((id))));
                        ()-> new ResourceNotFoundException("customer with id [%s] not found.".formatted((id))));
    }

    public void addCustomer(CustomerRegistration customerRegistration){

        String email = customerRegistration.email();
        //check if email exists
        if(customerDAO.existsPersonWithEmail(email)){
            throw new DuplicateResourceException(
                    "email taken.");
        }
        customerDAO.addCustomer(
                    new Customer(
                            customerRegistration.name(),
                            customerRegistration.email(),
                            customerRegistration.age())
            );

    }
    public void deleteCustomerById(Integer customerId) {

        if(customerDAO.existsPersonWithId(customerId)){
            customerDAO.deleteCustomerById(customerId);
        }
        else{
          //()-> new IllegalArgumentException("customer with id [%s] not found.".formatted((id))));
          throw new ResourceNotFoundException(("customer with id [%s] not found.".formatted((customerId))));
        }
/*
     AMIGOS WAY OF DOING
     =================================================================
          if(!customerDAO.existsPersonWithId(customerId)){
            throw new ResourceNotFoundException(
                    "customer with id [%s] not found.".formatted((customerId))
            );
        }
        customerDAO.deleteCustomerById(customerId);
 */

    }

    public void updateCustomerInfo(Integer customerId,
                                   CustomerUpdateRequest customerUpdateRequest) {

        Customer customer = getCustomer(customerId);

        boolean changes = false;

        if(customerUpdateRequest.name() != null && (!customerUpdateRequest.name().equals(customer.getName()))){
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }

        if(customerUpdateRequest.age() != null && (!customerUpdateRequest.age().equals(customer.getAge()))){
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if(customerUpdateRequest.email() != null && (!customerUpdateRequest.email().equals(customer.getEmail()))){
            if(customerDAO.existsPersonWithEmail(customerUpdateRequest.email())){
                throw new DuplicateResourceException(
                        "email taken.");
            }
            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }
        if (!changes){
            throw new RequestValidationException("no data changes found");
        }
        customerDAO.updateCustomerInfo(customer);

    }
}
