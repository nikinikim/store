package me.project.socksstoreapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sock {
    private Long id;
    private Color color;
    private Size size;
    private Composition composition;
    private int quantity;


}
