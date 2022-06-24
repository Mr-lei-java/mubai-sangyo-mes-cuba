package com.company.sangyo.core;

public interface CheckService {
    String NAME = "sangyo_CheckService";

    /**
     * 参数校验逻辑
     *
     * @param standardValue
     * @param calibrationMethod
     * @param tolerance
     * @param observedValue
     * @return
     */
    Boolean singleValueCheck(String standardValue, String calibrationMethod,
                             double tolerance, String observedValue, Float rsd);

    /**
     * 组值校验方法
     *
     * @param standardValue
     * @param calibrationMethod
     * @param tolerance
     * @param rsd
     * @param observedValue
     * @return
     */
    Boolean multiValueCheck(String standardValue, String calibrationMethod,
                            double tolerance, Float rsd, String observedValue, Double range);

}
