package arkham.knight.ontology.models;

import java.io.Serializable;
import java.util.ArrayList;

public class BaseResponse implements Serializable {

    /**
     * Número de resultados que se aproximan a nuestra búsqueda, solo usado en algunos casos,
     * por ejemplo en búsquedas de definiciones
     */
    private int approx;
    /**
     * Lista de los resultados
     */
    private ArrayList<Res> res;

    public class Res {
        /**
         * Cabecera que se muestra en la definición de la palabra
         */
        private String header;
        /**
         * ID de la palabra (se usa para realizar búsquedas por ID, por ejemplo)
         */
        private String id;
        /**
         * En caso de haber varios resultados, grupo al que pertenece la palabra
         */
        private int grp;

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getGrp() {
            return grp;
        }

        public void setGrp(int grp) {
            this.grp = grp;
        }

        @Override
        public String toString() {
            return "Res{" +
                    "header='" + header + '\'' +
                    ", id='" + id + '\'' +
                    ", grp=" + grp +
                    '}';
        }
    }


    public int getApprox() {
        return approx;
    }

    public void setApprox(int approx) {
        this.approx = approx;
    }

    public ArrayList<Res> getRes() {
        return res;
    }

    public void setRes(ArrayList<Res> res) {
        this.res = res;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "approx=" + approx +
                ", res=" + res +
                '}';
    }
}
