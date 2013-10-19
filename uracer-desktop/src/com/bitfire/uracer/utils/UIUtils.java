
package com.bitfire.uracer.utils;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bitfire.uracer.URacer;
import com.bitfire.uracer.resources.Art;

public final class UIUtils {

	public static Stage newScaledStage () {
		Stage stage = new Stage(0, 0, false);
		// UICamera cam = new UICamera();
		// stage.setCamera(cam);
		stage.setViewport(ScaleUtils.PlayWidth, ScaleUtils.PlayHeight, false, ScaleUtils.CropX, ScaleUtils.CropY,
			ScaleUtils.PlayWidth, ScaleUtils.PlayHeight);
		return stage;
	}

	public static TextButton newTextButton (String text, ClickListener listener) {
		TextButton btn = new TextButton(text, Art.scrSkin);
		btn.addListener(listener);
		return btn;
	}

	public static CheckBox newCheckBox (String text, boolean checked) {
		return newCheckBox(text, checked, null);
	}

	public static CheckBox newCheckBox (String text, boolean checked, ChangeListener listener) {
		CheckBox cb = new CheckBox(" " + text, Art.scrSkin);
		cb.setChecked(checked);
		if (listener != null) {
			cb.addListener(listener);
		}
		return cb;
	}

	public static SelectBox newSelectBox (Object[] items) {
		return newSelectBox(items, null);
	}

	public static SelectBox newSelectBox (Object[] items, ChangeListener listener) {
		SelectBox sb = new SelectBox(items, Art.scrSkin);
		if (listener != null) {
			sb.addListener(listener);
		}
		return sb;
	}

	public static List newListBox (Object[] items) {
		return newListBox(items, null);
	}

	public static List newListBox (Object[] items, ChangeListener listener) {
		List list = new List(items, Art.scrSkin);
		if (listener != null) {
			list.addListener(listener);
		}
		return list;
	}

	public static Label newLabel (String text, boolean wrap) {
		Label l = new Label(text, Art.scrSkin);
		l.setWrap(wrap);
		return l;
	}

	public static ScrollPane newScrollPane () {
		ScrollPane pane = new ScrollPane(null, Art.scrSkin);
		return pane;
	}

	public static Table newVersionInfoTable () {
		Table infoTable = new Table(Art.scrSkin);
		infoTable.debug();
		Label versionLabel = UIUtils.newLabel("uRacer " + URacer.versionInfo, false);
		infoTable.add(versionLabel).expand().bottom().left().padLeft(5);
		return infoTable;
	}

	private UIUtils () {
	}
}
