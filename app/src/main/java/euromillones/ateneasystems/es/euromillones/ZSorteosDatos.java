package euromillones.ateneasystems.es.euromillones;

/**
 * Created by cubel on 11/02/15.
 */
public class ZSorteosDatos {
    String tv_numeroSorteo;
    String tv_fechaSorteo;

    public ZSorteosDatos(String tv_numeroSorteo, String tv_fechaSorteo) {
        super();
        this.tv_numeroSorteo = tv_numeroSorteo;
        this.tv_fechaSorteo = tv_fechaSorteo;
    }

    public String getNumeroSorteo() {
        return tv_numeroSorteo;
    }

    public void setNumeroSorteo(String tv_numeroSorteo) {
        this.tv_numeroSorteo = tv_numeroSorteo;
    }

    public String getFechaSorteo() {
        return tv_fechaSorteo;
    }

    public void setFechaSorteo(String tv_fechaSorteo) {
        this.tv_fechaSorteo = tv_fechaSorteo;
    }


}
