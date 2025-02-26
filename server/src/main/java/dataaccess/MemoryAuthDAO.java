package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO {
    private HashMap<String, AuthData> authsKey = new HashMap<>();
}
