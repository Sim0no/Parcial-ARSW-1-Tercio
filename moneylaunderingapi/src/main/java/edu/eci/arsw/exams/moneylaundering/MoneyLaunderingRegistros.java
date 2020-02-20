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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
@Component
@Qualifier("moneyLaundering")
public class MoneyLaunderingRegistros {
    MoneyLaundering mn = new MoneyLaundering();
    public List<String> getCuentasBancariasFradudulentas() {
        return mn.getOffendingAccounts();
    }
    
}
