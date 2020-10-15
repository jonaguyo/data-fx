package fr.ara.test;

import java.util.Arrays;

import fr.ara.annotations.RangeEditor;
import fr.ara.editors.EditorKey;
import fr.ara.editors.EditorView;
import fr.ara.reflection.DataFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		DataFX.init("fr.ara");

		EditorKey key1 = new EditorKey(Integer.class, RangeEditor.class);
		EditorKey key2 = new EditorKey(Integer.class, RangeEditor.class);

		System.out.println(key1.hashCode() + " " + key2.hashCode());
		System.out.println(key1.equals(key2));

		DataTest data = new DataTest();
//		SimpleIntegerProperty i;
//		System.out.println(Arrays.toString(data.getClass().getDeclaredFields()));
//		Field f = data.getClass().getDeclaredField("intValueProperty");
		
//		f.getType().getMethod("set", int.class).invoke(f.get(data), 20);
		data.setIntValue(10);
		data.setRangeValue(3);
		
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
