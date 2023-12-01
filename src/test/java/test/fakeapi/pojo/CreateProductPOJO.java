package test.fakeapi.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateProductPOJO {

    private String title;
    private Integer price;
    private String description;
    private Integer categoryId;
    private List<String> images;

    public CreateProductPOJO(String title, Integer price, String description, List<String> images) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.images = images;
    }


}
