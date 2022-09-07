package pl.urbanskilukasz.onlineLibrary.catalog.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@ToString(exclude = "authors")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private Integer year;
    private BigDecimal price;
    private Long coverId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Author> authors;

    public Book(String title, int year, BigDecimal price) {
        this.title = title;
        this.year = year;
        this.price = price;
    }
}
