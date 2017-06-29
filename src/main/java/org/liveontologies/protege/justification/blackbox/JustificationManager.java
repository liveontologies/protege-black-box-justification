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

import org.protege.editor.core.Disposable;
import org.protege.editor.core.log.LogBanner;
import org.protege.editor.owl.model.OWLModelManager;
import org.semanticweb.owl.explanation.api.*;
import org.semanticweb.owl.explanation.impl.blackbox.checker.InconsistentOntologyExplanationGeneratorFactory;
import org.semanticweb.owl.explanation.impl.laconic.LaconicExplanationGeneratorFactory;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import javax.swing.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Matthew Horridge The University Of Manchester Information Management
 * Group Date: 03-Oct-2008 Manages aspects of explanation in Protege 4.
 */
public class JustificationManager implements Disposable {

	public static final String KEY = "org.liveontologies.protege.justification.blackbox";
	public static final Marker MARKER = MarkerFactory.getMarker("Explanation");
	private static final Logger logger = LoggerFactory
			.getLogger(JustificationManager.class);

	private final OWLOntologyChangeListener ontologyChangeListener_;
	private final OWLModelManager modelManager_;
	private final JustificationCacheManager justificationCacheManager_;

	private JustificationManager(JFrame parentWindow,
			OWLModelManager modelManager) {
		modelManager_ = modelManager;
		justificationCacheManager_ = new JustificationCacheManager();
		ontologyChangeListener_ = changes -> justificationCacheManager_.clear();
		modelManager.addOntologyChangeListener(ontologyChangeListener_);
	}

	public void getJustifications(OWLAxiom entailment, JustificationType type,
			ExplanationProgressMonitor<OWLAxiom> monitor)
			throws ExplanationException {
		JustificationCache cache = getJustificationCache(type);
		if (cache.contains(entailment)) {
			enumerateJustifications(cache.get(entailment), entailment, monitor);
		} else {
			cache.initialise(entailment);
			computeJustifications(entailment, type, monitor);
		}
	}

	private void enumerateJustifications(
			Set<Explanation<OWLAxiom>> justifications, OWLAxiom entailment,
			ExplanationProgressMonitor<OWLAxiom> monitor) {
		logger.info(LogBanner.start("Justifications are found in the cache"));
		logger.info(MARKER, "Obtaining justifications for {}", entailment);
		for (Explanation<OWLAxiom> justification : justifications) {
			monitor.foundExplanation(null, justification, null);
		}
		logger.info(MARKER,
				"A total of {} justifications have been obtained from the cache",
				justifications.size());
		logger.info(LogBanner.end());
	}

	private void computeJustifications(OWLAxiom entailment,
			JustificationType justificationType,
			ExplanationProgressMonitor<OWLAxiom> monitor)
			throws ExplanationException {
		logger.info(LogBanner.start("Computing Justifications"));
		logger.info(MARKER, "Computing justifications for {}", entailment);
		Set<OWLAxiom> axioms = new HashSet<>();
		for (OWLOntology ont : modelManager_.getActiveOntologies()) {
			axioms.addAll(ont.getAxioms());
		}
		ExplanationGeneratorFactory<OWLAxiom> factory = getCurrentExplanationGeneratorFactory(
				justificationType, monitor);
		ExplanationGenerator<OWLAxiom> generator = factory
				.createExplanationGenerator(axioms, monitor);
		Set<Explanation<OWLAxiom>> explanations = generator
				.getExplanations(entailment);
		logger.info(MARKER, "A total of {} justifications have been computed",
				explanations.size());
		logger.info(LogBanner.end());
	}

	private ExplanationGeneratorFactory<OWLAxiom> getCurrentExplanationGeneratorFactory(
			JustificationType type,
			ExplanationProgressMonitor<OWLAxiom> monitor) {
		OWLReasoner reasoner = modelManager_.getOWLReasonerManager()
				.getCurrentReasoner();
		if (reasoner.isConsistent()) {
			if (type.equals(JustificationType.LACONIC)) {
				OWLReasonerFactory rf = getReasonerFactory();
				return ExplanationManager
						.createLaconicExplanationGeneratorFactory(rf, monitor);
			} else {
				OWLReasonerFactory rf = getReasonerFactory();
				return ExplanationManager.createExplanationGeneratorFactory(rf,
						monitor);
			}
		} else {
			if (type.equals(JustificationType.LACONIC)) {
				OWLReasonerFactory rf = getReasonerFactory();
				InconsistentOntologyExplanationGeneratorFactory fac = new InconsistentOntologyExplanationGeneratorFactory(
						rf, Long.MAX_VALUE);
				return new LaconicExplanationGeneratorFactory<>(fac);
			} else {
				OWLReasonerFactory rf = getReasonerFactory();
				return new InconsistentOntologyExplanationGeneratorFactory(rf,
						Long.MAX_VALUE);
			}
		}

	}

	public OWLReasonerFactory getReasonerFactory() {
		return new ProtegeOWLReasonerFactoryWrapper(modelManager_
				.getOWLReasonerManager().getCurrentReasonerFactory());
	}

	public void dispose() {
		modelManager_.removeOntologyChangeListener(ontologyChangeListener_);
	}

	public static synchronized JustificationManager getJustificationManager(
			JFrame parentWindow, OWLModelManager modelManager) {
		JustificationManager m = modelManager.get(KEY);
		if (m == null) {
			m = new JustificationManager(parentWindow, modelManager);
			modelManager.put(KEY, m);
		}
		return m;
	}

	public JustificationCache getJustificationCache(JustificationType type) {
		return justificationCacheManager_.getJustificationCache(type);
	}
}