package com.shashi.service.impl;

import com.shashi.entity.Admin;
import com.shashi.entity.Customer;
import com.shashi.repository.AdminRepository;
import com.shashi.repository.CustomerRepository;
import com.shashi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(CustomerRepository customerRepository, AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Customer> getCustomerByMail(String email) {
        if (email == null) return Optional.empty();
        return customerRepository.findByMailIdIgnoreCase(email.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Admin> getAdminByMail(String email) {
        if (email == null) return Optional.empty();
        return adminRepository.findByMailIdIgnoreCase(email.trim());
    }

    @Override
    public Customer registerCustomer(Customer customer) {
        customer.setMailId(customer.getMailId().trim().toLowerCase());
        customer.setPword(passwordEncoder.encode(customer.getPword()));
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomerProfile(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public boolean changeCustomerPassword(String email, String oldPassword, String newPassword) {
        Optional<Customer> opt = getCustomerByMail(email);
        if (opt.isPresent()) {
            Customer c = opt.get();
            if (passwordEncoder.matches(oldPassword, c.getPword()) || oldPassword.equals(c.getPword())) {
                c.setPword(passwordEncoder.encode(newPassword));
                customerRepository.save(c);
                return true;
            }
        }
        return false;
    }
}
