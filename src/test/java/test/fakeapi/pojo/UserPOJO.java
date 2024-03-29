package test.fakeapi.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class UserPOJO{

	private String name;
	private String email;
	private String password;
	private String role;
	private String avatar;
	private Integer id;
	private String creationAt;
	private String updatedAt;

	public UserPOJO(String name, String email, String password, String role, String avatar) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.avatar = avatar;
	}
}