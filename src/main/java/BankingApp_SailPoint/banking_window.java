package BankingApp_SailPoint;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class banking_window {
    public static void main(String[] args) throws IOException, ParseException {
        mainWindow();
    }

    public static void mainWindow() throws IOException, ParseException {
        System.out.println("--------------------------------------------------");
        System.out.println("          Welcome to Sail Bank E system");
        System.out.println("                 1. Login ");
        System.out.println("                 2. New Account");
        System.out.println("                 3. Cancel");
        System.out.println("--------------------------------------------------");
        Scanner scan =new Scanner(System.in);
        int option=scan.nextInt();scan.nextLine();
        if(option==1)
        {
            System.out.println("Your Account Number: ");
            String account=scan.nextLine();
            System.out.println("Your PassWord: ");
            String pass=scan.nextLine();
            if(verifyAccount(account,pass))
            {
                String name=getName(account,pass);
                innerOptionsWindow(account,name);
            }
            else
            {
                System.out.println("Wrong account num or password, Try again!");
            }
        }
        else if(option==2)
        {
            String[] account;
            account=createAccount();
            insertIntoJson(account[0],account[1],account[2],account[3]);
            mainWindow();
        }
        else if(option==3)
        {
            System.out.println("Thank you! Have a good day!");
        }
        else
        {
            System.out.println("invalid Input, try again!");
        }

    }
    public static void innerOptionsWindow(String acc,String name) throws IOException, ParseException {
        System.out.println("--------------------------------------------------");
        System.out.println("                Welcome "+ name);
        System.out.println("                 1. Balance ");
        System.out.println("                 2. Deposit");
        System.out.println("                 3. Withdraw");
        System.out.println("                 4. Cancel");
        System.out.println("--------------------------------------------------");
        Scanner scan=new Scanner(System.in);
        int option=scan.nextInt();scan.nextLine();
        if(option==1)
        {
            System.out.println("current balance: $ "+getBalance(acc));
            innerOptionsWindow(acc,name);
        }
        else if(option==2)
        {
            System.out.println("how much do you deposit?");
            String deposit=scan.nextLine();
            updateBalance(acc,deposit);
            System.out.println("Deposit "+deposit+" have been successful!");
            innerOptionsWindow(acc,name);
        }
        else if(option==3)
        {
            System.out.println("how much do you withdraw?");
            String withdraw=scan.nextLine();
            if(Double.parseDouble(withdraw)>Double.parseDouble(getBalance(acc)))
            {
                System.out.println("withdraw amount exceed the current balance in your card!");
                innerOptionsWindow(acc,name);
            }
            else
            {
                updateBalance(acc,"-"+withdraw);
                System.out.println("Withdraw "+withdraw+" have been successful!");
                innerOptionsWindow(acc,name);
            }
        }
        else if(option==4)
        {
                System.out.println("Thank you! Have a good day!");
        }
        else
        {
            System.out.println("Wrong input, try again!");
            innerOptionsWindow(acc,name);
        }
    }

    public static String[] createAccount() throws IOException, ParseException {
        Scanner scan = new Scanner(System.in);
        boolean loop = true;
        String acc="";
        String pass="";
        String []account=new String[]{acc,pass};
        while (loop)
        {
            System.out.println("Your intended Account Number: ");
            acc = scan.nextLine();
            if(!verifyIfExit(acc))
            {
                System.out.println(acc +" is taken, try again!");
                continue;
            }
            System.out.println("Your intended PassWord: ");
            pass=scan.nextLine();

            if(!pass.matches("[0-9]+"))
            {
                System.out.println("Password can only contain digits");
                continue;
            }
            if(pass.length()!=4)
            {
                System.out.println("Password should have only 4 digits");
                continue;
            }
            System.out.println("Your Firstname: ");
            String fName=scan.nextLine();
            System.out.println("Your Lastname: ");
            String lName=scan.nextLine();
            account=new String[]{fName,lName,acc,pass};
            loop=false;
        }
        return account;
    }

    public static void insertIntoJson(String firstName,String lastName,String account, String pass) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject outer = new JSONObject();
        JSONObject inner = new JSONObject();
        JSONObject data = new JSONObject();
        ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
        inner.put("firstName",firstName);
        inner.put("lastName",lastName);
        inner.put("accountNum",account);
        inner.put("passWord",pass);
        inner.put("balance","0");
        arr.add(inner);
        File file = new File("E://customers.json");
        if (file.exists()) {
            Object obj = parser.parse(new FileReader("E://customers.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray array = (JSONArray) jsonObject.get("Customers");
            PrintWriter write = new PrintWriter(new FileWriter(file));
            Iterator<JSONObject> iterator = array.iterator();
            while (iterator.hasNext()) {
                JSONObject it = iterator.next();
                data = (JSONObject) it;
                arr.add(data);
            }
            outer.put("Customers", arr);
            write.write(outer.toString());
            write.flush();
            write.close();
        } else {
            PrintWriter write = new PrintWriter(new FileWriter(file));
            write.write(outer.toString());
            write.flush();
            write.close();
        }
    }

    public static void updateBalance(String acc,String change) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject data = new JSONObject();
        File file = new File("E://customers.json");
        JSONObject outer = new JSONObject();
        JSONObject inner = new JSONObject();
        ArrayList<JSONObject> arr = new ArrayList<JSONObject>();
        if (file.exists())
        {
            Object obj = parser.parse(new FileReader("E://customers.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray array = (JSONArray) jsonObject.get("Customers");
            PrintWriter write = new PrintWriter(new FileWriter(file));
            Iterator<JSONObject> iterator = array.iterator();
            while (iterator.hasNext()) {
                JSONObject it = iterator.next();
                data = (JSONObject) it;
                if(acc.contentEquals((String)data.get("accountNum")))
                {
                    String current=(String) data.get("balance");
                    String newBalance= String.valueOf(Double.parseDouble(current)+Double.parseDouble(change));
                    data.put("balance",newBalance);
                }
                arr.add(data);
            }
            outer.put("Customers", arr);
            write.write(outer.toString());
            write.flush();
            write.close();
        }
    }
    public static String getBalance(String acc) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject data = new JSONObject();
        File file = new File("E://customers.json");
        if (file.exists())
        {
            Object obj = parser.parse(new FileReader("E://customers.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray array = (JSONArray) jsonObject.get("Customers");
            Iterator<JSONObject> iterator = array.iterator();
            while (iterator.hasNext()) {
                JSONObject it = iterator.next();
                data = (JSONObject) it;
                if(acc.contentEquals((String)data.get("accountNum")))
                {
                    return (String) data.get("balance");
                }
            }
        }
        return null;
    }
    public static boolean verifyAccount(String acc, String pass) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject data = new JSONObject();
        File file = new File("E://customers.json");
        if (file.exists()) {
            Object obj = parser.parse(new FileReader("E://customers.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray array = (JSONArray) jsonObject.get("Customers");
            Iterator<JSONObject> iterator = array.iterator();
            while (iterator.hasNext()) {
                JSONObject it = iterator.next();
                data = (JSONObject) it;
                if(acc.contentEquals((String)data.get("accountNum"))&&pass.contentEquals((String)data.get("passWord")))
                {
                    return true;
                }
            }
            return false;

        } else {return false;}
    }

    public static boolean verifyIfExit(String acc) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject data = new JSONObject();
        File file = new File("E://customers.json");
        if (file.exists()) {
            Object obj = parser.parse(new FileReader("E://customers.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray array = (JSONArray) jsonObject.get("Customers");
            Iterator<JSONObject> iterator = array.iterator();
            while (iterator.hasNext()) {
                JSONObject it = iterator.next();
                data = (JSONObject) it;
                if(acc.contentEquals((String)data.get("accountNum")))
                {
                    return false;
                }
            }
            return true;

        } else {return false;}
    }
    public static String getName(String acc, String pass) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject data = new JSONObject();
        File file = new File("E://customers.json");
        if (file.exists())
        {
            Object obj = parser.parse(new FileReader("E://customers.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray array = (JSONArray) jsonObject.get("Customers");
            Iterator<JSONObject> iterator = array.iterator();
            while (iterator.hasNext()) {
                JSONObject it = iterator.next();
                data = (JSONObject) it;
                if(acc.contentEquals((String)data.get("accountNum"))&&pass.contentEquals((String)data.get("passWord")))
                {
                    return data.get("firstName")+" "+ data.get("lastName");
                }
            }
        }
        return null;
    }

}
