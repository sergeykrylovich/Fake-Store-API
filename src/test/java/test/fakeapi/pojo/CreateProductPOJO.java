package test.fakeapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductPOJO {

    private String title;
    private Integer price;
    private String description;
    private Integer categoryId;
    private List<String> images;

}
