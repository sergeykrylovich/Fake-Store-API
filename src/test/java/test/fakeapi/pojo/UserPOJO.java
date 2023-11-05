package test.fakeapi.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPOJO{

	private String name;
	private String email;
	private String password;
	private String role;
	private String avatar;
	private Integer id;
	private String creationAt;
	private String updatedAt;

}