package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Service;

public class MySqlDAOTest {
    private Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());

    public void setting(){
        DataAccessUser dataAccessUser = new MemoryUserDAO();
        DataAccessAuth dataAccessAuth = new MemoryAuthDAO();
        DataAccessGame dataAccessGame = new MemoryGameDAO();

        try {
            dataAccessUser = new MySqlDataAccessUser();
            dataAccessAuth = new MySqlDataAccessAuth();
            dataAccessGame = new MySqlDataAccessGame();

        } catch(DataAccessException e) {
            System.out.println(e.getMessage());
        }
        this.service = new Service(dataAccessUser, dataAccessAuth, dataAccessGame);
    }

    @BeforeEach
    public void reset() throws DataAccessException {
        setting();
        service.clearDataBase();
    }

    @Test
    public void addUserPositiveTest() {
        Service service = new Service(new MemoryUserDAO(), new MemoryAuthDAO(), new MemoryGameDAO());
        UserData newUser = new UserData("username","password","email");
        try{
            service.addUser(newUser);
        }catch(DataAccessException e){
            Assertions.assertNotNull(e,"addUser is not working");
        }
    }
}
