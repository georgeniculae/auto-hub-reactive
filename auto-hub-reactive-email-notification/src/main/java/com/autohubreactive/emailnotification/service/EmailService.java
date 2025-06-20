package com.autohubreactive.emailnotification.service;

import com.autohubreactive.dto.emailnotification.EmailResponse;
import com.autohubreactive.emailnotification.mapper.EmailResponseMapper;
import com.autohubreactive.emailnotification.util.Constants;
import com.autohubreactive.exception.AutoHubResponseStatusException;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.StringWriter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final SendGrid sendGrid;
    private final MustacheFactory mustacheFactory;
    private final EmailResponseMapper emailResponseMapper;

    @Value("${sendgrid.mail.from}")
    private String mailFrom;

    @Value("${sendgrid.mail.name}")
    private String name;

    public Mono<EmailResponse> sendEmail(String toAddressEmail, Object object) {
        return getMailResponse(createMail(toAddressEmail, object))
                .map(emailResponseMapper::mapToEmailResponse);
    }

    private Mono<Response> getMailResponse(Mail mail) {
        return Mono.fromCallable(() -> sendMail(mail))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Response sendMail(Mail mail) {
        sendGrid.setDataResidency(Constants.DATA_RESIDENCY);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint(Constants.ENDPOINT);
            request.setBody(mail.build());

            return sendGrid.api(request);
        } catch (Exception e) {
            throw new AutoHubResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    private Mail createMail(String toAddressEmail, Object object) {
        Email from = new Email(mailFrom, name);
        Email to = new Email(toAddressEmail);

        Content content = new Content(Constants.CONTENT_TYPE, getMailBody(object));

        Mail mail = new Mail(from, Constants.SUBJECT, to, content);
        mail.setSubject(Constants.SUBJECT);

        return mail;
    }

    private String getMailBody(Object object) {
        StringWriter stringWriter = new StringWriter();
        Mustache mustache = mustacheFactory.compile(Constants.MAIL_TEMPLATE_FOLDER + Constants.FILE_NAME + Constants.MUSTACHE_FORMAT);

        try {
            mustache.execute(stringWriter, object).flush();
        } catch (Exception e) {
            throw new AutoHubResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return stringWriter.toString();
    }

}
