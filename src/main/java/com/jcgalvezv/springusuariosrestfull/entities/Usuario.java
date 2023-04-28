package com.jcgalvezv.springusuariosrestfull.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jcgalvezv.springusuariosrestfull.customValidators.CustomEmail;
import com.jcgalvezv.springusuariosrestfull.customValidators.CustomPassword;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;

@Entity(name="usuarios")
@Table(name="usuarios",
        uniqueConstraints = {
            @UniqueConstraint(name = "usuario_email_unico", columnNames = {"email"})
        },
        indexes = {
            @Index(name = "usuario_email_idx", columnList = "email", unique = true)
        }
)
public class Usuario {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, columnDefinition = "binary(16)")
    UUID id;
    @Column(name = "name", nullable = false, columnDefinition = "varchar(60)")
    @NotEmpty(message="no puede estar vacio")
    @Size(min=4, max=60, message="debe tener entre 4 y 60 caracteres")
    String name;
    @Column(name = "email", nullable = false, columnDefinition = "varchar(60)")
    @NotEmpty(message="no puede estar vacio")
    @Size(min=4, max=60, message="debe tener entre 4 y 60 caracteres")
    @CustomEmail
    String email;
    @Column(name = "password", nullable = false, columnDefinition = "varchar(30)")
    @NotEmpty(message="no puede estar vacio")
    @Size(min=8, max=30, message="debe tener entre 8 y 30 caracteres")
    @CustomPassword
    String password;
    @Column(name = "jwt", columnDefinition = "varchar(1024)")
    @JsonIgnore
    String jwt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modified;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    private Boolean isactive;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "usuarios_telefonos",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "telefono_id")
    )

    @Valid
    private List<Telefono> phones = new ArrayList<Telefono>();

    public Usuario(String name, String email, String password, List<Telefono> phones) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phones = phones;
    }

    public Usuario() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Telefono> getPhones() {
        return phones;
    }

    public void setPhones(List<Telefono> telefonos) {
        if (this.phones != telefonos) {    // avoid unit testing failure due to same object references
            this.phones.clear();
            this.phones.addAll(telefonos);
        }
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    @PrePersist
    private void insertDefaultValues() {
        Date today = new Date();
        created = today;
        modified = today;
        lastLogin = today;
        // Si en el JSON al invocar el microservicio no indicaron "isactive"
        if (isactive == null) {
            isactive = true;
        }
        this.jwt = null;

    }

    @PreUpdate
    private void updateDefaultValues() {
        Date today = new Date();
        modified = today;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phones=" + phones +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        if (!id.equals(usuario.id)) return false;
        if (!name.equals(usuario.name)) return false;
        if (!email.equals(usuario.email)) return false;
        if (!password.equals(usuario.password)) return false;
        return Objects.equals(phones, usuario.phones);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (phones != null ? phones.hashCode() : 0);
        return result;
    }
}
