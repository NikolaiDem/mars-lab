package ru.mars.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.mars.entities.DeliveryPeriod;

public interface TimeRepository extends JpaRepository<DeliveryPeriod, Integer> {

}
