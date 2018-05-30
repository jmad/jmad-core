package cern.accsoft.steering.jmad.modeldefs.io;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import cern.accsoft.steering.jmad.domain.machine.RangeDefinition;
import cern.accsoft.steering.jmad.domain.machine.SequenceDefinition;
import cern.accsoft.steering.jmad.modeldefs.domain.JMadModelDefinition;
import cern.accsoft.steering.jmad.modeldefs.domain.OpticsDefinition;

/**
 * This class allows to formulate a 'request' for exporting particular optics,
 * sequences and/or ranges from a {@link JMadModelDefinition}. Request should be
 * created using the builder, available through the from() method.
 * 
 * This class is immutable.
 * 
 * @author mihostet
 *
 */
public class JMadModelDefinitionExportRequest {
	private final JMadModelDefinition modelDefinition;
	private final Collection<OpticsDefinition> opticsToExport;
	private final Collection<RangeDefinition> rangesToExport;

	private JMadModelDefinitionExportRequest(JMadModelDefinition modelDefinition,
			Collection<OpticsDefinition> opticsToExport, Collection<RangeDefinition> rangesToExport) {
		this.modelDefinition = modelDefinition;
		this.opticsToExport = opticsToExport;
		this.rangesToExport = rangesToExport;
	}

	/**
	 * Create a request to export ALL optics, sequences and ranges from a given
	 * model definition.
	 * 
	 * @param modelDefinition
	 *            the model to export
	 * @return the {@link JMadModelDefinitionExportRequest}
	 */
	public static JMadModelDefinitionExportRequest allFrom(JMadModelDefinition modelDefinition) {
		return from(modelDefinition).exportAllOptics().exportAllRanges().build();
	}

	/**
	 * Start the creation of a {@link JMadModelDefinitionExportRequest} for the
	 * given model definition.
	 * 
	 * @param modelDefinition
	 *            the model to export
	 * @return a builder for {@link JMadModelDefinitionExportRequest}
	 */
	public static JMadModelDefinitionExportRequest.Builder from(JMadModelDefinition modelDefinition) {
		return new Builder(modelDefinition);
	}

	/**
	 * Builder class
	 * 
	 * @author mihostet
	 *
	 */
	public static class Builder {
		private final JMadModelDefinition modelDefinition;
		private final ImmutableSet.Builder<OpticsDefinition> opticsToExport = ImmutableSet.builder();
		private final ImmutableSet.Builder<RangeDefinition> rangesToExport = ImmutableSet.builder();

		Builder(JMadModelDefinition modelDefinition) {
			this.modelDefinition = modelDefinition;
		}

		/**
		 * Request to export ALL optics from the model
		 * 
		 * @return this
		 */
		public Builder exportAllOptics() {
			opticsToExport.addAll(modelDefinition.getOpticsDefinitions());
			return this;
		}

		/**
		 * Request to export ALL ranges from the model
		 * 
		 * @return this
		 */
		public Builder exportAllRanges() {
			rangesToExport.addAll(modelDefinition.getRangeDefinitions());
			return this;
		}

		/**
		 * Add a particular {@link OpticsDefinition} to the list of exported optics
		 * 
		 * @param optics
		 *            the optic to export
		 * @return this
		 */
		public Builder export(OpticsDefinition optics) {
			opticsToExport.add(optics);
			return this;
		}

		/**
		 * Add all ranges from a particular {@link SequenceDefinition} to the list of
		 * ranges to export
		 * 
		 * @param sequence
		 *            the sequence to export including all ranges contained
		 * @return this
		 */
		public Builder exportAllRangesFrom(SequenceDefinition sequence) {
			sequence.getRangeDefinitions().forEach(this::export);
			return this;
		}

		/**
		 * Add a particular {@link RangeDefinition} to the list of ranges to export
		 * 
		 * @param range
		 *            the range to export
		 * @return this
		 */
		public Builder export(RangeDefinition range) {
			rangesToExport.add(range);
			return this;
		}

		/**
		 * Build the {@link JMadModelDefinitionExportRequest}
		 * 
		 * @return the final request
		 */
		public JMadModelDefinitionExportRequest build() {
			return new JMadModelDefinitionExportRequest(//
					modelDefinition, //
					opticsToExport.build(), //
					rangesToExport.build());
		}
	}

	/**
	 * Get all optics to export
	 * 
	 * @return collection of optics to export
	 */
	public Collection<OpticsDefinition> getOpticsToExport() {
		return opticsToExport;
	}

	/**
	 * Get all sequences to export (derived from the selected ranges)
	 * 
	 * @return collection of sequences to export
	 */
	public Collection<SequenceDefinition> getSequencesToExport() {
		return rangesToExport.stream().map(RangeDefinition::getSequenceDefinition).collect(toSet());
	}

	/**
	 * Get all ranges to export
	 * 
	 * @return collection of ranges to export
	 */
	public Collection<RangeDefinition> getRangesToExport() {
		return rangesToExport;
	}

	/**
	 * Get the model definition to export
	 * 
	 * @return the model definition
	 */
	public JMadModelDefinition getModelDefinition() {
		return modelDefinition;
	}
}
