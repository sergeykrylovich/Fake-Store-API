package test.fakeapi.pojo;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsPOJO {

	private String title;
	private Integer price;
	private String description;
	private List<String> images;
	private CategoryPOJO category;
	private Integer id;
	private Integer categoryId;
	private String creationAt;
	private String updatedAt;

}