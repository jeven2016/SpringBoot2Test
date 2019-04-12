package zjtech.webflux.person;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRep extends JpaRepository<PersonEntity, Long> {

}
