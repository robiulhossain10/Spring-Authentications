package com.abc.authlogintest.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties("password")
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;


}
