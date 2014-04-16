//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DemodaMail {
	
	private static Log log = LogFactory.getLog(DemodaMail.class);
	
	/**
	 * Envia un mail con los parametros pasados como parametros
	 * @param server
	 * @param to Puede ser una unica direccion de mail o una lista separada por ","
	 * @param from
	 * @param subject
	 * @param body
	 * @param cc Puede ser una unica direccion de mail o una lista separada por ","
	 * @throws Exception
	 */
    static public void send(String server, String to, String from, String subject, String body, String cc) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", server);
        props.put("mail.smtp.localhost", "localhost");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.mime.charset","text/plain;charset=\"iso-8859-1\"");

        //props.put("mail.from", from);

        // create some properties and get the default Session
        Session session = Session.getDefaultInstance(props, null);
       	session.setDebug(true); //hace que salga por el stdout lo que manda al server.


        // create a message
        Message msg = new MimeMessage(session);

        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        // Setea TO
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        
        // Setea CC
        if (!StringUtil.isNullOrEmpty(cc)) {
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
        }

        // Optional : You can also set your custom headers in the Email if you Want
        //msg.addHeader("MyHeaderName", "myHeaderValue");

        msg.setSentDate(new Date());
        
        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setContent(body, "text/plain;charset=\"iso-8859-1\"");
        Transport.send(msg);
    }

}
