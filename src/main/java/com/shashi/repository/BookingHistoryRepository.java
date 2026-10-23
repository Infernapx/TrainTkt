package com.shashi.repository;

import com.shashi.entity.BookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingHistoryRepository extends JpaRepository<BookingHistory, String> {

    List<BookingHistory> findByMailIdIgnoreCaseOrderByJourneyDateDesc(String mailId);
}
