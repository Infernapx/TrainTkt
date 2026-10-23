package com.shashi.repository;

import com.shashi.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

    Optional<Train> findByTrNo(Long trNo);

    List<Train> findByTrNameContainingIgnoreCase(String trName);

    @Query("SELECT t FROM Train t WHERE LOWER(t.fromStn) = LOWER(:fromStn) AND LOWER(t.toStn) = LOWER(:toStn)")
    List<Train> findTrainsBetweenStations(@Param("fromStn") String fromStn, @Param("toStn") String toStn);
}
