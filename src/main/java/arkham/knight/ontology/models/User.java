package arkham.knight.ontology.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String nameToShow;
    private String password;
    private boolean admin;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Rol> rolList;

    public User() {
    }

    public User(String username, String password, String nameToShow, boolean admin, List<Rol> rolList) {
        this.username = username;
        this.password = password;
        this.nameToShow = nameToShow;
        this.admin = admin;
        this.rolList = rolList;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getNameToShow() { return nameToShow; }

    public void setNameToShow(String nameToShow) { this.nameToShow = nameToShow; }

    public boolean isAdmin() { return admin; }

    public void setAdmin(boolean admin) { this.admin = admin; }

    public List<Rol> getRolList() {
        return rolList;
    }

    public void setRolList(List<Rol> rolList) { this.rolList = rolList; }
}
