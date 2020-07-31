package arkham.knight.ontology.services;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;

public class TDBConnectionService {

    private Dataset ds;


    public TDBConnectionService(String path) {

        ds = TDBFactory.createDataset(path);
    }


    public void loadModel(Model model, String path)
    {

        ds.begin(ReadWrite.WRITE);
        try
        {

            FileManager.get().readModel(model, path);
            ds.commit();
        }
        finally
        {
            ds.end();
        }
    }


    public void closeModel()
    {
        ds.close();
    }
}
