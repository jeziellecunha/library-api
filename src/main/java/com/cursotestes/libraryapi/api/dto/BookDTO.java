package com.cursotestes.libraryapi.api.dto;


import lombok.*;

import javax.validation.constraints.NotEmpty;


@Data //lombok cria os gets e sets em tempo de compilação e ToString e EqualsAndHashCode
@Builder // lombok facilita a criação das instâncias
@NoArgsConstructor//lombok cria um construtor vazio
@AllArgsConstructor//lombok cria construtor com todos os argumentos
public class BookDTO {


    private Long id;
    @NotEmpty
    private String title;
    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;

}
