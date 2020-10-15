package fr.ara.editors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import fr.ara.annotations.Editor;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

public class EditorView extends TitledPane {
	
	public EditorView(Object obj) throws Exception {
		List<EditorInfo> editablesField = new LinkedList<>();
		
		for(Field field : obj.getClass().getDeclaredFields()) {
			if(field.isAnnotationPresent(Editor.class)) {
				editablesField.add(new EditorInfo(field, field.getAnnotation(Editor.class)));
			} else {
				for(Annotation fieldAnnotation : field.getDeclaredAnnotations()) {
					for(Annotation annotation : fieldAnnotation.annotationType().getAnnotations()) {
						if(Editor.class.isInstance(annotation)) {
							editablesField.add(new EditorInfo(field, fieldAnnotation));
						}
					}
				}
			}
		}
		
		GridPane pane = new GridPane();
		int line = 0;
		for(EditorInfo editable : editablesField) {			
			String name = editable.getField().getName().replaceAll("([a-z0-9])([A-Z])", "$1 $2");
			Label fieldName = new Label(StringUtils.capitalize(name));
			fieldName.setId("editor-label");
			pane.add(fieldName, 0, line);
			pane.add(AbstractEditorFactory.createEditor(editable, obj), 1, line++);
		}
		setText(obj.getClass().getSimpleName());
		setContent(pane);
		setId("editor");
		pane.setId("editor-container");
	}
	
}
