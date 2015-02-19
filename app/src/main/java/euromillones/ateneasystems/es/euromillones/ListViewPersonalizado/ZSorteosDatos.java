package euromillones.ateneasystems.es.euromillones.ListViewPersonalizado;

/**
 * Created by cubel on 11/02/15.
 */
public class ZSorteosDatos {

    Integer id;
    String numeroSorteo;
    String fechaSorteo;

    /*public Course(String numeroSorteo, String fechaSorteo) {
        super();
        this.numeroSorteo = numeroSorteo;
        this.fechaSorteo = fechaSorteo;
    }*/

    public String getNumeroSorteo() {
        return numeroSorteo;
    }

    public void setNumeroSorteo(String numeroSorteo) {
        this.numeroSorteo = numeroSorteo;
    }

    public String getFechaSorteo() {
        return fechaSorteo;
    }

    public void setFechaSorteo(String fechaSorteo) {
        this.fechaSorteo = fechaSorteo;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}