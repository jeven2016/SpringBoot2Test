package zjtech.webflux.person;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CountryEntity extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column
  @Basic
  private String countryName;

  @Column
  @Basic
  private String desc;

}
