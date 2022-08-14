package pl.urbanskilukasz.onlineLibrary.catalog.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Book {

    private final Long id;
    private final String title;
    private final String author;
    private final int year;

}
