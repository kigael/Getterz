package com.getterz.bitcoin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.getterz.crypt.Cryptor;
import com.getterz.domain.enumclass.PurchaseState;
import com.getterz.domain.model.Buyer;
import com.getterz.domain.model.Purchase;
import com.getterz.domain.model.Seller;
import com.getterz.domain.repository.BuyerRepository;
import com.getterz.domain.repository.PurchaseRepository;
import com.getterz.domain.repository.SellerRepository;
import com.getterz.mail.GetterzMailService;
import lombok.RequiredArgsConstructor;
import org.bitcoinj.core.*;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.RegTestParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BCWalletService {

    @Autowired
    private final Environment env;
    private final GetterzMailService getterzMailService;
    private final PurchaseRepository purchaseRepository;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;

    private NetworkParameters params;
    private WalletAppKit kit;
    private final String baseDir = System.getProperty("user.dir")+"/bcw/";
    private Address getterzWalletAddress;

    @PostConstruct
    private void init(){
        String mode = env.getProperty("bcwallet.mode");
        String filePrefix;
        switch (mode) {
            case "testnet" -> {
                params = TestNet3Params.get();
                filePrefix = "Getterz-BitCoinWallet-testnet";
            }
            case "regtest" -> {
                params = RegTestParams.get();
                filePrefix = "Getterz-BitCoinWallet-regtest";
            }
            case "mainnet" -> {
                params = MainNetParams.get();
                filePrefix = "Getterz-BitCoinWallet-product";
            }
            default -> {
                System.err.println("Please set the bcwallet mode: [regtest|testnet|mainnet]");
                return;
            }
        }
        kit = new WalletAppKit(params, new File(baseDir), filePrefix);
        if (params == RegTestParams.get()) {
            kit.connectToLocalHost();
        }
        kit.setAutoSave(true);
        kit.startAsync();
        kit.awaitRunning();
        getterzWalletAddress = kit.wallet().currentReceiveAddress();
        kit.wallet().addCoinsReceivedEventListener(this::onReceived);
    }

    public Address getGetterzWalletAddress(){
        return this.getterzWalletAddress;
    }

    private void onReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
        String sender = "";
        for (TransactionOutput txo : tx.getOutputs()){
            Script txoScript = txo.getScriptPubKey();
            sender = txoScript.getToAddress(params).toString();
        }
        String certMemo = tx.getMemo();
        Coin value = tx.getValueSentToMe(w);
        try{
            String cert = Cryptor.DECRYPT(certMemo);
            String[] tokens = cert.split("\\+");
            Long purchaseId = Long.parseLong(tokens[0]);
            Long sellerId = Long.parseLong(tokens[1]);
            Long buyerId = Long.parseLong(tokens[2]);
            Optional<Purchase> purchase = purchaseRepository.findById(purchaseId);
            Optional<Seller> seller = sellerRepository.findById(sellerId);
            Optional<Buyer> buyer = buyerRepository.findById(buyerId);
            if(purchase.isEmpty()||seller.isEmpty()||buyer.isEmpty())  throw new Exception();
            else if(!purchase.get().getSeller().getId().equals(seller.get().getId())) throw new Exception();
            else if(!purchase.get().getBuyer().getId().equals(buyer.get().getId())) throw new Exception();
            else if(!purchase.get().getTotalSatoshi().equals(value.getValue())) throw new Exception();
            else{
                String sellerEmailAddress = Cryptor.DECRYPT(seller.get().getEmailAddress());
                String buyerEmailAddress = Cryptor.DECRYPT(buyer.get().getEmailAddress());
                purchaseRepository.save(purchase.get().setPurchaseState(PurchaseState.PREPARING));
                getterzMailService.sendSellerPaymentConfirmationMessage(sellerEmailAddress,purchase.get());
                getterzMailService.sendBuyerPaymentConfirmationMessage(buyerEmailAddress,purchase.get());
            }
        }catch(Exception e){
            returnCoins(sender,value);
        }
    }

    private void returnCoins(String returningAddressStr, Coin value) {
        try {
            Address returningAddress = LegacyAddress.fromBase58(params, returningAddressStr);
            kit.wallet().sendCoins(kit.peerGroup(), returningAddress, value);
        } catch (InsufficientMoneyException e) {
            System.err.println("Returning bitcoin failed.");
        }
    }

    public static Rate getRealTimeRate(){
        try{
            return new ObjectMapper().registerModule(new JavaTimeModule()).readValue(readJsonFromUrl("https://bitpay.com/api/rates/usd"),Rate.class);
        }catch (JsonProcessingException e){
            return null;
        }
    }

    public static String readJsonFromUrl(String url) {
        try {
            InputStream is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            is.close();
            return jsonText;
        } catch(Exception e) {
            return null;
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
