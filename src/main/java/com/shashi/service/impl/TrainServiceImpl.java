package com.shashi.service.impl;

import com.shashi.entity.Train;
import com.shashi.repository.TrainRepository;
import com.shashi.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrainServiceImpl implements TrainService {

    private final TrainRepository trainRepository;

    @Autowired
    public TrainServiceImpl(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Train> getTrainByIdOrName(String query) {
        if (query == null) return Optional.empty();
        String cleaned = query.trim().replaceAll("^#+", "");
        if (cleaned.isEmpty()) return Optional.empty();

        try {
            Long trNo = Long.parseLong(cleaned);
            Optional<Train> byId = trainRepository.findByTrNo(trNo);
            if (byId.isPresent()) return byId;
        } catch (NumberFormatException ignored) {
        }

        List<Train> byName = trainRepository.findByTrNameContainingIgnoreCase(cleaned);
        return byName.isEmpty() ? Optional.empty() : Optional.of(byName.get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Train> getTrainsBetweenStations(String fromStation, String toStation) {
        if (fromStation == null || toStation == null) return List.of();
        return trainRepository.findTrainsBetweenStations(fromStation.trim(), toStation.trim());
    }

    @Override
    public Train addTrain(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public Train updateTrain(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public boolean deleteTrainById(Long trNo) {
        if (trainRepository.existsById(trNo)) {
            trainRepository.deleteById(trNo);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Integer> checkAvailability(Long trNo) {
        return trainRepository.findByTrNo(trNo).map(Train::getSeats);
    }
}
