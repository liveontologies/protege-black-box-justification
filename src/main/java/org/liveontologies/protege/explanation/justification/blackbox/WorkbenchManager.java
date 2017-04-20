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


import org.semanticweb.owl.explanation.api.Explanation;
import org.semanticweb.owl.explanation.api.ExplanationGenerator;
import org.semanticweb.owl.explanation.api.ExplanationProgressMonitor;
import org.semanticweb.owlapi.model.OWLAxiom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author: Matthew Horridge
 * Stanford University
 * Bio-Medical Informatics Research Group
 * Date: 20/03/2012
 */
public class WorkbenchManager {

	private WorkbenchSettings workbenchSettings;
	private JustificationManager justificationManager;
	private OWLAxiom entailment;
	private ExplanationProgressMonitor<OWLAxiom> monitor;

	public WorkbenchManager(JustificationManager justificationManager, OWLAxiom entailment,
			ExplanationProgressMonitor<OWLAxiom> monitor, WorkbenchSettings workbenchSettings) {
		this.justificationManager = justificationManager;
		this.entailment = entailment;
		this.monitor = monitor;
		this.workbenchSettings = workbenchSettings;
	}

	public ExplanationProgressMonitor<OWLAxiom> getMonitor() {
		return monitor;
	}

	public WorkbenchSettings getWorkbenchSettings() {
		return workbenchSettings;
	}

	public OWLAxiom getEntailment() {
		return entailment;
	}

	public Set<Explanation<OWLAxiom>> getJustifications(OWLAxiom entailment) {
		JustificationType justificationType = workbenchSettings.getJustificationType();
		return justificationManager.getJustifications(entailment, justificationType, getMonitor());
	}

	public int getJustificationCount(OWLAxiom entailment) {
		JustificationType justificationType = workbenchSettings.getJustificationType();
		return justificationManager.getComputedExplanationCount(entailment, justificationType);
	}

	public JustificationManager getJustificationManager() {
		return justificationManager;
	}

	public int getPopularity(OWLAxiom axiom) {
		int count = 0;
		Set<Explanation<OWLAxiom>> justifications = justificationManager.getJustifications(entailment,
				workbenchSettings.getJustificationType(), getMonitor());
		for (Explanation<OWLAxiom> justification : justifications) {
			if (justification.contains(axiom)) {
				count++;
			}
		}
		return count;
	}
}