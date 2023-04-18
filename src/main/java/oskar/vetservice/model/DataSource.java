package oskar.vetservice.model;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private static final String DB_NAME = "Service.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/oskar/vetservice/db/" + DB_NAME;

    private static final String TABLE_ANIMALS = "animals";
    private static final String COLUMN_ANIMALS_ID = "_id";
    private static final String COLUMN_ANIMALS_NAME = "name";
    private static final String COLUMN_ANIMALS_DATE_OF_BIRTH = "birth_date";
    private static final String COLUMN_ANIMALS_SPECIES = "species";
    private static final String COLUMN_ANIMALS_GENDER = "gender";
    private static final String COLUMN_ANIMALS_PHOTO_PATH = "photo_path";
    private static final String COLUMN_ANIMALS_OWNER_ID = "owner_id";


    private static final String TABLE_OWNERS = "owners";
    private static final String COLUMN_OWNERS_ID = "_id";
    private static final String COLUMN_OWNERS_NAME = "name";
    private static final String COLUMN_OWNERS_SURNAME = "surname";
    private static final String COLUMN_OWNERS_CITY = "city";
    private static final String COLUMN_OWNERS_STREET = "street";
    private static final String COLUMN_OWNERS_HOUSE_NUMBER = "house_number";
    private static final String COLUMN_OWNERS_PHONE_NUMBER = "number";
    private static final String COLUMN_OWNERS_EMAIL = "email";

    public static final String CREATE_ANIMALS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ANIMALS + " ("
            + COLUMN_ANIMALS_ID + " INTEGER PRIMARY KEY  NOT NULL, "
            + COLUMN_ANIMALS_NAME + " TEXT, "
            + COLUMN_ANIMALS_DATE_OF_BIRTH + " TEXT, "
            + COLUMN_ANIMALS_SPECIES + " TEXT, "
            + COLUMN_ANIMALS_GENDER + " TEXT, "
            + COLUMN_ANIMALS_PHOTO_PATH + " TEXT, "
            + COLUMN_ANIMALS_OWNER_ID + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_ANIMALS_OWNER_ID + ") REFERENCES " + TABLE_OWNERS + "(" + COLUMN_OWNERS_ID + "))";

    public static final String CREATE_OWNERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OWNERS + " ("
            + COLUMN_OWNERS_ID + " INTEGER PRIMARY KEY  NOT NULL, "
            + COLUMN_OWNERS_NAME + " TEXT, "
            + COLUMN_OWNERS_SURNAME + " TEXT, "
            + COLUMN_OWNERS_CITY + " TEXT, "
            + COLUMN_OWNERS_STREET + " TEXT, "
            + COLUMN_OWNERS_HOUSE_NUMBER+ " TEXT, "
            + COLUMN_OWNERS_PHONE_NUMBER + " TEXT, "
            + COLUMN_OWNERS_EMAIL + " TEXT)";

    public static final String QUERY_ANIMAL_COUNT_BY_NAME_OWNER_BIRTHDAY = "SELECT COUNT(*) AS count FROM " + TABLE_ANIMALS + " WHERE "
            + COLUMN_ANIMALS_NAME + " = ? AND "
            + COLUMN_ANIMALS_OWNER_ID + " = ? AND "
            + COLUMN_ANIMALS_DATE_OF_BIRTH + " = ?";

    public static final String INSERT_ANIMAL = "INSERT INTO " + TABLE_ANIMALS + "(" + COLUMN_ANIMALS_NAME + ", "
            + COLUMN_ANIMALS_GENDER + ", " + COLUMN_ANIMALS_DATE_OF_BIRTH + ", " + COLUMN_ANIMALS_OWNER_ID + ", "
            + COLUMN_ANIMALS_PHOTO_PATH + ", " + COLUMN_ANIMALS_SPECIES + ") VALUES(?, ?, ?, ?, ?, ?)";

    public static final String INSERT_OWNER = "INSERT INTO " + TABLE_OWNERS + "(" + COLUMN_OWNERS_NAME + ", " + COLUMN_OWNERS_SURNAME
            + ", " + COLUMN_OWNERS_CITY + ", " + COLUMN_OWNERS_STREET + ", " + COLUMN_OWNERS_HOUSE_NUMBER + ", " + COLUMN_OWNERS_PHONE_NUMBER
            + ", " + COLUMN_OWNERS_EMAIL + ") VALUES(?, ?, ?, ?, ?, ?, ?)";

    public static final String QUERY_OWNERS_COUNT_BY_NAME_SURNAME_PHONE_NUMBER = "SELECT COUNT(*) AS count FROM " + TABLE_OWNERS
            + " WHERE " + COLUMN_OWNERS_NAME + " = ? AND " + COLUMN_OWNERS_SURNAME + " = ? AND " + COLUMN_OWNERS_PHONE_NUMBER + " = ?";

    public static final String QUERY_ALL_OWNERS = "SELECT * FROM " + TABLE_OWNERS;
    public static final String QUERY_ALL_ANIMALS = "SELECT * FROM " + TABLE_ANIMALS;
    public static final String QUERY_ANIMALS_BY_NAME = QUERY_ALL_ANIMALS + " WHERE "
            + COLUMN_ANIMALS_NAME +" LIKE ?";
    public static final String QUERY_OWNERS_BY_NAME_OR_SURNAME = QUERY_ALL_OWNERS + " WHERE "
            + COLUMN_OWNERS_NAME +" LIKE ? OR " + COLUMN_OWNERS_SURNAME +" LIKE ?";




    private Connection connection;

    private PreparedStatement queryAnimalsCountByNameOwnerIdBirthday;
    private PreparedStatement queryOwnersCountByNameSurnamePhoneNumber;
    private PreparedStatement queryAllOwners;
    private PreparedStatement queryAllAnimals;
    private PreparedStatement queryAnimalsByName;
    private PreparedStatement queryOwnersByNameOrSurname;
    private PreparedStatement insertAnimal;
    private PreparedStatement insertOwner;
    private static DataSource instance = new DataSource();
    private DataSource(){};
    public static DataSource getInstance(){return instance;}



    public boolean open(){
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            createTables();

            queryAnimalsCountByNameOwnerIdBirthday = connection.prepareStatement(QUERY_ANIMAL_COUNT_BY_NAME_OWNER_BIRTHDAY);
            insertAnimal = connection.prepareStatement(INSERT_ANIMAL);
            insertOwner = connection.prepareStatement(INSERT_OWNER);
            queryAllAnimals = connection.prepareStatement(QUERY_ALL_ANIMALS);
            queryAllOwners = connection.prepareStatement(QUERY_ALL_OWNERS);
            queryOwnersCountByNameSurnamePhoneNumber = connection.prepareStatement(QUERY_OWNERS_COUNT_BY_NAME_SURNAME_PHONE_NUMBER);
            queryOwnersByNameOrSurname = connection.prepareStatement(QUERY_OWNERS_BY_NAME_OR_SURNAME);
            queryAnimalsByName = connection.prepareStatement(QUERY_ANIMALS_BY_NAME);

            return true;
        } catch (SQLException e){
            System.out.println("Couldn't connect to database:" + e.getMessage());
            return false;
        }
    }

    private void createTables(){
        try {
            Statement tablesCreation = connection.createStatement();
            tablesCreation.execute(CREATE_OWNERS_TABLE);
            tablesCreation.execute(CREATE_ANIMALS_TABLE);
            tablesCreation.close();
        } catch (SQLException e){
            System.out.println("Couldn't create tables' creation statement " + e.getMessage());
        }

    }
    public void close(){
        try{
            if(queryAnimalsCountByNameOwnerIdBirthday != null){
                queryAnimalsCountByNameOwnerIdBirthday.close();
            }

            if(insertAnimal != null){
                insertAnimal.close();
            }

            if(insertOwner != null){
                insertOwner.close();
            }

            if(queryAnimalsByName != null){
                queryAnimalsByName.close();
            }

            if(queryOwnersByNameOrSurname != null){
                queryOwnersByNameOrSurname.close();
            }

            if(queryOwnersCountByNameSurnamePhoneNumber != null){
                queryOwnersCountByNameSurnamePhoneNumber.close();
            }

            if(queryAllOwners != null){
                queryAllOwners.close();
            }

            if(queryAllAnimals != null){
                queryAllAnimals.close();
            }
            connection.close();
        } catch (SQLException e){
            System.out.println("Couldn't close database connection " + e.getMessage());
        }

    }

    public boolean ownerHasAnimal(String name, LocalDate date, int ownerId) throws SQLException{
        String dateConverted = dateToString(date);
        queryAnimalsCountByNameOwnerIdBirthday.setString(1, name.toLowerCase());
        queryAnimalsCountByNameOwnerIdBirthday.setInt(2, ownerId);
        queryAnimalsCountByNameOwnerIdBirthday.setString(3, dateConverted);

        ResultSet result = queryAnimalsCountByNameOwnerIdBirthday.executeQuery();

        if(result.getInt("count") > 0){
            return true;
        }
        return false;
    }

    public int insertAnimal(String name, int ownerId, LocalDate date,
                             String species, String gender, String photoPath) throws SQLException{

        insertAnimal.setString(1, name.toLowerCase());
        insertAnimal.setString(2, gender);
        insertAnimal.setString(3, dateToString(date));
        insertAnimal.setInt(4, ownerId);
        insertAnimal.setString(5, photoPath);
        insertAnimal.setString(6, species.toLowerCase());

        int affectedRows = insertAnimal.executeUpdate();

        if(affectedRows != 1){
            throw new SQLException("Animal insertion failed!");
        }

        ResultSet generatedKeys = insertAnimal.getGeneratedKeys();

        if(generatedKeys.next()){
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Couldn't get id for animal");
        }
    }

    public int insertOwner(String name, String surname, String city, String street, String houseNumber,
                           String phoneNumber, String email) throws SQLException{
        insertOwner.setString(1,name.toLowerCase());
        insertOwner.setString(2, surname.toLowerCase());
        insertOwner.setString(3, city.toLowerCase());
        insertOwner.setString(4, street.toLowerCase());
        insertOwner.setString(5, houseNumber);
        insertOwner.setString(6, phoneNumber);
        insertOwner.setString(7, email);

        int affectedRows = insertOwner.executeUpdate();

        if(affectedRows != 1){
            throw new SQLException("Animal insertion failed!");
        }

        ResultSet generatedKeys = insertAnimal.getGeneratedKeys();

        if(generatedKeys.next()){
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Couldn't get id for owner");
        }
    }

    public boolean ownerExists(String name, String surname, String phoneNumber) throws SQLException{
        queryOwnersCountByNameSurnamePhoneNumber.setString(1, name.toLowerCase());
        queryOwnersCountByNameSurnamePhoneNumber.setString(2, surname.toLowerCase());
        queryOwnersCountByNameSurnamePhoneNumber.setString(3, phoneNumber);

        ResultSet result = queryOwnersCountByNameSurnamePhoneNumber.executeQuery();

        if(result.getInt("count") > 0){
            return true;
        }
        return false;
    }

    public List<Owner> getAllOwners() throws SQLException{
        ResultSet results =  queryAllOwners.executeQuery();

        List<Owner> owners = new ArrayList<>();

        while(results.next()){
            Owner owner = new Owner();
            owner.setId(results.getInt(1));
            owner.setName(capitalizeFirstLetter(results.getString(2)));
            owner.setSurname(capitalizeFirstLetter(capitalizeFirstLetter(results.getString(3))));
            owner.setCity(capitalizeFirstLetter(results.getString(4)));
            owner.setStreet(capitalizeFirstLetter(results.getString(5)));
            owner.setHouseNumber(results.getString(6));
            owner.setPhoneNumber(results.getString(7));
            owner.setEmail(results.getString(8));
            owners.add(owner);
        }

        return owners;
    }

    public List<Animal> getAllAnimals() throws SQLException{
        ResultSet results = queryAllAnimals.executeQuery();

        List<Animal> animals = new ArrayList<>();

        while(results.next()){
            Animal animal = new Animal();
            animal.setId(results.getInt(1));
            animal.setName(capitalizeFirstLetter(results.getString(2)));
            animal.setBirthday(stringToDate(results.getString(3)));
            animal.setSpecies(results.getString(4));
            animal.setGender(results.getString(5));
            animal.setPhotoPath(results.getString(6));
            animal.setOwnerId(results.getInt(7));
            animals.add(animal);
        }

        return animals;
    }



    private LocalDate stringToDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy"));
    }

    private String dateToString(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern("d/MM/yyyy"));
    }

    private String capitalizeFirstLetter(String word){
        return word.substring(0,1).toUpperCase() + word.substring(1).toLowerCase();
    }

}
