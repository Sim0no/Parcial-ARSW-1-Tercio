/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.exams.moneylaundering;

/**
 *
 * @author german.marin
 */
import java.io.File;
import java.util.ArrayList;
import java.util.List;
class LavadoActivosConcurrente extends Thread{
    private MoneyLaundering moneyLaundering;
    private List<Transaction> transactiones;
    private ArrayList<File> archivos;
    private TransactionReader lectorTransacciones;
    private TransactionAnalyzer analizadorTr;
    private boolean pausado;
    private Object monitor;
    public LavadoActivosConcurrente(ArrayList<File> arcivos, TransactionReader transacciones, TransactionAnalyzer analizador, MoneyLaundering ambiente){
        this.archivos=arcivos;
        this.lectorTransacciones = transacciones;
        this.analizadorTr = analizador;
        this.moneyLaundering = ambiente;
        this.monitor = new Object();
        this.pausado=false;
    }


    public void run(){
        for(File transactionFile : archivos)
        {
            List<Transaction> transactions = lectorTransacciones.readTransactionsFromFile(transactionFile);
            for(Transaction transaction : transactions)
            {
                while (pausado){

                    synchronized (monitor){
                        try {
                            monitor.wait();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                analizadorTr.addTransaction(transaction);
            }
            moneyLaundering.aumentarArchivosProcesados();
        }

    }

    public void parar(){
        this.pausado=true;
    }

    public void revivir(){
        System.out.println();
        synchronized (monitor){
            this.pausado=false;
            monitor.notify();
        }
    }

    public boolean estaVivo() {
        return pausado;
    }
 
}
