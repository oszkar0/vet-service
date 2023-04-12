package oskar.vetservice.model;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DataSource {
    public static final String DB_NAME = "Service.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:src/main/resources/oskar/vetservice/db/" + DB_NAME;

    public static final String TABLE_ANIMALS = "animals";
    public static final String COLUMN_ANIMALS_ID = "_id";
    public static final String COLUMN_ANIMALS_NAME = "name";
    public static final String COLUMN_ANIMALS_DATE_OF_BIRTH = "birth_date";
    public static final String COLUMN_ANIMALS_SPECIES = "species";
    public static final String COLUMN_ANIMALS_GENDER = "gender";
    public static final String COLUMN_ANIMALS_PHOTO_PATH = "photo_path";
    public static final String COLUMN_ANIMALS_OWNER_ID = "owner_id";


    public static final String TABLE_OWNERS = "owners";
    public static final String COLUMN_OWNERS_ID = "_id";
    public static final String COLUMN_OWNERS_NAME = "name";
    public static final String COLUMN_OWNERS_SURNAME = "surname";
    public static final String COLUMN_OWNERS_CITY = "city";
    public static final String COLUMN_OWNERS_STREET = "street";
    public static final String COLUMN_OWNERS_HOUSE_NUMBER = "house_number";
    public static final String COLUMN_OWNERS_PHONE_NUMBER = "number";
    public static final String COLUMN_OWNERS_EMAIL = "email";

    public static final String CREATE_ANIMALS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_ANIMALS + " ("
            + COLUMN_ANIMALS_ID + " INTEGER PRIMARY KEY  NOT NULL,"
            + COLUMN_ANIMALS_NAME + " TEXT"
            + COLUMN_ANIMALS_DATE_OF_BIRTH + " TEXT,"
            + COLUMN_ANIMALS_SPECIES + " TEXT,"
            + COLUMN_ANIMALS_GENDER + " TEXT,"
            + COLUMN_ANIMALS_PHOTO_PATH + " TEXT,"
            + COLUMN_ANIMALS_OWNER_ID + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_ANIMALS_OWNER_ID + ") REFERENCES " + TABLE_OWNERS + "(" + COLUMN_OWNERS_ID + "))";

    public static final String CREATE_OWNERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_OWNERS + " ("
            + COLUMN_OWNERS_ID + " INTEGER PRIMARY KEY  NOT NULL,"
            + COLUMN_OWNERS_NAME + " TEXT,"
            + COLUMN_OWNERS_SURNAME + " TEXT,"
            + COLUMN_OWNERS_CITY + " TEXT,"
            + COLUMN_OWNERS_STREET + " TEXT,"
            + COLUMN_OWNERS_HOUSE_NUMBER+ " TEXT,"
            + COLUMN_OWNERS_PHONE_NUMBER + " TEXT,"
            + COLUMN_OWNERS_EMAIL + " TEXT)";

    public static final String QUERY_ANIMAL_BY_NAME_OWNER_BIRTHDAY = "SELECT COUNT(*) AS count FROM " + TABLE_ANIMALS + " WHERE "
            + COLUMN_OWNERS_NAME + " = ? AND "
            + COLUMN_ANIMALS_OWNER_ID + " = ? AND "
            + COLUMN_ANIMALS_DATE_OF_BIRTH + " = ?";



    private Connection connection;

    private PreparedStatement queryAnimalByNameOwnerIdBithday;
    private static DataSource instance = new DataSource();
    private DataSource(){};
    public static DataSource getInstance(){return instance;}



    public boolean open(){
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            createTables();
            queryAnimalByNameOwnerIdBithday = connection.prepareStatement(QUERY_ANIMAL_NAME_OWNER_BIRTHDAY);
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
            if(queryAnimalByNameOwnerIdBithday != null){
                queryAnimalByNameOwnerIdBithday.close();
            }
            connection.close();
        } catch (SQLException e){
            System.out.println("Couldn't close database connection " + e.getMessage());
        }

    }

    public boolean ownerHasAnimal(String name, LocalDate date, int ownerId){
         try {
             String dateConverted = dateToString(date);
             queryAnimalByNameOwnerIdBithday.setString(1, name);
             queryAnimalByNameOwnerIdBithday.setInt(2, ownerId);
             queryAnimalByNameOwnerIdBithday.setString(3, dateConverted);

             ResultSet result = queryAnimalByNameOwnerIdBithday.executeQuery();

             int count = result.getInt("count");

             if(count > 0) return true;

         } catch (SQLException e){
             System.out.println("Query failed " + e.getMessage());
         }
         return false;
    }

    private LocalDate stringToDate(String date){
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy"));
    }

    private String dateToString(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern("d/MM/yyyy"));
    }

}
