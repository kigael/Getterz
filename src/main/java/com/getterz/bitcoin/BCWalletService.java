package com.getterz.bitcoin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;

@Service
public class BCWalletService {

    @Autowired
    private Environment env;

    private NetworkParameters params;
    private String filePrefix;
    private WalletAppKit kit;
    private String baseDir = System.getProperty("user.dir")+"/bcw/";

    @PostConstruct
    private void init(){
        String mode = env.getProperty("bcwallet.mode");
        if (mode.equals("testnet")) {
            params = TestNet3Params.get();
            filePrefix = "Getterz-BitCoinWallet-testnet";
        }
        else if (mode.equals("regtest")) {
            params = RegTestParams.get();
            filePrefix = "Getterz-BitCoinWallet-regtest";
        }
        else if(mode.equals("mainnet")){
            params = MainNetParams.get();
            filePrefix = "Getterz-BitCoinWallet-product";
        }
        else{
            System.err.println("Please set the bcwallet mode: [regtest|testnet|mainnet]");
            return;
        }
        kit = new WalletAppKit(params, new File(baseDir), filePrefix);
        if (params == RegTestParams.get()) {
            kit.connectToLocalHost();
        }
        kit.setAutoSave(true);
        kit.startAsync();
        kit.awaitRunning();
        kit.wallet().addCoinsReceivedEventListener(this::onReceived);
    }

    private void onReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
        //Get sender's address
        String sender = "";
        for (TransactionOutput txo : tx.getOutputs()){
            Script txoScript = txo.getScriptPubKey();
            sender = txoScript.getToAddress(params).toString();
        }
        //Get memo of transaction
        String certMemo = tx.getMemo();
        //Get value of sent coin
        Coin value = tx.getValueSentToMe(w);
        //Get exchange rate of bitcoin
        Rate rate = toRate(readJsonFromUrl("https://bitpay.com/api/rates/usd"));
        //Get usd amount of transaction based on ratio
        BigDecimal usd = BigDecimal.valueOf(0.00000001 * (double) value.getValue() * rate.getRate());
        //Verify certMemo and usd

        //If certMemo is valid proceed purchase

        //If certMemo isn't valid return coin

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static String readJsonFromUrl(String url) {
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            is.close();
            return jsonText;
        } catch(Exception e) {
            return null;
        }
    }

    public static Rate toRate(String json) {
        try{
            return new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json,Rate.class);
        }catch (JsonProcessingException e){
            e.printStackTrace();
            return null;
        }
    }

}
