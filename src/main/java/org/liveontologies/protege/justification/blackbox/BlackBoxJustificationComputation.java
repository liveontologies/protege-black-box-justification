package org.liveontologies.protege.justification.blackbox;

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


import java.util.Set;

import org.liveontologies.protege.explanation.justification.service.JustificationComputation;
import org.liveontologies.protege.explanation.justification.service.JustificationListener;
import org.protege.editor.owl.OWLEditorKit;
import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationProgressMonitor;
import org.semanticweb.owlapi.model.OWLAxiom;

/**
 * @author Alexander Stupnikov Date: 22/06/2017
 */
public class BlackBoxJustificationComputation extends JustificationComputation
		implements ExplanationProgressMonitor<OWLAxiom> {

	private WorkbenchLogic logic;

	public BlackBoxJustificationComputation(JustificationListener listener,
			InterruptMonitor monitor, OWLAxiom entailment, OWLEditorKit kit,
			WorkbenchSettings workbenchSettings) {
		super(listener, monitor);
		// !!! should probably be moved from constructor to speed up the process
		logic = new WorkbenchLogic(kit, entailment, this, workbenchSettings);
	}

	@Override
	public void startComputation() {
		logic.startComputation();
	}

	public void foundExplanation(
			ExplanationGenerator<OWLAxiom> owlAxiomExplanationGenerator,
			Explanation<OWLAxiom> explanation,
			Set<Explanation<OWLAxiom>> explanations) {
		notifyJustificationFound(explanation.getAxioms());
	}

	public boolean isCancelled() {
		return isInterrupted();
	}
}
