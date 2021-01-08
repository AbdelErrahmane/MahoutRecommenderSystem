package de.unima.dws.webmining.rs.model;


/**
 * Representation of User data
 * 
 * 
 */
public class UserData {

	public UserData() {
		new UserData(null, null, null, null);
	}

	public UserData(Integer age, Gender gender) {
		new UserData(age, gender, null, null);
	}

	public UserData(Integer age, Gender gender, Integer occupation,
			String zipCode) {
		this.age = age;
		this.gender = gender;
		this.occupation = occupation;
		this.zipCode = zipCode;
	}

	private Integer age;
	private Gender gender;
	private Integer occupation;
	private String zipCode;

	public Integer getOccupation() {
		return occupation;
	}

	public void setOccupation(Integer occupation) {
		this.occupation = occupation;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "UserData [age=" + age + ", gender=" + gender + "]";
	}

}
