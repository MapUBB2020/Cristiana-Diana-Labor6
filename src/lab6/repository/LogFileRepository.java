package lab6.repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogFileRepository {
    public List<String> eMails = new ArrayList<>(); //lista cu toate eMail-urile
    public List<String> passwords = new ArrayList<>(); //lista cu toate parolele


    public LogFileRepository() {
    }

    public void readAccounts() throws FileNotFoundException {
        String line = " ";
        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\lab6\\log.txt"));
            while ((line = br.readLine()) != null) {
                String[] obj = line.split(", ");
                eMails.add(obj[0]);
                passwords.add(obj[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
