package test.fakeapi.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@Builder
public class CategoryPOJO {
	private String image;

	public CategoryPOJO(String image, String name, Integer id) {
		this.image = image;
		this.name = name;
		this.id = id;
	}

	private String creationAt;
	private String name;
	private Integer id;
	private String updatedAt;
}