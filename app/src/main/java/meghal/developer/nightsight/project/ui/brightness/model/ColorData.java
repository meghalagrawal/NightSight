package meghal.developer.nightsight.project.ui.brightness.model;

/**
 * Created by Meghal on 6/22/2016.
 */
public class ColorData {

    private int color;
    private boolean selected = false;

    public int getColor() {
        return color;
    }

    public boolean isSelected() {
        return selected;
    }

    public ColorData(int color) {
        this.color = color;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
