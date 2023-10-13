package test.fakeapi.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ProductsPOJO {

	private String title;
	private Integer price;
	private String description;
	private List<String> images;
	private ProductsCategoryPOJO category;
	private Integer id;
	private String creationAt;
	private String updatedAt;

}