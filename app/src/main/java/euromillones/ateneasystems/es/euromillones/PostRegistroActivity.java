package euromillones.ateneasystems.es.euromillones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class PostRegistroActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registro);
        /**
         * Declaracion de componentes
         */
        Button btn_aceptar = (Button) findViewById(R.id.btn_aceptar);
        /**
         * Declaracion de variables
         */
        final Intent actividadLogin = new Intent(this, Login_Activity.class);//Esto lo ponemos aqui porque dentro del boton no funciona

        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(actividadLogin);
                finish();
            }
        });
    }
}
