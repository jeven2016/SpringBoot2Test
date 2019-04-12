package zjtech.piczz;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GlobalSetting {

  private boolean suspending;
}
