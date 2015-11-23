package cn.itcast.d_processVariables;

public class Person implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6757393795687480331L;
	
	private Integer id;
	private String name;
	
	private String education;
	
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
