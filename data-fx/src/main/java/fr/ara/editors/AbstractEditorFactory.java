package fr.ara.editors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import fr.ara.annotations.Editor;
import fr.ara.annotations.RangeEditor;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class AbstractEditorFactory {
	
	private static Map<EditorKey, IEditor> editors = new HashMap<>();
	
	static {
		//--------------------
		// Default editors :
		editors.put(new EditorKey(boolean.class, Editor.class), AbstractEditorFactory::createBooleanEditor);
		editors.put(new EditorKey(Boolean.class, Editor.class), AbstractEditorFactory::createBooleanEditor);
		editors.put(new EditorKey(short.class, Editor.class), AbstractEditorFactory::createShortEditor);
		editors.put(new EditorKey(Short.class, Editor.class), AbstractEditorFactory::createShortEditor);
		editors.put(new EditorKey(int.class, Editor.class), AbstractEditorFactory::createIntegerEditor);
		editors.put(new EditorKey(Integer.class, Editor.class), AbstractEditorFactory::createIntegerEditor);
		editors.put(new EditorKey(String.class, Editor.class), AbstractEditorFactory::createStringEditor);
		editors.put(new EditorKey(Color.class, Editor.class), AbstractEditorFactory::createColorEditor);
		
		//--------------------
		// Range editors :
		editors.put(new EditorKey(short.class, RangeEditor.class), AbstractEditorFactory::createSliderShortEditor);
		editors.put(new EditorKey(Short.class, RangeEditor.class), AbstractEditorFactory::createSliderShortEditor);
		editors.put(new EditorKey(int.class, RangeEditor.class), AbstractEditorFactory::createSliderIntegerEditor);
		editors.put(new EditorKey(Integer.class, RangeEditor.class), AbstractEditorFactory::createSliderIntegerEditor);
	}
	
	public static void registerEditor(EditorKey key, IEditor editor) {
		editors.put(key, editor);
	}
	
	public static Node createEditor(EditorInfo info, Object obj) throws Exception {
		if(info.getField().trySetAccessible()) {
			Class<?> fieldType = info.getField().getType();
			
			if(fieldType.isEnum()) {
				return createEnumEditor(info, obj);
			} else {
				IEditor editor = editors.get(info.getKey());
				if(Objects.nonNull(editor)) {
					return editor.generate(info, obj);
				}
			}
			
			return new Label("The field has no editor available.");
		} else {
			return new Label("The field is not accessible.");
		}
	}

	private static Node createBooleanEditor(EditorInfo info, Object obj) {
		Field field = info.getField();
		boolean initialValue = false;
		try {
			initialValue = field.getBoolean(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		CheckBox checkBox = new CheckBox();
		checkBox.setSelected(initialValue);
		checkBox.selectedProperty().addListener((obs, o, n) -> {
			try {
				field.setBoolean(obj, n);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		checkBox.setId("editor-check-box");
		return checkBox;
	}
	
	private static Node createShortEditor(EditorInfo info, Object obj) {
		Field field = info.getField();
		short initialValue = 0;
		try {
			initialValue = field.getShort(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Spinner<Number> spinner = new IntegerSpinner(Short.MIN_VALUE, Short.MAX_VALUE, initialValue);
		spinner.valueProperty().addListener((obs, o, n) -> {
			try {
				field.setShort(obj, n.shortValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		spinner.setId("editor-spinner");
		return spinner;
	}
	
	private static Node createIntegerEditor(EditorInfo info, Object obj) {
		Field field = info.getField();
		int initialValue = 0;
		try {
			initialValue = field.getInt(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Spinner<Number> spinner = new IntegerSpinner(Integer.MIN_VALUE, Integer.MAX_VALUE, initialValue);
		spinner.valueProperty().addListener((obs, o, n) -> {
			try {
				field.setInt(obj, n.intValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		spinner.setMaxWidth(Double.MAX_VALUE);
		spinner.setId("editor-spinner");
		return spinner;
	}
	
	private static Node createSliderShortEditor(EditorInfo info, Object obj) {
		Field field = info.getField();
		Annotation annotation = info.getAnnotation();
		RangeEditor rangeAnnotation = RangeEditor.class.cast(annotation);
		short initialValue = 0;
		try {
			initialValue = field.getShort(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TextField valueField = new TextField();
		valueField.setText("" + initialValue);
		valueField.setEditable(false);
		valueField.setPrefWidth(40);
		valueField.setId("editor-text-field");
		
		Slider slider = new Slider(rangeAnnotation.min(), rangeAnnotation.max(), initialValue);
		slider.valueProperty().addListener((obs, o, n) -> {
			try {
				field.setInt(obj, n.shortValue());
				valueField.setText("" + n.shortValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		slider.setId("editor-slider");
		HBox container = new HBox(3, slider, valueField);
		container.setAlignment(Pos.CENTER);
		return container;
	}
	
	private static Node createSliderIntegerEditor(EditorInfo info, Object obj) {
		Field field = info.getField();
		Annotation annotation = info.getAnnotation();
		RangeEditor rangeAnnotation = RangeEditor.class.cast(annotation);
		int initialValue = 0;
		try {
			initialValue = field.getInt(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TextField valueField = new TextField();
		valueField.setText("" + initialValue);
		valueField.setEditable(false);
		valueField.setPrefWidth(40);
		valueField.setId("editor-text-field");
		
		Slider slider = new Slider(rangeAnnotation.min(), rangeAnnotation.max(), initialValue);
		slider.valueProperty().addListener((obs, o, n) -> {
			try {
				field.setInt(obj, n.intValue());
				valueField.setText("" + n.intValue());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		slider.setId("editor-slider");
		HBox container = new HBox(3, slider, valueField);
		container.setAlignment(Pos.CENTER);
		return container;
	}
	
	private static Node createStringEditor(EditorInfo info, Object obj) {
		Field field = info.getField();
		Object fieldValue = null;
		try {
			fieldValue = field.get(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		TextField textField = new TextField(Objects.nonNull(fieldValue) ? fieldValue.toString() : "");
		textField.textProperty().addListener((obs, o, n) -> {
			try {
				field.set(obj, n);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		textField.setId("editor-text-field");
		return textField;
	}
	
	private static Node createEnumEditor(EditorInfo info, Object obj) {
		Field field = info.getField();
		Class<?> fieldType = field.getType();
		ComboBox<Object> comboBox = new ComboBox<>();
		comboBox.getItems().addAll(fieldType.getEnumConstants());
		comboBox.valueProperty().addListener((obs, o, n) -> {
			try {
				field.set(obj, n);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		comboBox.setId("editor-combo-box");
		comboBox.setMaxWidth(Double.MAX_VALUE);
		comboBox.setValue(fieldType.getEnumConstants()[0]);
		return comboBox;
	}
	
	private static Node createColorEditor(EditorInfo info, Object obj) {
		Field field = info.getField();
		ColorPicker picker = new ColorPicker();
		try {
			Object fieldValue = field.get(obj);
			if(Objects.nonNull(fieldValue)) {
				picker.setValue(Color.class.cast(fieldValue));
			} else {
				field.set(obj, picker.valueProperty().get());
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		picker.valueProperty().addListener((obs, o, n) -> {
			try {
				field.set(obj, n);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		picker.setMaxWidth(Double.MAX_VALUE);
		return picker;
	}
	
}
