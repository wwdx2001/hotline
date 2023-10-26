package com.sh3h.hotline.ui.nonresident.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CallTabStatisticsEntity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class CallXYMarkerView extends MarkerView {

    private final TextView tvContent;
    private final TextView tvContentOther;
    private final ValueFormatter xAxisValueFormatter;

    private final DecimalFormat format;

    private List<CallTabStatisticsEntity> itemBeans;

    private Map<String, CallTabStatisticsEntity> maps;

    public CallXYMarkerView(Context context, ValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);

        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvContentOther = (TextView) findViewById(R.id.tvContentOther);
        format = new DecimalFormat("###");
    }

    public List<CallTabStatisticsEntity> getItemBeans() {
        return itemBeans;
    }

    public void setItemBeans(List<CallTabStatisticsEntity> itemBeans) {
        this.itemBeans = itemBeans;
//        maps = Maps.uniqueIndex(itemBeans, new Function<CallTabStatisticsEntity, String>() {
//            @Override
//            public String apply(CallTabStatisticsEntity callTabStatisticsEntity) {
//                return callTabStatisticsEntity.getPch();
//            }
//        });

//        maps = itemBeans.stream().collect(Collectors.toMap(CallTabStatisticsEntity::getPch, Function.identity(), (key1, key2) -> key2));
        maps = new HashMap<>();
        for (CallTabStatisticsEntity entityMap : itemBeans) {
            maps.put(entityMap.getPch(), entityMap);
        }
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent.setText(String.format("批次号: %s, 总工单数: %s", xAxisValueFormatter.getFormattedValue(e.getX()), format.format(e.getY())));
        CallTabStatisticsEntity entity = maps.get(xAxisValueFormatter.getFormattedValue(e.getX()));
        tvContentOther.setText(String.format("未完成数: %s, 已完成数: %s", entity.getWeiwancheng(),entity.getYiwancheng()));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}

