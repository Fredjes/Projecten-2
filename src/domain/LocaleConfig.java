package domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * A class that holds configurations, for example the date format that should be
 * used over the application.
 *
 * @author Frederik De Smedt
 */
public class LocaleConfig {

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

}
