import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


class TransactionException extends Exception{
    public final static int UNKNOWN_ERROR = -1;
    public final static int USER_NOT_AUTHORIZED=0;
    public final static int INVALID_AMOUNT=1;
    public final static int INSUFFICIENT_BALANCE=2;

    private int errorCode = UNKNOWN_ERROR;

    public TransactionException(String errorMessage, int errorCode){
        super(errorMessage);
        this.errorCode = errorCode;
    }

    public String getErrorCode(){
        switch(this.errorCode){
            case USER_NOT_AUTHORIZED:
                return "USER_NOT_AUTHORIZED";
            case INVALID_AMOUNT:
                return "INVALID_AMOUNT";
            case INSUFFICIENT_BALANCE:
                return "INSUFFICIENT_BALANCE";
            default:
                return "UNKNOWN_ERROR";
        }
    }
}

class DigitalWalletTransaction  {
    public DigitalWalletTransaction(){
    }

    public void addMoney(DigitalWallet wallet, final int money) throws TransactionException{
        if (money <= 0) {
            throw new TransactionException("Amount should be greater than zero", TransactionException.INVALID_AMOUNT);
        }
        if(wallet.getUserAccessToken()==null)
        {
            throw new TransactionException("User not authorized",TransactionException.USER_NOT_AUTHORIZED);
        }
        int walletBalance = wallet.getWalletBalance();
        wallet.setWalletBalance(walletBalance + money);
    }


    public void payMoney(DigitalWallet wallet, final int money) throws TransactionException{
        if (money <= 0) {
            throw new TransactionException("Amount should be greater than zero", TransactionException.INVALID_AMOUNT);
        }
        if(wallet.getWalletBalance()<money){
            throw new TransactionException("Insufficient balance",TransactionException.INSUFFICIENT_BALANCE);
        }
        int walletBalance = wallet.getWalletBalance() - money;
        wallet.setWalletBalance(walletBalance);
    }
}

class DigitalWallet{
    private String walletId;
    private String userName;
    private String userAccessCode;
    private int walletBalance;

    public DigitalWallet(String walletId, String userName){
        this.walletId = walletId;
        this.userName = userName;
    }

    public DigitalWallet(String walletId, String userName, String userAccessCode){
        this.walletId = walletId;
        this.userName = userName;
        this.userAccessCode = userAccessCode;
    }

    public String getWalletId(){return this.walletId;};

    public String getUsername(){return this.userName;};
   
    public String getUserAccessToken(){return this.userAccessCode;};

    public int getWalletBalance(){return this.walletBalance;};

    public void setWalletBalance(int balance){
            this.walletBalance = balance;
        };

}


public class DigitalWalletTest {
    private static final Scanner INPUT_READER = new Scanner(System.in);
    private static final DigitalWalletTransaction DIGITAL_WALLET_TRANSACTION = new DigitalWalletTransaction();

    private static final Map<String, DigitalWallet> DIGITAL_WALLETS = new HashMap<>();

    public static void main(String[] args) {
        int numberOfWallets = Integer.parseInt(INPUT_READER.nextLine());
        while (numberOfWallets-- > 0) {
            String[] wallet = INPUT_READER.nextLine().split(" ");
            DigitalWallet digitalWallet;

            if (wallet.length == 2) {
                digitalWallet = new DigitalWallet(wallet[0], wallet[1]);
            } else {
                digitalWallet = new DigitalWallet(wallet[0], wallet[1], wallet[2]);
            }

            DIGITAL_WALLETS.put(wallet[0], digitalWallet);
        }

        int numberOfTransactions = Integer.parseInt(INPUT_READER.nextLine());
        while (numberOfTransactions-- > 0) {
            String[] transaction = INPUT_READER.nextLine().split(" ");
            DigitalWallet digitalWallet = DIGITAL_WALLETS.get(transaction[0]);

            if (transaction[1].equals("add")) {
                try {
                    DIGITAL_WALLET_TRANSACTION.addMoney(digitalWallet, Integer.parseInt(transaction[2]));
                    System.out.println("Wallet successfully credited.");
                } catch (TransactionException ex) {
                    System.out.println(ex.getErrorCode() + ": " + ex.getMessage() + ".");
                }
            } else {
                try {
                    DIGITAL_WALLET_TRANSACTION.payMoney(digitalWallet, Integer.parseInt(transaction[2]));
                    System.out.println("Wallet successfully debited.");
                } catch (TransactionException ex) {
                    System.out.println(ex.getErrorCode() + ": " + ex.getMessage() + ".");
                }
            }
        }

        System.out.println();

        List<String> digitalWalletIds = new ArrayList<>();
        digitalWalletIds.addAll(DIGITAL_WALLETS.keySet());

        Collections.sort(digitalWalletIds);
        for (String digitalWalletId: digitalWalletIds) {
            DigitalWallet digitalWallet = DIGITAL_WALLETS.get(digitalWalletId);
            System.out.println(digitalWallet.getWalletId()
                    + " " + digitalWallet.getUsername()
                    + " " + digitalWallet.getWalletBalance());
        }
    }
}
