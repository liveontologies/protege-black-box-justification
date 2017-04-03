package uk.ac.manchester.cs.owl.explanation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.liveontologies.protege.explanation.justification.service.ComputationService;
import org.liveontologies.protege.explanation.justification.service.ComputationServiceListener;
import org.liveontologies.protege.explanation.justification.service.JustificationComputation;
import org.semanticweb.owlapi.model.OWLAxiom;

public class JustificationComputationServiceImpl extends ComputationService {

	private SettingsPanel panel;
	private WorkbenchSettings workbenchSettings;

	@Override
	public void initialise() throws Exception {
		panel = new SettingsPanel();
		workbenchSettings = new WorkbenchSettings();
	}

	@Override
	public boolean canComputeJustification(OWLAxiom entailment) {
		return true;
	}

	@Override
	public void dispose() {
	}

	@Override
	public JustificationComputation createJustificationComputation(OWLAxiom entailment) {
		return new JustificationComputator(entailment, getOWLEditorKit(), workbenchSettings);
	}

	@Override
	public String getName() {
		return "Workbench Justification";
	}

	@Override
	public JPanel getSettingsPanel() {
		return panel;
	}

	public class SettingsPanel extends JPanel {

		public SettingsPanel() {
			setLayout(new GridBagLayout());
			JRadioButton regularButton = new JRadioButton(new AbstractAction("Show regular justifications") {
				public void actionPerformed(ActionEvent e) {
					workbenchSettings.setJustificationType(JustificationType.REGULAR);
					settingsChanged();
				}
			});
			regularButton.setSelected(true);
			add(regularButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

			JRadioButton laconicButton = new JRadioButton(new AbstractAction("Show laconic justifications") {
				public void actionPerformed(ActionEvent e) {
					workbenchSettings.setJustificationType(JustificationType.LACONIC);
					settingsChanged();
				}
			});
			add(laconicButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST,
					GridBagConstraints.HORIZONTAL, new Insets(2, 10, 2, 2), 0, 0));

			ButtonGroup bg = new ButtonGroup();
			bg.add(regularButton);
			bg.add(laconicButton);
		}
	}
}