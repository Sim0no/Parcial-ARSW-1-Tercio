package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaundering.MoneyLaunderingRegistros;
import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import java.util.ArrayList;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service("mlServices")
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
    MoneyLaunderingRegistros reg = new MoneyLaunderingRegistros();        
    public MoneyLaunderingServiceStub(){
        List<SuspectAccount> lista=new ArrayList<SuspectAccount>();
        /*for (String i :reg.getCuentasBancariasFradudulentas()) {
            System.out.println(i);
            
        }*/
    }
    
    
            
    
    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) {
        //TODO
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) {
        //TODO
        return null;
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        //TODO
        return null;
    }
    

    @Override
    public void addNewAccount(SuspectAccount o) {
        
        
    }
}
