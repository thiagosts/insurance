package br.com.company.insurance.adapters.out.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String id;
    private String name;
    @JsonProperty("created_at")
    private String createdAt;
    private boolean active;
    private List<String> offers;
}
