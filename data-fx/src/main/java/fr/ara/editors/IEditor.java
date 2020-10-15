package fr.ara.editors;

import javafx.scene.Node;

@FunctionalInterface
public interface IEditor {
	
	public Node generate(EditorInfo info, Object obj);
	
}
