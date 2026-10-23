package com.shashi.controller;

import com.shashi.entity.BookingHistory;
import com.shashi.entity.Customer;
import com.shashi.entity.Train;
import com.shashi.service.BookingService;
import com.shashi.service.TrainService;
import com.shashi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class WebController {

    private final TrainService trainService;
    private final UserService userService;
    private final BookingService bookingService;

    @Autowired
    public WebController(TrainService trainService, UserService userService, BookingService bookingService) {
        this.trainService = trainService;
        this.userService = userService;
        this.bookingService = bookingService;
    }

    // ==========================================
    // Public & Auth Endpoints
    // ==========================================

    @GetMapping({"/", "/index.html", "/UserLogin.html", "/AdminLogin.html"})
    public String indexPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        model.addAttribute("hasError", error != null);
        model.addAttribute("hasLogout", logout != null);
        return "index";
    }

    @GetMapping("/UserRegister.html")
    public String registerPage() {
        return "UserRegister";
    }

    @PostMapping("/userreg")
    public String registerUser(@RequestParam("mailid") String mailid,
                               @RequestParam("pword") String pword,
                               @RequestParam("firstname") String firstname,
                               @RequestParam("lastname") String lastname,
                               @RequestParam("address") String address,
                               @RequestParam("phoneno") Long phoneno,
                               Model model) {
        Customer customer = new Customer(mailid, pword, firstname, lastname, address, phoneno);
        try {
            userService.registerCustomer(customer);
            return "redirect:/index.html?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: Email already exists.");
            return "UserRegister";
        }
    }

    // ==========================================
    // Passenger Portal Endpoints
    // ==========================================

    @GetMapping({"/userhome", "/UserHome.html"})
    public String userHome(Principal principal, Model model) {
        if (principal != null) {
            userService.getCustomerByMail(principal.getName())
                    .ifPresent(customer -> model.addAttribute("customer", customer));
        }
        return "UserHome";
    }

    @GetMapping({"/userviewtrainfwd", "/UserViewTrains.html"})
    public String userViewTrains(Model model) {
        List<Train> trains = trainService.getAllTrains();
        model.addAttribute("trains", trains);
        return "UserViewTrains";
    }

    @GetMapping({"/usersearchtrain", "/SearchTrains.html"})
    public String searchTrainsPage() {
        return "SearchTrains";
    }

    @PostMapping("/searchtrainservlet")
    @ResponseBody
    public String searchTrainAjax(@RequestParam("trainnumber") String query) {
        Optional<Train> optTrain = trainService.getTrainByIdOrName(query);
        if (optTrain.isPresent()) {
            Train train = optTrain.get();
            return """
                <div class='train-result-card'>
                  <div class='train-result-header'>
                    <div class='train-identity'>
                      <span class='train-number-badge'>#%d</span>
                      <h3 class='train-name'>%s</h3>
                    </div>
                    <span class='status-badge-active'>&#9679; Active &amp; Scheduled</span>
                  </div>
                  <div class='train-route-visual'>
                    <div class='route-point'>
                      <span class='route-city'>%s</span>
                      <span class='route-label'>Departure Station</span>
                    </div>
                    <div class='route-path'>
                      <div class='path-line'></div>
                      <span class='path-icon'>&#128646;</span>
                    </div>
                    <div class='route-point' style='text-align: right;'>
                      <span class='route-city'>%s</span>
                      <span class='route-label'>Destination Station</span>
                    </div>
                  </div>
                  <div class='train-meta-grid'>
                    <div class='meta-item'>
                      <span class='meta-label'>Available Seats</span>
                      <span class='meta-val seats-val'>&#128186; %d Seats</span>
                    </div>
                    <div class='meta-item'>
                      <span class='meta-label'>Base Fare</span>
                      <span class='meta-val fare-val'>&#8377; %.2f</span>
                    </div>
                    <div class='meta-item' style='display:flex; justify-content:flex-end;'>
                      <a href='booktrainbyref?trainNo=%d&fromStn=%s&toStn=%s' class='btn-book-primary'>Book Ticket Now &#10142;</a>
                    </div>
                  </div>
                </div>
            """.formatted(
                    train.getTrNo(),
                    train.getTrName(),
                    train.getFromStn(),
                    train.getToStn(),
                    train.getSeats(),
                    train.getFare(),
                    train.getTrNo(),
                    train.getFromStn(),
                    train.getToStn()
            );
        } else {
            return """
                <div class='alert-card-danger'>
                  <div class='alert-card-icon'>&#9888;</div>
                  <div>
                    <div class='alert-card-title'>Train Not Found</div>
                    <div class='alert-card-desc'>No train matching <strong>%s</strong> was found in the database.</div>
                  </div>
                </div>
            """.formatted(query);
        }
    }

    @GetMapping({"/trainbwstnfwd", "/TrainBwStn.html"})
    public String trainBwStnPage() {
        return "TrainBwStn";
    }

    @PostMapping("/trainbwstn")
    public String findTrainsBetweenStations(@RequestParam("fromstation") String fromStation,
                                           @RequestParam("tostation") String toStation,
                                           Model model) {
        List<Train> trains = trainService.getTrainsBetweenStations(fromStation, toStation);
        model.addAttribute("trains", trains);
        model.addAttribute("fromStation", fromStation);
        model.addAttribute("toStation", toStation);
        model.addAttribute("searched", true);
        return "TrainBwStn";
    }

    @GetMapping({"/useravailfwd", "/Availability.html"})
    public String availabilityPage() {
        return "Availability";
    }

    @PostMapping("/useravail")
    public String checkAvailability(@RequestParam("trainno") String trainNoStr, Model model) {
        Optional<Train> optTrain = trainService.getTrainByIdOrName(trainNoStr);
        model.addAttribute("train", optTrain.orElse(null));
        model.addAttribute("searched", true);
        model.addAttribute("trainQuery", trainNoStr);
        return "Availability";
    }

    @GetMapping({"/fareenqfwd", "/Fare.html"})
    public String fareEnquiryPage() {
        return "Fare";
    }

    @PostMapping("/fareenq")
    public String fareEnquiry(@RequestParam("fromstation") String fromStn,
                              @RequestParam("tostation") String toStn,
                              Model model) {
        List<Train> trains = trainService.getTrainsBetweenStations(fromStn, toStn);
        model.addAttribute("trains", trains);
        model.addAttribute("fromStation", fromStn);
        model.addAttribute("toStation", toStn);
        model.addAttribute("searched", true);
        return "Fare";
    }

    @GetMapping({"/booktrainbyref", "/booktrains", "/BookTrains.html"})
    public String bookTrainPage(@RequestParam(value = "trainNo", required = false) Long trainNo,
                                @RequestParam(value = "fromStn", required = false) String fromStn,
                                @RequestParam(value = "toStn", required = false) String toStn,
                                Model model) {
        model.addAttribute("trainNo", trainNo);
        model.addAttribute("fromStn", fromStn);
        model.addAttribute("toStn", toStn);
        return "BookTrains";
    }

    @PostMapping("/payment")
    public String paymentCheckout(@RequestParam("trainnumber") Long trainNumber,
                                  @RequestParam("seats") int seats,
                                  @RequestParam("journeydate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate journeyDate,
                                  @RequestParam(value = "class", defaultValue = "Sleeper(SL)") String travelClass,
                                  Principal principal,
                                  Model model) {
        Optional<Train> optTrain = trainService.getTrainByIdOrName(String.valueOf(trainNumber));
        if (optTrain.isEmpty()) {
            model.addAttribute("error", "Train number " + trainNumber + " not found.");
            return "BookTrains";
        }
        Train train = optTrain.get();
        model.addAttribute("train", train);
        model.addAttribute("seats", seats);
        model.addAttribute("journeyDate", journeyDate);
        model.addAttribute("travelClass", travelClass);
        model.addAttribute("totalAmount", train.getFare() * seats);
        return "Payment";
    }

    @PostMapping("/confirmpayment")
    public String confirmPayment(@RequestParam("trainNumber") Long trainNumber,
                                 @RequestParam("seats") int seats,
                                 @RequestParam("journeyDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate journeyDate,
                                 @RequestParam(value = "fromStn", required = false) String fromStn,
                                 @RequestParam(value = "toStn", required = false) String toStn,
                                 Principal principal,
                                 Model model) {
        String email = principal != null ? principal.getName() : "shashi@demo.com";
        Optional<BookingHistory> optBooking = bookingService.bookTicket(email, trainNumber, seats, journeyDate, fromStn, toStn);
        if (optBooking.isPresent()) {
            model.addAttribute("booking", optBooking.get());
            return "BookingSuccess";
        } else {
            model.addAttribute("error", "Booking failed: Not enough available seats or invalid train.");
            return "BookTrains";
        }
    }

    @GetMapping("/bookingdetails")
    public String bookingHistory(Principal principal, Model model) {
        String email = principal != null ? principal.getName() : "shashi@demo.com";
        List<BookingHistory> bookings = bookingService.getAllBookingsByCustomer(email);
        model.addAttribute("bookings", bookings);
        return "BookingHistory";
    }

    @GetMapping({"/userprofile", "/viewuserprofile"})
    public String userProfile(Principal principal, Model model) {
        if (principal != null) {
            userService.getCustomerByMail(principal.getName()).ifPresent(c -> model.addAttribute("customer", c));
        }
        return "UserProfile";
    }

    // ==========================================
    // Administrator Portal Endpoints
    // ==========================================

    @GetMapping({"/adminhome", "/AdminHome.html"})
    public String adminHome(Principal principal, Model model) {
        if (principal != null) {
            userService.getAdminByMail(principal.getName()).ifPresent(a -> model.addAttribute("admin", a));
        }
        return "AdminHome";
    }

    @GetMapping({"/adminviewtrainfwd", "/ViewTrains.html"})
    public String adminViewTrains(Model model) {
        model.addAttribute("trains", trainService.getAllTrains());
        return "AdminViewTrains";
    }

    @GetMapping({"/addtrainfwd", "/AddTrains.html"})
    public String addTrainPage() {
        return "AddTrains";
    }

    @PostMapping("/addtrain")
    public String addTrain(@RequestParam("trainno") Long trainno,
                           @RequestParam("trainname") String trainname,
                           @RequestParam("fromstation") String fromstation,
                           @RequestParam("tostation") String tostation,
                           @RequestParam("seats") int seats,
                           @RequestParam("fare") double fare,
                           Model model) {
        Train train = new Train(trainno, trainname.toUpperCase(), fromstation.toUpperCase(), tostation.toUpperCase(), seats, fare);
        trainService.addTrain(train);
        return "redirect:/adminviewtrainfwd?added=true";
    }

    @GetMapping({"/cancletrainfwd", "/CancleTrain.html"})
    public String cancelTrainPage() {
        return "CancleTrain";
    }

    @PostMapping("/cancletrain")
    public String cancelTrain(@RequestParam("trainno") Long trainno, Model model) {
        boolean deleted = trainService.deleteTrainById(trainno);
        model.addAttribute("deleted", deleted);
        model.addAttribute("trainNo", trainno);
        return "redirect:/adminviewtrainfwd?deleted=" + deleted;
    }

    @GetMapping({"/updatetrain", "/AdminUpdateTrain.html"})
    public String updateTrainPage() {
        return "AdminUpdateTrain";
    }

    @PostMapping("/updatetrain")
    public String updateTrain(@RequestParam("trainno") Long trainno,
                              @RequestParam("seats") int seats,
                              @RequestParam("fare") double fare,
                              Model model) {
        Optional<Train> opt = trainService.getTrainByIdOrName(String.valueOf(trainno));
        if (opt.isPresent()) {
            Train t = opt.get();
            t.setSeats(seats);
            t.setFare(fare);
            trainService.updateTrain(t);
            return "redirect:/adminviewtrainfwd?updated=true";
        }
        return "redirect:/updatetrain?error=notfound";
    }
}
