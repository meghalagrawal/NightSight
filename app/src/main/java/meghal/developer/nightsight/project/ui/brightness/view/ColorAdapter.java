package meghal.developer.nightsight.project.ui.brightness.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import meghal.developer.nightsight.project.CustomViews.Shape;
import meghal.developer.nightsight.project.R;
import meghal.developer.nightsight.project.helper.SharedPrefs;
import meghal.developer.nightsight.project.ui.brightness.model.ColorData;

/**
 * Created by Meghal on 6/22/2016.
 */
public class ColorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ColorData> colorDataList = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private int SELECTED_COLOR;
    private SharedPrefs sharedPrefs;

    ColorAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        sharedPrefs=new SharedPrefs(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.color_item, parent, false);
        return new ColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ColorData colorData = colorDataList.get(position);
        final ColorViewHolder colorViewHolder = (ColorViewHolder) holder;
        colorViewHolder.colorView.setShapeColor(ContextCompat.getColor(context, colorData.getColor()));

        if (SELECTED_COLOR == colorData.getColor()) {
            colorViewHolder.selectedImage.setVisibility(View.VISIBLE);
        } else {
            colorViewHolder.selectedImage.setVisibility(View.GONE);
        }

        colorViewHolder.colorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefs.setColorId(colorData.getColor());
                if (context instanceof ChangeBrightness) {
                    ((ChangeBrightness) context).changeColor(ContextCompat.getColor(context, colorData.getColor()));
                }
                SELECTED_COLOR = colorData.getColor();
                colorViewHolder.selectedImage.setVisibility(View.VISIBLE);
                colorData.setSelected(true);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return colorDataList.size();
    }

    public void setColorData(List<ColorData> colorDataList) {
        this.colorDataList = colorDataList;
    }

    class ColorViewHolder extends RecyclerView.ViewHolder {

        private Shape colorView;
        private ImageView selectedImage;

        public ColorViewHolder(View itemView) {
            super(itemView);
            colorView = (Shape) itemView.findViewById(R.id.colorView);
            selectedImage = (ImageView) itemView.findViewById(R.id.selectedImage);
        }
    }

}
