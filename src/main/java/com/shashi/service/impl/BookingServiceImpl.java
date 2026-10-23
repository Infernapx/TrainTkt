package com.shashi.service.impl;

import com.shashi.entity.BookingHistory;
import com.shashi.entity.Train;
import com.shashi.repository.BookingHistoryRepository;
import com.shashi.repository.TrainRepository;
import com.shashi.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final TrainRepository trainRepository;
    private final BookingHistoryRepository historyRepository;

    @Autowired
    public BookingServiceImpl(TrainRepository trainRepository, BookingHistoryRepository historyRepository) {
        this.trainRepository = trainRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    public Optional<BookingHistory> bookTicket(String mailId, Long trNo, int seatCount, LocalDate journeyDate, String fromStn, String toStn) {
        Optional<Train> optTrain = trainRepository.findByTrNo(trNo);
        if (optTrain.isEmpty()) {
            return Optional.empty();
        }

        Train train = optTrain.get();
        if (train.getSeats() < seatCount) {
            return Optional.empty();
        }

        // Deduct seats
        train.setSeats(train.getSeats() - seatCount);
        trainRepository.save(train);

        // Generate PNR / transaction ID
        String transId = "NTBS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        double totalAmount = train.getFare() * seatCount;

        BookingHistory booking = new BookingHistory(
                transId,
                mailId,
                trNo,
                journeyDate != null ? journeyDate : LocalDate.now(),
                fromStn != null ? fromStn : train.getFromStn(),
                toStn != null ? toStn : train.getToStn(),
                seatCount,
                totalAmount
        );

        return Optional.of(historyRepository.save(booking));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingHistory> getAllBookingsByCustomer(String mailId) {
        if (mailId == null) return List.of();
        return historyRepository.findByMailIdIgnoreCaseOrderByJourneyDateDesc(mailId.trim());
    }
}
