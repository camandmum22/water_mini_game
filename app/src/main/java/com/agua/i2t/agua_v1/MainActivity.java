package com.agua.i2t.agua_v1;

import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends Activity {

    //CONTROLES
    int meta = 3;
    int puntos = 0;
    TextView llave, gota, agua;
    Button llave_tope;
    int width=0;
    int height=0;

    RelativeLayout juego_foreground;
    SoundPool soundPool;
    //ID de sonidos
    int IDClick, IDYou_lose, IDYou_win, IDpop, IDAnswer, IDSplash;
    TextView dragon, estrella, globo_instrucciones;

    Button globo_texto_2;
    TextView globo_texto_3;
    LinearLayout presentacion, telon, tryagain;
    RelativeLayout padre;


    //Dinamics d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        padre = (RelativeLayout) findViewById(R.id.padre);
        presentacion = (LinearLayout) findViewById(R.id.presentacion);
        telon = (LinearLayout) findViewById(R.id.telon);
        tryagain = (LinearLayout) findViewById(R.id.tryagain);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;


        //SONIDO
        soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
        IDClick = soundPool.load(this, R.raw.click, 0);
        IDYou_lose = soundPool.load(this, R.raw.you_lose, 0);
        IDYou_win= soundPool.load(this, R.raw.you_win, 0);
        IDpop = soundPool.load(this, R.raw.pop, 0);
        IDAnswer = soundPool.load(this, R.raw.dragon_answer, 0);
        IDSplash = soundPool.load(this, R.raw.splash, 0);

        //SPRITES
        llave = (TextView) findViewById(R.id.llave);
        gota = (TextView) findViewById(R.id.gota);
        agua = (TextView) findViewById(R.id.agua);
        llave_tope = (Button) findViewById(R.id.llave_tope);

        dragon = (TextView) findViewById(R.id.dragon);
        globo_texto_2 = (Button) findViewById(R.id.globo_texto_2);
        globo_texto_3 = (TextView) findViewById(R.id.globo_texto_3);

        //globo_texto_2.setEnabled(false);


        estrella = (TextView) findViewById(R.id.estrella);

        globo_instrucciones = (TextView) findViewById(R.id.globo_informacion);

        //INSTANCIAS
        juego_foreground = (RelativeLayout) findViewById(R.id.juego_foreground);
        /*
        contador = (TextView) findViewById(R.id.contador);
        bombilos_ID = new int[]{R.id.B1, R.id.B2, R.id.B3, R.id.B4, R.id.B5, R.id.B6, R.id.B7, R.id.B8,R.id.B9};

        bombillos = new Button[9];
        bombillos_apagados = new ArrayList<>();
        for(int i=0 ; i<9 ; i++){
            bombillos[i] = (Button) findViewById(bombilos_ID[i]);
            bombillos_apagados.add(bombillos[i]);
        }
        */
        //ACOMODAR POR METICAS
        resizeAll();


        //DESAGREGAR DEL PADRE TODOS LOS VIES EXPETO LA PRESENTACION

        padre.removeView(dragon);
        padre.removeView(globo_texto_2);
        padre.removeView(globo_texto_3);
        padre.removeView(juego_foreground);
        padre.removeView(estrella);
        padre.removeView(telon);
        padre.removeView(tryagain);
        //
        globo_instrucciones.setScaleX((float) 0.1);
        globo_instrucciones.setScaleY((float) 0.1);

        globo_instrucciones.animate().setDuration(100).scaleX((float) 1.3).scaleY((float) 1.3).withEndAction(new Runnable() {
            @Override
            public void run() {
                globo_instrucciones.animate().setDuration(200).scaleX(1).scaleY(1);
                soundPool.play(IDpop, 1, 1, 1, 0, 1);
            }
        });
        animaciones();
    }

    public void animaciones(){
        final TranslateAnimation anim;
        final TranslateAnimation anim2;

        final int h2 = (int)(height*0.4);
        anim = new TranslateAnimation(0, 0, 0, h2);
        anim.setDuration(1000);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                soundPool.play(IDSplash, 1, 1, 1, 0, 1);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });


        anim2 = new TranslateAnimation(0, 0, (int)(height*0.1), 0);
        anim2.setDuration(1800);
        anim2.setRepeatCount(9);
        anim2.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) agua.getLayoutParams();
                params.topMargin -= (int)(height*0.1);
                params.bottomMargin += (int)(height*0.1);
                agua.setLayoutParams(params);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) agua.getLayoutParams();
                params.topMargin = (int)(height*0.1) * 10;
                params.bottomMargin = (int)(-height*0.1) * 10;
                agua.setLayoutParams(params);
                anim.cancel();
                puntos=0;
                rLose.run();
            }
        });
        gota.startAnimation(anim);
        agua.startAnimation(anim2);
    }


    public void ordenarCapas(){
        juego_foreground.removeAllViews();
        juego_foreground.addView(gota);
        juego_foreground.addView(llave);
        juego_foreground.addView(agua);
        juego_foreground.addView(llave_tope);
    }


    public void resizeAll(){
        /*
        for(int i=0 ; i<9 ; i++){
            bombillos[i].setLayoutParams(new LinearLayout.LayoutParams(ancho_boton, altura_boton));
        }
        */

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width/20, width/20);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        estrella.setLayoutParams(params);

        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams((int) (height/2.2), (int)(height/2.2));
        params2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        dragon.setLayoutParams(params2);
        dragon.setX(-500);

        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams((int) (height/2.2), (int)(height/2.2));
        params3.setMargins(0, height / 100, 0, 0);
        params3.addRule(RelativeLayout.CENTER_HORIZONTAL);
        globo_texto_2.setLayoutParams(params3);

        LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams((int) (width*0.75), (int) (width*0.75));
        params4.gravity = Gravity.CENTER;
        globo_instrucciones.setLayoutParams(params4);

        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams((int) (height/2.2), (int)(height/3.5));
        params5.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params5.setMargins(0, height / 5, 0, 0);
        globo_texto_3.setLayoutParams(params5);

        int w_llave = (int) (width*0.6);
        int h_margin = (int) (height*0.1);

        RelativeLayout.LayoutParams params6 = new RelativeLayout.LayoutParams(w_llave, (int)(w_llave*0.85));
        params6.addRule(RelativeLayout.START_OF);
        params6.setMargins(0, h_margin, 0, 0);
        llave.setLayoutParams(params6);

        RelativeLayout.LayoutParams params8 = new RelativeLayout.LayoutParams((int) (w_llave*0.3), (int)(w_llave*0.3));
        params8.addRule(RelativeLayout.START_OF);
        params8.setMargins(w_llave / 3, h_margin + 5, 0, 0);
        llave_tope.setLayoutParams(params8);

        RelativeLayout.LayoutParams params7 = new RelativeLayout.LayoutParams((int) (w_llave*0.2), (int)(w_llave*0.2/0.56));
        params7.addRule(RelativeLayout.START_OF);
        params7.setMargins((int) (w_llave - w_llave * 0.2), (int) (h_margin + w_llave *0.4), 0, 0);
        gota.setLayoutParams(params7);

        RelativeLayout.LayoutParams params9 = new RelativeLayout.LayoutParams((int) (width), height);
        params9.addRule(RelativeLayout.ALIGN_PARENT_START);
        params9.setMargins(0, (int) (height*0.9), 0, (int) (-height*0.9));
        agua.setLayoutParams(params9);
    }

    public void llave_tope(View view){
        //-----------------------
        soundPool.play(IDClick, 1, 1, 1, 0, 1);
        puntos++;
        if(puntos==meta) {
            puntos = 0;
            //Toast.makeText(this, "META OK", Toast.LENGTH_SHORT).show();
            gota.animate().cancel(); //gota.clearAnimation();
            agua.animate().cancel(); //agua.clearAnimation();

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) agua.getLayoutParams();
            params.topMargin = (int) (height * 0.1) * 10;
            params.bottomMargin = (int) (-height * 0.1) * 10;
            agua.setLayoutParams(params);
            /**/
            ordenarCapas();
            rWin.run();
            soundPool.play(IDAnswer, 1, 1, 1, 0, 1);
            soundPool.play(IDYou_win, 1, 1, 1, 0, 1);
        }
       // Toast.makeText(this, "PUNTOS "+puntos, Toast.LENGTH_SHORT).show();

    }

    Runnable rWin = new Runnable() {
        @Override
        public void run() {
            padre.addView(estrella);
            //reinicio=true;

            estrella.setAlpha(1);
            estrella.setScaleX((float) 0.1);
            estrella.setScaleY((float) 0.1);
            estrella.animate().scaleY(150).scaleX(150).setDuration(700).withEndAction(new Runnable() {
                @Override
                public void run() {
                    padre.removeView(estrella);
                    padre.removeView(juego_foreground);

                    telon.setAlpha(1);
                    telon.setBackgroundColor(Color.rgb(255, 236, 0));
                    padre.addView(telon);
                    padre.addView(dragon);
                    padre.addView(globo_texto_3);
                    padre.addView(tryagain);


                    globo_texto_3.setScaleX((float) 0.1);
                    globo_texto_3.setScaleY((float) 0.1);
                    globo_texto_3.animate().setDuration(100).scaleY((float) 1.3).scaleX((float) 1.5).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            globo_texto_3.animate().scaleX(1).scaleY(1).setDuration(200);
                        }
                    });

                    dragon.animate().x(0).setDuration(300);
                    tryagain.setAlpha(0);
                    tryagain.animate().alpha(1).setDuration(500).setStartDelay(800).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            tryagain.animate().setStartDelay(0);
                        }
                    });

                }
            });

            //View foreground = findViewById(R.id.juego_foreground);
            //((ViewGroup) foreground.getParent()).removeView(foreground);
            TextView diskView = (TextView) findViewById(R.id.estrella);

            // Create an animation instance
            int px = (int) estrella.getWidth() / 2;
            int py = (int) estrella.getHeight() / 2;


            Animation an = new RotateAnimation(0.0f, 360.0f, px, py);

            // Set the animation's parameters
            an.setDuration(500);               // duration in ms
            an.setRepeatCount(0);                // -1 = infinite repeated
            an.setRepeatMode(Animation.REVERSE); // reverses each repeat
            an.setFillAfter(true);               // keep rotation after animation

            // Aply animation to image view
            diskView.setAnimation(an);
            diskView.startAnimation(an);

        }
    };

    Runnable rLose = new Runnable() {
        @Override
        public void run() {
            telon.setBackgroundColor(Color.rgb(236, 155, 1));
            padre.addView(telon);
            padre.addView(dragon);
            padre.addView(globo_texto_2);
            globo_texto_2.setEnabled(true);
            soundPool.play(IDAnswer, 1, 1, 1, 0, 1);
            soundPool.play(IDYou_lose, 1, 1, 1, 0, 1);
            //reinicio=true;

            telon.animate().alpha(1).setDuration(300).withEndAction(new Runnable() {
                @Override
                public void run() {
                    padre.removeView(juego_foreground);
                }
            });

            juego_foreground.animate().alpha(0).setDuration(300);

            dragon.animate().x(0).setDuration(300);
            globo_texto_2.animate().alpha(1).scaleX((float) 1.2).scaleY((float) 1.5).setDuration(150).withEndAction(new Runnable() {
                @Override
                public void run() {
                    globo_texto_2.animate().alpha(1).scaleX(1).scaleY(1).setDuration(150);

                }
            });
            }
        };


    public void iniciarJuego(View view) {
        ((Button) view).setEnabled(false);

        soundPool.play(IDClick, 1, 1, 1, 0, 1);
        ordenarCapas();

        padre.addView(juego_foreground);
        padre.removeView(presentacion);
        padre.addView(presentacion);
        llave_tope.setEnabled(true);

        globo_instrucciones.animate().setDuration(50).scaleY((float) 1.3).scaleX((float) 1.5).withEndAction(new Runnable() {
            @Override
            public void run() {
                globo_instrucciones.animate().setDuration(50).scaleX(0).scaleY(0);
            }
        });


        presentacion.animate().alpha(0).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                padre.removeView(presentacion);
            }
        });
    }

    public void salir(View view) {
        tryagain.animate().setDuration(300).alpha(0).setStartDelay(0);
        telon.animate().alpha(0).setDuration(300);
        dragon.animate().setDuration(200).x(-dragon.getWidth());
        globo_texto_3.animate().scaleX((float) 1.2).scaleY((float) 1.4).setDuration(200).withEndAction(new Runnable() {
            @Override
            public void run() {
                globo_texto_3.animate().scaleY(0).scaleX(0).setDuration(150).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        System.exit(0);
                    }
                });

            }
        });

    }

    @Override
    public void onBackPressed() {
        //d.IS_ALIVE=false;
        //contador_alive=false;
    }

    //!INABILITAR EL BOTON OJO
    //boolean reinicio=true;
    public void reiniciar(View view) {
        //if(reinicio){
        //reinicio=false;
        juego_foreground.setAlpha(0);
        padre.addView(juego_foreground);
        juego_foreground.animate().alpha(1).setDuration(300);

        dragon.animate().setDuration(300).x(-dragon.getWidth());
        globo_texto_2.animate().setDuration(150).scaleX((float) 1.1).scaleY((float) 1.4).withEndAction(new Runnable() {
            @Override
            public void run() {
                globo_texto_2.animate().setDuration(100).scaleX(0).scaleY(0);
            }
        });

        globo_texto_3.animate().setDuration(80).scaleX((float) 1.1).scaleY((float) 1.4).withEndAction(new Runnable() {
            @Override
            public void run() {
                globo_texto_3.animate().setDuration(80).scaleX(0).scaleY(0);
            }
        });
        telon.animate().alpha(0).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                resizeAll();
                //globo_texto_2.setEnabled(false);
                padre.removeView(dragon);
                padre.removeView(globo_texto_2);
                padre.removeView(telon);
                //padre.removeView(juego_foreground);
                padre.removeView(estrella);
                padre.removeView(tryagain);
                //padre.removeView(dragon);
                padre.removeView(globo_texto_3);
            }
        });
        animaciones();
        //}
   }

}