package fr.ara.test;

import fr.ara.annotations.Editor;
import fr.ara.annotations.RangeEditor;
import javafx.scene.paint.Color;
import lombok.Data;

@Data
public class DataTest {
	
	@Editor private int intValue;
//	@RangeEditor(min = -10, max = 10) private int rangeValue;
//	@Editor private String stringValue;
//	@Editor private EnumTest enumValue;
//	@Editor private Color colorValue;
//	@Editor private boolean booleanValue;
	
//	@Override
//	public String toString() {
//		return String.format("data : %d %d %s %s %s %s", intValue, rangeValue, stringValue, enumValue, colorValue.toString(), booleanValue);
//	}
	
}
