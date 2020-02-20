package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class
MoneyLaundering
{
    private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private AtomicInteger amountOfFilesProcessed;
    private LavadoActivosConcurrente[] hilosUsados;

    public MoneyLaundering()
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
    }

    public void processTransactionData()
    {
        int numHilos=5;
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        int files2Read =Math.floorDiv(amountOfFilesTotal,numHilos);
        int sobrante= amountOfFilesTotal%numHilos;
        LavadoActivosConcurrente[] hilos = new LavadoActivosConcurrente[numHilos];
        hilosUsados = new LavadoActivosConcurrente[numHilos];
        int inicio=0;
        int fin= numHilos;
        for (int i=0; i<numHilos;i++) {
            ArrayList<File> auxiliar = new ArrayList<File>();
            for (int j =inicio; j< fin ; j++){
                auxiliar.add(transactionFiles.get(j));
            }
            if (i==numHilos-2){
                inicio=amountOfFilesTotal-sobrante;
                fin=amountOfFilesTotal;
            } else {
                inicio=fin;
                fin +=numHilos;
            }
            hilos[i] = new LavadoActivosConcurrente(auxiliar,transactionReader,transactionAnalyzer,this);
            hilosUsados[i] = hilos[i];
            hilos[i].start();
        }

        for (LavadoActivosConcurrente hilo: hilos){
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public void aumentarArchivosProcesados(){
        amountOfFilesProcessed.incrementAndGet();
    }

    public LavadoActivosConcurrente[] procesos(){
        return hilosUsados;
    }


    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }


    public static void main(String[] args)
    {
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        Thread processingThread = new Thread(() -> moneyLaundering.processTransactionData());
        processingThread.start();
        boolean flag;
        principal:
        while(true)
        {
            flag=true;
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.equals("exit")){
                break;
            }
            for(LavadoActivosConcurrente hilo: moneyLaundering.procesos()){
                hilo.parar();
            }            

            String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
            List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
            String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);            
            System.out.println(message);
            System.out.println("Hilos Pausados, presione enter para continuar, exit para salir");
            scanner = new Scanner(System.in);
            line = scanner.nextLine();
            if(line.equals("exit")){
                break;
            }
            for(LavadoActivosConcurrente hilo: moneyLaundering.procesos()){
                hilo.revivir();
            }
            System.out.println("Hilos Renaudados, presione enter para Mostrar registros, exit para salir");
            padre:
            for(LavadoActivosConcurrente hilo: moneyLaundering.procesos()){
                if(hilo.estaVivo()){
                    flag=false;
                    break padre;
                }
            }
            if (!flag){
                System.out.println(message);
                System.out.println("Se terminaron de analizar los archivos");
                break principal;
            }
            for (int i = 0; i < offendingAccounts.size(); i++) {
                System.out.println(offendingAccounts.get(i));
                
            }

        }

    }



}
