package com.delivery.domain.address.repository;

import com.delivery.domain.address.entity.Address;
import com.delivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user = :user")
    void updateAllDefaultFalseByUser(User user);

    @Query("SELECT a FROM Address a WHERE a.user = :user AND a.deletedAt IS NULL")
    List<Address> findAllByUser(User user);

    Optional<Address> findByAddressIdAndUser(UUID addressId, User user);
    Optional<Address> findByAddressIdAndDeletedAtIsNull(UUID addressId);
}
