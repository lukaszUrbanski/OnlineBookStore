package pl.urbanskilukasz.onlineLibrary.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import pl.urbanskilukasz.onlineLibrary.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(exclude = "authors")
public class Book extends BaseEntity {
    private String title;
    private Integer year;
    private BigDecimal price;
    private Long coverId;
    private Long available;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable
    @JsonIgnoreProperties("books")
    private Set<Author> authors = new HashSet<>();

    public Book(String title, int year, BigDecimal price, Long available) {
        this.title = title;
        this.year = year;
        this.price = price;
        this.available = available;
    }

    public void addAuthor(Author author){
        authors.add(author);
        author.getBooks().add(this);
    }
    public void removeAuthor(Author author){
        authors.remove(author);
        author.getBooks().remove(author);
    }

    public void removeAuthors(){
        Book self = this;
        authors.forEach(author -> author.getBooks().remove(self));
        authors.clear();
    }
}
