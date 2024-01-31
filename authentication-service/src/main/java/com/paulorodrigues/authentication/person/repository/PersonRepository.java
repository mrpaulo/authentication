package com.paulorodrigues.authentication.person.repository;

import com.paulorodrigues.authentication.person.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("SELECT s "
            + " FROM Person p "
            + " WHERE (:id IS NULL OR p.id = :id) "
            + " AND (:name IS NULL OR :name = '' OR LOWER(s.firstName) LIKE LOWER(CONCAT('%',:name,'%')) OR LOWER(s.lastName) LIKE LOWER(CONCAT('%',:name,'%'))) "
            + " AND (:email IS NULL OR :email = '' OR p.email LIKE CONCAT('%',:email,'%')) "
            + " AND (:cpf IS NULL OR :cpf = '' OR p.cpf LIKE CONCAT('%',:cpf,'%')) "
            + " AND (:sex IS NULL OR :sex = '' OR p.sex LIKE CONCAT('%',:sex,'%')) "
            + " AND ((coalesce(:startDate, null) is null AND coalesce(:endDate, null) is null) OR (p.birthdate BETWEEN :startDate AND :endDate)) ")
    Page<Person> findByFilterPageable(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("email") String email,
            @Param("cpf") String cpf,
            @Param("sex") String sex,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);

    @Query("SELECT s "
            + " FROM Person p "
            + " WHERE (:name IS NULL OR :name = '' OR"
            + " LOWER(p.firstName) LIKE LOWER(CONCAT('%',:name,'%')) OR "
            + " LOWER(p.lastName) LIKE LOWER(CONCAT('%',:name,'%'))) ")
    Page<Person> findByName(@Param("name") String name,
                            Pageable pageable);
}
