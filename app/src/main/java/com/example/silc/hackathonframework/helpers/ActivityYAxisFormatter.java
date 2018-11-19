package com.example.silc.hackathonframework.helpers;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class ActivityYAxisFormatter implements IAxisValueFormatter{

    private DecimalFormat mFormat;

    public ActivityYAxisFormatter(){
        mFormat = new DecimalFormat("###,###,##0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis){
        return mFormat.format(value);
    }
}
