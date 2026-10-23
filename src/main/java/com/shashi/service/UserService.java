package com.shashi.service;

import com.shashi.entity.Customer;
import com.shashi.entity.Admin;
import java.util.Optional;

public interface UserService {

    Optional<Customer> getCustomerByMail(String email);

    Optional<Admin> getAdminByMail(String email);

    Customer registerCustomer(Customer customer);

    Customer updateCustomerProfile(Customer customer);

    boolean changeCustomerPassword(String email, String oldPassword, String newPassword);
}
