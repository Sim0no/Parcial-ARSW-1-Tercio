package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
@RestController
public class MoneyLaunderingController
{
    @Autowired
    @Qualifier("mlServices")
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping( value = "/fraud-bank-accounts")
    public List<SuspectAccount> offendingAccounts() {
        return moneyLaunderingService.getSuspectAccounts();
    }
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorMoneyLaundering()  {
        //obtener datos que se enviarán a través del API
        return new ResponseEntity<>(moneyLaunderingService.getSuspectAccounts(), HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = RequestMethod.GET,value="/{accountId}")
    public ResponseEntity<?> manejadorMoneyLaunderingByAuthor(@PathVariable String id)  {
        //obtener datos que se enviarán a través del API
        return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(id), HttpStatus.ACCEPTED);
    }


    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> manejadorPostMoneyLaundering(@RequestBody SuspectAccount o){
        moneyLaunderingService.addNewAccount(o);
        return new ResponseEntity<>("Registrado",HttpStatus.CREATED);                 
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{accountId}")
    public ResponseEntity<?> manejadorPutBlueprint(@RequestBody SuspectAccount as){
        moneyLaunderingService.updateAccountStatus(as);
        return new ResponseEntity<>("Actualizado",HttpStatus.CREATED);

    }
}
