package meghal.developer.nightsight.project.Events;

/**
 * Created by Meghal on 6/22/2016.
 */
public class BrightnessEvent {
    private int brightness;

    public BrightnessEvent(int brightness) {
        this.brightness = brightness;
    }

    public int getBrightness() {
        return brightness;
    }
}
