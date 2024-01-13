package com.cybersoft.crm04.entity;

import jakarta.persistence.*;

import java.util.List;

/**
 * Để mapping khóa ngoại tring Entity
 * Bước 1: Xem khóa chính 2 bảng đang quan hệ với nhau có phải là tự động tăng hay không. Nếu
 * tự động tăng thì không phải là OneToOne => OneToMany
 * Bước 2: Nếu khóa chính không tự đông tăng và vừa là khóa chính, vừa là khóa ngoại => OneToOne
 *
 *
 *
 *
 *
 * (*): OneToOne: Entity nào giữ khóa ngoại thì sẻ có 2 Annotation sau đây
 *  - @ManyToOne và @JoiColumn
 *  Bảng bào tham chiếu kháo ngoại sẻ map ngược lại
 * @OneToMany
 *
 *
 */

@Entity(name = "users")
public class UsersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "avatar")
    private String avatar;

//    @Column(name = "role_id")
//    private int roleId;

    @ManyToOne
    @JoinColumn(name = "role_id") // tên cột khóa ngoại trong database dùng để liên kết dữ liệu
    private RolesEntity rolesEntity; // Dựa vào chữ đằng sau OneToMany hay ManytoOne thì sẻ biết được là một đối tượng hay một list đối tượng

    @OneToMany(mappedBy = "usersEntity")
    private List<TasksEntity> tasks;

    public RolesEntity getRolesEntity() {
        return rolesEntity;
    }

    public void setRolesEntity(RolesEntity rolesEntity) {
        this.rolesEntity = rolesEntity;
    }

    public List<TasksEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TasksEntity> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFirstName(String fullname){
        String firstName = "";

        return firstName;
    }
}
