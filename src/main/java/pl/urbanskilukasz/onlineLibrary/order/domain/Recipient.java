package pl.urbanskilukasz.onlineLibrary.order.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Recipient {
    String name;
    String phone;
    String street;
    String city;
    String zipCode;
    String email;

}
