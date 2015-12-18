package com.zhoukl.androidRDP.RdpDataSource.RdpNetwork;


public interface IRdpNetRequestCallBackListener {
        /** 提交任务前先添加到任务观察器管理中.
         * @param task 实现了Observer接口的任务实例
         * @param taskKey 任务标识
         * @return 正常情况下返回true, 如果返回false, 应该终止任务继续执行.
         */
        //public boolean onBeforeExecute(String requestKey);

        /** 后台执行任务       */
        String doTaskInBackground(String requestKey);

        /** 任务返回后将要执行的操作.        */
        void onResult(Object requestKey, RdpResponseResult result);

        /** 任务返回后将要执行的操作.        */
        //public void onErrorResult(String requestKey, RdpResponseResult result);

        /** 错误回调，包括：任务取消后将要执行的操作、网络异常、业务逻辑异常等.        */
        void onErrorResult(Object requestKey, RdpResponseResult result);

}
