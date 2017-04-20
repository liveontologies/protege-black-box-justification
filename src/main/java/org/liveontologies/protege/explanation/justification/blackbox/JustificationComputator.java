package org.liveontologies.protege.explanation.justification.blackbox;

/*-
 * #%L
 * Protege Blackbox Justification Explanation
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2016 - 2017 Live Ontologies Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.liveontologies.protege.explanation.justification.service.JustificationComputation;
import org.liveontologies.protege.explanation.justification.service.JustificationComputationListener;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationProgressMonitor;
import org.semanticweb.owlapi.model.OWLAxiom;

public class JustificationComputator extends JustificationComputation {

	private boolean isInterrupted = false;
	private List<JustificationComputationListener> listeners;
	private WorkbenchLogic logic;

	public JustificationComputator(OWLAxiom entailment, OWLEditorKit kit, WorkbenchSettings workbenchSettings) {
		super(entailment);
		//!!! should probably be moved from constructor to speed up the process
		listeners = new ArrayList<JustificationComputationListener>();
		JustificationProgressMonitor monitor = new JustificationProgressMonitor(this);
		logic = new WorkbenchLogic(kit, entailment, monitor, workbenchSettings);
	}

	@Override
	public void startComputation() {
		logic.startComputation();
	}

	private void foundExplanation(ArrayList<OWLAxiom> explanation) {
		for (JustificationComputationListener listener : new ArrayList<>(listeners))
			listener.foundJustification(explanation);
	}

	@Override
	public void interruptComputation() {
		isInterrupted = true;
	}

	@Override
	public boolean isComputationInterrupted() {
		return isInterrupted;
	}

	@Override
	public void addComputationListener(JustificationComputationListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeComputationListener(JustificationComputationListener listener) {
		listeners.remove(listener);
	}

	private class JustificationProgressMonitor implements ExplanationProgressMonitor<OWLAxiom> {

		private final JustificationComputator computation;

		public JustificationProgressMonitor(JustificationComputator computation) {
			this.computation = computation;
		}

		public void foundExplanation(ExplanationGenerator<OWLAxiom> owlAxiomExplanationGenerator,
				Explanation<OWLAxiom> explanation, Set<Explanation<OWLAxiom>> explanations) {
			ArrayList<OWLAxiom> list = new ArrayList<OWLAxiom>();
			for (OWLAxiom axiom : explanation.getAxioms())
				list.add(axiom);
			computation.foundExplanation(list);
		}

		public boolean isCancelled() {
			return computation.isComputationInterrupted();
		}
	}
}