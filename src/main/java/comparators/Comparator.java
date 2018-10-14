package comparators;

import entities.DuckProduct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * сравнение сущностей
 */
public class Comparator {

    public static Logger LOGGER = LogManager.getLogger(Comparator.class.getName());

    public static boolean equalsDuckProduct(DuckProduct ar, DuckProduct er) {
        LOGGER.info("Сравнение двух уточек по названию, обычной и акционной ценам");

        if (ar.getName().equals(er.getName())
                && ar.getCampaignPrice().equals(er.getCampaignPrice())
                && ar.getRegularPrice().equals(er.getRegularPrice())
        ) {
            LOGGER.info("Сравнение двух уточек по названию, обычной и акционной ценам - УСПЕШНО");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Проверка цвета на серость - три параметра RGB одинаковы
     *
     * @return
     */
    public static boolean checkColorGray(String color) {
        LOGGER.info("Сравнение цвета на серость - три параметра RGB одинаковы");
        HashMap<String, Integer> rgb = getRGB(color);

        if (rgb.get("red").equals(rgb.get("green"))
                && rgb.get("red").equals(rgb.get("blue"))
                && rgb.get("blue").equals(rgb.get("green"))
        ) {
            LOGGER.info("Сравнение цвета на серость - три параметра RGB одинаковы - УСПЕШНО");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Проверка цвета на красность - зеленый и синий составляющие нули
     *
     * @return
     */
    public static boolean checkColorRed(String color) {
        LOGGER.info("Сравнение цвета на на красность - зеленый и синий составляющие нули");
        HashMap<String, Integer> rgb = getRGB(color);

        if (rgb.get("green").equals(0)
                && rgb.get("blue").equals(0)
        ) {
            LOGGER.info("Сравнение цвета на на красность - зеленый и синий составляющие нули - УСПЕШНО");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Проверка что один размер шрифта больше другого
     *
     * @param smallText
     * @param bigText
     * @return
     */
    public static boolean checkUpperSizeText(String smallText, String bigText) {
        LOGGER.info("Сравнение большести размера шрифта");
        smallText = smallText.replaceAll("px", "");
        smallText = smallText.replace("\\.", ",");
        bigText = bigText.replaceAll("px", "");
        bigText = bigText.replace("\\.", ",");

        if (Double.valueOf(smallText) < Double.valueOf(bigText)) {
            LOGGER.info("Сравнение большести размера шрифта - УСПЕШНО");
            return true;
        } else {
            return false;
        }
    }

    private static HashMap<String, Integer> getRGB(String color) {
        HashMap<String, Integer> rgb = new HashMap<String, Integer>();
        color = color.replaceAll("rgba\\(", "");
        color = color.replaceAll("\\)", "");
        String[] rgbString = color.split(",");
        rgbString[0] = rgbString[0].trim();
        rgb.put("red", Integer.parseInt(rgbString[0]));
        rgbString[1] = rgbString[1].trim();
        rgb.put("green", Integer.parseInt(rgbString[1]));
        rgbString[2] = rgbString[2].trim();
        rgb.put("blue", Integer.parseInt(rgbString[1]));

        LOGGER.info(String.format("Получили цвет %s", rgb.toString()));
        return rgb;
    }
}
