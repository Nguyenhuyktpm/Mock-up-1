package org.example.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    String id;
    String name;
    Double price;
    Integer stockAvailable;

    public Product getProductById(String id) {
        if (this.id.equals(id)) {
            return this;
        }
        return null;
    }
}
