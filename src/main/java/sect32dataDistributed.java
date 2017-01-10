import eu.amidst.core.datastream.DataInstance;
import eu.amidst.core.datastream.DataStream;
import eu.amidst.core.io.DataStreamLoader;
import eu.amidst.core.io.DataStreamWriter;
import eu.amidst.core.variables.StateSpaceTypeEnum;
import eu.amidst.dynamic.io.DynamicDataStreamLoader;
import eu.amidst.flinklink.core.data.DataFlink;
import eu.amidst.flinklink.core.io.DataFlinkLoader;
import eu.amidst.flinklink.core.io.DataFlinkWriter;
import org.apache.flink.api.java.ExecutionEnvironment;

import java.io.IOException;

/**
 * Created by rcabanas on 05/01/17.
 */
public class sect32dataDistributed {
	public static void main(String[] args) throws Exception {

		String path = "datasets/static/classdata/";

		// Set-up Flink session
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

		// Load the distributed data
		DataFlink<DataInstance> data = DataFlinkLoader.open(env, path+"dataFlink0.arff", false);

		// Print all the instances from the distributed file
		data.getDataSet().collect().forEach(dataInstance -> System.out.println(dataInstance));

		// Save the data into a distributed ARFF folder
		DataFlinkWriter.writeDataToARFFFolder(data, path+"dataFlink0_copy.arff");
	}
}
