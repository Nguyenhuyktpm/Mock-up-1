package org.example.model;


import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    String id;
    String name;
    String email;
    @Pattern(
            regexp = "^(\\+84|0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-5]|9[0-9])[0-9]{7}$",
            message = "Số điện thoại không hợp lệ. Phải là số di động Việt Nam hợp lệ."
    )
    String phoneNumber;
}
