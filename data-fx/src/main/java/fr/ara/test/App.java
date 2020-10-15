package fr.ara.test;

import java.lang.reflect.Field;
import java.util.Arrays;

import fr.ara.annotations.RangeEditor;
import fr.ara.editors.EditorKey;
import fr.ara.editors.EditorView;
import fr.ara.reflection.DataFX;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		DataFX.init("fr.ara");
		SimpleIntegerProperty intValueProperty = null;
		if(intValueProperty == null) {
			intValueProperty = new javafx.beans.property.SimpleIntegerProperty();
			System.out.println("property constructed " + System.identityHashCode(intValueProperty));
			intValueProperty.addListener((obs, o, n) -> {
				
			});
		}

		DataTest data = new DataTest();
//		SimpleIntegerProperty i;
//		System.out.println(Arrays.toString(data.getClass().getDeclaredFields()));
		Field f = data.getClass().getDeclaredField("intValueProperty");
		SimpleIntegerProperty prop = (SimpleIntegerProperty) f.get(data);
		System.out.println("hashcode = " + System.identityHashCode(prop));
		prop.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				
			}
		});
		System.out.println(prop + " " + data.getIntValue());
		data.setIntValue(10);
		System.out.println(prop + " " + data.getIntValue());
		prop.set(20);
		System.out.println(prop + " " + data.getIntValue());
		
		System.out.println(Arrays.toString(data.getClass().getDeclaredFields()));
		
		EditorView editor = new EditorView(data);
		VBox container = new VBox();
		Button test = new Button("Print data");
		test.setOnAction(e -> System.out.println(data));
		container.getChildren().addAll(editor, new EditorView(new DataTest()), test);
		
		Scene scene = new Scene(container, 400, 500);
		scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

}
