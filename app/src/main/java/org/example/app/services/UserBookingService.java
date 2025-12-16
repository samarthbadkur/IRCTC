package org.example.app.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.example.app.Util.UserServiceUtil;
import org.example.app.entities.Train;
import org.example.app.entities.User;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//THis is in a different package than User class. So we need to import User class to be able to refer to it here.
//This is not a statit class, we are assmuing user has already logged in and then we will call userbookingservice methods for that particular user and pass that user object to the constructor of this class. Constructor will initialize the user field of this class.
//this.user = user; this is how we will initialize the user field of this class. 
public class UserBookingService {

    private User user;
    private List<User> userList;

    //ObjectMapper is a class from Jackson library which is used to convert Java objects to JSON and JSON to Java objects.
    //Here we are creating an instance of ObjectMapper to be used in this class for reading
    //We have added jackson databind dependency in build.gradle file to use ObjectMapper class.
    //When dependency is added in build.gradle file, Gradle will download the required jar files and add them to the classpath of the project so that we can use the classes from that library in our code.
    private ObjectMapper objectMapper = new ObjectMapper();

    //static is used here we can directly refer to it using class name without creating object of this class. This will be stored in class area of memory without creating object of this class.
    //final is used here to make sure this variable value cannot be changed once initialized.
    private static final  String USERS_PATH = "localData/users.json";

    public UserBookingService(User user) throws IOException {
        this.user = user;
        this.userList = loadUsers();
    }

    // Default constructor to fetch all users without initializing user field.
    // This loads the user list from the JSON file.
    public UserBookingService() throws IOException {
        this.userList = loadUsers();
    }

    public List<User> loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        return objectMapper.readValue(users, new TypeReference<List<User>>() {
        });
    }

    public Boolean loginUser() {
        // Here we are using Java Streams to filter the userList based on the username and password provided in the user object.
        // If a matching user is found, it means the login is successful.
        // 
        Optional<User> foundUser = userList.stream()
                .filter(user1 -> user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword()))
                .findFirst();
        // isPresent() method returns true if a value is present, otherwise false.
        return foundUser.isPresent();
    }

    // Method to sign up a new user by adding them to the user list and saving to the JSON file.
    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        } catch (IOException e) {
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile() throws IOException {
        File users = new File(USERS_PATH);
        //write the userList to users.json file using ObjectMapper
        // Serialization is the process of converting Java objects into JSON format.
        // Here we are doing serialization of userList object to JSON and writing it to users.json file.
        objectMapper.writeValue(users, userList);
    }

    public void fetchBookings() {
        Optional<User> userFetched = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        if (userFetched.isPresent()) {
            userFetched.get().printTickets();
        }
    }

    public Boolean cancelBooking(String ticketId) {

        //Scanner class is used to get input from user.
        // Scanner is a class in Java that allows reading input from various sources, including the console.
        // System.in represents the standard input stream, which is typically the keyboard.
        // 
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the ticket id to cancel");
        ticketId = s.next();

        if (ticketId == null || ticketId.isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        String finalTicketId1 = ticketId;  //Because strings are immutable
        boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId1));

        String finalTicketId = ticketId;
        user.getTicketsBooked().removeIf(Ticket -> Ticket.getTicketId().equals(finalTicketId));
        if (removed) {
            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return Boolean.TRUE;
        } else {
            System.out.println("No ticket found with ID " + ticketId);
            return Boolean.FALSE;
        }
    }

    public List<Train> getTrains(String source, String destination) throws IOException {
        TrainService trainService = new TrainService();
        return trainService.searchTrains(source, destination);
    }

    public List<List<Integer>> fetchSeats(Train train) {
        //Seat booking method
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) throws IOException {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return true; // Booking successful
                } else {
                    return false; // Seat is already booked
                }
            } else {
                return false; // Invalid row or seat index
            }
        } catch (IOException ex) {
            return Boolean.FALSE;
        }
    }
}
