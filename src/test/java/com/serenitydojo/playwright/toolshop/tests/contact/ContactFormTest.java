package com.serenitydojo.playwright.toolshop.tests.contact;

import com.serenitydojo.playwright.toolshop.fixtures.BaseTest;
import com.serenitydojo.playwright.toolshop.fixtures.FinalScreenshot;
import com.serenitydojo.playwright.toolshop.fixtures.TracingManager;
import com.serenitydojo.playwright.toolshop.pages.common.NavBar;
import com.serenitydojo.playwright.toolshop.pages.contact.ContactForm;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Contact form")
@Feature("Contacts")
public class ContactFormTest extends BaseTest implements TracingManager {

    ContactForm contactForm;
    NavBar navigate;

    @BeforeEach
    void openContactPage() {
        contactForm = new ContactForm(page);
        navigate = new NavBar(page);
        navigate.toTheContactPage();
    }

    @Story("Contact form")
    @DisplayName("Customers can use the contact form to contact us")
    @Test
    void completeForm() throws URISyntaxException {
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
        contactForm.selectSubject("Warranty");

        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
        contactForm.setAttachment(fileToUpload);

        contactForm.submitForm();

        assertThat(contactForm.alertMessage()).isVisible();
        assertThat(contactForm.alertMessage()).hasText("Thanks for your message! We will contact you shortly.");
    }

    @Story("Contact form")
    @DisplayName("First name, last name, email and message are mandatory")
    @ParameterizedTest(name = "{arguments} is a mandatory field")
    @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
    void mandatoryFields(String fieldName) {
        // Fill in the field values
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
        contactForm.selectSubject("Warranty");

        // Clear one of the fields
        contactForm.clearField(fieldName);

        contactForm.submitForm();

        // Check the error message for that field
        assertThat(contactForm.alertMessage()).isVisible();
        assertThat(contactForm.alertMessage()).hasText(fieldName + " is required");
    }

    @Story("Contact form")
    @DisplayName("The message must be at least 50 characters long")
    @Test
    void messageTooShort() {

        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A short long message.");
        contactForm.selectSubject("Warranty");

        contactForm.submitForm();

        assertThat(contactForm.alertMessage()).isVisible();
        assertThat(contactForm.alertMessage()).hasText("Message must be minimal 50 characters");
    }

    @Story("Contact form")
    @DisplayName("The email address must be correctly formatted")
    @ParameterizedTest(name = "'{arguments}' should be rejected")
    @ValueSource(strings = {"not-an-email", "not-an.email.com", "notanemail"})
    void invalidEmailField(String invalidEmail) {
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail(invalidEmail);
        contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
        contactForm.selectSubject("Warranty");

        contactForm.submitForm();

        assertThat(contactForm.alertMessage()).isVisible();
        assertThat(contactForm.alertMessage()).hasText("Email format is invalid");
    }
}
