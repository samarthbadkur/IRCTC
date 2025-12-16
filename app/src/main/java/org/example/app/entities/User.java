package org.example.app.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

//Using @JsonNaming annotation to specify the naming strategy for JSON properties.
//Here we are using SnakeCaseStrategy which means that JSON properties will be in snake_case format
// (e.g., "hashed_password") while Java fields will be in camelCase format (e.g., "hashedPassword").
@JsonNaming (PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String name;
    private String password;
    private String hashedPassword;
    //Here we are able <Ticket> because Ticket is in the same package as User. So whenever we refer to Ticket, Java knows we mean org.example.app.entities.Ticket and we do not need to import it.
    private List<Ticket> ticketsBooked;
    private String userID;

    public User(String name, String password, String hashedPassword, List<Ticket> ticketsBooked, String userID){
        this.name = name;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.ticketsBooked = ticketsBooked;
        this.userID = userID;
    }
    public User(){}

    public String getName() {
        return name;
    }

    public String getPassword(){
        return password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public void printTickets(){
        for (int i = 0; i<ticketsBooked.size(); i++){
            System.out.println(ticketsBooked.get(i).getTicketInfo());
        }
    }

    public String getUserId() {
        return userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
