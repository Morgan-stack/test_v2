package com.samtest.demo.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // alternate way of getting this
    //    @RequestMapping(
    //            path = "api/v1/customer",
    //            method = RequestMethod.GET
    //    )
    @GetMapping
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }
    @GetMapping("{customerId}")
    public Customer getCustomer(
            @PathVariable("customerId") Integer customerId){

        return customerService.getCustomer(customerId);
    }

    @PostMapping
    public void addCustomer(
            @RequestBody CustomerRegistration request){
        customerService.addCustomer(request);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(
            @PathVariable("customerId") Integer customerId){

         customerService.deleteCustomerById(customerId);
    }

    @PutMapping("{customerId}")
    public void updateCustomerInfo(
           @PathVariable("customerId") Integer customerId,
           @RequestBody CustomerUpdateRequest customerUpdateRequest){

        customerService.updateCustomerInfo(customerId,customerUpdateRequest);
    }

}
