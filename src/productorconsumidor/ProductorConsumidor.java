
package productorconsumidor;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductorConsumidor implements Runnable{
 
    //Se emplea lock, que es una herramienta que controla el acceso a un recurso compartido
    //por multiples hilos
   
    //variable que compraten todos los hilos
    private static int magdalena;
    
    //la variable productor indica que si es true, es producotr, y si es false 
    //es un consumidor
    private boolean productor;
    

    //Tambien es importante que el cerrojo se comparta con todos los hilos
    private static Object lock = new Object();
 
    //constructor para consumidor/productor
    public ProductorConsumidor(boolean productor) {
        this.productor = productor;
    }
    public static void main(String[] args) {
        Thread[] hilos = new Thread[8];
        Runnable runnable = null;
        for(int i=0; i<hilos.length;i++){
            if (i==0){
            runnable = new ProductorConsumidor(true);//productor
        }else{
                runnable = new ProductorConsumidor(false);//consumidor
                }
        hilos[i] = new Thread(runnable);
        hilos[i].start();
    
        }
        try {
            for (int i = 0; i< hilos.length; i++) {
                hilos[i].join();
            }
        } catch (Exception e) {
        }
        
 
       
    }
    public void productor() throws InterruptedException {
    synchronized (lock) {
        if (magdalena == 0) {
            magdalena = 1;
            lock.notifyAll();
            System.out.println("Productor ha terminado de cocinar " + magdalena + " magdalena");
        }
        wait();
    }
}
    public void consumidor() throws InterruptedException {
    synchronized (lock) {
        if (magdalena > 0) {
            System.out.println("Consumidor tiene la magdalena numero: " + magdalena);
            magdalena--;
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Error de interrupcion");
            }
        } else {
            lock.notifyAll();
            wait();
        }
    }
}
    

    @Override
    public void run() {
        while (true) {
            if (productor) {try {
                //es productor
                productor();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProductorConsumidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                try {
                //es consumidor
                consumidor();
                } catch (InterruptedException ex) {
                    Logger.getLogger(ProductorConsumidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
 
    public void esperar() {
        try {
            lock.wait();
        } catch (InterruptedException ex) {
            System.out.println("Error de interrupcion");
        }
    }
    }
    

