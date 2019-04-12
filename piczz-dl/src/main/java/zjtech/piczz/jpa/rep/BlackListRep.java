package zjtech.piczz.jpa.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import zjtech.piczz.jpa.entity.BlackListEntity;

@Component
public interface BlackListRep extends JpaRepository<BlackListEntity, Long> {

}
