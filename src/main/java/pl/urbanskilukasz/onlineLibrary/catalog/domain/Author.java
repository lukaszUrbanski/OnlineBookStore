package pl.urbanskilukasz.onlineLibrary.catalog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import pl.urbanskilukasz.onlineLibrary.jpa.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(exclude = "books")
public class Author  extends BaseEntity {
    private String firstName;
    private String lastName;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "authors")
    @JsonIgnoreProperties("authors")

    private Set<Book> books = new HashSet<>();

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addBook(Book book){
        books.add(book);
        book.getAuthors().add(this);
    } public void removeBook(Book book){
        books.remove(book);
        book.getAuthors().remove(this);
    }

}


