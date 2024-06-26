package capstone.replyRecoommend.pet.repository;

import capstone.replyRecoommend.global.auth.domain.User;
import capstone.replyRecoommend.pet.domain.Enum.PetStatus;
import capstone.replyRecoommend.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet,Long> {


    List<Pet> findByUser(User user);

    Optional<Pet> findById(Long petId);

    List<Pet> findByUserAndPetStatus(User user, PetStatus status);



}
