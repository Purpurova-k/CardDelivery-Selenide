package ru.netology.web;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class CardDeliveryFormSecondTest {

    LocalDate currentDate = LocalDate.now();
    LocalDate deliveryDate = currentDate.plusDays(7);

    @Test
    public void shouldSendFormWithComplexElements() {
        open("http://localhost:9999/");

        $("[data-test-id=city] input").val("Мо");
        $(".input__menu").find(withText("Москва")).click();
        $("[data-test-id=date] input").doubleClick().sendKeys(Keys.BACK_SPACE);
        $(".input_focused button").click();
        if (currentDate.getMonth() != deliveryDate.getMonth()) {
            $(".calendar__arrow_direction_right[data-step=\"1\"]").click();
        }
        $$("td.calendar__day").find(exactText(String.valueOf(deliveryDate.getDayOfMonth()))).click();
        $("[data-test-id=name] input").val("Иванов Иван");
        $("[data-test-id=phone] input").val("+79998887766");
        $("[data-test-id=agreement] span").click();
        $$("button").find(exactText("Забронировать")).click();

        $("[data-test-id=notification] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(exactText("Встреча успешно забронирована на " + deliveryDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));

    }
}

