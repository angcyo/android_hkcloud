package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork;

public interface RdpResponseListener {
    /** 提交任务前先添加到任务观察器管理中.
     * @param task 实现了Observer接口的任务实例
     * @param taskKey 任务标识
     * @return 正常情况下返回true, 如果返回false, 应该终止任务继续执行.
     */
    boolean onBeforeExecute(long taskKey);

    /** 任务返回后将要执行的操作.        */
    void onReponse(long taskKey, RdpResponseResult result);

    /** 任务取消后将要执行的操作.        */
    void onCancelled(long taskKey, RdpResponseResult result);
}
