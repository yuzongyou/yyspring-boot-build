package com.duowan.common.timer;

/**
 * 周期，仅执行一次
 * @author Arvin
 */
public class OncePeriod implements Period {
    @Override
    public boolean sleep() {
        return false;
    }
}
