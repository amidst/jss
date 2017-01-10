import eu.amidst.core.datastream.DataStream;
import eu.amidst.core.io.DataStreamLoader;
import eu.amidst.core.io.DataStreamWriter;
import eu.amidst.core.variables.StateSpaceTypeEnum;
import eu.amidst.dynamic.io.DynamicDataStreamLoader;

import java.io.IOException;

/**
 * Created by rcabanas on 05/01/17.
 */
public class sect31datastreams {
	public static void main(String[] args) throws IOException {

		//Static data
		String path = "datasets/static/classdata/";
		DataStream data = DataStreamLoader.open(path+"data0.arff");

		data.getAttributes().forEach(att -> {
			String name = att.getName();
			StateSpaceTypeEnum type = att.getStateSpaceType().getStateSpaceTypeEnum();
			System.out.println(name +" "+type.name());
		});


		data.stream().forEach(
				dataInstance -> System.out.println(dataInstance)
		);

		DataStreamWriter.writeDataToFile(data, path+"data0_copy.arff");


		//Dynamic data
		path = "datasets/dynamic/classdata/";
		data = DynamicDataStreamLoader.open(path+"data0.arff");

		data.getAttributes().forEach(att -> {
			String name = att.getName();
			StateSpaceTypeEnum type = att.getStateSpaceType().getStateSpaceTypeEnum();
			System.out.println(name +" "+type.name());
		});


		data.stream().forEach(
				dataInstance -> System.out.println(dataInstance)
		);

		DataStreamWriter.writeDataToFile(data, path+"data0_copy.arff");


	}
}
