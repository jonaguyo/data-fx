package fr.ara.reflection;

import java.io.File;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import fr.ara.annotations.Editor;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

public class DataFX {
	
	private static final java.util.logging.Logger log = java.util.logging.Logger.getLogger(DataFX.class.getName());

	private static final String SIMPLE_BOOLEAN_PROPERTY = "javafx.beans.property.SimpleBooleanProperty";
	private static final String SIMPLE_SHORT_PROPERTY = "javafx.beans.property.SimpleShortProperty";
	private static final String SIMPLE_INTEGER_PROPERTY = "javafx.beans.property.SimpleIntegerProperty";
	private static final String SIMPLE_LONG_PROPERTY = "javafx.beans.property.SimpleLongProperty";
	private static final String SIMPLE_FLOAT_PROPERTY = "javafx.beans.property.SimpleFloatProperty";
	private static final String SIMPLE_DOUBLE_PROPERTY = "javafx.beans.property.SimpleDoubleProperty";
	private static final String SIMPLE_STRING_PROPERTY = "javafx.beans.property.SimpleStringProperty";
	
	private static Map<String, String> propertiesClassNames = new HashMap<>();
	
	static {
		propertiesClassNames.put(boolean.class.getName(), SIMPLE_BOOLEAN_PROPERTY);
		propertiesClassNames.put(Boolean.class.getName(), SIMPLE_BOOLEAN_PROPERTY);
		propertiesClassNames.put(short.class.getName(), SIMPLE_SHORT_PROPERTY);
		propertiesClassNames.put(Short.class.getName(), SIMPLE_SHORT_PROPERTY);
		propertiesClassNames.put(int.class.getName(), SIMPLE_INTEGER_PROPERTY);
		propertiesClassNames.put(Integer.class.getName(), SIMPLE_INTEGER_PROPERTY);
		propertiesClassNames.put(long.class.getName(), SIMPLE_LONG_PROPERTY);
		propertiesClassNames.put(Long.class.getName(), SIMPLE_LONG_PROPERTY);
		propertiesClassNames.put(float.class.getName(), SIMPLE_FLOAT_PROPERTY);
		propertiesClassNames.put(Float.class.getName(), SIMPLE_FLOAT_PROPERTY);
		propertiesClassNames.put(double.class.getName(), SIMPLE_DOUBLE_PROPERTY);
		propertiesClassNames.put(Double.class.getName(), SIMPLE_DOUBLE_PROPERTY);
		propertiesClassNames.put(String.class.getName(), SIMPLE_STRING_PROPERTY);
	}

	public static void init(String packageName) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		pool.importPackage(packageName);
		
		Enumeration<URL> classpath = ClassLoader.getSystemClassLoader().getResources("");
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.class");

		while (classpath.hasMoreElements()) {
			URL url = (URL) classpath.nextElement();
			Path root = new File(url.getPath()).toPath();
			Files.walk(root).forEach(path -> {
				if (matcher.matches(path)) {
					Path relativePath = root.relativize(path);
					String className = relativePath.toString().replace("\\", ".");
					className = className.substring(0, className.lastIndexOf("."));
					DataFX.modifyClass(className, pool);
				}
			});
		}
	}
	
	private static void modifyClass(String className, ClassPool pool) {
		try {
			CtClass cls = pool.get(className);
			boolean save = false;
			
			for(CtField field : cls.getDeclaredFields()) {
				if(AnnotationUtils.hasAnnotationOrSuperAnnotation(field, Editor.class)) {
					save = true;
					String fieldName = field.getName();
					try {
						String propertyClassName = propertiesClassNames.get(field.getType().getName());
						if(Objects.nonNull(propertyClassName)) {
							// Check if a setter is defined for the current editable field
							String setterName = String.format("set%s", StringUtils.capitalize(fieldName));
							CtMethod setter = cls.getDeclaredMethod(setterName, new CtClass[] { field.getType() });
							log.info(String.format("Setter find for the field '%s'", fieldName));
						
							// Add a property field corresponding to the current editable field
							CtClass propertyClass = pool.get(propertyClassName);
							String fieldPropertyName = String.format("%sProperty", fieldName);
							String fieldConstructor = String.format("new %s()", propertyClassName);
							cls.addField(new CtField(propertyClass, fieldPropertyName, cls), fieldConstructor);
							log.info(String.format("Property '%s' added to '%s'", fieldPropertyName, className));
							
							// Add an update of the property at the end of the setter
							setter.insertAfter(String.format("%s.set($1);", fieldPropertyName));
						} else {
							log.warning(String.format("No property class found for the type '%s' of the field '%s'", field.getType().getName(), fieldName));
						}
					} catch(Exception e) {
						continue;
					}
				}
			}
			
			if(save) {
				cls.toClass();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
