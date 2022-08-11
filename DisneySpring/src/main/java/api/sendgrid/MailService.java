package api.sendgrid;

import com.sendgrid.*;
import java.io.IOException;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.ApplicationScope;

@Service
@ApplicationScope
public class MailService {

    public void enviarEmail(String direccion) throws IOException {
        Email from = new Email("acostaxf5@gmail.com");
        String subject = "Bienvenido a Disney Spring";
        Email to = new Email(direccion);
        Content content = new Content("text/plain", "Te damos la bienvenida a Disney Spring!");

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sendGrid = new SendGrid("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");

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        sendGrid.api(request);
    }

}
