package utils;

import helpers.StoryHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Генерация тестовых данных
 */
public class Generator {
    public static Logger LOGGER = LogManager.getLogger(Generator.class.getName());

    private static String numberScope = "123456789";
    private static String vowelScope = "АЕИОУЫЮЯ";
    private static String consonantScope = "БВГДЖЗКЛМНПРСТФЧЦ";

    public static String generateFirstName() {
        String firstName = "";
        for (byte j = 0; j < 8; j++) {
            if (j == 0 | j == 2 | j == 4 | j == 6) {
                char c = consonantScope.charAt(new Random().nextInt(consonantScope.length()));
                firstName = firstName + c;
            } else if (j == 1 | j == 3 | j == 5) {
                char c = vowelScope.charAt(new Random().nextInt(vowelScope.length()));
                firstName = firstName + c;
            } else if (j == 7) {
                firstName = firstName + "ЕЙ";
            }
        }
        LOGGER.info(String.format("Сгенерировано имя - %s", firstName));
        return firstName;
    }

    public static String generateLastName() {
        String lastName = "";
        for (byte j = 0; j < 8; ++j) {
            if (j == 0 | j == 2 | j == 4 | j == 6) {
                char c = consonantScope.charAt(new Random().nextInt(consonantScope.length()));
                lastName = lastName + c;
            } else {
                if (j == 1 | j == 3 | j == 5) {
                    char c = vowelScope.charAt(new Random().nextInt(vowelScope.length()));
                    lastName = lastName + c;
                } else if (j == 7) {
                    lastName = lastName + "ОВ";
                }
            }
        }
        LOGGER.info(String.format("Сгенерирована фамилия - %s", lastName));
        return lastName;
    }


    public static String generateEmail() {
        String email = "test-selenium";
        email = email + String.valueOf(ThreadLocalRandom.current().nextInt(99, 999 + 1));
        email = email + "@test.ru";

        LOGGER.info(String.format("Сгенерирован емайл - %s", email));
        return email;
    }

    public static String generateProductName() {
        String name = "new-duck";
        name = name + String.valueOf(ThreadLocalRandom.current().nextInt(99, 999 + 1));
        name = name + "-cool";

        LOGGER.info(String.format("Сгенерировано имя продукта - %s", name));
        return name;
    }

    public static int generateProductPrice() {
        int price = ThreadLocalRandom.current().nextInt(11, 99 + 1);

        LOGGER.info(String.format("Сгенерирована стоимость продукта - %s", price));
        return price;
    }

    public static int generateProductCount() {
        int count = ThreadLocalRandom.current().nextInt(10, 90 + 1);

        LOGGER.info(String.format("Сгенерировано колво продукта - %s", count));
        return count;
    }

}