package product.loader.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yuriy Tumakha
 */
public enum RGBColor {

  BLACK("000000"),
  BLUE("0000FF"),
  CYAN("00FFFF"),
  MAGENTA("FF00FF"),
  BROWN("964b00"),
  GOLD("FFD700"),
  SILVER("C0C0C0"),
  GREEN("008000"),
  LIME("00FF00"),
  GREY("808080"),
  ORANGE("FFA500"),
  MAROON("800000"),
  OLIVE("808000"),
  PINK("F0A1C2"),
  PURPLE("800080"),
  RED("FF0000"),
  WHITE("FFFFFF"),
  YELLOW("FFFF00"),
  TEAL("008080"),
  NAVY("000080"),
  METALLICS("AAA9AD"),
  NEUTRALS("F5F5DC"),
  MULTI(""),
  DEFAULT("");

  private static final Logger LOG = LoggerFactory.getLogger(RGBColor.class);

  private final String rgb;

  RGBColor(String rgb) {
    this.rgb = rgb;
  }

  public String getRGB() {
    return rgb;
  }

  public static RGBColor of(String color) {
    try {
      return RGBColor.valueOf(color.toUpperCase());
    } catch (Exception e) {
      LOG.warn("Unknown color '{}'", color);
      return DEFAULT;
    }
  }

}
