package com.shashi.config;

import com.shashi.entity.Admin;
import com.shashi.entity.Customer;
import com.shashi.entity.Train;
import com.shashi.repository.AdminRepository;
import com.shashi.repository.CustomerRepository;
import com.shashi.repository.TrainRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(AdminRepository adminRepo, CustomerRepository customerRepo, TrainRepository trainRepo) {
        return args -> {
            // Seed Admin
            if (adminRepo.count() == 0) {
                adminRepo.save(new Admin("admin@demo.com", "admin", "System", "Admin", "Admin Center 1", 9876543210L));
            }

            // Seed Customer
            if (customerRepo.count() == 0) {
                customerRepo.save(new Customer("shashi@demo.com", "shashi", "Shashi", "Raj", "Patna, Bihar", 9876543211L));
            }

            // Seed Trains
            if (trainRepo.count() == 0) {
                List<Train> trains = List.of(
                        new Train(12367L, "VIKRAMSHILA EXP", "PATNA", "ANAND VIHAR", 240, 620.0),
                        new Train(10001L, "JODHPUR EXP", "HOWRAH", "JODHPUR", 152, 490.50),
                        new Train(10002L, "YAMUNA EXP", "GAYA", "DELHI", 52, 550.50),
                        new Train(10003L, "NILANCHAL EXP", "GAYA", "HOWRAH", 92, 451.0),
                        new Train(10004L, "JAN SATABDI EXP", "RANCHI", "PATNA", 182, 550.0),
                        new Train(10005L, "GANGE EXP", "MUMBAI", "KERALA", 12, 945.0),
                        new Train(10006L, "GARIB RATH EXP", "PATNA", "DELHI", 15, 1450.75),
                        new Train(10010L, "VANDE BHARAT EXP", "NEW DELHI", "VARANASI", 180, 1750.0),
                        new Train(10103L, "MAHABODHI EXP", "GAYA", "DELHI", 73, 840.0),
                        new Train(10104L, "GARIB RATH EXPRESS", "PATNA", "MUMBAI", 26, 920.0),
                        new Train(12004L, "SHATABDI EXP", "NEW DELHI", "LUCKNOW", 150, 1120.0),
                        new Train(12301L, "RAJDHANI EXP", "HOWRAH", "NEW DELHI", 120, 1850.0),
                        new Train(12397L, "MAHABODHI EXPRESS", "GAYA", "NEW DELHI", 180, 580.0)
                );
                trainRepo.saveAll(trains);
                System.out.println("✅ Seeded " + trains.size() + " trains into PostgreSQL database.");
            }
        };
    }
}
