package com.gofind.gofind.service;

import com.gofind.gofind.domain.Objet;
import com.gofind.gofind.domain.User;
import com.gofind.gofind.domain.Utilisateur;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import reactor.core.publisher.Mono;
import tech.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final UserService userService;

    public MailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine,
        UserService userService
    ) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.userService = userService;
    }

    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        Mono.defer(() -> {
            this.sendEmailSync(to, subject, content, isMultipart, isHtml);
            return Mono.empty();
        }).subscribe();
    }

    private void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        Mono.defer(() -> {
            this.sendEmailFromTemplateSync(user, templateName, titleKey);
            return Mono.empty();
        }).subscribe();
    }

    private void sendEmailFromTemplateSync(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        this.sendEmailSync(user.getEmail(), subject, content, false, true);
    }

    public void sendObjetReportEmail(Objet objet) {
        Utilisateur proprietaireUtil = objet.getProprietaire();
        // User proprietaireUser = proprietaireUtil.getLogin();
        // log.debug("! ! ! ! ! ! ! ProprietaireUtil: {}", proprietaireUtil);
        // log.debug("! ! ! ! ! ! ! Proprietaire: {}", proprietaireUser);
        Long loginId = proprietaireUtil.getLoginId();

        Utilisateur signalant = objet.getSignalant();

        String subject = "Signalement d'un objet retrouvé";
        String content =
            "Bonjour/Bonsoir cher utilisateur de nos services goFind!\n\n" +
            "Nous vous écrivons pour vous signaler que votre objet volé d'informations:\n" +
            "{ Libelle: " +
            objet.getLibelle() +
            "\n" +
            ", Identifiant: " +
            objet.getIdentifiant() +
            "\n" +
            "} aurait été retrouvé par un de nos utilisateurs dont les informations pour entrer en contact avec lui suivent: \n" +
            "{ Nom: " +
            signalant.getLogin().getLogin() +
            "\n" +
            ", Email: " +
            signalant.getLogin().getEmail() +
            "\n" +
            ", Telephone: " +
            signalant.getTelephone() +
            "\n\n" +
            "}. Cordialement,\n" +
            "L'equipe goFind!";

        userService
            .getUserWithAuthoritiesById(loginId)
            .subscribe(user -> {
                log.debug("*** Sending a report mail to '{}' from {} about its object {}", user.getEmail(), signalant, objet);
                this.sendEmail(user.getEmail(), subject, content, false, true);
                log.debug("*** Sucessful sending of a report mail to '{}' from {} about its object {}", user.getEmail(), signalant, objet);
            });
    }

    public void sendObjetUnReportEmail(Objet objet) {
        Utilisateur proprietaireUtil = objet.getProprietaire();
        // User proprietaireUser = proprietaireUtil.getLogin();
        // log.debug("! ! ! ! ! ! ! ProprietaireUtil: {}", proprietaireUtil);
        // log.debug("! ! ! ! ! ! ! Proprietaire: {}", proprietaireUser);
        Long loginId = proprietaireUtil.getLoginId();

        String subject = "Erreur de Signalement d'un objet retrouvé";
        String content =
            "Bonjour/Bonsoir cher utilisateur de nos services goFind!\n\n" +
            "Nous vous écrivons pour vous signaler que le message du précédent mail par rapport à votre objet volé d'informations:\n" +
            "{ Libelle: " +
            objet.getLibelle() +
            "\n" +
            ", Identifiant: " +
            objet.getIdentifiant() +
            "\n" +
            "} s'est avéré erroné! Un utilisateur a fait une fausse manipulation! Toutes nos excuses pour ce désagrément!\n" +
            "}. Cordialement,\n" +
            "L'equipe goFind!";

        userService
            .getUserWithAuthoritiesById(loginId)
            .subscribe(user -> {
                log.debug("*** Sending a unreport mail to '{}' about its object {}", user.getEmail(), objet);
                this.sendEmail(user.getEmail(), subject, content, false, true);
                log.debug("*** Sucessful sending of a unreport mail to '{}' about its object {}", user.getEmail(), objet);
            });
    }

    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        this.sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        this.sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        this.sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }
}
