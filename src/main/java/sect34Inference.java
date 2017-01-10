import eu.amidst.core.datastream.DataStream;
import eu.amidst.core.distribution.Distribution;
import eu.amidst.core.inference.ImportanceSampling;
import eu.amidst.core.inference.InferenceAlgorithm;
import eu.amidst.core.inference.messagepassing.VMP;
import eu.amidst.core.io.DataStreamLoader;
import eu.amidst.core.models.BayesianNetwork;
import eu.amidst.core.variables.Assignment;
import eu.amidst.core.variables.HashMapAssignment;
import eu.amidst.core.variables.Variable;
import eu.amidst.dynamic.inference.FactoredFrontierForDBN;
import eu.amidst.dynamic.inference.InferenceAlgorithmForDBN;
import eu.amidst.dynamic.io.DynamicDataStreamLoader;
import eu.amidst.dynamic.models.DynamicBayesianNetwork;
import eu.amidst.dynamic.variables.HashMapDynamicAssignment;
import eu.amidst.latentvariablemodels.dynamicmodels.DynamicModel;
import eu.amidst.latentvariablemodels.dynamicmodels.KalmanFilter;
import eu.amidst.latentvariablemodels.staticmodels.GaussianMixture;
import eu.amidst.latentvariablemodels.staticmodels.Model;

import java.io.IOException;
import java.util.Random;

/**
 * Created by rcabanas on 05/01/17.
 */
public class sect34Inference {
	public static void main(String[] args) throws IOException {


		// Note: this code-example shows again the code for learning the models (i.e., class sect33learning)
		// The new code is the one corresponding to the inference

		/*
		 * Learning a static predefined model
		 */

		String path = "datasets/static/noclassdata/";
		DataStream data = DataStreamLoader.open(path+"data0.arff");

		// Build the model
		Model model =
				new GaussianMixture(data.getAttributes())
						.setNumStatesHiddenVar(2);

		model.updateModel(data);  // Learn the distributions
		BayesianNetwork bn = model.getModel();  // Obtain the learnt BN
		System.out.println(bn);  // Print the BN


		//Update the model with new information
		for(int i=1; i<12; i++) {
			data = DataStreamLoader.open(path+"data"+i+".arff");
			model.updateModel(data);
			System.out.println(model.getModel());
		}


		/*
		 * Learning a dynamic predefined model
		 */

		path = "datasets/dynamic/noclassdata/";
		data = DynamicDataStreamLoader.open(path+"data0.arff");


		//Learn the model
		DynamicModel dmodel =
				new KalmanFilter(data.getAttributes())
						.setNumHidden(2);

		dmodel.updateModel(data); //Learn the distributions
		DynamicBayesianNetwork dbn = dmodel.getModel(); //Obtain the learnt dynamic BN
		System.out.println(dbn); // Print the dynamic BN and save it


		/**
		 * Inference in the static model
		 */


		// Set the variables of interest
		Variable varTarget = bn.getVariables().getVariableByName("HiddenVar");

		// Set the evidence
		Assignment assignment = new HashMapAssignment();
		assignment.setValue(bn.getVariables().getVariableByName("GaussianVar8"), 8.0);
		assignment.setValue(bn.getVariables().getVariableByName("GaussianVar9"), -1.0);

		// Set the inference algorithm
		InferenceAlgorithm infer = new VMP();
		infer.setModel(bn);
		infer.setEvidence(assignment);

		// Run the inference
		infer.runInference();
		Distribution p = infer.getPosterior(varTarget);
		System.out.println("P(HiddenVar|GaussianVar8=8.0, GaussianVar9=-1.0) = "+p);


		/**
		 * Inference in a dynamic model
		 */


		Random rand = new Random(1234);

		// Select the inference algorithm
		InferenceAlgorithmForDBN dinfer = new FactoredFrontierForDBN(new ImportanceSampling());
		dinfer.setModel(dbn);

		// Set the variables of interest
		Variable dvarTarget = dbn.getDynamicVariables().getVariableByName("gaussianHiddenVar1");

		for(int t=0; t<10; t++) {
			// Set the evidence
			HashMapDynamicAssignment dassignment = new HashMapDynamicAssignment(2);
			dassignment.setValue(dbn.getDynamicVariables().getVariableByName("GaussianVar9"),
					rand.nextDouble());
			dassignment.setValue(dbn.getDynamicVariables().getVariableByName("GaussianVar8"),
					rand.nextDouble());
			dassignment.setTimeID(t);

			// Run the inference
			dinfer.addDynamicEvidence(dassignment);
			dinfer.runInference();

			// Get the posterior at current instant of time
			Distribution posterior_t = dinfer.getFilteredPosterior(dvarTarget);
			System.out.println("t="+t+" "+posterior_t);

			// Get the posterior in the future
			Distribution posterior_t_1 = dinfer.getPredictivePosterior(dvarTarget, 1);
			System.out.println("t="+t+"+1 "+posterior_t_1);

		}



	}
}
