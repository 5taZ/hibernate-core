package entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "registraion_date")
    private LocalDateTime registrationDate;

    @OneToOne(mappedBy = "client",cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Order> orders;

    @ManyToMany
    @JoinTable(name = "client_coupons",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
            )
    @ToString.Exclude
    private List<Coupon> coupons;




}
