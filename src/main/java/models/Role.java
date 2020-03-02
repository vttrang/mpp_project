package models;

public enum Role {
	LIBRARIAN("librarian", 1),
	ADMIN("admin", 2),
	MEMBER("library member", 3),
	AUTHOR("author", 4);
	
	private String name;
	private int code;

	private Role(String name, int code) {
		this.name = name;
		this.code = code;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int id) {
		this.code = id;
	}
	
	public static Role getGenderByCode(int code) {
		for (Role role : Role.values()) {
			if (role.code == code) {
				return role;
			}
		}
		return null;
	}
	
}
