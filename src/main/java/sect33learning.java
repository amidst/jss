import eu.amidst.core.datastream.DataInstance;
import eu.amidst.core.datastream.DataStream;
import eu.amidst.core.io.DataStreamLoader;
import eu.amidst.core.io.DataStreamWriter;
import eu.amidst.core.models.BayesianNetwork;
import eu.amidst.core.variables.StateSpaceTypeEnum;
import eu.amidst.dynamic.io.DynamicDataStreamLoader;
import eu.amidst.dynamic.models.DynamicBayesianNetwork;
import eu.amidst.latentvariablemodels.dynamicmodels.DynamicModel;
import eu.amidst.latentvariablemodels.dynamicmodels.KalmanFilter;
import eu.amidst.latentvariablemodels.staticmodels.GaussianMixture;
import eu.amidst.latentvariablemodels.staticmodels.Model;

import java.io.IOException;

/**
 * Created by rcabanas on 05/01/17.
 */
public class sect33learning {
	public static void main(String[] args) throws IOException {

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

		/*
		 * Learning a static custom model
		 */

		path = "datasets/static/noclassdata/";
		data = DataStreamLoader.open(path+"data0.arff");
		model = new CustomModel(data.getAttributes());
		model.updateModel(data);
		System.out.println(model.getModel());


	}
}
