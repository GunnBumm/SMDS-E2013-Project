/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.itu.smds.e2013.security.tokenService;

import dk.itu.smds.e2013.security.DESEncryptionHelper;
import dk.itu.smds.e2013.security.RoleBasedToken;
import dk.itu.smds.e2013.security.SSHAuthenticationHelper;
import dk.itu.smds.e2013.security.Utilities;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author rao
 */
public class TokenService {

    private static HashMap<String, String> roleMappings = new HashMap();
    private static int tokenServerPort = 8008;
    private static String Client_Key_Passcode = "Topmost Secret";//K_CT
    private static String Server_Key_Passcode = "Don't reveal the Secret";//K_TS
    private static String Server_Client_Shared_key_Passcode ="Hey man";//K_SC
    private static BufferedReader in;
    private static final String Encoding_Format = "UTF8";
    private static final SimpleDateFormat formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static String clientName="Testing";//created by efrin
    
    public static void main(String args[]) throws Exception {

        // Add role mappings for users.
        roleMappings.put("rao", "student,teacher,ta");
        roleMappings.put("efag", "student,teacher,ta");
        


        // hook on to console input ..
        in = new BufferedReader(new InputStreamReader(System.in));
        ServerSocket serverSocket = null;
        DataInputStream dis;
//        DataOutputStream dos;
//        ObjectInputStream ois;
        ObjectOutputStream oos;
        try {
            serverSocket = new ServerSocket(tokenServerPort);
            System.out.println("ITU's Token Service started at 8008");
            while (true) {                
                Socket socket = serverSocket.accept(); // blocking call
                // Data Input and output streams
                dis = new DataInputStream(socket.getInputStream());
                String usertoken = dis.readUTF(); // blocking call
                System.out.println("Token received: " + usertoken);
                RoleBasedToken generateToken = generateToken(usertoken);
                
                // Write the token into socket..
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(generateToken);
                oos.flush();

                // wait for the next request...
                
            }

        } catch (Exception ex) {

            System.out.println(ex.getMessage());
        }


    }

    //The user token should contain the credential username and password from port 8009
    private static RoleBasedToken generateToken(String userToken) {
        RoleBasedToken token = new RoleBasedToken();
        String tokenString;
        try {
            byte[] encryptedBytes = Utilities.getBase64DecodedBytes(userToken);            
            // Decrypt the user token.
            byte[] keyBytes = Client_Key_Passcode.getBytes(Encoding_Format);
            byte[] decryptMessage = DESEncryptionHelper.decryptMessage(keyBytes, encryptedBytes);
            tokenString = Utilities.bytes2String(decryptMessage);
        } catch (Exception ex) {
            System.out.println("Authentication error: " + ex.getMessage());
            token.result = false;
            token.errorMessage = "Failed to decrypt message! Error message: " + ex.getMessage();
            return token;
        }

        String[] tokenArray = tokenString.split(";");
        if (tokenArray.length != 2) {
            token.result = false;
            token.errorMessage = "The supplied user credentials are not in right format! Format: Username=XXX;Password=XXX";
            return token;
        }

        String[] userNameArray = tokenArray[0].split("=");
        String[] passwordArray = tokenArray[1].split("=");

        if ((userNameArray.length != 2) || (passwordArray.length != 2)) {
            token.result = false;
            token.errorMessage = "The supplied user credentials are not in right format! Format: Username=XXX;Password=XXX";
            return token;
        }
        try {
            // Authenticate using SSH...
            boolean authntication = SSHAuthenticationHelper.Authenticate(userNameArray[1], passwordArray[1]);
            //boolean authntication = LDAPAuthenticationHelper.AuthenticateRevised(userNameArray[1], passwordArray[1]);

            if (!authntication) {
                token.result = false;
                token.errorMessage = "Incorrect user credentials!";
                return token;
            }            
            System.out.println("Authentication successed for user: " + userNameArray[1] );
            token.token = userNameArray[1];
        } catch (Exception ex) {
            System.out.println("Authentication error: " + ex.getMessage());
            token.result = false;
            token.errorMessage = "Failed to authenticate user credentials with SSH! Error message: " + ex.getMessage();           
            return token;
        }

        String role;
        // return error if we dont find a mapping
        if (roleMappings.containsKey(userNameArray[1])) {            
            role = roleMappings.get(userNameArray[1]);
            //Set the clientName
            clientName = roleMappings.get(userNameArray[1]);
        }
        else
        {
            // if no role mapping is found, defaulted to student role.
            System.out.println("No role mapping found for user: " + userNameArray[1] 
                    + ". Therefore the role has been defaulted to 'student'." );            
            role = "student";
        }        
        try {
            // Finally return the eberated token.
            token.token = getRoleBasedToken(role);
            token.result = true;
            return token;
        } catch (Exception ex) {

            System.out.println("Failed to encrypt role token: " + ex.getMessage());
            token.result = false;
            token.errorMessage = "Failed to create encrypted token for the server! Error message: "
                    + ex.getMessage();
            return token;

        }

    }
    

    private static String getRoleBasedToken(String role) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);
        Date expiryTime = calendar.getTime();
        
        // Format of server token = [role];[timestamp];[clientName];[shared K_SC];[shared K_SC]
        /*The right format must be: {{[role];[timestamp];[clientName];[shared K_SC]}, [shared K_SC]}
         * So it must be implemented as required in the exercise description*/
        //I am passing the K_SC twice just to test the functionality
        String roleToken = role + ";" + formatted.format(expiryTime) + ";" + clientName+ ";" + Server_Client_Shared_key_Passcode+ ";" +Server_Client_Shared_key_Passcode;

        // First encrypt the server token with server encryption key..
        byte[] encryptTokenBytes = DESEncryptionHelper.encryptMessage(Server_Key_Passcode.getBytes(Encoding_Format),
                roleToken.getBytes(Encoding_Format));
        String base64ServerToken = Utilities.getBase64EncodedString(encryptTokenBytes);

        // Now again encrypt the base 64 encoded server token with Client key.
        byte[] doubleEncryptTokenBytes = DESEncryptionHelper.encryptMessage(Client_Key_Passcode.getBytes(Encoding_Format),
                base64ServerToken.getBytes(Encoding_Format));
        String encryptedRoleToken = Utilities.getBase64EncodedString(doubleEncryptTokenBytes);        
        return encryptedRoleToken;
    }
}
