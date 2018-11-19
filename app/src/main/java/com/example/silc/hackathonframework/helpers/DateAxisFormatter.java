package com.example.silc.hackathonframework.helpers;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class DateAxisFormatter implements IAxisValueFormatter {

//    private DecimalFormat mFormat;
    private HashMap<Long, Long> domainHM;

    public DateAxisFormatter(HashMap hm){
        domainHM = swap(hm);
//        mFormat = new DecimalFormat("###,###,##0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis){
//        return mFormat.format(value);
        long lvalue = (long) value;
        long millies = domainHM.get(lvalue);
        DateTime dateTime = new DateTime(millies);
        return String.format("%d:%d", dateTime.getHourOfDay(), dateTime.getMinuteOfHour());
    }

    private HashMap swap(HashMap<Object, Object> hm){
        HashMap ret = new HashMap();
        for (Map.Entry<Object, Object> entry: hm.entrySet()){
            ret.put(entry.getValue(), entry.getKey());
        }
        return ret;
    }
}
