package test.fakeapi.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoMessage {

	@JsonProperty("path")
	private String path;

	@JsonProperty("name")
	private String name;

	@JsonProperty("message")
	private String message;

	@JsonProperty("timestamp")
	private String timestamp;


}