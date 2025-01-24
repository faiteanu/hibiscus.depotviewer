package de.open4me.depot.gui.dialogs;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.text.StringSubstitutor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.dialogs.AbstractDialog;
import de.willuhn.jameica.gui.input.TextAreaInput;
import de.willuhn.jameica.gui.parts.ButtonArea;
import de.willuhn.jameica.gui.parts.FormTextPart;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.util.ApplicationException;

public class WWWLinksDialog extends AbstractDialog<String> {

	private String url;
	private TextAreaInput textarea;
	private StringSubstitutor sub;
	private Map<String, Object> valuesMap;

	/**
	 * ct
	 * @param position
	 * @param sub 
	 * @param valuesMap 
	 * @param fehlerhafteOrder 
	 */
	public WWWLinksDialog(int position, String title, String url, StringSubstitutor sub, Map<String, Object> valuesMap)
	{
		super(position);
		this.setTitle(i18n.tr(title));
		setSize(600, SWT.DEFAULT);
		this.url = url;
		this.sub = sub;
		this.valuesMap = valuesMap;
	}

	/**
	 * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#getData()
	 */
	@Override
	protected String getData() throws Exception
	{	
		return url;
	}

	/**
	 * @see de.willuhn.jameica.gui.dialogs.AbstractDialog#paint(org.eclipse.swt.widgets.Composite)
	 */
	protected void paint(Composite parent) throws Exception
	{


		Container container = new SimpleContainer(parent, true);
		container.addHeadline(i18n.tr("CSV Download konfigurieren"));

		FormTextPart text = new FormTextPart();
		String out = "<form>" +
				"<p>Bitte die URL eintragen, von welcher die Kurse heruntergelanden werden sollen.</p><p>Folgende Variablen können in der URL genutzt werden:</p>";
		for (Entry<String, Object> x: valuesMap.entrySet()) {
			out = out + "${" + x.getKey() + "}  " + x.getValue()+"<br/>"; 
		}
		out = out + "</form>";
		text.setText(out);
		container.addPart(text);
		container.addHeadline(i18n.tr("URL mit optionalen Parametern"));
		textarea = new TextAreaInput(url, 2000);
		textarea.enable();
		container.addPart(textarea);


		container.addHeadline(i18n.tr("Finale URL"));
		TextAreaInput textareaout = new TextAreaInput("", 2000);
		container.addPart(textareaout);

		// Beim Ändern der Eingabe automatisch die Ausgabe aktualisieren
		((Text)textarea.getControl()).addModifyListener(new ModifyListener() {		
			@Override
			public void modifyText(ModifyEvent e) {
				url = (String) textarea.getValue();
		        String subsource = sub.replace(url);
		        textareaout.setValue(subsource);
			}
		});
		
		ButtonArea buttons = new ButtonArea();
		buttons.addButton(i18n.tr("Weiter"),new Action() {
			public void handleAction(Object context) throws ApplicationException
			{
				url = (String) textarea.getValue();
				close();
			}
		},null,true,"ok.png");
		container.addButtonArea(buttons);


	}

}