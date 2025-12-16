package org.example.app.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.example.app.entities.Train;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TrainService {
    
    //final is used here to make sure trainList reference cannot be changed once initialized.
    private final List<Train> trainList;
    private static String TRAINS_PATH = "localData/trains.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TrainService() throws IOException {
        File file = new File(TRAINS_PATH);
        trainList = objectMapper.readValue(file, new TypeReference<List<Train>>() {});
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream().filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stations = train.getStations();
        int sourceIndex = stations.indexOf(source);
        int destinationIndex = stations.indexOf(destination);
        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }

        public void addTrain(Train newTrain) {
        // Check if a train with the same trainId already exists
        Optional<Train> existingTrain = trainList.stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (existingTrain.isPresent()) {
            // If a train with the same trainId exists, update it instead of adding a new one
            updateTrain(newTrain);
        } else {
            // Otherwise, add the new train to the list
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) {
        // Find the index of the train with the same trainId
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            // If found, replace the existing train with the updated one
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            // If not found, treat it as adding a new train
            addTrain(updatedTrain);
        }
    }

    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAINS_PATH), trainList);
        } catch (IOException e) {
            // Handle the exception based on your application's requirements
            
        }
    }
}
