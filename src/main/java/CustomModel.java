import eu.amidst.core.datastream.Attributes;
import eu.amidst.core.datastream.DataInstance;
import eu.amidst.core.datastream.DataStream;
import eu.amidst.core.io.DataStreamLoader;
import eu.amidst.core.models.DAG;
import eu.amidst.core.variables.Variable;
import eu.amidst.latentvariablemodels.staticmodels.Model;
import eu.amidst.latentvariablemodels.staticmodels.exceptions.WrongConfigurationException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rcabanas on 23/05/16.
 */

public class CustomModel extends Model{

    public CustomModel(Attributes attributes) throws WrongConfigurationException {
        super(attributes);
    }

    @Override
    protected void buildDAG() {

        //Obtain the observed variables
        List<Variable> attrVars = vars.getListOfVariables().stream()
                .collect(Collectors.toList());

        // Create a list of local hidden variables
        List<Variable> localHidden = new ArrayList<Variable>();
        for(int i= 0; i< attrVars.size(); i++) {
            localHidden.add(vars.newGaussianVariable("LocallHidden"+i));
        }

        // Create a global hidden variable
        Variable globalHidden = vars.newMultinomialVariable("GlobalHidden",2);

        // Create a DAG over the variables (hidden and observed)
        DAG dag = new DAG(vars);

        // Add the links
        for (int i=0; i<attrVars.size(); i++) {
            dag.getParentSet(attrVars.get(i)).addParent(globalHidden);
            dag.getParentSet(attrVars.get(i)).addParent(localHidden.get(i));
        }

        //This is needed to maintain coherence in the Model class.
        this.dag = dag;
    }

    //Method for testing the custom model
    public static void main(String[] args) {
        String path = "datasets/static/noclassdata/";
        DataStream<DataInstance> data = DataStreamLoader.open(path+"data0.arff");
        Model model = new CustomModel(data.getAttributes());
        model.updateModel(data);
        System.out.println(model.getModel());
    }


}
