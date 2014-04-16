//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

import java.util.*; 
import java.io.*; 
import javax.mail.*; 
import javax.mail.internet.*; 
import javax.activation.*; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* Sendmail: Aplicacion que permite enviar mails con attachments.
* Version: 	1.0
*
*/
public class SendMail {

  private static final String END_OF_BODY = ".";

  public static void main(String args[]) {

    try {
      // Mapa con los argumetntos de la linea de comandos
      Map<String, String> argMap = SendMail.parseArguments(args);

      String to = argMap.get("to");
      String from = argMap.get("from");
      String cc = argMap.get("cc");
      String subject = argMap.get("subject");
      String body = argMap.get("body");
      String attach = argMap.get("attach");
      String help = argMap.get("help");
  
      if (argMap.get("help") != null) {
        printHelpMessage();
        System.exit(0);
      }

      InputStreamReader stream = new InputStreamReader(System.in);
      BufferedReader in = new BufferedReader(stream);
      if (to == null) {
        System.out.print("To: ");
        to = in.readLine();
      }

      if (from == null) {
        System.out.print("From: ");
        from = in.readLine();
      }

      if (cc == null && body == null) {
        System.out.print("Cc: ");
        cc = in.readLine();
      }
  
      // Asunto del mensaje
      if (subject == null) {
        System.out.print("Subject: ");
        subject = in.readLine();
      }
  
      // Cuerpo del Mensaje
      if (body == null) {
        body = "";
        String bodyLine = "";
        while(!(bodyLine=in.readLine()).equals(END_OF_BODY)) {
          body += bodyLine + "\n"; 
        }
      }
        
      String server = System.getProperty("MailServer");
      if (server == null) {
        throw new IllegalArgumentException("No se encontro la propiedad MailServer");
      }
      send(server,from,to,cc,subject,body,attach);

    } catch (Exception e) {
      System.out.println("No se pudo enviar el mail: " );
      e.printStackTrace();
      System.exit(-1);
    }
  }

  public static void send(String server, String from, String to, String cc, 
          String subject, String body, String attach) throws Exception {

    Properties props = System.getProperties();
    props.put("mail.smtp.host", server);
    props.put("mail.smtp.localhost", "localhost");
    props.put("mail.smtp.connectiontimeout", "1000000");
    props.put("mail.smtp.timeout", "1000000");
    //props.put("mail.debug", "true"); 

    Session session = Session.getInstance(props, null);

    Message message = new MimeMessage(session);

    if (from == null) 
      throw new IllegalArgumentException("El argumento \"from\" es requerido.");
    else {
      if (!isValidMail(from))
        throw new IllegalArgumentException("\"from\" no es una direccion de mail valida.");

      message.setFrom(new InternetAddress(from));
    }

    if (to == null) 
      throw new IllegalArgumentException("El argumento \"to\" es requerido.");
    else {
      if (!isValidMailList(to))
        throw new IllegalArgumentException("\"to\" no es una lista de direcciones de mail valida.");

      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
    }

    if (cc != null && !cc.equals("")) { 
      if (!isValidMailList(cc)) 
        throw new IllegalArgumentException("\"cc\" no es una lista de direcciones de mail valida.");

      message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
    }

    message.setSubject(subject);

    BodyPart messageBodyPart = new MimeBodyPart();
    messageBodyPart.setText(body);

    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(messageBodyPart);

    if (attach != null) {
      if (!isValidFile(attach)) 
        throw new IllegalArgumentException("El archivo para adjuntar es inexistente.");

      messageBodyPart = new MimeBodyPart();
      DataSource source = new FileDataSource(attach);
      messageBodyPart.setDataHandler(new DataHandler(source));
      messageBodyPart.setFileName(attach);
      multipart.addBodyPart(messageBodyPart);
    }

    message.setContent(multipart);

    try {
      Transport.send(message);
    }
    catch(SendFailedException sfe) {
      message.setRecipients(Message.RecipientType.TO,  sfe.getValidUnsentAddresses());
      Transport.send(message);
    }
  }

 /**
  * Parser de argumentos de la linea de comandos.
  */
  private static Map<String,String> parseArguments(String args[]) throws IllegalArgumentException {

    HashMap<String, String> argMap = new HashMap<String, String>();

    String arg = null;
    String value = null;

    for (int i=0; i < args.length; i++) {
       arg = args[i];

      if (arg.equals("-t") || arg.equals("--to")) {
        value = fetchArgValue(args,i++);
        argMap.put("to",value);

      } else if (arg.equals("-f") || arg.equals("--from")) {
        value = fetchArgValue(args,i++);
        argMap.put("from",value);

      } else if (arg.equals("-c") || arg.equals("--cc")) {
        value = fetchArgValue(args,i++);
        argMap.put("cc",value);

      } else if (arg.equals("-s") || arg.equals("--subject")) {
        value = fetchArgValue(args,i++);
        argMap.put("subject",value);

      } else if (arg.equals("-b") || arg.equals("--body")) {
        value = fetchArgValue(args,i++);
        argMap.put("body",value);

      } else if (arg.equals("-a") || arg.equals("--attach")) {
        value = fetchArgValue(args,i++);
        argMap.put("attach",value);

      } else if (arg.equals("-h") || arg.equals("--help")) {
        argMap.put("help","");

      } else {
        throw new IllegalArgumentException(arg + " no es un parametro valido.");
      }
    }

    return argMap;
  }

  /**
    * Valida si una direccion de correo electronico es 
    * correcta.
    */
  private static boolean isValidMail(String strMail) {
    try {
      if (strMail == null)
        return false;

      //Set the email pattern string
      Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

      //Match the given string with the pattern
      Matcher m = p.matcher(strMail);

      //check whether match is found 
      boolean matchFound = m.matches();

      if (matchFound)
        return true;
      else
        return false;

    } catch (Exception e) {
            return false;
    }
  }

  /**
    * Valida si una string separada por coma es una lista 
    * de direcciones de mail.
    */
  private static boolean isValidMailList(String mailList) {

    if (mailList == null)
      return false;

    for (String mail: mailList.split("\n")) {
      if (!isValidMail(mail)) {
        return false;
      }
    }

    return true;
  }

  /**
    * Valida si existe un archivo con nombre filename
    */
  private static boolean isValidFile(String filename) {
    return (new File(filename)).exists();
  }

  /**
    * Obtiene el valor del parametro que esta en la posicion pos.
    * Si no lo encuentra, retorna null.
    */
  private static String fetchArgValue(String args[], int paramIndex) {
    try {

      return (!args[paramIndex + 1].startsWith("-")) ? args[paramIndex + 1]: null;

    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  private static void printHelpMessage() {
    System.out.println("");
    System.out.println("Modo de empleo: sendmail [OPCIÓN...]");
    System.out.println("");
    System.out.println("\t-a <file>     , --attach <file>     \t Adjunta un archivo.");
    System.out.println("\t-f <address>  , --from <address>    \t Direccion de origen.");
    System.out.println("\t-t <addresses>, --to <addresses>    \t Direcciones destino.");
    System.out.println("\t-c <addresses>, --cc <addresses>    \t Direcciones con copia.");
    System.out.println("\t-s <message>  , --subject <message> \t Asunto del mail.");
    System.out.println("\t-b <message>  , --body <message>    \t Cuerpo del mail.");
    System.out.println("\t-h            , --help              \t Imprime este mensaje.");
    System.out.println("");
  }
}
