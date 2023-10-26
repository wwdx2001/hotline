package com.sh3h.hotline.ui.nonresident.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.sh3h.hotline.R;
import com.sh3h.hotline.entity.CallTabStatisticsEntity;
import com.sh3h.hotline.entity.ReceiptTabStatisticsEntity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
@SuppressLint("ViewConstructor")
public class ReceiptXYMarkerView extends MarkerView {

    private final TextView tvContent;
    private final TextView tvContentOther;
    private final ValueFormatter xAxisValueFormatter;

    private final DecimalFormat format;

    private List<ReceiptTabStatisticsEntity> itemBeans;

    private Map<String, ReceiptTabStatisticsEntity> maps;

    public ReceiptXYMarkerView(Context context, ValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);

        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvContentOther = (TextView) findViewById(R.id.tvContentOther);
        format = new DecimalFormat("###");
    }

    public List<ReceiptTabStatisticsEntity> getItemBeans() {
        return itemBeans;
    }

    public void setItemBeans(List<ReceiptTabStatisticsEntity> itemBeans) {
        this.itemBeans = itemBeans;
//        maps = Maps.uniqueIndex(itemBeans, new Function<CallTabStatisticsEntity, String>() {
//            @Override
//            public String apply(CallTabStatisticsEntity callTabStatisticsEntity) {
//                return callTabStatisticsEntity.getPch();
//            }
//        });

//        maps = itemBeans.stream().collect(Collectors.toMap(CallTabStatisticsEntity::getPch, Function.identity(), (key1, key2) -> key2));
        maps = new HashMap<>();
        for (ReceiptTabStatisticsEntity entityMap : itemBeans) {
            maps.put(entityMap.getPch(), entityMap);
        }
        LogUtils.e("------" + maps.toString());
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent.setText(String.format("批次号: %s, 总工单数: %s", xAxisValueFormatter.getFormattedValue(e.getX()), format.format(e.getY())));
        ReceiptTabStatisticsEntity entity = maps.get(xAxisValueFormatter.getFormattedValue(e.getX()));
        tvContentOther.setText(String.format("未完成数: %s, 已完成数: %s", entity.getWeiwancheng(),entity.getYiwancheng()));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}

