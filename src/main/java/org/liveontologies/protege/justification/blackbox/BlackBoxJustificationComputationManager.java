package org.liveontologies.protege.justification.blackbox;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.liveontologies.protege.explanation.justification.service.JustificationComputation;
import org.liveontologies.protege.explanation.justification.service.JustificationComputation.InterruptMonitor;
import org.liveontologies.protege.explanation.justification.service.JustificationComputationManager;
import org.liveontologies.protege.explanation.justification.service.JustificationListener;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * @author Alexander Stupnikov Date: 22/06/2017
 */
public class BlackBoxJustificationComputationManager
		extends JustificationComputationManager {

	private final SettingsPanel panel_;
	private final WorkbenchSettings workbenchSettings_;
	private final OWLEditorKit kit_;

	private JustificationComputation computation_;

	public BlackBoxJustificationComputationManager(OWLAxiom entailment,
			JustificationListener listener, InterruptMonitor monitor,
			OWLEditorKit kit) {
		super(entailment, listener, monitor);
		kit_ = kit;
		panel_ = new SettingsPanel();
		workbenchSettings_ = new WorkbenchSettings();
		recreateComputation();
	}

	private void recreateComputation() {
		computation_ = new BlackBoxJustificationComputation(
				getJustificationListener(), getInterruptMonitor(),
				getEntailment(), kit_, workbenchSettings_);
		notifyJustificationsOutdated();
	}

	@Override
	public JustificationComputation getComputation() {
		return computation_;
	}

	@Override
	public JPanel getSettingsPanel() {
		return panel_;
	}

	public class SettingsPanel extends JPanel {
		private static final long serialVersionUID = -7739713369763932582L;

		public SettingsPanel() {
			JComboBox<String> cb = new JComboBox<String>();
			cb.addItem("Regular justifications");
			cb.addItem("Laconic justifications");
			cb.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					workbenchSettings_
							.setJustificationType((cb.getSelectedIndex() == 0
									? JustificationType.REGULAR
									: JustificationType.LACONIC));
					recreateComputation();
				}
			});
			setLayout(new BorderLayout());
			add(cb);
		}
	}
}