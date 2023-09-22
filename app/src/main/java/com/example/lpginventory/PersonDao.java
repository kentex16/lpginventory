package com.example.lpginventory;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PersonDao {
    @Insert
    void insert(Person person);

    @Query("SELECT * FROM persons")
    LiveData<List<Person>> getAllPersons();

    @Query("SELECT * FROM persons")
    List<Person> getAllPersonsSync();


    @Query("SELECT * FROM persons WHERE id = :personId")
    Person getPersonById(int personId);

    @Query("SELECT * FROM persons WHERE name = :personName")
    Person getPersonByName(String personName);

    @Update
    void update(Person person);

    @Update
    void updateAll(List<Person> persons);
    @Delete
    void delete(Person person);

    @Query("UPDATE persons SET containerReturned = :containerReturned WHERE id = :personId")
    void updateContainerReturned(boolean containerReturned, int personId);

    @Query("UPDATE persons SET updated_total_amount = :updatedTotalAmount WHERE id = :personId")
    void updateUpdatedTotalAmount(int personId, double updatedTotalAmount);

    @Query("SELECT updated_total_amount FROM persons WHERE id = :personId")
    double getUpdatedTotalAmount(int personId);

    @Query("SELECT SUM(amount) FROM persons WHERE modeOfPayment = 'Credit'")
    double getCurrentTotalAmount();

    @Query("UPDATE persons SET updated_total_amount = :newTotalAmount WHERE id = 1")
    void updateTotalAmount(double newTotalAmount);


    @Query("DELETE FROM persons")
    void deleteAll();

    @Query("SELECT * FROM ResetData WHERE name = :resetName")
    ResetData getResetDataByName(String resetName);




}




