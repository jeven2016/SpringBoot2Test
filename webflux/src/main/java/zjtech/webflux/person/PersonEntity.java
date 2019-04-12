package zjtech.webflux.person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class PersonEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column
  private long id;

  @Column
  private String name;

  @Column
  private String desc;

  @Column
  private int age;

  @Pattern(regexp = "^[1-9]\\d{6,14}$", message = "The msisdn is invalid, "
      + "the maximum length of an MSISDN to 15 digits. 1-3 digits are reserved for country code")
  private String msisdn;
}