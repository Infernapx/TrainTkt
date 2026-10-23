package com.shashi.service;

import com.shashi.entity.Train;
import java.util.List;
import java.util.Optional;

public interface TrainService {

    List<Train> getAllTrains();

    Optional<Train> getTrainByIdOrName(String query);

    List<Train> getTrainsBetweenStations(String fromStation, String toStation);

    Train addTrain(Train train);

    Train updateTrain(Train train);

    boolean deleteTrainById(Long trNo);

    Optional<Integer> checkAvailability(Long trNo);
}
