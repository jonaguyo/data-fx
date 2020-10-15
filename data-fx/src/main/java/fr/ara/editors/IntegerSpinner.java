package fr.ara.editors;

import javafx.scene.control.Spinner;
import javafx.scene.control.TextFormatter;

public class IntegerSpinner extends Spinner<Number> {
	
	public IntegerSpinner(double min, double max, double initialValue) {
		super(min, max, initialValue);

		setEditable(true);
		getEditor().setTextFormatter(new TextFormatter<>(change -> 
		{
			try {
				Integer.parseInt(change.getControlNewText());
				return change;
			} catch(Exception e) {
				return null;
			}
		}));
	}

}
