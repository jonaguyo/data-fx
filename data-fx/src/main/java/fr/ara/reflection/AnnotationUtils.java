package fr.ara.reflection;

import java.lang.annotation.Annotation;

import javassist.CtField;

public class AnnotationUtils {

	public static boolean hasAnnotationOrSuperAnnotation(CtField field, Class<? extends Annotation> annotationType) throws ClassNotFoundException {
		if(field.hasAnnotation(annotationType)) {
			return true;
		} else {
			for(Object fieldAnnotation : field.getAnnotations()) {
				Annotation trueFieldAnnotation = Annotation.class.cast(fieldAnnotation);
				for(Annotation annotation : trueFieldAnnotation.annotationType().getAnnotations()) {
					if(annotationType.isInstance(annotation)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
}
