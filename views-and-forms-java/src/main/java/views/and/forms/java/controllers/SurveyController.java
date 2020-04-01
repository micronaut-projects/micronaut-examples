package views.and.forms.java.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import views.and.forms.java.model.FormData;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.Set;

@Controller("/")
public class SurveyController {

    private static final Logger LOG = LoggerFactory.getLogger(SurveyController.class);

    @Get
    @View("home")
    public FormData home() {

        if (LOG.isInfoEnabled()) {
            LOG.info("Sending home page ");
        }

        return new FormData();

    }

    @View("thankyou")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/survey")
    public FormData processHomeScreen(@Body @Valid FormData formData) {

        if (LOG.isInfoEnabled()) {
            LOG.info( "{} has a chocolate preference of: {}", formData.getUserName(), formData.getChocolate());
        }

        formData.clearErrors();

        return formData;

    }

    @View("home")
    @Error(exception = ConstraintViolationException.class)
    public FormData handleInvalidInput(HttpRequest<FormData> request, ConstraintViolationException cv) {

        Set<ConstraintViolation<?>> constraintViolations = cv.getConstraintViolations();

        if (LOG.isInfoEnabled()) {
            LOG.info("Form validation failed with {} error(s)", constraintViolations.size());
        }

        FormData model = request.getBody(FormData.class).orElse(new FormData());

        constraintViolations.forEach(constraintViolation -> {
            model.addError(constraintViolation.getMessage());
        });

        return model;
    }

}
