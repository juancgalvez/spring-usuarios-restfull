package com.jcgalvezv.springusuariosrestfull.entities;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Entity(name="telefonos")
@Table(name="telefonos")
public class Telefono {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, columnDefinition = "binary(16)")
    UUID id;
    @Column(name = "number", nullable = false, columnDefinition = "varchar(12)")
    @NotEmpty(message="no puede estar vacio")
    @Size(min=8, max=12, message="debe tener entre 8 y 12 caracteres")
    String number;
    @Column(name = "citycode", nullable = false, columnDefinition = "varchar(2)")
    @NotEmpty(message="no puede estar vacio")
    @Size(min=2, max=2, message="debe tener 2 caracteres")
    String citycode;
    @Column(name = "countrycode", nullable = false, columnDefinition = "varchar(3)")
    @NotEmpty(message="no puede estar vacio")
    @Size(min=1, max=3, message="debe tener 1 y 3 caracteres")
    String countrycode;

    public Telefono(String number, String citycode, String countrycode) {
        this.number = number;
        this.citycode = citycode;
        this.countrycode = countrycode;
    }

    public Telefono() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }
}
