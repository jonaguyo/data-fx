package fr.ara.editors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class EditorInfo {

	private Field field;
	private Annotation annotation;
	private EditorKey key;

	public EditorInfo(Field field, Annotation annotation) {
		this.field = field;
		this.annotation = annotation;
		this.key = new EditorKey(field.getType(), annotation.annotationType());
	}

	public Field getField() {
		return field;
	}

	public Annotation getAnnotation() {
		return annotation;
	}

	public EditorKey getKey() {
		return key;
	}
	
}
