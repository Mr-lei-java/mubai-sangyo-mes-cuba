package com.company.sangyo.core;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service(CheckService.NAME)
public class CheckServiceBean implements CheckService {
    @Override
    public Boolean singleValueCheck(String standardValue, String calibrationMethod, double tolerance, String observedValue, Float rsd) {
        if (observedValue.equals("")) {
            observedValue = "0";
        }
        if (standardValue.equals("")) {
            standardValue = "0";
        }
//        log.info("standardValue:{},tolerance:{},calibrationMethod:{},observedValue:{},rsd:{}", standardValue, tolerance, calibrationMethod, observedValue, rsd);
        Boolean match;
        switch (calibrationMethod) {
            case "GREATER":
                if (Float.parseFloat(observedValue) > (Float.parseFloat(standardValue) + rsd)) {
                    match = true;
                    break;
                }
                match = false;
                break;
            case "GREATER_EQUAL":
                if (Float.parseFloat(observedValue) >= (Float.parseFloat(standardValue) + rsd)) {
                    match = true;
                    break;
                }
                match = false;
                break;
            case "LESS_THAN":
                if (Float.parseFloat(observedValue) < (Float.parseFloat(standardValue) + rsd)) {
                    match = true;
                    break;
                }
                match = false;
                break;
            case "LESS_THAN_EQUAL":
                if (Float.parseFloat(observedValue) <= (Float.parseFloat(standardValue) + rsd)) {
                    match = true;
                    break;
                }
                match = false;
                break;
            case "EQUAL":
                //判断文字相等
                if (standardValue != null && observedValue != null) {
                    if (isNumeric(observedValue) && isNumeric(standardValue)) {
                        if (Math.abs(Float.parseFloat(observedValue) - (Float.parseFloat(standardValue) + rsd)) <= 0) {
                            match = true;
                            break;
                        }
                    } else {
                        if (observedValue.equals(standardValue)) {
                            match = true;
                            break;
                        }
                    }
                }
                match = false;
                break;
            case "CONTAINS":
                //先计算对标差再计算公差
                if (isNumeric(observedValue) && isNumeric(standardValue)) {
                    if ((Float.parseFloat(standardValue) + rsd - tolerance) <= Float.parseFloat(observedValue) && (Float.parseFloat(standardValue) + rsd + tolerance) >= Float.parseFloat(observedValue)) {
                        match = true;
                        break;
                    }
                } else {
                    if (observedValue.equals(standardValue)) {
                        match = true;
                        break;
                    }
                }
                match = false;
                break;
            default:
                match = false;
                break;
        }
//        log.info("单值校验结果:{}", match);
        return match;
    }

    @Override
    public Boolean multiValueCheck(String standardValue, String calibrationMethod, double tolerance, Float rsd, String observedValue, Double range) {
        if (observedValue.equals("")) {
            observedValue = "0";
        }
        if (standardValue.equals("")) {
            standardValue = "0";
        }
//        log.info("standardValue:{},calibrationMethod:{},tolerance:{},rsd:{},observedValue:{}", standardValue, calibrationMethod, tolerance, rsd, observedValue);
        Boolean match;
        switch (calibrationMethod) {
            //网版平均值
            case "A":
                String[] strings = observedValue.split("-");
                float observedValueFloat = 0;
                for (int i = 0; i < strings.length; i++) {
                    observedValueFloat = observedValueFloat + Float.parseFloat(strings[i]);
                }
                observedValueFloat = observedValueFloat / strings.length;
                if (((Float.parseFloat(standardValue) + rsd - tolerance)) <= observedValueFloat && ((Float.parseFloat(standardValue) + rsd + tolerance)) >= observedValueFloat) {
                    match = true;
                    break;
                }
                match = false;
                break;
            //网版极差
            case "B":
                String[] strings1 = observedValue.split("-");
                List<Float> floatList = new ArrayList<>();
                for (int i = 0; i < strings1.length; i++) {
                    floatList.add(Float.parseFloat(strings1[i]));
                }
                floatList.sort(new Comparator<Float>() {
                    @Override
                    public int compare(Float o1, Float o2) {
                        return o1.compareTo(o2);
                    }
                });
                if ((floatList.get(floatList.size() - 1) - floatList.get(0)) <= range) {
                    match = true;
                    break;
                }
                match = false;
                break;
            //网版单值落在标准值组
            case "C":
                String[] strings2 = observedValue.split("-");
                boolean match1 = true;
                for (int i = 0; i < strings2.length; i++) {
                    if (((Float.parseFloat(standardValue) - tolerance)) > Float.parseFloat(strings2[i]) || ((Float.parseFloat(standardValue) + tolerance)) < Float.parseFloat(strings2[i])) {
                        match1 = false;
                        break;
                    }
                    match1 = true;
                }
                if (!match1) {
                    match = false;
                    break;
                }
                match = true;
                break;
            default:
                match = false;
                break;
        }
//        log.info("组值校验结果:{}", match);
        return match;
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        if (str.indexOf(".") > 0) {//判断是否有小数点
            if (str.indexOf(".") == str.lastIndexOf(".") && str.split("\\.").length == 2) { //判断是否只有一个小数点
                return pattern.matcher(str.replace(".", "")).matches();
            } else {
                return false;
            }
        } else {
            return pattern.matcher(str).matches();
        }
    }
}
