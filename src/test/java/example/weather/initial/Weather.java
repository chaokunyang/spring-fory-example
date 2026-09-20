package example.weather.initial;

import java.util.List;

public record Weather(
    String city,
    int temperature,
    boolean raining,
    List<String> tips) {}
