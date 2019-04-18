package com.example.gerard.afinal;

import android.os.AsyncTask;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.account.AccountManager;
import com.textrazor.account.model.Account;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entity;

public class NER extends AsyncTask<Void, Void, Void> {

    String API_KEY = "5d9a93f99b0aab73d3f8be94453d6d83f3ea2b193b7780ca43751893";

    @Override
    protected Void doInBackground(Void... params) {

        try {

            AccountManager manager = new AccountManager(API_KEY);
            Account account = manager.getAccount();
            System.out.println("Your current account plan is " + account.getPlan() + ", which includes " + account.getPlanDailyRequestsIncluded() + " daily requests, " + account.getRequestsUsedToday() + " used today");

            findEntity();

        } catch (NetworkException e) {
            e.printStackTrace();
        }catch (AnalysisException a){
            a.printStackTrace();
        }
        return null;
    }

    private void findEntity(){
        try{

            TextRazor client = new TextRazor(API_KEY);

            client.addExtractor("words");
            client.addExtractor("entities");

            AnalyzedText response = client.analyze("IEEE, Bilkent University, 20:00, 28/05/2019, B Building, Painting Exhibition.");

            for (Entity entity : response.getResponse().getEntities()) {
                System.out.println(entity.getEntityId() + ": " + entity.getDBPediaTypes());
            }

        }catch (NetworkException e){
            e.printStackTrace();
        }catch (AnalysisException a){
            a.printStackTrace();
        }


    }





}
