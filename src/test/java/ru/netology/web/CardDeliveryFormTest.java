package ru.netology.web;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CardDeliveryFormTest {

    Faker faker = new Faker(new Locale("ru"));
    String name = faker.name().fullName();
    String phone = faker.numerify("+79#########");

    String tele = faker.phoneNumber().phoneNumber();

    String deliveryDate = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));


    @Test
    public void shouldSendForm() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val(name);
        $("[data-test-id=phone] input").val(phone);
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(exactText("Встреча успешно забронирована на " + deliveryDate));
    }


    @Test
    public void shouldNotSendFormInvalidCityFieldEmpty() {

        open("http://localhost:9999/");

        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val(name);
        $("[data-test-id=phone] input").val(phone);
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Поле обязательно для заполнения";
        String actualText = $("[data-test-id=city] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


    @Test
    public void shouldNotSendFormInvalidCityFieldNotAdministrativeCenter() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Азов");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val(name);
        $("[data-test-id=phone] input").val(phone);
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Доставка в выбранный город недоступна";
        String actualText = $("[data-test-id=city] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


    @Test
    public void shouldNotSendFormInvalidCityFieldEnglish() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Moscow");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val(name);
        $("[data-test-id=phone] input").val(phone);
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Доставка в выбранный город недоступна";
        String actualText = $("[data-test-id=city] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


//    @Test
//    public void invalidDateFieldEmpty() {
//
//        open("http://localhost:9999/");
//
//        $("[data-test-id=city] input").val("Москва");
//        $("[data-test-id=date] input").clear();
//        $("[data-test-id=name] input").val(name);
//        $("[data-test-id=phone] input").val(phone);
//        $("[data-test-id=agreement] span").click();
//        $$("button").find(exactText("Забронировать")).click();
//
//        String expectedText = "Неверно введена дата";
//        String actualText = $("[data-test-id=date] .input__sub").getText().trim();
//
//        assertEquals(expectedText, actualText);
//    }


    @Test
    public void shouldNotSendFormInvalidDateFieldEmpty() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $("[data-test-id=name] input").val(name);
        $("[data-test-id=phone] input").val(phone);
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Неверно введена дата";
        String actualText = $("[data-test-id=date] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


    @Test
    public void shouldNotSendFormInvalidNameFieldEmpty() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=phone] input").val(phone);
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Поле обязательно для заполнения";
        String actualText = $("[data-test-id=name] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


    @Test
    public void shouldNotSendFormInvalidNameFieldEnglish() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val("Ekaterina Ivanova");
        $("[data-test-id=phone] input").val(phone);
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = $("[data-test-id=name] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


    @Test
    public void shouldNotSendFormInvalidNameFieldWithNumbers() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val("Иван999");
        $("[data-test-id=phone] input").val(phone);
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actualText = $("[data-test-id=name] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


    @Test
    public void shouldNotSendFormInvalidPhoneFieldEmpty() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val(name);
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Поле обязательно для заполнения";
        String actualText = $("[data-test-id=phone] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


    @Test
    public void shouldNotSendFormInvalidPhoneFieldLetters() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val(name);
        $("[data-test-id=phone] input").val("телефон");
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = $("[data-test-id=phone] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


    @Test
    public void shouldNotSendFormInvalidPhoneFieldFewNumbers() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val(name);
        $("[data-test-id=phone] input").val("+7999888");
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = $("[data-test-id=phone] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


    @Test
    public void shouldNotSendFormInvalidPhoneFieldWithoutPlus() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val(name);
        $("[data-test-id=phone] input").val("79998887766");
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        String expectedText = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actualText = $("[data-test-id=phone] .input__sub").getText().trim();

        assertEquals(expectedText, actualText);
    }


    @Test
    public void shouldNotSendFormUncheckedCheckbox() {

        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Москва");
        $("[data-test-id=date] input").doubleClick().val(deliveryDate);
        $("[data-test-id=name] input").val(name);
        $("[data-test-id=phone] input").val(phone);
        $$("button").find(exactText("Забронировать")).click();

        String checkboxInvalid = $("[data-test-id=agreement].checkbox").getAttribute("className");
        assertTrue(checkboxInvalid.contains("input_invalid"));
    }
}
