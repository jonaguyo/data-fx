package fr.ara.editors;

public class EditorKey {
	
	private Class<?> fieldType;
	private Class<?> annotationType;
	
	public EditorKey(Class<?> fieldType, Class<?> annotationType) {
		this.fieldType = fieldType;
		this.annotationType = annotationType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annotationType == null) ? 0 : annotationType.hashCode());
		result = prime * result + ((fieldType == null) ? 0 : fieldType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(getClass() == obj.getClass()) {
			EditorKey key = EditorKey.class.cast(obj);
			if(this.fieldType == key.fieldType && this.annotationType == key.annotationType) {
				return true;
			}
		}
		
		return false;
	}
	
}
