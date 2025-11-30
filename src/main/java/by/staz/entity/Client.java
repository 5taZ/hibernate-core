package by.staz.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @OneToOne(mappedBy = "client",cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Order> orders = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "client_coupons",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
            )
    @ToString.Exclude
    private List<Coupon> coupons = new ArrayList<>();

    public void setProfile(Profile profile) {
        if (profile == null) {
            if (this.profile != null) {
                this.profile.setClient(null);
            }
        } else {
            profile.setClient(this);
        }
        this.profile = profile;
    }
    public void addOrder(Order order) {
        orders.add(order);
        order.setClient(this);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
        order.setClient(null);
    }
    public void addCoupon(Coupon coupon) {
        coupons.add(coupon);
    }
}
